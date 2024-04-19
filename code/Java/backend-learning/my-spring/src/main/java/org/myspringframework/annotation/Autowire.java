package org.myspringframework.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD) // 作用在属性上， 在 Spring 中， 其实可以作用在类，构造方法 等上面， 为了 省力，这里就不写了。
@Documented
public @interface Autowire {
    String value() default "";
}
/**
 * 1. Autowire 没有 value, 可以用Resource, 那个有和value相似的东西， 叫name
 * .这边只是写着玩玩。
 */