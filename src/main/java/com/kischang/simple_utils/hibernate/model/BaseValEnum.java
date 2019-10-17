package com.kischang.simple_utils.hibernate.model;

/**
 * Hibernate 使用int存储枚举
 * 需要在对应字段上增加：@Type(type = "valEnum")
 * 注意：作为Pojo属性时，增加int的Get和Set
 *
 * @author KisChang
 * @date 2018-12-08
 */
public interface BaseValEnum {

    int getValue();

}
