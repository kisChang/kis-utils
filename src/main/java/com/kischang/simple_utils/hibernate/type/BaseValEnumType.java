package com.kischang.simple_utils.hibernate.type;

import com.kischang.simple_utils.hibernate.model.BaseValEnum;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.internal.util.ReflectHelper;
import org.hibernate.usertype.DynamicParameterizedType;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Properties;

/**
 * Hibernate Enum int 的 Types
 * 适用于5.2即以上的Hibernate
 *
 * @author KisChang
 * @date 2018-12-08
 */
public class BaseValEnumType<E extends Enum<?> & BaseValEnum> implements UserType, DynamicParameterizedType {

    private static final int SQL_TYPE = Types.INTEGER;
    private static final String ENUM = "enumClass";

    private Class<E> enumClass;

    @Override
    public void setParameterValues(Properties parameters) {
        final ParameterType reader = (ParameterType) parameters.get(PARAMETER_TYPE);

        if (reader != null) {
            enumClass = reader.getReturnedClass().asSubclass(Enum.class);
        } else {
            final String enumClassName = (String) parameters.get(ENUM);
            try {
                enumClass = ReflectHelper.classForName(enumClassName, this.getClass()).asSubclass(Enum.class);
            } catch (ClassNotFoundException exception) {
                throw new HibernateException("Enum class not found: " + enumClassName, exception);
            }
        }
    }


    @Override
    public int[] sqlTypes() {
        return new int[]{SQL_TYPE};
    }

    @Override
    public Class returnedClass() {
        return enumClass;
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        return x == y;
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        return x == null ? 0 : x.hashCode();
    }

    @Override
    public E nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException, SQLException {
        final int value = rs.getInt(names[0]);
        return rs.wasNull() ? null : codeOf(value);
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor sharedSessionContractImplementor) throws HibernateException, SQLException {
        if (value instanceof BaseValEnum){
            st.setObject(index, ((BaseValEnum) value).getValue(), SQL_TYPE);
        }else {
            st.setObject(index, value, SQL_TYPE);
        }
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        return value;
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) value;
    }

    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return cached;
    }

    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }

    private E codeOf(int code) {
        try {
            return CodeEnumUtil.codeOf(enumClass, code);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Cannot convert " + code + " to " + enumClass.getSimpleName() + " by code value.", ex);
        }
    }
}
