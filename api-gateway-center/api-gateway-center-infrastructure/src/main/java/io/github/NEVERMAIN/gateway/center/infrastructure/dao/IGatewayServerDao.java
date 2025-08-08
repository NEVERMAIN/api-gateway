package io.github.NEVERMAIN.gateway.center.infrastructure.dao;

import io.github.NEVERMAIN.gateway.center.infrastructure.dao.po.GatewayServer;
import io.github.NEVERMAIN.gateway.center.types.common.OperationRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IGatewayServerDao {

    List<GatewayServer> queryGatewayServerList();

    /**
     * 分页查询网关服务列表
     * @param req
     * @return
     */
    List<GatewayServer> queryGatewayServerListByPage(OperationRequest<String> req);

    /**
     * 统计网关服务列表数量
     * @param req
     * @return
     */
    int queryGatewayServerListCountByPage(OperationRequest<String> req);
}
