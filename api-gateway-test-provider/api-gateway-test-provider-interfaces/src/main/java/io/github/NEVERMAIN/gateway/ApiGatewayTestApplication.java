package io.github.NEVERMAIN.gateway;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
public class ApiGatewayTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayTestApplication.class, args);
    }
}