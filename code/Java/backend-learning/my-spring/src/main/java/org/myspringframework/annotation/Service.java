package org.myspringframework.annotation;

import java.lang.annotation.*;

/**
 * 自定义Service注解
 */
@Target(ElementType.TYPE) //表示注解可以作用在类上
@Retention(RetentionPolicy.RUNTIME) //运行时生效
@Documented // 可以生成doc
public @interface Service {
    String value() default "";
}
