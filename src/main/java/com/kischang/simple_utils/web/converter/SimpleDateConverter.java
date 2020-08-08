package com.kischang.simple_utils.web.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * date类型转换
 */
public class SimpleDateConverter implements Converter<String, Date> {

    private static Logger logger = LoggerFactory.getLogger(SimpleDateConverter.class);
    private static SimpleDateFormat simpleDateFormat(){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }
    private static SimpleDateFormat dateFormat(){
        return new SimpleDateFormat("yyyy-MM-dd");
    }
    private static SimpleDateFormat timeFormat(){
        return new SimpleDateFormat("HH:mm:ss");
    }

    @Override
    public Date convert(String source) {
        if (source == null
                || "".equals(source)
                || "null".equals(source)) {
            return null;
        }

        if (source.length() >= 19){
            try {
                return simpleDateFormat().parse(source);
            } catch (ParseException e) {
                logger.debug("parse date [] fail, source: {}", source, e);
            }
        }else {
            try {
                return dateFormat().parse(source);
            }catch (ParseException e){
                logger.debug("parse date fail, source: {}", source, e);
                try {
                    return timeFormat().parse(source);
                } catch (ParseException e1) {
                    logger.debug("parse date fail, source: {}", source, e1);
                }
            }
        }
        return null;
    }

}