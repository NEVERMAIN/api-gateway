package io.github.NEVERMAIN.gateway.center.trigger.http;

import io.github.NEVERMAIN.gateway.center.api.IRpcRegisterManage;
import io.github.NEVERMAIN.gateway.center.api.response.Response;
import io.github.NEVERMAIN.gateway.center.domain.manage.IConfigManageService;
import io.github.NEVERMAIN.gateway.center.domain.register.IRegisterManageService;
import io.github.NEVERMAIN.gateway.center.domain.register.model.entity.ApplicationInterfaceEntity;
import io.github.NEVERMAIN.gateway.center.domain.register.model.entity.ApplicationInterfaceMethodEntity;
import io.github.NEVERMAIN.gateway.center.domain.register.model.entity.ApplicationSystemEntity;
import io.github.NEVERMAIN.gateway.center.types.enums.ResponseCode;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.yaml.snakeyaml.constructor.DuplicateKeyException;

@RestController
@CrossOrigin("*")
@RequestMapping("/wg/admin/register")
@Slf4j
public class RpcRegisterManageController implements IRpcRegisterManage {

    @Resource
    private IRegisterManageService registerManageService;

    @Resource
    private IConfigManageService configManageService;


    @PostMapping(value = "registerApplication", produces = "application/json;charset=UTF-8")
    @Override
    public Response<Boolean> registerApplication(@RequestParam String systemId, @RequestParam String systemName,
                                                 @RequestParam String systemType, @RequestParam String systemRegistry) {

        try {
            log.info("注册应用服务 systemId:{}", systemId);
            // 1.创建实体类
            ApplicationSystemEntity applicationSystemEntity = new ApplicationSystemEntity();
            applicationSystemEntity.setSystemId(systemId);
            applicationSystemEntity.setSystemName(systemName);
            applicationSystemEntity.setSystemType(systemType);
            applicationSystemEntity.setSystemRegistry(systemRegistry);

            // 2.保存
            Boolean result = registerManageService.registerApplication(applicationSystemEntity);

            // 3.返回结果
            return Response.<Boolean>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(result)
                    .build();

        } catch (DuplicateKeyException e) {
            log.warn("注册应用服务重复 systemId:{}", systemId, e);
            return Response.<Boolean>builder()
                    .code(ResponseCode.DUPLICATE_KEY.getCode())
                    .info(e.getMessage())
                    .data(true)
                    .build();
        } catch (Exception e) {
            log.error("注册应用服务失败 systemId：{}", systemId, e);
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(e.getMessage())
                    .data(false)
                    .build();
        }

    }

    @PostMapping(value = "registerApplicationInterface", produces = "application/json;charset=UTF-8")
    @Override
    public Response<Boolean> registerApplicationInterface(
            @RequestParam String systemId, @RequestParam String interfaceId,
            @RequestParam String interfaceName, @RequestParam String interfaceVersion) {

        try {
            log.info("注册应用接口 systemId：{} interfaceId：{}", systemId, interfaceId);
            // 1.创建实体类
            ApplicationInterfaceEntity applicationInterfaceEntity = new ApplicationInterfaceEntity();
            applicationInterfaceEntity.setSystemId(systemId);
            applicationInterfaceEntity.setInterfaceId(interfaceId);
            applicationInterfaceEntity.setInterfaceName(interfaceName);
            applicationInterfaceEntity.setInterfaceVersion(interfaceVersion);

            // 2.保存
            Boolean result = registerManageService.registerApplicationInterface(applicationInterfaceEntity);

            // 3.返回结果
            return Response.<Boolean>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(result)
                    .build();

        } catch (DuplicateKeyException e) {
            log.warn("注册应用接口重复 systemId:{} interfaceId:{}", systemId, interfaceId, e);
            return Response.<Boolean>builder()
                    .code(ResponseCode.DUPLICATE_KEY.getCode())
                    .info(e.getMessage())
                    .data(true)
                    .build();
        } catch (Exception e) {
            log.error("注册应用接口失败 systemId:{}", systemId, e);
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(e.getMessage())
                    .data(false)
                    .build();
        }


    }

    @PostMapping(value = "registerApplicationInterfaceMethod", produces = "application/json;charset=UTF-8")
    @Override
    public Response<Boolean> registerApplicationInterfaceMethod(@RequestParam String systemId,
                                                                @RequestParam String interfaceId,
                                                                @RequestParam String methodId,
                                                                @RequestParam String methodName,
                                                                @RequestParam String parameterTypes,
                                                                @RequestParam String uri,
                                                                @RequestParam String httpCommandType,
                                                                @RequestParam Integer auth) {
        try {
            log.info("注册应用接口方法 systemId：{} interfaceId：{} methodId：{}", systemId, interfaceId, methodId);
            // 1.创建实体类
            ApplicationInterfaceMethodEntity applicationInterfaceMethodEntity = new ApplicationInterfaceMethodEntity();
            applicationInterfaceMethodEntity.setSystemId(systemId);
            applicationInterfaceMethodEntity.setInterfaceId(interfaceId);
            applicationInterfaceMethodEntity.setMethodId(methodId);
            applicationInterfaceMethodEntity.setMethodName(methodName);
            applicationInterfaceMethodEntity.setParameterType(parameterTypes);
            applicationInterfaceMethodEntity.setUri(uri);
            applicationInterfaceMethodEntity.setHttpCommandType(httpCommandType);
            applicationInterfaceMethodEntity.setAuth(auth);

            // 2.保存
            Boolean result = registerManageService.registerApplicationInterfaceMethod(applicationInterfaceMethodEntity);

            // 3.返回结果
            return Response.<Boolean>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(result)
                    .build();

        } catch (DuplicateKeyException e) {
            log.warn("注册应用接口重复 systemId：{} interfaceId：{}", systemId, interfaceId);
            return Response.<Boolean>builder()
                    .code(ResponseCode.DUPLICATE_KEY.getCode())
                    .info(e.getMessage())
                    .data(true)
                    .build();
        } catch (Exception e) {
            log.error("注册应用接口失败 systemId：{}", systemId, e);
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(e.getMessage())
                    .data(false)
                    .build();
        }

    }

    @PostMapping(value = "registerEvent", produces = "application/json;charset=utf-8")
    @Override
    public Response<Boolean> registerEvent(@RequestParam String systemId) {

        try{
            log.info("应用信息注册完成通知 systemId:{}",systemId);
            // 推送注册信息
            Boolean result = configManageService.registerEvent(systemId);

            // 返回结果
            return Response.<Boolean>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(result)
                    .build();

        }catch (Exception e){
            log.error("应用信息注册完成通知失败 systemId：{}", systemId, e);
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(false)
                    .build();
        }


    }
}
