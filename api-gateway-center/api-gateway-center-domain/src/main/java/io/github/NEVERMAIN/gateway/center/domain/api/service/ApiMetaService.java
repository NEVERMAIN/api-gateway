package io.github.NEVERMAIN.gateway.center.domain.api.service;

import io.github.NEVERMAIN.gateway.center.domain.api.adapter.repository.IApiMetaRepository;
import io.github.NEVERMAIN.gateway.center.domain.api.model.entity.ApiMetaData;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApiMetaService implements IApiMetaService{

    @Resource
    private IApiMetaRepository apiMetaRepository;

    @Override
    public List<ApiMetaData> queryHttpStatementList() {
        return apiMetaRepository.queryHttpStatement();
    }
}
