package com.kischang.simple_utils.redis;

import com.kischang.simple_utils.utils.RedisUtil;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 启用RedisUtils
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({RedisUtil.class})
public @interface EnableMyRedisUtil {
}