package io.github.NEVERMAIN.gateway.core.datasource;

/**
 * @description 数据源接口,RPC、HTTP 都当做连接的数据源使用
 */
public interface DataSource {

    /**
     * 获取连接
     * @return 连接
     */
    Connection getConnection();

}
