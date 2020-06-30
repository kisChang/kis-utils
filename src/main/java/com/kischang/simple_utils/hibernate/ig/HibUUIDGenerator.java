package com.kischang.simple_utils.hibernate.ig;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.util.UUID;

/**
 * 自定义UUID生成器
 * 
 */
public class HibUUIDGenerator implements IdentifierGenerator {

    public static final String strategy = "com.kischang.simple_utils.hibernate.ig.HibUUIDGenerator";

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

}