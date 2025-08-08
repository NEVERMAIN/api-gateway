package io.github.NEVERMAIN.gateway.core.datasource;

import io.github.NEVERMAIN.gateway.core.session.Configuration;

/**
 * @description 数据源工厂
 */
public interface DataSourceFactory {

    void setProperties(Configuration configuration, String uri, DataSourceType dataSourceType);

    DataSource getDatasource();

}
