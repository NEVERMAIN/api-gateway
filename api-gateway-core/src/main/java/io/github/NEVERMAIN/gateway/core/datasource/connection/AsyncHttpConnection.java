package io.github.NEVERMAIN.gateway.core.datasource.connection;

import io.github.NEVERMAIN.gateway.core.datasource.BaseConnection;
import io.github.NEVERMAIN.gateway.core.mapping.HttpCommandType;
import io.github.NEVERMAIN.gateway.core.mapping.HttpStatement;
import io.github.NEVERMAIN.gateway.core.session.Configuration;
import org.apache.hc.client5.http.async.methods.*;
import org.apache.hc.core5.net.URIBuilder;
import org.slf4j.MDC;

import java.net.URI;
import java.util.HashMap;
import java.util.concurrent.CompletionStage;

public class AsyncHttpConnection extends BaseConnection {

    private ApacheAsyncHttpClient asyncHttpClient;

    private HttpStatement httpStatement;

    public AsyncHttpConnection(Configuration configuration, HttpStatement httpStatement) {
        this.httpStatement = httpStatement;
        this.asyncHttpClient = configuration.getApacheAsyncHttpClient();

    }

    @Override
    public CompletionStage<Object> executeAsync(String method, String[] parameterTypes, String[] parametersName, Object[] args) {

        String url = buildUrl(httpStatement);
        String traceId = MDC.get("traceId");
        try {
            if (HttpCommandType.GET.equals(httpStatement.getCommandType())) {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("X-Trace-Id", traceId);
                return asyncHttpClient.get(url, headers, parametersName, args);
            } else if (HttpCommandType.POST.equals(httpStatement.getCommandType())) {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
                headers.put("X-Trace-Id", traceId);
                return asyncHttpClient.post(url, headers, args);
            }
        } catch (Exception e) {
            throw new RuntimeException("HTTP request failed", e);
        }
        return null;
    }

    private String buildUrl(HttpStatement httpStatement) {
        return httpStatement.getSystemAddress() + httpStatement.getUri();
    }


}
