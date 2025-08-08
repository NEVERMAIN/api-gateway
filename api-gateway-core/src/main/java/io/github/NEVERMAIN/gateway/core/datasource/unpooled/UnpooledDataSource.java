package io.github.NEVERMAIN.gateway.core.datasource.unpooled;

import io.github.NEVERMAIN.gateway.core.datasource.Connection;
import io.github.NEVERMAIN.gateway.core.datasource.DataSource;
import io.github.NEVERMAIN.gateway.core.datasource.DataSourceType;
import io.github.NEVERMAIN.gateway.core.datasource.connection.DubboConnection;
import io.github.NEVERMAIN.gateway.core.mapping.HttpStatement;
import io.github.NEVERMAIN.gateway.core.session.Configuration;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.rpc.service.GenericService;

/**
 * @description 无池化的连接池
 */
public class UnpooledDataSource implements DataSource {

    private Configuration configuration;
    private HttpStatement httpStatement;
    private DataSourceType dataSourceType;


    @Override
    public Connection getConnection() {
        switch (dataSourceType){
            case HTTP:
                // TODO
                break;
            case Dubbo:
                // 1.配置信息
                String application = httpStatement.getApplication();
                String interfaceName = httpStatement.getInterfaceName();
                // 2.获取服务
                ApplicationConfig applicationConfig = configuration.getApplicationConfig(application);
                RegistryConfig registryConfig = configuration.getRegistryConfig(application);
                ReferenceConfig<GenericService> referenceConfig = configuration.getReferenceConfig(interfaceName);
                // 3.创建连接
                return new DubboConnection(applicationConfig, registryConfig, referenceConfig);
            default:
                break;
        }
        throw new RuntimeException("DataSourceType: ["+ dataSourceType + "] 没有对应的数据源实现.");
    }

    public void setDataSourceType(DataSourceType dataSourceType) {
        this.dataSourceType = dataSourceType;
    }

    public void setHttpStatement(HttpStatement httpStatement) {
        this.httpStatement = httpStatement;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }
}
