package io.github.NEVERMAIN.gateway.core.datasource;

/**
 * @description 数据源类型枚举
 */
public enum DataSourceType {

    RPC,
    HTTP,
    MQ ;

    public static DataSourceType getDataSourceType(String type) {
        for (DataSourceType value : DataSourceType.values()) {
            if (value.name().equals(type)) {
                return value;
            }
        }
        return null;
    }

}
