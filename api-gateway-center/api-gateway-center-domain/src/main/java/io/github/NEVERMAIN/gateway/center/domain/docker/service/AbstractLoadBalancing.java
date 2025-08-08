package io.github.NEVERMAIN.gateway.center.domain.docker.service;

import io.github.NEVERMAIN.gateway.center.domain.docker.ILoadBalancingService;
import io.github.NEVERMAIN.gateway.center.domain.docker.model.aggregates.NginxConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @description: 负载均衡抽象类
 */
public abstract class AbstractLoadBalancing implements ILoadBalancingService {

    private Logger logger = LoggerFactory.getLogger(AbstractLoadBalancing.class);

    @Override
    public void updateNginxConfig(NginxConfig nginxConfig) throws IOException, InterruptedException {
        // 1.创建 Nginx 配置文件
        String containerFilePath = createNginxConfigFile(nginxConfig);
        logger.info("步骤1: 创建 Nginx 配置文件 containerFilePath:{}", containerFilePath);
        // 3.刷新 Nginx 配置文件
        refreshNginxConfig(nginxConfig.getNginxName());
        logger.info("步骤2: 刷新 Nginx 配置文件 Done!!!");

    }

    protected abstract String createNginxConfigFile(NginxConfig nginxConfig) throws IOException;

    protected abstract void copyDockerFile(String applicationName,
                                           String containerFilePath,
                                           String localNginxPath) throws IOException;

    protected abstract void refreshNginxConfig(String nginxName) throws InterruptedException, IOException;

}
