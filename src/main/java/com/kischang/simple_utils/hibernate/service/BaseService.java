package com.kischang.simple_utils.hibernate.service;

import com.kischang.simple_utils.hibernate.exception.ObjectPersistenceException;
import com.kischang.simple_utils.hibernate.model.BaseModel;
import com.kischang.simple_utils.hibernate.service.impl.BaseDao;
import com.kischang.simple_utils.page.PageInfo;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 基础curd service
 *
 * @author KisChang
 * @version 1.0
 */
public interface BaseService {

    <T extends BaseModel> T save(T obj) throws ObjectPersistenceException;

    <T extends BaseModel> T update(T obj) throws ObjectPersistenceException;

    <T extends BaseModel> T saveOrUpdate(T obj) throws ObjectPersistenceException;

    <K extends Serializable, T extends BaseModel<K>> T findById(Class<T> clzss, K id);

    <K extends Serializable, T extends BaseModel<K>> T findWhere(Class<T> clzss, Collection<Criterion> criterion);

    <K extends Serializable, T extends BaseModel<K>> T findWhere(Class<T> clzss, Criterion... criterion);

    <T extends BaseModel<Long>> T deleteById(Class<T> clzss, long id) throws ObjectPersistenceException;

    <T extends BaseModel> List<T> findAll(Class<T> clzss, PageInfo pageInfo);

    <T extends BaseModel> List<T> findAllWhere(Class<T> clzss, Criterion... criterion);

    <T extends BaseModel> List<T> findAllWhere(Class<T> clzss, BaseDao.DealCriteria dealCriteria, Collection<Criterion> criterion, Collection<Order> order, PageInfo pageInfo);

    <T extends BaseModel> List<T> findAllWhere(Class<T> clzss, Collection<Criterion> criterion, Collection<Order> order, PageInfo pageInfo);

    <T extends BaseModel> List findProjectionAllWhere(Class<T> clzss, Projection projection, Collection<Criterion> criterion);

    Session getCurrentSession();

}
