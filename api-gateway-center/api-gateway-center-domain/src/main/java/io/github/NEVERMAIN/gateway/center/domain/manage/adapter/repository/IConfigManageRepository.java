package io.github.NEVERMAIN.gateway.center.domain.manage.adapter.repository;


import io.github.NEVERMAIN.gateway.center.domain.manage.model.valobj.*;

import java.util.List;

public interface IConfigManageRepository {

    List<GatewayServerVO> queryGatewayServerList();

    GatewayServerDetailVO queryGatewayServerDetail(String gatewayId, String gatewayAddress);

    Boolean registerGatewayServerNode(String groupId, String gatewayId, String gatewayName, String gatewayAddress);

    Boolean updateGatewayStatus(String gatewayId, String gatewayAddress, GatewayStatusTypeEnum gatewayServerStatusTypeEnum);

    List<String> queryGatewayDistributionSystemIdList(String gatewayId);

    List<ApplicationSystemVO> queryApplicationSystemList(List<String> systemIdList);

    List<ApplicationInterfaceVO> queryApplicationInterfaceList(String systemId);

    List<ApplicationInterfaceMethodVO> queryApplicationInterfaceMethodList(String systemId, String interfaceId);

    List<ApplicationInterfaceVO> queryApplicationInterfaceListBySystemIds(List<String> systemIdList);

    List<ApplicationInterfaceMethodVO> queryApplicationInterfaceMethodListBySystemIds(List<String> systemIdList);

    Boolean registerEvent(String systemId);

    /**
     * 查询所有网关服务器详情列表
     * @return
     */
    List<GatewayServerDetailVO> queryGatewayServerDetailList();

}
