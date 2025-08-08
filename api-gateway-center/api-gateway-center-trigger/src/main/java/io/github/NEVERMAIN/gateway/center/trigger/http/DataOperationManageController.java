package io.github.NEVERMAIN.gateway.center.trigger.http;

import com.alibaba.fastjson2.JSON;
import io.github.NEVERMAIN.gateway.center.domain.data.model.valobj.*;
import io.github.NEVERMAIN.gateway.center.domain.data.service.IDataOperationManageService;
import io.github.NEVERMAIN.gateway.center.types.common.OperationRequest;
import io.github.NEVERMAIN.gateway.center.types.common.OperationResponse;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/wg/admin/data")
@Slf4j
public class DataOperationManageController {

    @Resource
    private IDataOperationManageService dataOperationManageService;

    @GetMapping(value = "queryGatewayServer", produces = "application/json;charset=UTF-8")
    public OperationResponse<GatewayServerDataVO> queryGatewayServer(
            @RequestParam String groupId, @RequestParam String page, @RequestParam String limit) {

        try {

            log.info("查询网关服务数据开始 groupId:{} page:{} limit:{}", groupId, page, limit);
            OperationRequest<String> req = new OperationRequest<>(page, limit);
            req.setData(groupId);
            OperationResponse<GatewayServerDataVO> operationResult = dataOperationManageService.queryGatewayServer(req);
            log.info("查询网关服务数据完成 operationResponse:{}", JSON.toJSONString(operationResult));
            return operationResult;
        } catch (Exception e) {
            log.error("查询网关服务数据异常 groupId:{}", groupId, e);
            return OperationResponse.<GatewayServerDataVO>builder()
                    .pageTotal(0)
                    .list(null)
                    .build();
        }

    }

    @GetMapping(value = "queryGatewayServerDetail", produces = "application/json;charset=UTF-8")
    public OperationResponse<GatewayServerDetailDataVO> queryGatewayServerDetail(
            @RequestParam String groupId, @RequestParam String gatewayId,
            @RequestParam String page, @RequestParam String limit) {

        try {
            log.info("查询网关服务详情数据开始 groupId:{} gatewayId:{} page:{} limit:{}", groupId, gatewayId, page, limit);
            OperationRequest<GatewayServerDetailDataVO> req = new OperationRequest<>(page, limit);
            req.setData(new GatewayServerDetailDataVO(groupId, gatewayId));
            OperationResponse<GatewayServerDetailDataVO> operationResult = dataOperationManageService.queryGatewayServerDetail(req);
            log.info("查询网关服务详情数据完成 operationResult:{}", JSON.toJSONString(operationResult));
            return operationResult;
        } catch (Exception e) {
            log.error("查询网关服务详情数据异常 groupId:{}", groupId, e);
            return OperationResponse.<GatewayServerDetailDataVO>builder()
                    .pageTotal(0)
                    .list(null)
                    .build();
        }
    }

    @GetMapping(value = "queryGatewayDistribution", produces = "application/json;charset=UTF-8")
    public OperationResponse<GatewayDistributionDataVO> queryGatewayDistribution(
            @RequestParam String groupId, @RequestParam String gatewayId,
            @RequestParam String page, @RequestParam String limit) {

        try {

            log.info("查询网关分配数据开始 groupId:{} gatewayId:{} page:{} limit:{}", groupId, gatewayId, page, limit);
            OperationRequest<GatewayDistributionDataVO> req = new OperationRequest<>(page, limit);
            req.setData(new GatewayDistributionDataVO(groupId, gatewayId));
            OperationResponse<GatewayDistributionDataVO> operationResponse = dataOperationManageService.queryGatewayDistribution(req);
            log.info("查询网关分配数据完成 operationResponse:{}", JSON.toJSONString(operationResponse));
            return operationResponse;
        } catch (Exception e) {
            log.error("查询网关分配数据异常 groupId:{}", groupId, e);
            return OperationResponse.<GatewayDistributionDataVO>builder()
                    .pageTotal(0)
                    .list(null)
                    .build();
        }
    }

    @GetMapping(value = "queryApplicationSystem", produces = "application/json;charset=UTF-8")
    public OperationResponse<ApplicationSystemDataVO> queryApplicationSystem(@RequestParam String systemId,
                                                                             @RequestParam String systemName,
                                                                             @RequestParam String page,
                                                                             @RequestParam String limit) {
        try {
            log.info("查询应用系统信息开始 systemId:{} systemName:{} page:{} limit:{}", systemId, systemName, page, limit);
            OperationRequest<ApplicationSystemDataVO> req = new OperationRequest<>(page, limit);
            req.setData(new ApplicationSystemDataVO(systemId, systemName));
            OperationResponse<ApplicationSystemDataVO> operationResponse = dataOperationManageService.queryApplicationSystem(req);
            log.info("查询应用系统信息完成 operationResponse:{}", operationResponse);
            return operationResponse;
        } catch (Exception e) {
            log.error("查询应用系统信息异常 systemId:{} systemName:{}", systemId, systemName, e);
            return OperationResponse.<ApplicationSystemDataVO>builder()
                    .pageTotal(0)
                    .list(null)
                    .build();
        }
    }

    @GetMapping(value = "queryApplicationInterface", produces = "application/json;charset=UTF-8")
    public OperationResponse<ApplicationInterfaceDataVO> queryApplicationInterface(
            @RequestParam String systemId,
            @RequestParam String interfaceId,
            @RequestParam String page,
            @RequestParam String limit) {

        try {
            log.info("查询应用接口信息开始 systemId:{} interfaceId:{} page:{} limit:{}", systemId, interfaceId, page, limit);
            OperationRequest<ApplicationInterfaceDataVO> req = new OperationRequest<>(page, limit);
            req.setData(new ApplicationInterfaceDataVO(systemId, interfaceId));
            OperationResponse<ApplicationInterfaceDataVO> operationResponse = dataOperationManageService.queryApplicationInterface(req);
            log.info("查询应用接口信息完成 operationResponse:{}", JSON.toJSONString(operationResponse));
            return operationResponse;
        } catch (Exception e) {
            log.error("查询应用接口信息异常 systemId:{} interfaceId:{}", systemId, interfaceId, e);
            return OperationResponse.<ApplicationInterfaceDataVO>builder()
                    .pageTotal(0)
                    .list(null)
                    .build();
        }
    }

    @GetMapping(value = "queryApplicationInterfaceMethodList", produces = "application/json;charset=UTF-8")
    public OperationResponse<ApplicationInterfaceMethodDataVO> queryApplicationInterfaceMethodList(
            @RequestParam String systemId,
            @RequestParam String interfaceId,
            @RequestParam String page,
            @RequestParam String limit) {

        try {
            log.info("查询应用接口方法信息开始 systemId:{} interfaceId:{} page:{} limit:{}", systemId, interfaceId, page, limit);
            OperationRequest<ApplicationInterfaceMethodDataVO> operationRequest = new OperationRequest<>(page, limit);
            operationRequest.setData(new ApplicationInterfaceMethodDataVO(systemId, interfaceId));
            OperationResponse<ApplicationInterfaceMethodDataVO> operationResponse = dataOperationManageService.queryApplicationInterfaceMethodList(operationRequest);
            log.info("查询应用接口方法信息结束 operationResponse:{}", JSON.toJSONString(operationResponse));
            return operationResponse;
        } catch (Exception e) {
            log.error("查询应用接口方法信息异常 systemId:{} interfaceId:{}", systemId, interfaceId, e);
            return OperationResponse.<ApplicationInterfaceMethodDataVO>builder()
                    .pageTotal(0)
                    .list(null)
                    .build();
        }
    }
}
