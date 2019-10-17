package com.kischang.simple_utils.hibernate.annotation;


import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Exception 回滚事务，只读
 *
 * @author KisChang
 * @version 1.0
 * @date 2016年01月23日
 * @since 1.0
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Transactional(readOnly = true)
public @interface TxReadonly {
}
