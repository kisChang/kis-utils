package com.kischang.simple_utils.web.bind.annotation;

import java.lang.annotation.*;

/**
 * 用于DataTables 分页对象注入
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ReqDataTablePage {

    /**
     * 页面baseUrl
     */
    String url() default "";

    String start() default "start";

    String length() default "length";

}