package io.github.NEVERMAIN.gateway.assist.config;

import io.github.NEVERMAIN.gateway.assist.application.GatewayApplication;
import io.github.NEVERMAIN.gateway.assist.domain.service.GatewayCenterService;
import io.github.NEVERMAIN.gateway.core.session.defaults.DefaultGatewaySessionFactory;
import io.github.NEVERMAIN.gateway.core.socket.GatewaySocketServer;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @description 网关自动配置类
 */
@Configuration
@EnableConfigurationProperties(GatewayServiceProperties.class)
public class GatewayAutoConfig {

    private static final Logger log = LoggerFactory.getLogger(GatewayAutoConfig.class);

    /**
     * 创建 Redis 链接对象
     * @param properties 网关服务配置文件
     * @param gatewayCenterService 网关中心服务
     * @return
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory(GatewayServiceProperties properties,
                                                         GatewayCenterService gatewayCenterService) {
        // 1.拉取注册中心的 redis 配置信息
        Map<String, String> redisConfig = gatewayCenterService.queryRedisConfig(properties.getAddress());
        // 2.构建 Redis 服务
        RedisStandaloneConfiguration standaloneConfiguration = new RedisStandaloneConfiguration();
        standaloneConfiguration.setHostName(redisConfig.get("host"));
        standaloneConfiguration.setPort(Integer.parseInt(redisConfig.get("port")));
        // 3.默认配置信息
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(100);
        jedisPoolConfig.setMaxWaitMillis(30 * 1000);
        jedisPoolConfig.setMinIdle(20);
        jedisPoolConfig.setMaxIdle(40);
        jedisPoolConfig.setTestWhileIdle(true);
        // 4.创建 Redis 配置
        JedisClientConfiguration jedisClientConfiguration = JedisClientConfiguration.builder()
                .connectTimeout(Duration.ofSeconds(2))
                .clientName("api-gateway-assist-redis-" + properties.getGatewayId())
                .usePooling().poolConfig(jedisPoolConfig).build();

        // 5.实例化 Redis 链接对象
        return new JedisConnectionFactory(standaloneConfiguration, jedisClientConfiguration);

    }

    /**
     * 创建 Redis 监听容器
     * @param properties 网关服务配置文件
     * @param redisConnectionFactory Redis 链接对象
     * @param msgAgreementListenerAdapter 消息监听适配器
     * @return
     */
    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(GatewayServiceProperties properties, RedisConnectionFactory redisConnectionFactory,
                                                                       MessageListenerAdapter msgAgreementListenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        container.addMessageListener(msgAgreementListenerAdapter,new PatternTopic(properties.getGatewayId()));
        return container;
    }

    /**
     * 创建消息监听适配器
     * @param gatewayApplication
     * @return
     */
    @Bean
    public MessageListenerAdapter msgAgreementListenerAdapter(GatewayApplication gatewayApplication) {
        return new MessageListenerAdapter(gatewayApplication, "receiveMessage");
    }

    /**
     * 注册网关服务
     * @return
     */
    @Bean
    public GatewayCenterService registerGatewayService() {
        return new GatewayCenterService();
    }

    /**
     * 创建网关应用
     * @param gatewayServiceProperties 网关服务配置文件
     * @param gatewayCenterService 网关中心服务
     * @param configuration 网关核心配置类
     * @param gatewaySocketServerChannel 网关服务通道
     * @return
     */
    @Bean
    public GatewayApplication gatewayApplication(GatewayServiceProperties gatewayServiceProperties,
                                                 GatewayCenterService gatewayCenterService,
                                                 io.github.NEVERMAIN.gateway.core.session.Configuration configuration,
                                                 Channel gatewaySocketServerChannel) {
        return new GatewayApplication(gatewayServiceProperties, gatewayCenterService, configuration, gatewaySocketServerChannel);
    }

    /**
     * 创建网关核心配置类
     * @param properties
     * @return
     */
    @Bean
    public io.github.NEVERMAIN.gateway.core.session.Configuration gatewayCoreConfiguration(GatewayServiceProperties properties) {

        // 1.创建 Configuration
        io.github.NEVERMAIN.gateway.core.session.Configuration configuration = new io.github.NEVERMAIN.gateway.core.session.Configuration();
        String[] split = properties.getGatewayAddress().split(":");
        configuration.setHostName(split[0]);
        configuration.setPort(Integer.parseInt(split[1].trim()));
        return configuration;
    }

    /**
     * 初始化网关服务
     * @param configuration 网关核心配置类
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Bean
    public Channel initGateway(io.github.NEVERMAIN.gateway.core.session.Configuration configuration) throws ExecutionException, InterruptedException {
        // 1.基于配置构建会话工厂
        DefaultGatewaySessionFactory gatewaySessionFactory = new DefaultGatewaySessionFactory(configuration);
        // 2.创建启动网关网络服务
        GatewaySocketServer server = new GatewaySocketServer(gatewaySessionFactory, configuration);
        Future<Channel> future = Executors.newFixedThreadPool(2).submit(server);
        Channel channel = future.get();
        if (null == channel) {
            throw new RuntimeException("api gateway core netty server start error channel is null");
        }

        while (!channel.isActive()) {
            log.info("api gateway core netty server gateway start Ing ...");
            Thread.sleep(500);
        }
        log.info("api gateway core netty server gateway start Done! {}", channel.localAddress());
        return channel;
    }


}
