package com.kischang.simple_utils.web.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * date类型转换
 */
public class SimpleDateConverter implements Converter<String, Date> {

    private static Logger logger = LoggerFactory.getLogger(SimpleDateConverter.class);
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public Date convert(String source) {
        try {
            return simpleDateFormat.parse(source);
        } catch (ParseException e) {
            logger.warn("parse date fail, source: {}", source, e);
        }
        return null;
    }

}