package io.github.NEVERMAIN.gateway.core.test;


import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.net.URIBuilder;
import org.junit.Test;

import java.net.URI;

public class HttpClient5Test {

    @Test
    public void test_sayHi() {

        // 1.构建 HttpClient
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
        // 最大连接数
        connManager.setMaxTotal(100);
        // 每路由最大连接数
        connManager.setDefaultMaxPerRoute(20);
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(connManager).build();

        try {
            // 2.构建带参数的 URL
            URI uri = new URIBuilder("http://127.0.0.1:8082/api/v1/activity/sayHi")
                    .addParameter("str", "NEVERMAIN").build();

            // 3.创建 GET 请求
            HttpGet httpGet = new HttpGet(uri);

            // 4. 执行请求并处理响应
            CloseableHttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String result = EntityUtils.toString(entity);
                System.out.println("响应状态: " + response.getCode());
                System.out.println("响应内容: " + result);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (Exception e) {

            }
        }
    }

    @Test
    public void test_insert() {
        // 1. 创建 HttpClient
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // 2. 构建 POST 请求（JSON 格式）
            HttpPost httpPost = new HttpPost("http://127.0.0.1:8082/api/v1/activity/insert");
            String jsonBody = "{\n" +
                    "    \"uid\": \"10001\",\n" +
                    "    \"name\": \"Antonie\"\n" +
                    "}";

            // 3. 设置 JSON 请求体和 Header
            StringEntity entity = new StringEntity(jsonBody, ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "application/json");

            // 4. 发送请求并解析响应
            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                String result = EntityUtils.toString(response.getEntity());
                System.out.println("创建状态: " + response.getCode());
                System.out.println("返回数据: " + result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
