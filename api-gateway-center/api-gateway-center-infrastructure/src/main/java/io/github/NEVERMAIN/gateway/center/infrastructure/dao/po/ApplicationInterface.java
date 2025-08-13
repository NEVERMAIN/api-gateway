package io.github.NEVERMAIN.gateway.center.infrastructure.dao.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationInterface {

    /**
     * 自增ID
     */
    private Long id;
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
     * 接口协议类型
     */
    private String protocolType;
    /**
     * 接口版本
     */
    private String interfaceVersion;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;


}
