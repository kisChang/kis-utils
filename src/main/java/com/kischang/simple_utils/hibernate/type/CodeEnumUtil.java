package com.kischang.simple_utils.hibernate.type;


import com.kischang.simple_utils.hibernate.model.BaseValEnum;

/**
 * Hibernate 使用int存储枚举 中转换对象
 *
 * @author KisChang
 * @date 2018-12-08
 */
public class CodeEnumUtil {

    public static <E extends Enum<?> & BaseValEnum> E codeOf(Class<E> enumClass, int code) {
        E[] enumConstants = enumClass.getEnumConstants();
        for (E e : enumConstants) {
            if (e.getValue() == code)
                return e;
        }
        return null;
    }

}