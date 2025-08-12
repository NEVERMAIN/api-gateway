package io.github.NEVERMAIN.gateway.controller;


import com.alibaba.fastjson2.JSON;
import io.github.NEVERMAIN.gateway.rpc.dto.XReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/activity")
public class ActivityController {

    private static final Logger logger = LoggerFactory.getLogger(ActivityController.class);

    @GetMapping(value = "/sayHi")
    public String sayHi(@RequestParam String str) {
        logger.info("[ActivityController] sayHi receive: {}", str);
        return "hi " + str + " by [ActivityController] ";
    }

    @PostMapping(value = "/insert")
    public String insert(@RequestBody XReq xReq) {
        logger.info("[ActivityController] insert receive: {}", JSON.toJSONString(xReq));
        return " hi " + JSON.toJSONString(xReq) + " by [ActivityController] ";
    }


}
