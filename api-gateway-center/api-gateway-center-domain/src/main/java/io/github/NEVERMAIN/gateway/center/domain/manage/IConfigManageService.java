package io.github.NEVERMAIN.gateway.center.domain.manage;

import io.github.NEVERMAIN.gateway.center.domain.manage.model.aggregate.ApplicationSystemRichInfo;
import io.github.NEVERMAIN.gateway.center.domain.manage.model.valobj.GatewayServerDetailVO;
import io.github.NEVERMAIN.gateway.center.domain.manage.model.valobj.GatewayServerVO;

import java.util.List;
import java.util.Map;

public interface IConfigManageService {

    List<GatewayServerVO> queryGatewayServerList();

    /**
     * 注册网关节点
     * @param groupId 分组唯一ID
     * @param gatewayId 网关唯一ID
     * @param gatewayName 网关名称
     * @param gatewayAddress 网关通信地址
     * @return
     */
    Boolean registryGatewayServerNode(String groupId, String gatewayId, String gatewayName, String gatewayAddress);

    /**
     * 聚合查询网关下服务信息
     * @param gatewayId
     * @return
     */
    ApplicationSystemRichInfo queryApplicationSystemRichInfo(String gatewayId, String systemId);

    /**
     * 注册事件
     * @param systemId
     * @return
     */
    Boolean registerEvent(String systemId);

    Map<String, String> queryRedisConfig();

    List<GatewayServerDetailVO> queryGatewayServerDetailList();

}
