package io.github.NEVERMAIN.gateway.center.domain.api.service;

import io.github.NEVERMAIN.gateway.center.domain.api.model.entity.ApiMetaData;

import java.util.List;

/**
 * @description: API 元数据服务
 */
public interface IApiMetaService {

    List<ApiMetaData> queryHttpStatementList();

}
