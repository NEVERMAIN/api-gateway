package io.github.NEVERMAIN.gateway.core.datasource.connection;

import com.alibaba.fastjson2.JSON;
import io.github.NEVERMAIN.gateway.core.session.Configuration;
import org.apache.hc.client5.http.async.methods.*;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManager;
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManagerBuilder;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.util.Timeout;

import java.io.Closeable;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class ApacheAsyncHttpClient implements Closeable {

    private final CloseableHttpAsyncClient client;

    public ApacheAsyncHttpClient(Configuration configuration) {
        PoolingAsyncClientConnectionManager connectionManager = PoolingAsyncClientConnectionManagerBuilder.create()
                .setMaxConnPerRoute(200)
                .setMaxConnTotal(1000)
                .build();

        RequestConfig defaultConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(Timeout.ofMilliseconds(300))
                .setResponseTimeout(Timeout.ofMilliseconds(1200))
                .build();

        this.client = HttpAsyncClients.custom()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(defaultConfig)
                .evictExpiredConnections()
                .build();

        this.client.start();
    }

    public CompletionStage<Object> get(String url, Map<String, String> headers, String[] parametersName, Object[] args) {
        CompletableFuture<Object> future = new CompletableFuture<>();
        SimpleHttpRequest request = SimpleRequestBuilder.get(url)
                .addParameter(parametersName[0], args[0].toString())
                .build();
        headers.forEach(request::setHeader);

        // 执行
        client.execute(
                SimpleRequestProducer.create(request),
                SimpleResponseConsumer.create(),
                new FutureCallback<SimpleHttpResponse>() {
                    @Override
                    public void completed(SimpleHttpResponse simpleHttpResponse) {
                        future.complete(simpleHttpResponse.getBodyText());
                    }

                    @Override
                    public void failed(Exception e) {
                        future.completeExceptionally(e);
                    }

                    @Override
                    public void cancelled() {
                        future.cancel(true);
                    }
                }
        );
        return future;
    }

    public CompletionStage<Object> post(String url, Map<String, String> headers, Object[] args) {
        CompletableFuture<Object> future = new CompletableFuture<Object>();

        byte[] data = JSON.toJSONBytes(args[0]);
        SimpleHttpRequest request = SimpleRequestBuilder.post(url)
                .setBody(data, ContentType.APPLICATION_JSON)
                .build();
        headers.forEach(request::setHeader);

        client.execute(
                SimpleRequestProducer.create(request),
                SimpleResponseConsumer.create(),
                new FutureCallback<SimpleHttpResponse>() {
                    public void completed(SimpleHttpResponse res) {
                        future.complete(res.getBodyText());
                    }

                    public void failed(Exception ex) {
                        future.completeExceptionally(ex);
                    }

                    public void cancelled() {
                        future.cancel(true);
                    }
                });
        return future;
    }

    @Override
    public void close() throws IOException {
        client.close();
    }
}
