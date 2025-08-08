package io.github.NEVERMAIN.gateway.center.domain.manage.model.aggregate;

import io.github.NEVERMAIN.gateway.center.domain.manage.model.valobj.ApplicationSystemVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationSystemRichInfo {

    /** 网关ID */
    private  String gatewayId;
    /** 系统列表 */
    private List<ApplicationSystemVO> applicationSystemVOList;

}
