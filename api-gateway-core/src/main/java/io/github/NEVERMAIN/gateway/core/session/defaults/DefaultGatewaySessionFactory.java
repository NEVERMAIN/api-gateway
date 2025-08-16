package io.github.NEVERMAIN.gateway.core.session.defaults;

import io.github.NEVERMAIN.gateway.core.datasource.Connection;
import io.github.NEVERMAIN.gateway.core.datasource.DataSource;
import io.github.NEVERMAIN.gateway.core.datasource.DataSourceFactory;
import io.github.NEVERMAIN.gateway.core.datasource.DataSourceType;
import io.github.NEVERMAIN.gateway.core.datasource.pooled.PooledDatasourceFactory;
import io.github.NEVERMAIN.gateway.core.datasource.unpooled.UnpooledDataSourceFactory;
import io.github.NEVERMAIN.gateway.core.executor.Executor;
import io.github.NEVERMAIN.gateway.core.mapping.HttpStatement;
import io.github.NEVERMAIN.gateway.core.session.Configuration;
import io.github.NEVERMAIN.gateway.core.session.GatewaySession;
import io.github.NEVERMAIN.gateway.core.session.GatewaySessionFactory;

/**
 * @description 默认网关会话工厂
 */
public class DefaultGatewaySessionFactory implements GatewaySessionFactory {

    private final Configuration configuration;

    public DefaultGatewaySessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public GatewaySession openSession(String uri) {
        // 1.获取数据源连接信息
        DataSourceFactory dataSourceFactory = new PooledDatasourceFactory();
        HttpStatement httpStatement = configuration.getHttpStatement(uri);
        dataSourceFactory.setProperties(configuration, httpStatement, httpStatement.getSystemType());
        DataSource datasource = dataSourceFactory.getDatasource();
        // 2.创建执行器
        Connection connection = datasource.getConnection();
        Executor executor = configuration.newExecutor(connection);
        // 3.创建会话
        return new DefaultGatewaySession(configuration, uri, executor);
    }
}
