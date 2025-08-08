package io.github.NEVERMAIN.gateway.center.infrastructure.adapter.repository;

import io.github.NEVERMAIN.gateway.center.domain.api.adapter.repository.IApiMetaRepository;
import io.github.NEVERMAIN.gateway.center.domain.api.model.entity.ApiMetaData;
import io.github.NEVERMAIN.gateway.center.domain.api.model.valobj.ApiAuthTypeEnum;
import io.github.NEVERMAIN.gateway.center.infrastructure.dao.IHttpStatementDao;
import io.github.NEVERMAIN.gateway.center.infrastructure.dao.po.HttpStatement;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: APi 仓储实现类
 */
@Repository
public class ApiMetaRepository implements IApiMetaRepository {

    @Resource
    private IHttpStatementDao httpStatementDao;

    @Override
    public List<ApiMetaData> queryHttpStatement() {

        // 1. 查询接口元数据
        List<HttpStatement> httpStatementList = httpStatementDao.queryHttpStatementList();

        // 2. 转换实体数据
        ArrayList<ApiMetaData> apiMetaDataList = new ArrayList<>(httpStatementList.size());
        httpStatementList.forEach(httpStatement -> {

            ApiMetaData apiMetaData = new ApiMetaData();
            apiMetaData.setApplication(httpStatement.getApplication());
            apiMetaData.setInterfaceName(httpStatement.getInterfaceName());
            apiMetaData.setMethodName(httpStatement.getMethodName());
            apiMetaData.setParameterType(httpStatement.getParameterType());
            apiMetaData.setUri(httpStatement.getUri());
            apiMetaData.setHttpCommandType(httpStatement.getHttpCommandType());
            apiMetaData.setAuth(ApiAuthTypeEnum.valuesOf(httpStatement.getAuth()));

            apiMetaDataList.add(apiMetaData);
        });

        return apiMetaDataList;
    }
}
