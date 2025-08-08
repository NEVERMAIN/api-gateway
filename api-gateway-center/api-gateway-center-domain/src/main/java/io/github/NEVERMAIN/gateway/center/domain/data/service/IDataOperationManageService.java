package io.github.NEVERMAIN.gateway.center.domain.data.service;


import io.github.NEVERMAIN.gateway.center.domain.data.model.valobj.*;
import io.github.NEVERMAIN.gateway.center.types.common.OperationRequest;
import io.github.NEVERMAIN.gateway.center.types.common.OperationResponse;

public interface IDataOperationManageService {
    /**
     * 查询网关服务列表
     * @param req
     * @return
     */
    OperationResponse<GatewayServerDataVO> queryGatewayServer(OperationRequest<String> req);

    /**
     * 查询网关服务详情
     * @param req
     * @return
     */
    OperationResponse<GatewayServerDetailDataVO> queryGatewayServerDetail(OperationRequest<GatewayServerDetailDataVO> req);

    /**
     * 查询网关分配数据
     * @param req
     * @return
     */
    OperationResponse<GatewayDistributionDataVO> queryGatewayDistribution(OperationRequest<GatewayDistributionDataVO> req);

    /**
     * 查询应用系统列表
     * @param req
     * @return
     */
    OperationResponse<ApplicationSystemDataVO> queryApplicationSystem(OperationRequest<ApplicationSystemDataVO> req);

    /**
     * 获取应用系统接口列表
     * @param req
     * @return
     */
    OperationResponse<ApplicationInterfaceDataVO> queryApplicationInterface(OperationRequest<ApplicationInterfaceDataVO> req);

    /**
     * 获取应用系统接口方法列表
     * @param operationRequest 请求参数
     * @return
     */
    OperationResponse<ApplicationInterfaceMethodDataVO> queryApplicationInterfaceMethodList(OperationRequest<ApplicationInterfaceMethodDataVO> operationRequest);
}
