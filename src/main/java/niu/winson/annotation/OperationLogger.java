package niu.winson.annotation;

import niu.winson.enumation.OperationType;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperationLogger {
    //String Function() default "";//操作API
    String Name() default "";//操作API接口名称
    OperationType Type() default OperationType.SELECT; //操作类型
}
