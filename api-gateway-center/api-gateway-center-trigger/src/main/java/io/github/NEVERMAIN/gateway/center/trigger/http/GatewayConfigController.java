package io.github.NEVERMAIN.gateway.center.trigger.http;

import com.alibaba.fastjson2.JSON;
import io.github.NEVERMAIN.gateway.center.api.IGatewayConfigManager;
import io.github.NEVERMAIN.gateway.center.api.dto.ApplicationSystemRichInfoDTO;
import io.github.NEVERMAIN.gateway.center.api.dto.GatewayServerDTO;
import io.github.NEVERMAIN.gateway.center.api.response.Response;
import io.github.NEVERMAIN.gateway.center.domain.docker.ILoadBalancingService;
import io.github.NEVERMAIN.gateway.center.domain.docker.model.aggregates.NginxConfig;
import io.github.NEVERMAIN.gateway.center.domain.manage.IConfigManageService;
import io.github.NEVERMAIN.gateway.center.domain.manage.model.aggregate.ApplicationSystemRichInfo;
import io.github.NEVERMAIN.gateway.center.domain.manage.model.valobj.*;
import io.github.NEVERMAIN.gateway.center.types.enums.ResponseCode;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/wg/admin/config")
public class GatewayConfigController implements IGatewayConfigManager {

    private static final Logger log = LoggerFactory.getLogger(GatewayConfigController.class);

    @Resource
    private IConfigManageService configManageService;

    @Resource
    private ILoadBalancingService loadBalancingService;


    @GetMapping(value = "queryServerConfig")
    @Override
    public Response<List<GatewayServerDTO>> queryServerConfig() {
        try {
            log.info("查询网关服务配置项信息");
            // 1. 查询所有网关服务节点信息
            List<GatewayServerVO> gatewayServerVOList = configManageService.queryGatewayServerList();

            // 2.转换数据
            List<GatewayServerDTO> gatewayServerDTOList = new ArrayList<>(gatewayServerVOList.size());
            gatewayServerVOList.forEach(gatewayServerVO -> {
                GatewayServerDTO gatewayServerDTO = new GatewayServerDTO();
                gatewayServerDTO.setGroupId(gatewayServerVO.getGroupId());
                gatewayServerDTO.setGroupName(gatewayServerVO.getGroupName());

                gatewayServerDTOList.add(gatewayServerDTO);
            });

            // 3.返回结果
            return Response.<List<GatewayServerDTO>>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(gatewayServerDTOList)
                    .build();


        } catch (Exception e) {
            log.error(" 查询网关服务配置配置项信息异常 ", e);
            return Response.<List<GatewayServerDTO>>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }

    }

    @RequestMapping(value = "registryGateway", method = RequestMethod.POST)
    @Override
    public Response<Boolean> registerGatewayServerNode(@RequestParam String groupId,
                                                       @RequestParam String gatewayId,
                                                       @RequestParam String gatewayName,
                                                       @RequestParam String gatewayAddress) {

        try {
            log.info("注册网关服务节点 gatewayId:{} gatewayName:{} gatewayAddress:{} ", gatewayId, gatewayName, gatewayAddress);

            if (StringUtils.isAnyBlank(groupId, gatewayId, gatewayName, gatewayAddress)) {
                return Response.<Boolean>builder()
                        .code(ResponseCode.ILLEGAL_PARAMETER.getCode())
                        .info(ResponseCode.ILLEGAL_PARAMETER.getInfo())
                        .data(false)
                        .build();
            }

            // 1.注册&更新网关服务节点
            Boolean done = configManageService.registryGatewayServerNode(groupId, gatewayId, gatewayName, gatewayAddress);
            log.info(" 网关服务节点注册&更新结果 done:{} ", done);
            // 2.读取最新网关算力数据
            List<GatewayServerDetailVO> gatewayServerDetailVOList = configManageService.queryGatewayServerDetailList();
            log.info(" 网关服务节点详情列表 gatewayServerDetailVOList:{} ", JSON.toJSONString(gatewayServerDetailVOList));
            // 3.组装 Nginx 网关刷新配置信息
//            NginxConfig nginxConfig = loadBalancingService.assembleNginxConfig(gatewayServerDetailVOList);
//            log.info(" Nginx 配置信息 nginxConfig:{} ", JSON.toJSONString(nginxConfig));
//            // 4.刷新 Nginx 配置
//            loadBalancingService.updateNginxConfig(nginxConfig);

            // 2.返回结果
            return Response.<Boolean>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(done)
                    .build();

        } catch (Exception e) {
            log.error(" 注册网关服务节点异常 ", e);
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(false)
                    .build();
        }
    }


    @GetMapping(value = "queryApplicationSystemRichInfo", produces = "application/json;charset=UTF-8")
    @Override
    public Response<ApplicationSystemRichInfoDTO> queryApplicationSystemRichInfo(@RequestParam String gatewayId, @RequestParam String systemId) {
        try {
            log.info("查询分配到网关下的待注册系统信息(系统、接口、方法) gatewayId:{} systemId:{}", gatewayId, systemId);
            // 1.查询注册系统信息
            ApplicationSystemRichInfo applicationSystemRichInfo =
                    configManageService.queryApplicationSystemRichInfo(gatewayId, systemId);

            // 2.转换实体数据
            ApplicationSystemRichInfoDTO applicationSystemRichInfoDTO = createApplicationSystemRichInfoDTO(applicationSystemRichInfo);

            // 3.返回数据
            return Response.<ApplicationSystemRichInfoDTO>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(applicationSystemRichInfoDTO)
                    .build();

        } catch (Exception e) {
            return Response.<ApplicationSystemRichInfoDTO>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }

    }

    @GetMapping(value = "queryRedisConfig", produces = "application/json;charset=utf-8")
    @Override
    public Response<Map<String, String>> queryRedisConfig() {
        try {
            log.info("查询配置中心 Redis 配置");
            Map<String, String> redisConfig = configManageService.queryRedisConfig();
            return Response.<Map<String, String>>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(redisConfig)
                    .build();
        } catch (Exception e) {
            log.error("查询配置中心 Redis 配置出现异常");
            return Response.<Map<String, String>>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(null)
                    .build();
        }

    }

    public ApplicationSystemRichInfoDTO createApplicationSystemRichInfoDTO(ApplicationSystemRichInfo applicationSystemRichInfo) {

        String gatewayId = applicationSystemRichInfo.getGatewayId();
        ApplicationSystemRichInfoDTO applicationSystemRichInfoDTO = new ApplicationSystemRichInfoDTO();
        // 1.设置网关ID
        applicationSystemRichInfoDTO.setGatewayId(gatewayId);
        // 2.设置系统信息
        List<ApplicationSystemVO> applicationSystemVOList = applicationSystemRichInfo.getApplicationSystemVOList();
        List<ApplicationSystemRichInfoDTO.ApplicationSystemDTO> ApplicationSystemDTOList =
                applicationSystemVOList.stream().map(this::getApplicationSystemDTOList).toList();

        applicationSystemRichInfoDTO.setApplicationSystemDTOList(ApplicationSystemDTOList);
        // 3.返回转换后的数据
        return applicationSystemRichInfoDTO;
    }


    /**
     * 转换系统实体信息
     *
     * @param applicationSystemVO 系统信息
     * @return 转换后对的系统 DTO 信息
     */
    private ApplicationSystemRichInfoDTO.ApplicationSystemDTO getApplicationSystemDTOList(ApplicationSystemVO applicationSystemVO) {

        ApplicationSystemRichInfoDTO.ApplicationSystemDTO applicationSystemDTO = new ApplicationSystemRichInfoDTO.ApplicationSystemDTO();
        applicationSystemDTO.setSystemId(applicationSystemVO.getSystemId());
        applicationSystemDTO.setSystemName(applicationSystemVO.getSystemName());
        applicationSystemDTO.setSystemRegistry(applicationSystemVO.getSystemRegistry());
        applicationSystemDTO.setSystemAddress(applicationSystemVO.getSystemAddress());
        // 1.网关下的接口列表
        List<ApplicationInterfaceVO> applicationInterfaceVOList = applicationSystemVO.getApplicationInterfaceVOList();
        List<ApplicationSystemRichInfoDTO.ApplicationInterfaceDTO> applicationInterfaceDTOList =
                applicationInterfaceVOList.stream().map(this::getApplicationInterfaceDTOList).toList();

        // 2.设置接口信息
        applicationSystemDTO.setApplicationInterfaceDTOList(applicationInterfaceDTOList);

        // 3.返回系统信息
        return applicationSystemDTO;

    }

    /**
     * 转换接口实体信息
     *
     * @param applicationInterfaceVO
     * @return
     */
    private ApplicationSystemRichInfoDTO.ApplicationInterfaceDTO getApplicationInterfaceDTOList(ApplicationInterfaceVO applicationInterfaceVO) {

        ApplicationSystemRichInfoDTO.ApplicationInterfaceDTO applicationInterfaceDTO = new ApplicationSystemRichInfoDTO.ApplicationInterfaceDTO();
        applicationInterfaceDTO.setSystemId(applicationInterfaceVO.getSystemId());
        applicationInterfaceDTO.setInterfaceId(applicationInterfaceVO.getInterfaceId());
        applicationInterfaceDTO.setInterfaceName(applicationInterfaceVO.getInterfaceName());
        applicationInterfaceDTO.setProtocolType(applicationInterfaceVO.getProtocolType());
        applicationInterfaceDTO.setInterfaceVersion(applicationInterfaceVO.getInterfaceVersion());

        applicationInterfaceDTO.setApplicationInterfaceMethodDTOList(getApplicationInterfaceMethodDTOList(applicationInterfaceVO));

        return applicationInterfaceDTO;
    }

    /**
     * 转换接口方法实体信息
     *
     * @param applicationInterfaceVO 接口方法信息
     * @return 转换后的接口实体信息
     */
    private static List<ApplicationSystemRichInfoDTO.ApplicationInterfaceMethodDTO> getApplicationInterfaceMethodDTOList(ApplicationInterfaceVO applicationInterfaceVO) {
        List<ApplicationInterfaceMethodVO> applicationInterfaceMethodVOList = applicationInterfaceVO.getApplicationInterfaceMethodVOList();
        return applicationInterfaceMethodVOList.stream().map(applicationInterfaceMethodVO -> {

            ApplicationSystemRichInfoDTO.ApplicationInterfaceMethodDTO applicationInterfaceMethodDTO =
                    new ApplicationSystemRichInfoDTO.ApplicationInterfaceMethodDTO();

            applicationInterfaceMethodDTO.setSystemId(applicationInterfaceMethodVO.getSystemId());
            applicationInterfaceMethodDTO.setInterfaceId(applicationInterfaceMethodVO.getInterfaceId());
            applicationInterfaceMethodDTO.setMethodId(applicationInterfaceMethodVO.getMethodId());
            applicationInterfaceMethodDTO.setMethodName(applicationInterfaceMethodVO.getMethodName());
            applicationInterfaceMethodDTO.setParameterType(applicationInterfaceMethodVO.getParameterType());
            applicationInterfaceMethodDTO.setParameterName(applicationInterfaceMethodVO.getParameterName());
            applicationInterfaceMethodDTO.setUri(applicationInterfaceMethodVO.getUri());
            applicationInterfaceMethodDTO.setAuth(applicationInterfaceMethodVO.getAuth());
            applicationInterfaceMethodDTO.setHttpCommandType(applicationInterfaceMethodVO.getHttpCommandType());

            return applicationInterfaceMethodDTO;

        }).toList();
    }


}
