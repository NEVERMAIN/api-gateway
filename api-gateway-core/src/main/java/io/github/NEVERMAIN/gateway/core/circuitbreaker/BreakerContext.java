package io.github.NEVERMAIN.gateway.core.circuitbreaker;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;

public class BreakerContext {
    /**
     * 熔断器
     */
    private CircuitBreaker breaker;
    /**
     * 开始时间
     */
    private long startNano;
    /**
     * 后端服务Key
     */
    private String apiKey;

    public BreakerContext(CircuitBreaker breaker, String apiKey, long startNano) {
        this.breaker = breaker;
        this.apiKey = apiKey;
        this.startNano = startNano;
    }

    public BreakerContext(){

    }

    public CircuitBreaker getBreaker() {
        return breaker;
    }

    public void setBreaker(CircuitBreaker breaker) {
        this.breaker = breaker;
    }

    public long getStartNano() {
        return startNano;
    }

    public void setStartNano(long startNano) {
        this.startNano = startNano;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}
