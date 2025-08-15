package io.github.NEVERMAIN.gateway.core.socket.handlers;

import io.github.NEVERMAIN.gateway.core.bind.IGenericReference;
import io.github.NEVERMAIN.gateway.core.circuitbreaker.CircuitBreakerFactory;
import io.github.NEVERMAIN.gateway.core.executor.result.SessionResult;
import io.github.NEVERMAIN.gateway.core.session.Configuration;
import io.github.NEVERMAIN.gateway.core.session.GatewaySession;
import io.github.NEVERMAIN.gateway.core.session.defaults.DefaultGatewaySessionFactory;
import io.github.NEVERMAIN.gateway.core.socket.BaseHandler;
import io.github.NEVERMAIN.gateway.core.socket.agreement.AgreementConstants;
import io.github.NEVERMAIN.gateway.core.socket.agreement.GatewayResultMessage;
import io.github.NEVERMAIN.gateway.core.socket.agreement.RequestParser;
import io.github.NEVERMAIN.gateway.core.socket.agreement.ResponseParser;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.function.Supplier;

/**
 * 封装受保护的协议数据处理器
 */
public class ProtectedProtocolDataHandler extends BaseHandler<FullHttpRequest> {

    private static final Logger log = LoggerFactory.getLogger(ProtectedProtocolDataHandler.class);
    private final DefaultGatewaySessionFactory gatewaySessionFactory;
    private final Configuration configuration;

    public ProtectedProtocolDataHandler(DefaultGatewaySessionFactory gatewaySessionFactory,
                                        Configuration configuration) {
        this.gatewaySessionFactory = gatewaySessionFactory;
        this.configuration = configuration;
    }

    @Override
    protected void session(ChannelHandlerContext ctx, Channel channel, FullHttpRequest request) {
        log.info("网关接收请求【受保护的消息】 uri:{} method:{}", request.uri(), request.method());

        // 1.解析请求参数
        RequestParser requestParser = new RequestParser(request);
        String uri = requestParser.getUri();
        if (uri == null) return;
        Map<String, Object> args = requestParser.parse();

        // 2.根据 uri 获取熔断器
        CircuitBreakerFactory circuitBreakerFactory = configuration.getCircuitBreakerFactory();
        CircuitBreaker circuitBreader = circuitBreakerFactory.getCircuitBreader(uri);

        // 3.将后端调用包装成一个 Supplier
        Supplier<SessionResult> backendCallSupplier = () -> {
            // 2.调用会话服务
            GatewaySession gatewaySession = gatewaySessionFactory.openSession(uri);
            IGenericReference reference = gatewaySession.getMapper();
            // 需要被保护的方法
            return reference.invoke(args);
        };

        // 4.使用熔断器装饰我们的调用
        Supplier<SessionResult> protectedSupplier = CircuitBreaker.decorateSupplier(circuitBreader, backendCallSupplier);

        // 5.使用标准的 try-catch 块来执行并处理结果
        try {
            // 直接调用被包装后的 Supplier
            // TODO 后续修改成 非阻塞 的调用
            SessionResult sessionResult = protectedSupplier.get();

            // 业务调用成功
            log.info("服务调用成功 uri:{}", uri);
            // 封装返回结果
            DefaultFullHttpResponse response = new ResponseParser().parse("0000".equals(sessionResult.getCode()) ?
                    GatewayResultMessage.buildSuccess(sessionResult.getData()) :
                    GatewayResultMessage.buildError(AgreementConstants.ResponseCode._404.getCode(), "网关协议调用失败！"));
            channel.writeAndFlush(response);

        } catch (Exception e) {
            // 业务调用失败 或 熔断器打开
            if (e instanceof CallNotPermittedException) {
                // 这是熔断器打开的情况，请求被拒绝
                log.warn("熔断器打开，请求被拒绝! uri: {}", uri, e);
                handleFallback(channel, "服务暂时不可用，请求已被熔断。");
            } else {
                // 这是其他业务异常
                log.error("后端调用发生异常! uri: {}", uri, e);
                handleFallback(channel, "网关协议调用失败！" + e.getMessage());
            }
        }
    }

    /**
     * 统一的降级逻辑处理器
     *
     * @param channel
     * @param message
     */
    private void handleFallback(Channel channel, String message) {
        DefaultFullHttpResponse response = new ResponseParser().parse(
                GatewayResultMessage.buildError(AgreementConstants.ResponseCode._503.getCode(), message)
        );
        channel.writeAndFlush(response);
    }

}
