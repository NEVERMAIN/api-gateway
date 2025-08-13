package io.github.NEVERMAIN.gateway.sdk.test.controller;

import com.alibaba.fastjson2.JSON;
import io.github.NEVERMAIN.gateway.sdk.annotation.ApiProducerClazz;
import io.github.NEVERMAIN.gateway.sdk.annotation.ApiProducerMethod;
import io.github.NEVERMAIN.gateway.sdk.test.XReq;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/activity")
@ApiProducerClazz(interfaceName = "活动测试服务", interfaceVersion = "1.0.0", protocolType = "HTTP")
public class ActivityController {


    @GetMapping(value = "/sayHi")
    @ApiProducerMethod(methodName = "Hi", uri = "/api/v1/activity/sayHi", httpCommandType = "GET", auth = 0)
    public String sayHi(String str){
        return "Hi" + str + " by sayHi [ActivityController]";
    }

    @ApiProducerMethod(methodName = "insert", uri = "/api/v1/activity/insert", httpCommandType = "POST", auth = 0)
    @PostMapping(value = "/insert")
    public String insert(@RequestBody XReq xReq){
        return "Hi" + JSON.toJSONString(xReq) + "by insert [ActivityController] ";
    }


}
