package io.github.NEVERMAIN.gateway.center.domain.api.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @description api鉴权枚举类
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ApiAuthTypeEnum {
    // 不鉴权
    NO_AUTH(0, "不需要鉴权"),
    // 鉴权
    AUTH(1, "需要鉴权");

    private Integer code;

    private String info;

    /**
     * 根据code获取枚举
     * @param code
     * @return
     */
    public static ApiAuthTypeEnum valuesOf(Integer code) {
        ApiAuthTypeEnum[] values = values();
        for (ApiAuthTypeEnum value : values) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return null;

    }




}
