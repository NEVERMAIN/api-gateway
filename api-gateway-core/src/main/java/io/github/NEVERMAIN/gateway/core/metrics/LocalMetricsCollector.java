package io.github.NEVERMAIN.gateway.core.metrics;

import io.github.resilience4j.core.metrics.Metrics;

import java.util.LongSummaryStatistics;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

/**
 * 本地实现的计数器
 */
public class LocalMetricsCollector implements MetricsCollector {

    private final ConcurrentHashMap<String, LongAdder> requestCounters = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, LongAdder> statusCounters = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, LongSummaryStatistics> latencyStats = new ConcurrentHashMap<>();


    @Override
    public void recordRequest(String uri) {
        requestCounters.computeIfAbsent(uri, k -> new LongAdder()).increment();
    }

    @Override
    public void recordLatency(String uri, long durationNanos) {
        latencyStats.computeIfAbsent(uri, k -> new LongSummaryStatistics())
                .accept(durationNanos / 1_000_000); // 转换为毫秒
    }

    @Override
    public void recordStatus(String uri, int statusCode) {
        String key = uri + ":" + statusCode;
        statusCounters.computeIfAbsent(key, k -> new LongAdder()).increment();
    }

    @Override
    public String exportMetrics() {

        StringBuilder sb = new StringBuilder("=== Local Metrics ===\n");
        sb.append("QPS:\n");
        requestCounters.forEach((uri, counter) -> {
            sb.append(uri).append(" -> ").append(counter.sum()).append("\n");
        });
        sb.append("\nLatency (ms):\n");
        latencyStats.forEach((uri, stats) -> {
            sb.append(uri).append(" -> avg=").append(stats.getAverage())
                    .append(", max=").append(stats.getMax())
                    .append(", min=").append(stats.getMin())
                    .append("\n");
        });
        sb.append("\nStatus Codes:\n");
        statusCounters.forEach((key, counter) -> {
            sb.append(key).append(" -> ").append(counter.sum()).append("\n");
        });

        return sb.toString();
    }
}
