package io.github.NEVERMAIN.gateway.core.metrics;

import io.github.NEVERMAIN.gateway.core.session.Configuration;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class PrometheusMetricsCollector implements MetricsCollector {

    private final PrometheusMeterRegistry registry;

    private final ConcurrentHashMap<String, Counter> requestCounters = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Timer> latencyTimers = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Counter> statusCounters = new ConcurrentHashMap<>();

    public PrometheusMetricsCollector() {
        registry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
    }

    @Override
    public void recordRequest(String uri) {
        requestCounters.computeIfAbsent(uri,
                        u -> Counter.builder("gateway_requests_total")
                                .description("Total requests by uri")
                                .tag("uri", u)
                                .register(registry))
                .increment();
    }

    @Override
    public void recordLatency(String uri, long durationNanos) {
        latencyTimers.computeIfAbsent(uri,
                        u -> Timer.builder("gateway_request_latency")
                                .description("Request latency by uri")
                                .tag("uri", u)
                                .register(registry))
                .record(durationNanos, TimeUnit.NANOSECONDS);
    }

    @Override
    public void recordStatus(String uri, int statusCode) {
        String key = uri + ":" + statusCode;
        statusCounters.computeIfAbsent(key,
                        k -> Counter.builder("gateway_response_status_total")
                                .description("Response status count by uri and code")
                                .tag("uri", uri)
                                .tag("status", String.valueOf(statusCode))
                                .register(registry))
                .increment();
    }

    @Override
    public String exportMetrics() {
        return registry.scrape();
    }

    public MeterRegistry getRegistry() {
        return registry;
    }

}
