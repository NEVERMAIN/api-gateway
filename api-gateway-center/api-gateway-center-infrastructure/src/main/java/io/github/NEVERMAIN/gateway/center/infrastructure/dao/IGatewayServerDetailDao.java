package io.github.NEVERMAIN.gateway.center.infrastructure.dao;

import io.github.NEVERMAIN.gateway.center.domain.data.model.valobj.GatewayServerDetailDataVO;
import io.github.NEVERMAIN.gateway.center.infrastructure.dao.po.GatewayServerDetail;
import io.github.NEVERMAIN.gateway.center.types.common.OperationRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IGatewayServerDetailDao {

    /**
     * 查询网关服务详情
     * @param req
     * @return
     */
    GatewayServerDetail queryGatewayServerDetail(GatewayServerDetail req);

    /**
     * 新增网关服务详情
     * @param req 网关服务详情
     * @return
     */
    Boolean insert(GatewayServerDetail req);

    /**
     * 更新网关服务状态
     * @param req
     * @return
     */
    Boolean updateGatewayStatus(GatewayServerDetail req);

    /**
     * 查询网关服务详情列表
     * @param req
     * @return
     */
    List<GatewayServerDetail> queryGatewayServerDetailListByPage(OperationRequest<GatewayServerDetailDataVO> req);

    /**
     * 统计网关服务详情列表数量
     * @param req
     * @return
     */
    int queryGatewayServerDetailListCountByPage(OperationRequest<GatewayServerDetailDataVO> req);

    /**
     * 查询所有网关服务详情列表
     * @return
     */
    List<GatewayServerDetail> queryGatewayServerDetailList();

}
