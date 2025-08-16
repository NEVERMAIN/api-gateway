package io.github.NEVERMAIN.gateway.core.metrics;

/**
 * @description
 */
public interface MetricsCollector {

    /**
     * 记录一次请求(QPS统计)
     * @param uri 请求URI
     */
    void recordRequest(String uri);

    /**
     * 记录请求耗时
     * @param uri 请求路径
     * @param durationNanos 耗时(纳秒)
     */
    void recordLatency(String uri, long durationNanos);

    /**
     * 记录状态码分布
     * @param uri 请求路径
     * @param statusCode HTTP 响应码
     */
    void recordStatus(String uri, int statusCode);

    /**
     * 导出指标(本地实现用来调试)
     */
    String exportMetrics();

}
