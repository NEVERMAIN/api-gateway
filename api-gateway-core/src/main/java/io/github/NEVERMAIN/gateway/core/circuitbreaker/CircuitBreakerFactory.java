package io.github.NEVERMAIN.gateway.core.circuitbreaker;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 熔断降级的配置工厂
 */
public class CircuitBreakerFactory {

    private  final CircuitBreakerRegistry circuitBreakerRegistry;

    private final ConcurrentHashMap<String, CircuitBreaker> breakers = new ConcurrentHashMap<>();

    public CircuitBreakerFactory() {
        // 创建详细的熔断器配置
        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
                // 失败率阈值: 50%
                .failureRateThreshold(50.0f)
                // 慢调用比例阈值: 50%
                .slowCallRateThreshold(50.0f)
                // 慢调用时间阈值: 10秒
                .slowCallDurationThreshold(Duration.ofSeconds(10))
                // 熔断器打开后，等待时间: 30秒
                .waitDurationInOpenState(Duration.ofSeconds(30))
                // 熔断器打开后，半开状态允许的调用次数: 5次
                .permittedNumberOfCallsInHalfOpenState(5)
                .slidingWindow(100, 10, CircuitBreakerConfig.SlidingWindowType.COUNT_BASED,
                        CircuitBreakerConfig.SlidingWindowSynchronizationStrategy.SYNCHRONIZED)
                // 禁用完整的堆栈跟踪以减少日志噪音
                .writableStackTraceEnabled(false)
                .build();

        // 使用该默认配置创建一个注册表
        circuitBreakerRegistry = CircuitBreakerRegistry.of(config);
    }

    /**
     * 根据后端服务名获取或创建一个熔断器实例
     *
     * @param apiKey 后端服务名
     * @return CircuitBreaker 实例
     */
    public  CircuitBreaker getCircuitBreader(String apiKey) {
        return breakers.computeIfAbsent(apiKey, k -> circuitBreakerRegistry.circuitBreaker(apiKey));
    }

}
