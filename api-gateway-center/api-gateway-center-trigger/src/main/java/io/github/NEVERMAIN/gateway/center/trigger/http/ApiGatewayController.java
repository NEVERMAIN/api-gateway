package io.github.NEVERMAIN.gateway.center.trigger.http;

import io.github.NEVERMAIN.gateway.center.api.dto.ApiMetaDataDTO;
import io.github.NEVERMAIN.gateway.center.api.response.Response;
import io.github.NEVERMAIN.gateway.center.domain.api.model.entity.ApiMetaData;
import io.github.NEVERMAIN.gateway.center.domain.api.service.IApiMetaService;
import io.github.NEVERMAIN.gateway.center.types.enums.ResponseCode;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @description: 网关接口服务
 */
@RestController
@CrossOrigin("*")
@RequestMapping("/api")
public class ApiGatewayController {

    @Resource
    private IApiMetaService apiMetaService;

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public Response<List<ApiMetaDataDTO>> getHttpStatementList() {

        // 1.查询所有接口元数据
        List<ApiMetaData> apiMetaDataList = apiMetaService.queryHttpStatementList();

        // 2.转换数据
        List<ApiMetaDataDTO> apiMetaDataDTOList = apiMetaDataList.stream().map(apiMetaData -> ApiMetaDataDTO.builder()
                .application(apiMetaData.getApplication())
                .interfaceName(apiMetaData.getInterfaceName())
                .methodName(apiMetaData.getMethodName())
                .parameterType(apiMetaData.getParameterType())
                .uri(apiMetaData.getUri())
                .httpCommandType(apiMetaData.getHttpCommandType())
                .auth(apiMetaData.getAuth().getCode())
                .build()).toList();

        // 3. 返回结果
        return Response.<List<ApiMetaDataDTO>>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(apiMetaDataDTOList)
                .build();
    }

}
