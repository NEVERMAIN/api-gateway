package io.github.NEVERMAIN.gateway.center.domain.api.adapter.repository;

import io.github.NEVERMAIN.gateway.center.domain.api.model.entity.ApiMetaData;

import java.util.List;

/**
 * @description: api接口数据仓库
 */
public interface IApiMetaRepository {

    /**
     * 查询接口元数据
     * @return 接口元数据列表
     */
    List<ApiMetaData> queryHttpStatement();

}
