package io.github.NEVERMAIN.gateway.core.test;

import com.sun.tools.jconsole.JConsoleContext;
import com.sun.tools.jconsole.JConsolePlugin;
import io.github.NEVERMAIN.gateway.core.datasource.DataSourceType;
import io.github.NEVERMAIN.gateway.core.mapping.HttpCommandType;
import io.github.NEVERMAIN.gateway.core.mapping.HttpStatement;
import io.github.NEVERMAIN.gateway.core.session.Configuration;
import io.github.NEVERMAIN.gateway.core.session.defaults.DefaultGatewaySessionFactory;
import io.github.NEVERMAIN.gateway.core.socket.GatewaySocketServer;
import io.netty.channel.Channel;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ApiTest {


    private static final Logger log = LoggerFactory.getLogger(ApiTest.class);

    private static final String application_name = "api-gateway-test";

    private static final String Interfaces = "io.github.NEVERMAIN.gateway.rpc.IActivityBooth";

    private static final String zookeeper_address = "zookeeper://192.168.198.138:2181";

    @Test
    public void test_GenericReference03() throws InterruptedException, ExecutionException {
        // 1.创建配置信息加载注册
        Configuration configuration = new Configuration();
        configuration.setHostName("127.0.0.1");
        configuration.setPort(7397);

        // 2.基于配置构建会话工厂
        DefaultGatewaySessionFactory gatewaySessionFactory = new DefaultGatewaySessionFactory(configuration);

        // 3.创建启动网关网络服务
        GatewaySocketServer server = new GatewaySocketServer(gatewaySessionFactory, configuration);
        Future<Channel> future = Executors.newFixedThreadPool(2).submit(server);
        Channel channel = future.get();

        if (null == channel) throw new RuntimeException("netty server start error channel is null");

        while (!channel.isActive()) {
            log.info("netty server gateway start Ing ...");
            Thread.sleep(500);
        }
        log.info("netty server gateway start Done! {}", channel.localAddress());


        // 4.注册接口
        configuration.registryConfig(application_name, zookeeper_address, Interfaces, "1.0.0");

        HttpStatement httpStatement01 = new HttpStatement(
                "api-gateway-test",
                "io.github.NEVERMAIN.gateway.rpc.IActivityBooth",
                "sayHi",
                "/wg/activity/sayHi",
                HttpCommandType.GET,
                "java.lang.String",
                false
        );
        httpStatement01.setSystemType(DataSourceType.Dubbo);

        HttpStatement httpStatement02 = new HttpStatement(
                "api-gateway-test",
                "io.github.NEVERMAIN.gateway.rpc.IActivityBooth",
                "insert",
                "/wg/activity/insert",
                HttpCommandType.POST,
                "io.github.NEVERMAIN.gateway.rpc.dto.XReq",
                true
        );
        httpStatement02.setSystemType(DataSourceType.Dubbo);

        HttpStatement httpStatement03 = new HttpStatement();
        httpStatement03.setApplication("api-gateway-test");
        httpStatement03.setSystemType(DataSourceType.HTTP);
        httpStatement03.setMethodName("sayHi");
        httpStatement03.setUri("/api/v1/activity/sayHi");
        httpStatement03.setSystemAddress("http://127.0.0.1:8082");
        httpStatement03.setCommandType(HttpCommandType.GET);
        httpStatement03.setParameterType("java.lang.String");
        httpStatement03.setParameterName("str");
        httpStatement03.setAuth(false);


        HttpStatement httpStatement04 = new HttpStatement();
        httpStatement04.setApplication("api-gateway-test");
        httpStatement04.setSystemType(DataSourceType.HTTP);
        httpStatement04.setMethodName("insert");
        httpStatement04.setUri("/api/v1/activity/insert");
        httpStatement04.setSystemAddress("http://127.0.0.1:8082");
        httpStatement04.setCommandType(HttpCommandType.POST);
        httpStatement04.setParameterType("io.github.NEVERMAIN.gateway.rpc.dto.XReq");
        httpStatement04.setParameterName("xReq");
        httpStatement04.setAuth(false);

        configuration.addMapper(httpStatement01);
        configuration.addMapper(httpStatement02);
        configuration.addMapper(httpStatement03);
        configuration.addMapper(httpStatement04);

        Thread.sleep(Long.MAX_VALUE);
    }


}
