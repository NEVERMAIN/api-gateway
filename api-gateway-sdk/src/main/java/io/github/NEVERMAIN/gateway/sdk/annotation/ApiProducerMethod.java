package io.github.NEVERMAIN.gateway.sdk.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ApiProducerMethod {

    /**
     * 方法名称
     * @return
     */
    String methodName() default "";

    /**
     * 访问路径: /wg/activity/sayHi
     * @return
     */
    String uri() default "";

    /**
     * 接口类型: GET、POST、PUT、DELETE
     * @return
     */
    String httpCommandType() default "GET";

    /**
     * 是否认证: true = 1 是、false = 0 否
     * @return
     */
    int auth() default  0;

}
