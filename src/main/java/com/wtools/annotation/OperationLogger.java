package com.wtools.annotation;

import com.wtools.enumation.OperationType;

import java.lang.annotation.*;

/**
 * @author niu
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperationLogger {
    /**
     * 操作API接口名称
     */
    String Name() default "";

    /**
     * 操作类型
     */

    OperationType Type() default OperationType.SELECT;
}
