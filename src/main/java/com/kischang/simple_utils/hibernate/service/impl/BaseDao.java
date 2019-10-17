package com.kischang.simple_utils.hibernate.service.impl;

import com.kischang.simple_utils.hibernate.model.BaseModel;
import com.kischang.simple_utils.page.PageInfo;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 增删改查基础dao
 *
 * @author KisChang
 * @version 1.0
 */
public interface BaseDao {

    <T extends BaseModel> T save(T obj);

    <T extends BaseModel> T update(T obj);

    <T extends BaseModel> T saveOrUpdate(T obj);

    <K extends Serializable, T extends BaseModel<K>> T findById(Class<T> clzss, K id);

    <K extends Serializable, T extends BaseModel<K>> T deleteById(Class<T> clzss, K id);

    <K extends Serializable, T extends BaseModel<K>> T deleteById(Class<T> clzss, K id, boolean hard);

    <T extends BaseModel> List<T> findAllWhere(Class<T> clzss, DealCriteria dealCriteria, Collection<Criterion> criterion, Collection<Order> order, PageInfo pageInfo);

    <K extends Serializable, T extends BaseModel<K>> T findWhere(Class<T> clzss, Criterion... criterion);

    <T extends BaseModel> List<T> findAllWhere(Class<T> clzss, Criterion... criterion);


    interface DealCriteria {

        Criteria deal(Criteria criteria);

    }

}
