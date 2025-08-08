package io.github.NEVERMAIN.gateway.center.domain.data.service;

import io.github.NEVERMAIN.gateway.center.domain.data.adapter.repository.IDataOperationManageRepository;
import io.github.NEVERMAIN.gateway.center.domain.data.model.valobj.*;
import io.github.NEVERMAIN.gateway.center.types.common.OperationRequest;
import io.github.NEVERMAIN.gateway.center.types.common.OperationResponse;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class DataOperationManageService implements IDataOperationManageService {

    @Resource
    private IDataOperationManageRepository repository;

    @Override
    public OperationResponse<GatewayServerDataVO> queryGatewayServer(OperationRequest<String> req) {
        // 1.查询网关服务列表信息
        List<GatewayServerDataVO> list = repository.queryGatewayServerListByPage(req);
        // 2.查询网关服务总数
        int count = repository.queryGatewayServerListCountByPage(req);
        // 3.返回结果
        return new OperationResponse<>(count, list);
    }

    @Override
    public OperationResponse<GatewayServerDetailDataVO> queryGatewayServerDetail(OperationRequest<GatewayServerDetailDataVO> req) {
        // 1.查询数据
        List<GatewayServerDetailDataVO> list = repository.queryGatewayServerDetailListByPage(req);
        // 2.统计数据
        int count = repository.queryGatewayServerDetailListCountByPage(req);
        return new OperationResponse<>(count, list);
    }

    @Override
    public OperationResponse<GatewayDistributionDataVO> queryGatewayDistribution(OperationRequest<GatewayDistributionDataVO> req) {
        // 1.查询数据
        List<GatewayDistributionDataVO> list = repository.queryGatewayDistribution(req);
        // 2.统计数据
        int count = repository.queryGatewayDistributionListCountByPage(req);
        return new OperationResponse<>(count, list);
    }

    @Override
    public OperationResponse<ApplicationSystemDataVO> queryApplicationSystem(OperationRequest<ApplicationSystemDataVO> req) {
        // 1.查询数据
        List<ApplicationSystemDataVO> list = repository.queryApplicationSystemListByPage(req);
        // 2.统计数据
        int count = repository.queryApplicationSystemCountByPage(req);
        return new OperationResponse<>(count, list);
    }

    @Override
    public OperationResponse<ApplicationInterfaceDataVO> queryApplicationInterface(OperationRequest<ApplicationInterfaceDataVO> req) {
        List<ApplicationInterfaceDataVO> list = repository.queryApplicationInterfaceListByPage(req);
        int count = repository.queryApplicationInterfaceListCountByPage(req);
        return new OperationResponse<>(count, list);
    }

    @Override
    public OperationResponse<ApplicationInterfaceMethodDataVO> queryApplicationInterfaceMethodList(OperationRequest<ApplicationInterfaceMethodDataVO> operationRequest) {
        // 1.查询数据
        List<ApplicationInterfaceMethodDataVO> list = repository.queryApplicationInterfaceMethodListByPage(operationRequest);
        // 2.统计数据
        int count = repository.queryApplicationInterfaceMethodListCountByPage(operationRequest);
        // 3.返回结果
        return new OperationResponse<>(count, list);
    }
}
