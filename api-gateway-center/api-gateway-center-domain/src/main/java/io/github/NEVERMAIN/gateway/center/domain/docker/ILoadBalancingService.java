package io.github.NEVERMAIN.gateway.center.domain.docker;


import io.github.NEVERMAIN.gateway.center.domain.docker.model.aggregates.NginxConfig;
import io.github.NEVERMAIN.gateway.center.domain.manage.model.valobj.GatewayServerDetailVO;

import java.io.IOException;
import java.util.List;

/**
 * @description: 负载均衡配置服务
 */
public interface ILoadBalancingService {

    /**
     * 更新 Nginx 配置文件
     * @param nginxConfig nginx 配置
     * @throws IOException
     * @throws InterruptedException
     */
    void updateNginxConfig(NginxConfig nginxConfig) throws IOException, InterruptedException;

    /**
     * 组装 Nginx 配置文件
     * @param gatewayServerDetailVOList 网关服务详情列表
     */
    NginxConfig assembleNginxConfig(List<GatewayServerDetailVO> gatewayServerDetailVOList);

}
