package io.github.NEVERMAIN.gateway.center.api;


import io.github.NEVERMAIN.gateway.center.api.dto.ApplicationSystemRichInfoDTO;
import io.github.NEVERMAIN.gateway.center.api.dto.GatewayServerDTO;
import io.github.NEVERMAIN.gateway.center.api.response.Response;

import java.util.List;
import java.util.Map;

/**
 * @description: 配置管理接口
 */
public interface IGatewayConfigManager {

    Response<List<GatewayServerDTO>> queryServerConfig();

    /**
     * 注册网关服务节点
     *
     * @param groupId        分组标识
     * @param gatewayId      网关唯一标识
     * @param gatewayName    网关名称
     * @param gatewayAddress 网关地址
     * @return
     */
    Response<Boolean> registerGatewayServerNode(String groupId, String gatewayId, String gatewayName,
                                                String gatewayAddress);

    /**
     * 聚合查询网关下的系统信息
     *
     * @param gatewayId
     * @return
     */
    Response<ApplicationSystemRichInfoDTO> queryApplicationSystemRichInfo(String gatewayId, String systemId);


    /**
     * 查询当前系统redis的配置
     * @return
     */
    Response<Map<String, String>> queryRedisConfig();


}
