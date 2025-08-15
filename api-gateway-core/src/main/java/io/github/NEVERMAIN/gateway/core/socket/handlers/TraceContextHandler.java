package io.github.NEVERMAIN.gateway.core.socket.handlers;

import io.github.NEVERMAIN.gateway.core.mapping.HttpCommandType;
import io.github.NEVERMAIN.gateway.core.socket.agreement.AgreementConstants;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponse;
import org.slf4j.MDC;

import java.security.SecureRandom;
import java.util.Locale;

/**
 * 链路追踪
 */
public class TraceContextHandler extends ChannelDuplexHandler {

    private static final SecureRandom RANDOM = new SecureRandom();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if (msg instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest) msg;

            // 1.尝试从请求头获取 traceId
            String traceId = extractTraceId(request.headers());
            ctx.channel().attr(AgreementConstants.TRACE_ID_KEY).set(traceId);
            // 2. 放到 MDC，用于日志打印
            MDC.put("traceId", traceId);
        }
        super.channelRead(ctx, msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // 入站结束后,清理 MDC
        MDC.remove("traceId");
        super.channelReadComplete(ctx);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {

        try {
            if (msg instanceof HttpResponse) {
                HttpResponse response = (HttpResponse) msg;
                String traceId = ctx.channel().attr(AgreementConstants.TRACE_ID_KEY).get();
                if (traceId != null) {
                    // 在响应头中回写 traceId
                    response.headers().set("X-Trace-Id", traceId);
                }

                // 让写路径日志也能带 traceId
                if (traceId != null) MDC.put("traceId", traceId);
                try {
                    super.write(ctx, msg, promise);
                } finally {
                    MDC.remove("traceId");
                }
                return;
            }
            super.write(ctx, msg, promise);
        } finally {
            // 出站结束后清理 MDC，避免线程复用时串 traceId
            MDC.remove("traceId");
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        MDC.remove("traceId");
        super.exceptionCaught(ctx, cause);
    }

    private String extractTraceId(HttpHeaders headers) {

        // 优先解析 traceparent（W3C）
        String traceparent = headers.get("traceparent");
        if (traceparent != null && traceparent.length() >= 55) {
            String maybeTraceId = traceparent.substring(3, 35);
            if (isHex128(maybeTraceId)) {
                return maybeTraceId;
            }
        }

        // 次优先 X-Trace-Id
        String traceId = headers.get("X-Trace-Id");
        if (traceId != null && isHex128(traceId)) {
            return traceId;
        }

        // 都没有就生成一个新的
        return generateTraceId();
    }

    private String generateTraceId() {
        byte[] bytes = new byte[16];
        RANDOM.nextBytes(bytes);
        StringBuilder sb = new StringBuilder(32);
        for (byte b : bytes) {
            sb.append(String.format(Locale.ROOT, "%02x", b));
        }
        return sb.toString();
    }

    private boolean isHex128(String s) {
        return s != null && s.length() == 32 && s.chars().allMatch(c ->
                (c >= '0' && c <= '9') ||
                        (c >= 'a' && c <= 'f') ||
                        (c >= 'A' && c <= 'F'));
    }

}
