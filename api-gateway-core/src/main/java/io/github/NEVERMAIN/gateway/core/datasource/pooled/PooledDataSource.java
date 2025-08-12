package io.github.NEVERMAIN.gateway.core.datasource.pooled;

import io.github.NEVERMAIN.gateway.core.datasource.Connection;
import io.github.NEVERMAIN.gateway.core.datasource.DataSource;
import io.github.NEVERMAIN.gateway.core.datasource.DataSourceType;
import io.github.NEVERMAIN.gateway.core.datasource.connection.DubboPooledConnection;
import io.github.NEVERMAIN.gateway.core.mapping.HttpStatement;
import io.github.NEVERMAIN.gateway.core.session.Configuration;
import org.apache.dubbo.common.logger.FluentLogger;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.bootstrap.DubboBootstrap;
import org.apache.dubbo.rpc.service.GenericService;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: 数据源连接池
 */
public class PooledDataSource implements DataSource {

    private Configuration configuration;
    private HttpStatement httpStatement;
    private DataSourceType dataSourceType;

    // 缓存池: 接口名称+版本号
    private static final ConcurrentHashMap<String, GenericService> serviceCache = new ConcurrentHashMap<>();
    // 全局 DubboBootstrap 单例
    private static final DubboBootstrap dubboBootstrap  = DubboBootstrap.getInstance();

    @Override
    public Connection getConnection() {
        switch(dataSourceType){
            case HTTP:
                break;
            case Dubbo:
                // 1.创建服务的唯一标识
                String key = buildServiceKey(httpStatement);
                // 2.从缓存中获取 GenericService
                GenericService genericService = serviceCache.computeIfAbsent(key, k -> createGenericService(httpStatement));
                // 3.创建连接
                return new DubboPooledConnection(genericService, httpStatement, serviceCache);
            default:
                break;

        }
        throw new RuntimeException("DataSourceType: ["+ dataSourceType + "] 没有对应数据源实现.");
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public void setDataSourceType(DataSourceType dataSourceType) {
        this.dataSourceType = dataSourceType;
    }

    public void setHttpStatement(HttpStatement httpStatement) {
        this.httpStatement = httpStatement;
    }

    private String buildServiceKey(HttpStatement httpStatement){
        // 1.配置信息
        String application = httpStatement.getApplication();
        String interfaceName = httpStatement.getInterfaceName();
        return application + ":" + interfaceName;

    }

    private GenericService createGenericService(HttpStatement httpStatement) {

        // 1.配置信息
        String application = httpStatement.getApplication();
        String interfaceName = httpStatement.getInterfaceName();
        // 2.获取服务
        ApplicationConfig applicationConfig = configuration.getApplicationConfig(application);
        RegistryConfig registryConfig = configuration.getRegistryConfig(application);
        ReferenceConfig<GenericService> referenceConfig = configuration.getReferenceConfig(interfaceName);
        dubboBootstrap.application(applicationConfig)
                .registry(registryConfig)
                .reference(referenceConfig)
                .start();

        return referenceConfig.get();
    }

}
