package io.github.NEVERMAIN.gateway.controller;


import com.alibaba.fastjson2.JSON;
import io.github.NEVERMAIN.gateway.rpc.dto.XReq;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/activity")
public class ActivityController {

    private static final Logger logger = LoggerFactory.getLogger(ActivityController.class);

    @GetMapping(value = "/sayHi")
    public String sayHi(@RequestParam String str) {
        logger.info("[ActivityController] sayHi receive: {}", str);
        // 如果获取当前请求 HttpServletRequest
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String traceId = request.getHeader("X-Trace-Id");
        return "hi " + str + " by [ActivityController]  traceId:" + traceId;
    }

    @PostMapping(value = "/insert")
    public String insert(@RequestBody XReq xReq) {
        logger.info("[ActivityController] insert receive: {}", JSON.toJSONString(xReq));
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String traceId = request.getHeader("X-Trace-Id");
        return " hi " + JSON.toJSONString(xReq) + " by [ActivityController]  traceId:"+ traceId;
    }


}
