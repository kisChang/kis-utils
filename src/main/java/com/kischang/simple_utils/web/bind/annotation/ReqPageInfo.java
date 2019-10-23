package com.kischang.simple_utils.web.bind.annotation;

import java.lang.annotation.*;

/**
 * 用于分页对象注入
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ReqPageInfo {
    /**
     * 单个页面显示条数
     */
    int pageSize() default 10;

    /**
     * 页面baseUrl
     */
    String url() default "";

    /**
     * 当前页面在request中的参数名
     */
    String pageNowParam() default "pageNow";

    /**
     * 单页条数参数
     */
    String pageSizeParam() default "pageSize";

}
