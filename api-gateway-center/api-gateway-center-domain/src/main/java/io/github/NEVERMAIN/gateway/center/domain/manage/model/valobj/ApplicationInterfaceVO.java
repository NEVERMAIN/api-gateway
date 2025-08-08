package io.github.NEVERMAIN.gateway.center.domain.manage.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @description 应用系统接口信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationInterfaceVO {

    /**
     * 应用唯一标识
     */
    private String systemId;
    /**
     * 系统接口唯一标识
     */
    private String interfaceId;
    /**
     * 服务接口名称
     */
    private String interfaceName;
    /**
     * 接口版本
     */
    private String interfaceVersion;
    /**
     * 接口方法信息
     */
    private List<ApplicationInterfaceMethodVO> applicationInterfaceMethodVOList;


}
