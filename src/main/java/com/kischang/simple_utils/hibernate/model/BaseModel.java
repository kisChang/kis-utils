package com.kischang.simple_utils.hibernate.model;

import java.io.Serializable;

/**
 * 模型父类
 *
 * @author KisChang
 * @version 1.0
 */
public interface BaseModel<T extends Serializable> extends Serializable {

    T getId();

    void setId(T id);

}
