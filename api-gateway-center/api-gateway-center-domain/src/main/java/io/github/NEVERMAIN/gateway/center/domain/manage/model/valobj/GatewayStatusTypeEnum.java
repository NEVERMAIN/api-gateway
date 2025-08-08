package io.github.NEVERMAIN.gateway.center.domain.manage.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum GatewayStatusTypeEnum {

    FORBIDDEN(0, "禁用"),
    AVAILABLE(1, "可用"),

    ;

    private Integer code;

    private String info;

    public static GatewayStatusTypeEnum valuesOf(Integer code){
        GatewayStatusTypeEnum[] values = values();
        for (GatewayStatusTypeEnum value : values) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }




}
