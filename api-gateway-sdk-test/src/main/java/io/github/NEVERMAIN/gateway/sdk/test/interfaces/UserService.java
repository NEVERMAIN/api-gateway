package io.github.NEVERMAIN.gateway.sdk.test.interfaces;

import io.github.NEVERMAIN.gateway.sdk.annotation.ApiProducerClazz;
import io.github.NEVERMAIN.gateway.sdk.annotation.ApiProducerMethod;
import org.springframework.stereotype.Component;


@Component
@ApiProducerClazz(interfaceName = "用户服务", interfaceVersion = "1.0.0", protocolType = "RPC")
public class UserService implements IUserService{

    @ApiProducerMethod(methodName = "探测", uri = "/wg/user/hi", httpCommandType = "POST", auth = 0)
    public String hi(String str) {
        return "h1:" + str + " by api-gateway-sdk";
    }

}
