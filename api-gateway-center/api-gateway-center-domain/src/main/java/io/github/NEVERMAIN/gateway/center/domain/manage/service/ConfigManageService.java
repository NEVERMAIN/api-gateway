package io.github.NEVERMAIN.gateway.center.domain.manage.service;


import io.github.NEVERMAIN.gateway.center.domain.manage.IConfigManageService;
import io.github.NEVERMAIN.gateway.center.domain.manage.adapter.repository.IConfigManageRepository;
import io.github.NEVERMAIN.gateway.center.domain.manage.model.aggregate.ApplicationSystemRichInfo;
import io.github.NEVERMAIN.gateway.center.domain.manage.model.valobj.*;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.yaml.snakeyaml.constructor.DuplicateKeyException;

import java.util.*;

@Service
@Slf4j
public class ConfigManageService implements IConfigManageService {

    @Resource
    private IConfigManageRepository configManageRepository;

    @Value("${spring.data.redis.host}")
    private String host;
    @Value("${spring.data.redis.port}")
    private Integer port;

    @Override
    public List<GatewayServerVO> queryGatewayServerList() {
        return configManageRepository.queryGatewayServerList();
    }

    @Override
    public Boolean registryGatewayServerNode(String groupId, String gatewayId, String gatewayName,
                                             String gatewayAddress) {

        // 1.查询网关节点是否已经存在
        GatewayServerDetailVO gatewayServerDetailVO = configManageRepository.queryGatewayServerDetail(gatewayId, gatewayAddress);

        if (null == gatewayServerDetailVO) {
            try {
                // 2.网关节点不存在
                return configManageRepository.registerGatewayServerNode(groupId, gatewayId, gatewayName, gatewayAddress);
            } catch (DuplicateKeyException e) {
                log.error(" 网关服务节点已经存在 ", e);
                return configManageRepository.updateGatewayStatus(gatewayId, gatewayAddress, GatewayStatusTypeEnum.AVAILABLE);
            }
        } else {
            // 3.网关节点已经存在
            return configManageRepository.updateGatewayStatus(gatewayId, gatewayAddress, GatewayStatusTypeEnum.AVAILABLE);
        }

    }

    @Override
    public ApplicationSystemRichInfo queryApplicationSystemRichInfo(String gatewayId, String systemId) {

        // 1.查询出网关ID对应的关联系统ID集合。也就是一个网关ID会被分配一些系统RPC服务注册进来，需要把这些服务查询出来。
        List<String> systemIdList = new ArrayList<>();
        if(StringUtils.isBlank(systemId)){
            systemIdList = configManageRepository.queryGatewayDistributionSystemIdList(gatewayId);
            if (CollectionUtils.isEmpty(systemIdList)) {
                return new ApplicationSystemRichInfo(gatewayId, Collections.emptyList());
            }
        }else{
            systemIdList.add(systemId);
        }

        // 2.查询系统ID对应的系统列表信息
        List<ApplicationSystemVO> allApplicationSystemVOList =
                configManageRepository.queryApplicationSystemList(systemIdList);
        // 3.遍历系统列表
        for (ApplicationSystemVO applicationSystemVO : allApplicationSystemVOList) {
            // 获取系统ID
            String currentSystemId = applicationSystemVO.getSystemId();
            List<ApplicationInterfaceVO> applicationInterfaceVOList =
                    configManageRepository.queryApplicationInterfaceList(currentSystemId);

            // 4.遍历接口下的方法详情
            for (ApplicationInterfaceVO applicationInterfaceVO : applicationInterfaceVOList) {
                String currentInterfaceId = applicationInterfaceVO.getInterfaceId();
                // 4.1查询接口下的方法详情
                List<ApplicationInterfaceMethodVO> applicationInterfaceMethodVOList =
                        configManageRepository.queryApplicationInterfaceMethodList(currentSystemId,currentInterfaceId);

                // 4.2 设置接口方法详情列表
                applicationInterfaceVO.setApplicationInterfaceMethodVOList(applicationInterfaceMethodVOList);
            }

            // 设置系统下的接口列表
            applicationSystemVO.setApplicationInterfaceVOList(applicationInterfaceVOList);

        }

        // 5.返回系统列表
        return new ApplicationSystemRichInfo(gatewayId, allApplicationSystemVOList);

    }

    @Override
    public Boolean registerEvent(String systemId) {
       return configManageRepository.registerEvent(systemId);
    }

    @Override
    public Map<String, String> queryRedisConfig() {
        return new HashMap<String, String>() {{
            put("host", host);
            put("port", String.valueOf(port));
        }};
    }

    @Override
    public List<GatewayServerDetailVO> queryGatewayServerDetailList() {
        return configManageRepository.queryGatewayServerDetailList();
    }


}
