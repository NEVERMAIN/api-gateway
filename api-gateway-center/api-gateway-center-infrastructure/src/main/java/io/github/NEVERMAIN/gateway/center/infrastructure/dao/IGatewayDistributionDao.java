package io.github.NEVERMAIN.gateway.center.infrastructure.dao;

import io.github.NEVERMAIN.gateway.center.domain.data.model.valobj.GatewayDistributionDataVO;
import io.github.NEVERMAIN.gateway.center.infrastructure.dao.po.GatewayDistribution;
import io.github.NEVERMAIN.gateway.center.types.common.OperationRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IGatewayDistributionDao {

    /**
     * 查询网关分发的系统ID列表
     * @param gatewayId
     * @return
     */
    List<String> queryGatewayDistributionSystemIdList(String gatewayId);

    /**
     * 查询网关分派系统
     * @param systemId
     * @return
     */
    String queryGatewayDistribution(String systemId);

    /**
     * 查询网关分派系统列表
     * @param req
     * @return
     */
    List<GatewayDistribution> queryGatewayDistributionListByPage(OperationRequest<GatewayDistributionDataVO> req);

    /**
     * 查询网关分派系统列表总数
     * @param req
     * @return
     */
    int queryGatewayDistributionListCountByPage(OperationRequest<GatewayDistributionDataVO> req);
}
