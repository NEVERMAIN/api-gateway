package io.github.NEVERMAIN.gateway.core.datasource.connection;

import com.alibaba.fastjson2.JSON;
import com.fasterxml.jackson.core.JsonToken;
import io.github.NEVERMAIN.gateway.core.datasource.Connection;
import io.github.NEVERMAIN.gateway.core.mapping.HttpCommandType;
import io.github.NEVERMAIN.gateway.core.mapping.HttpStatement;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpUriRequest;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.net.URIBuilder;
import org.apache.hc.core5.util.Timeout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.nio.charset.StandardCharsets;

/**
 * @description: HTTP 池化的连接对象
 */
public class HTTPConnection implements Connection {

    private static final Logger logger = LoggerFactory.getLogger(HTTPConnection.class);

    private final HttpStatement httpStatement;

    private final CloseableHttpClient httpClient;

    public HTTPConnection(HttpStatement httpStatement) {
        this.httpStatement = httpStatement;
        this.httpClient = HttpClients.custom()
                .setConnectionManager(new PoolingHttpClientConnectionManager())
                .evictExpiredConnections()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(Timeout.ofMilliseconds(3000))
                        .setResponseTimeout(Timeout.ofMilliseconds(5000))
                        .build())
                .build();
    }

    @Override
    public Object execute(String method, String[] parameterTypes, String[] parametersName, Object[] args) {

        String url = buildUrl(httpStatement);
        HttpUriRequest request = null;
        try {
            if (HttpCommandType.GET.equals(httpStatement.getCommandType())) {
                URI uri = new URIBuilder(url)
                        .addParameter(parametersName[0], args[0].toString()).build();
                request = new HttpGet(uri);
            } else if (HttpCommandType.POST.equals(httpStatement.getCommandType())) {
                HttpPost httpPost = new HttpPost(url);
                // 设置请求体
                StringEntity entity = new StringEntity(JSON.toJSONString(args[0]), ContentType.APPLICATION_JSON);
                httpPost.setEntity(entity);
                // 设置请求头
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-Type", "application/json");
                request = httpPost;
            }

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                return EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            throw new RuntimeException("HTTP request failed", e);
        }
    }

    private String buildUrl(HttpStatement httpStatement) {
        return httpStatement.getSystemAddress() + httpStatement.getUri();
    }


}
