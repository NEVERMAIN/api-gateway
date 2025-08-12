package io.github.NEVERMAIN.gateway.core.datasource.pooled;

import io.github.NEVERMAIN.gateway.core.datasource.DataSource;
import io.github.NEVERMAIN.gateway.core.datasource.DataSourceFactory;
import io.github.NEVERMAIN.gateway.core.datasource.DataSourceType;
import io.github.NEVERMAIN.gateway.core.mapping.HttpStatement;
import io.github.NEVERMAIN.gateway.core.session.Configuration;

public class PooledDatasourceFactory implements DataSourceFactory {

    private final PooledDataSource pooledDataSource;

    public PooledDatasourceFactory() {
        pooledDataSource = new PooledDataSource();
    }

    @Override
    public void setProperties(Configuration configuration, String uri, DataSourceType dataSourceType) {
        HttpStatement httpStatement = configuration.getHttpStatement(uri);
        pooledDataSource.setConfiguration(configuration);
        pooledDataSource.setHttpStatement(httpStatement);
        pooledDataSource.setDataSourceType(dataSourceType);
    }

    @Override
    public DataSource getDatasource() {
        return pooledDataSource;
    }
}
