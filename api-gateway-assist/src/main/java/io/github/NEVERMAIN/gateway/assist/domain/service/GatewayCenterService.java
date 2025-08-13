package io.github.NEVERMAIN.gateway.assist.domain.service;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import io.github.NEVERMAIN.gateway.assist.common.Response;
import io.github.NEVERMAIN.gateway.assist.domain.model.aggregates.ApplicationSystemRichInfo;
import io.github.NEVERMAIN.gateway.assist.exception.GatewayException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @description 网关注册服务
 */
public class GatewayCenterService {

    private static final Logger log = LoggerFactory.getLogger(GatewayCenterService.class);

    final String REGISTRY_URL = "/wg/admin/config/registryGateway";

    final String QUERY_APPLICATION_SYSTEM_RICH_INFO_URL = "/wg/admin/config/queryApplicationSystemRichInfo";

    final String QUERY_REDIS_CONFIG_URI = "/wg/admin/config/queryRedisConfig";

    /**
     * 网关服务注册
     *
     * @param address        注册网关服务的地址
     * @param groupId        分组Id
     * @param gatewayId      网关唯一Id
     * @param gatewayName    网关名称
     * @param gatewayAddress 网关服务地址
     */
    public void doRegister(String address, String groupId, String gatewayId, String gatewayName,
                           String gatewayAddress) {

        log.info("向网关中心注册网关算力服务[开始] gatewayId:{} gatewayName:{} gatewayAddress:{}", gatewayId, gatewayName, gatewayAddress);
        // 1.创建参数
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("groupId", groupId);
        paramMap.put("gatewayId", gatewayId);
        paramMap.put("gatewayName", gatewayName);
        paramMap.put("gatewayAddress", gatewayAddress);

        // 2.构造完整的 URL
        String fullAddress = address + REGISTRY_URL;

        // 3.发起请求,注册算力节点
        String resultStr = null;
        try {
            resultStr = HttpUtil.post(fullAddress, paramMap, 10000);
        } catch (Exception e) {
            log.error("网关服务注册异常,链接资源不可用 fullAddress:{}", fullAddress);
            throw e;
        }
        Response<Boolean> response = JSON.parseObject(resultStr, new TypeReference<Response<Boolean>>() {
        });
        log.info("向网关中心注册网关算力服务[结束] gatewayId:{} gatewayName:{} gatewayAddress:{} 注册结果:{}", gatewayId, gatewayName,
                gatewayAddress, resultStr);
        if (!"0000".equals(response.getCode())) {
            throw new GatewayException("网关服务注册异常 [gatewayId：" + gatewayId + "] 、[gatewayAddress：" + gatewayAddress + "]");
        }

    }

    /**
     * 从网关中心拉取应用服务和接口的配置信息到本地完成注册
     * @param address 网关管理中心的地址
     * @param gatewayId 网关唯一ID
     * @param systemId 系统唯一ID
     * @return
     */
    public ApplicationSystemRichInfo pullApplicationSystemRichInfo(String address, String gatewayId, String systemId) {

        log.info("从网关中心拉取应用服务和接口的配置信息到本地完成注册[开始],gatewayId:{} systemId:{}", gatewayId, systemId);
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("gatewayId", gatewayId);
        paramMap.put("systemId", systemId);

        // 2.构建完整的请求路径
        String fullAddress = address + QUERY_APPLICATION_SYSTEM_RICH_INFO_URL;

        // 3.发起请求
        String resultStr = null;
        try {
            resultStr = HttpUtil.get(fullAddress, paramMap, 10000);
        } catch (Exception e) {
            log.error("网关服务拉取异常,链接资源不可用 fullAddress:{}", fullAddress);
        }
        Response<ApplicationSystemRichInfo> result = JSON.parseObject(resultStr, new TypeReference<Response<ApplicationSystemRichInfo>>() {
        });
        log.info("从网关中心拉取应用服务和接口的配置信息到本地完成注册[结束],gatewayId:{}, 拉取结果:{}", gatewayId, resultStr);

        assert result != null;
        if (!"0000".equals(result.getCode())) {
            throw new GatewayException("从网关中心拉取应用服务和接口的配置信息到本地完成注册异常 [gatewayId：" + gatewayId + "]");
        }
        return result.getData();
    }

    /**
     * 从网关中心拉取Redis配置信息
     * @param address 网关管理中心的地址
     * @return
     */
    public Map<String, String> queryRedisConfig(String address) {

        HashMap<String, Object> params = new HashMap<>();
        String fullAddress = address + QUERY_REDIS_CONFIG_URI;
        String resultStr = "";
        try {
            resultStr = HttpUtil.get(fullAddress, params, 1550);
        } catch (Exception e) {
            log.error("网关服务拉取配置异常，链接资源不可用：{}", address + "/wg/admin/config/queryRedisConfig", e);
        }
        Response<Map<String, String>> result = JSON.parseObject(resultStr, new TypeReference<Response<Map<String, String>>>() {
        });
        log.info("从网关中心拉取Redis配置信息完成。result：{}", resultStr);
        if (!"0000".equals(result.getCode()))
            throw new GatewayException("从网关中心拉取Redis配置信息异常");
        return result.getData();
    }
}
