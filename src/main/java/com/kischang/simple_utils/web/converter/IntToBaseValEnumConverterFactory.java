package com.kischang.simple_utils.web.converter;

import com.kischang.simple_utils.hibernate.model.BaseValEnum;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

/**
 *
 * @author KisChang
 * @date 2019-10-17
 */
public class IntToBaseValEnumConverterFactory implements ConverterFactory<String, Enum> {

    @Override
    public <T extends Enum> Converter<String, T> getConverter(Class<T> targetType) {
        Class<?> enumType = targetType;
        while (enumType != null && !enumType.isEnum()) {
            enumType = enumType.getSuperclass();
        }
        if (enumType == null) {
            throw new IllegalArgumentException(
                    "The target type " + targetType.getName() + " does not refer to an enum");
        }
        return new IntegerToEnum(enumType);
    }

    public static class IntegerToEnum<T extends BaseValEnum> implements Converter<String, BaseValEnum> {
        private final Class<T> enumType;

        public IntegerToEnum(Class<T> enumType) {
            this.enumType = enumType;
        }

        @Override
        public BaseValEnum convert(String source) {
            for (T t : enumType.getEnumConstants()) {
                if (t instanceof BaseValEnum) {
                    if (t.getValue() == Integer.valueOf(source)) {
                        return t;
                    }
                }
            }
            return null;
        }
    }

}