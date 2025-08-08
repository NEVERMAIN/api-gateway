package io.github.NEVERMAIN.gateway.center.domain.data.adapter.repository;



import io.github.NEVERMAIN.gateway.center.domain.data.model.valobj.*;
import io.github.NEVERMAIN.gateway.center.types.common.OperationRequest;

import java.util.List;

public interface IDataOperationManageRepository {

    List<GatewayServerDataVO> queryGatewayServerListByPage(OperationRequest<String> req);

    int queryGatewayServerListCountByPage(OperationRequest<String> req);

    List<GatewayServerDetailDataVO> queryGatewayServerDetailListByPage(OperationRequest<GatewayServerDetailDataVO> req);

    int queryGatewayServerDetailListCountByPage(OperationRequest<GatewayServerDetailDataVO> req);

    List<GatewayDistributionDataVO> queryGatewayDistribution(OperationRequest<GatewayDistributionDataVO> req);

    int queryGatewayDistributionListCountByPage(OperationRequest<GatewayDistributionDataVO> req);

    /**
     * 查询应用系统列表
     * @param req
     * @return
     */
    List<ApplicationSystemDataVO> queryApplicationSystemListByPage(OperationRequest<ApplicationSystemDataVO> req);

    /**
     * 查询应用系统列表总数
     * @param req
     * @return
     */
    int queryApplicationSystemCountByPage(OperationRequest<ApplicationSystemDataVO> req);

    /**
     * 查询应用接口列表
     * @param req
     * @return
     */
    List<ApplicationInterfaceDataVO> queryApplicationInterfaceListByPage(OperationRequest<ApplicationInterfaceDataVO> req);

    /**
     * 统计应用接口列表总数
     * @param req
     * @return
     */
    int queryApplicationInterfaceListCountByPage(OperationRequest<ApplicationInterfaceDataVO> req);

    /**
     * 查询应用接口方法列表总数
     * @param operationRequest
     * @return
     */
    List<ApplicationInterfaceMethodDataVO> queryApplicationInterfaceMethodListByPage(OperationRequest<ApplicationInterfaceMethodDataVO> operationRequest);

    /**
     * 统计应用接口方法列表总数
     * @param operationRequest
     * @return
     */
    int queryApplicationInterfaceMethodListCountByPage(OperationRequest<ApplicationInterfaceMethodDataVO> operationRequest);
}
