package io.github.NEVERMAIN.gateway.interfaces;

import com.alibaba.fastjson2.JSON;
import io.github.NEVERMAIN.gateway.rpc.IActivityBooth;
import io.github.NEVERMAIN.gateway.rpc.dto.XReq;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService(version = "1.0.0",interfaceClass = IActivityBooth.class)
public class ActivityBooth implements IActivityBooth {
    @Override
    public String sayHi(String str) {
        System.out.println("api-gateway-test-provider sayHi receive: " + str);
        return "hi "+ str + " by api-gateway-test-provider ";
    }

    @Override
    public String insert(XReq req) {
        System.out.println("api-gateway-test-provider insert receive: " + req);
        return " hi "+ JSON.toJSONString(req) + " by api-gateway-test-provider ";
    }

    @Override
    public String test(String str, XReq req) {
        System.out.println("api-gateway-test-provider test receive: " + str + " " + JSON.toJSONString(req));
        return " hi "+ str + " "+ JSON.toJSONString(req) + " by api-gateway-test-provider ";
    }
}
