package io.github.NEVERMAIN.gateway.core.datasource.connection;

import io.github.NEVERMAIN.gateway.core.datasource.BaseConnection;
import io.github.NEVERMAIN.gateway.core.datasource.Connection;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.bootstrap.DubboBootstrap;
import org.apache.dubbo.rpc.service.GenericService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletionStage;

/**
 * @description Dubbo 的 RPC 接口
 */
public class DubboConnection extends BaseConnection {

    private static final Logger log = LoggerFactory.getLogger(DubboConnection.class);
    /**
     * 泛化调用服务
     */
    private final GenericService genericService;

    public DubboConnection(ApplicationConfig applicationConfig, RegistryConfig registryConfig,
                           ReferenceConfig<GenericService> referenceConfig){
        // 创建连接远程服务
        DubboBootstrap bootstrap = DubboBootstrap.newInstance();
        bootstrap.application(applicationConfig).registry(registryConfig).reference(referenceConfig).start();
        // 获取泛化调用服务
        // TODO 后续修改成异步调用
         this.genericService = referenceConfig.get();
    }


    @Override
    public Object execute(String method, String[] parameterTypes, String[] parametersName, Object[] args) {
        log.info("Dubbo 泛化调用. 方法:{} 参数类型:{} 参数名称:{} 参数:{}", method, parameterTypes, parametersName, args);
        return genericService.$invoke(method,parameterTypes,args);
    }
}
