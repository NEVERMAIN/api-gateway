package io.github.NEVERMAIN.gateway.core.session;

import io.github.NEVERMAIN.gateway.core.authorization.IAuth;
import io.github.NEVERMAIN.gateway.core.authorization.auth.AuthService;
import io.github.NEVERMAIN.gateway.core.bind.IGenericReference;
import io.github.NEVERMAIN.gateway.core.bind.MapperRegistry;
import io.github.NEVERMAIN.gateway.core.circuitbreaker.CircuitBreakerFactory;
import io.github.NEVERMAIN.gateway.core.datasource.Connection;
import io.github.NEVERMAIN.gateway.core.executor.Executor;
import io.github.NEVERMAIN.gateway.core.executor.SimpleExecutor;
import io.github.NEVERMAIN.gateway.core.mapping.HttpStatement;
import io.github.NEVERMAIN.gateway.core.metrics.LocalMetricsCollector;
import io.github.NEVERMAIN.gateway.core.metrics.MetricsCollector;
import io.github.NEVERMAIN.gateway.core.metrics.PrometheusMetricsCollector;
import io.github.NEVERMAIN.gateway.core.ratelimit.RedisClientFactory;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.rpc.service.GenericService;

import java.util.HashMap;
import java.util.Map;

/**
 * @description 会话生命周期配置项
 */
public class Configuration {

    // 网关 Netty 服务地址
    private String hostName = "127.0.0.1";
    // 网关 Netty 服务端口
    private int port = 7397;
    // 网关 Netty 服务线程配置
    private int bossNThreads = 1;
    private int workNThreads = 4;

    private final MapperRegistry mapperRegistry = new MapperRegistry(this);

    /**
     * 网关接口映射容器
     * uri --> HttpStatement
     */
    private final Map<String, HttpStatement> httpStatementMap = new HashMap<>();


    private final IAuth auth = new AuthService();

    /**
     * 限流窗口时间
     */
    private String timeWindow = "1";
    /**
     * 限流最大请求数
     */
    private String maxRequests = "1000";
    /**
     * Redis 客户端工厂
     */
    private final RedisClientFactory redisClientFactory = new RedisClientFactory();


    /**
     * 熔断器工厂
     */
    private final CircuitBreakerFactory circuitBreakerFactory = new CircuitBreakerFactory();

    /**
     * 本地监控
     */
    private final MetricsCollector metricsCollector = new PrometheusMetricsCollector();


    // RPC 应用服务配置项 api-gateway-test
    private final Map<String, ApplicationConfig> applicationConfigMap = new HashMap<>();
    // RPC 注册中心配置项 zookeeper://127.0.0.1:2181、nacos://127.0.0.1:8848
    private final Map<String, RegistryConfig> registryConfigMap = new HashMap<>();
    // RPC 泛化服务配置项 io.github.NEVERMAIN.gateway.rpc.IActivityBooth
    private final Map<String, ReferenceConfig<GenericService>> referenceConfigMap = new HashMap<>();


    public Configuration() {
    }

    public synchronized void registryConfig(String applicationName, String registryAddress, String interfaceName, String version){

        if(applicationConfigMap.get(applicationName) == null){
            // 1.应用配置
            ApplicationConfig applicationConfig = new ApplicationConfig();
            applicationConfig.setName(applicationName);
            applicationConfig.setQosEnable(false); // 避免 QOS 冲突

            applicationConfigMap.put(applicationName, applicationConfig);
        }

        if(registryConfigMap.get(applicationName) == null){
            // 2.注册中心配置
            RegistryConfig registryConfig = new RegistryConfig();
            registryConfig.setAddress(registryAddress);
            registryConfig.setRegister(false);

            registryConfigMap.put(applicationName, registryConfig);
        }

        if(referenceConfigMap.get(interfaceName) == null){
            // 3.泛化服务配置
            ReferenceConfig<GenericService> reference = new ReferenceConfig<>();
            reference.setInterface(interfaceName);
            reference.setVersion(version); // 和服务端版本匹配
            reference.setGeneric("true");  // 启用泛化调用
            reference.setTimeout(5000);

            referenceConfigMap.put(interfaceName, reference);
        }

    }


    /**
     * 创建简单执行器
     * @param connection 数据源连接
     * @return 执行器
     */
    public Executor newExecutor(Connection connection) {
        return new SimpleExecutor(this,connection);
    }

    /**
     * 获取 RPC 应用服务配置项
     *
     * @param applicationName 应用名
     * @return
     */
    public ApplicationConfig getApplicationConfig(String applicationName) {
        return applicationConfigMap.get(applicationName);
    }

    /**
     * 获取 RPC 注册中心配置项
     *
     * @param applicationName
     * @return
     */
    public RegistryConfig getRegistryConfig(String applicationName) {
        return registryConfigMap.get(applicationName);
    }

    /**
     * 获取 RPC 泛化服务配置项
     *
     * @param interfaceName
     * @return
     */
    public ReferenceConfig<GenericService> getReferenceConfig(String interfaceName) {
        return referenceConfigMap.get(interfaceName);
    }

    /**
     * 添加泛化服务
     *
     * @param httpStatement HTTP接口方法
     */
    public void addMapper(HttpStatement httpStatement) {
        mapperRegistry.addMapper(httpStatement);
    }

    /**
     * 获取泛化服务
     *
     * @param uri
     * @return
     */
    public IGenericReference getMapper(String uri, GatewaySession gatewaySession) {
        return mapperRegistry.getMapper(uri, gatewaySession);
    }

    /**
     * 添加 HTTP 接口方法
     *
     * @param httpStatement
     */
    public void addHttpStatement(HttpStatement httpStatement) {
        httpStatementMap.put(httpStatement.getUri(), httpStatement);
    }

    /**
     * 获取 HTTP 接口方法
     *
     * @param uri
     * @return
     */
    public HttpStatement getHttpStatement(String uri) {
        return httpStatementMap.get(uri);
    }

    /**
     * 鉴权
     * @param uid 用户ID
     * @param token 令牌
     * @return
     */
    public boolean authValidate(String uid,String token){
        return auth.validate(uid,token);
    }

    public int getPort() {
        return port;
    }

    public String getHostName() {
        return hostName;
    }

    public int getBossNThreads() {
        return bossNThreads;
    }

    public int getWorkNThreads() {
        return workNThreads;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public void setWorkNThreads(int workNThreads) {
        this.workNThreads = workNThreads;
    }

    public void setBossNThreads(int bossNThreads) {
        this.bossNThreads = bossNThreads;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public RedisClientFactory getRedisClientFactory() {
        return redisClientFactory;
    }

    public String getTimeWindow() {
        return timeWindow;
    }

    public void setTimeWindow(String timeWindow) {
        this.timeWindow = timeWindow;
    }

    public String getMaxRequests() {
        return maxRequests;
    }

    public void setMaxRequests(String maxRequests) {
        this.maxRequests = maxRequests;
    }

    public CircuitBreakerFactory getCircuitBreakerFactory() {
        return circuitBreakerFactory;
    }

    public MetricsCollector getMetricsCollector() {
        return metricsCollector;
    }
}
