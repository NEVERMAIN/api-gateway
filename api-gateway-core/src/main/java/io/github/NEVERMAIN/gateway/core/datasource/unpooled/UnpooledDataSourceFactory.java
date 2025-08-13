package io.github.NEVERMAIN.gateway.core.datasource.unpooled;

import io.github.NEVERMAIN.gateway.core.datasource.DataSource;
import io.github.NEVERMAIN.gateway.core.datasource.DataSourceFactory;
import io.github.NEVERMAIN.gateway.core.datasource.DataSourceType;
import io.github.NEVERMAIN.gateway.core.mapping.HttpStatement;
import io.github.NEVERMAIN.gateway.core.session.Configuration;

/**
 * @description 数据源工厂
 */
public class UnpooledDataSourceFactory implements DataSourceFactory {

    protected UnpooledDataSource dataSource;

    public UnpooledDataSourceFactory() {
        this.dataSource = new UnpooledDataSource();
    }

    @Override
    public void setProperties(Configuration configuration, HttpStatement httpStatement, DataSourceType dataSourceType) {
        this.dataSource.setConfiguration(configuration);
        this.dataSource.setHttpStatement(httpStatement);
        this.dataSource.setDataSourceType(dataSourceType);
    }

    @Override
    public DataSource getDatasource() {
        return dataSource;
    }
}
