package com.kischang.simple_utils.hibernate.service.impl;

import com.kischang.simple_utils.hibernate.annotation.TxExceptionRoll;
import com.kischang.simple_utils.hibernate.annotation.TxReadonly;
import com.kischang.simple_utils.hibernate.exception.ObjectPersistenceException;
import com.kischang.simple_utils.hibernate.model.BaseModel;
import com.kischang.simple_utils.hibernate.service.BaseService;
import com.kischang.simple_utils.page.PageInfo;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 基础curd service实现
 *
 * @author KisChang
 * @version 1.0
 */
public class BaseServiceImpl implements BaseService {

    private BaseDao baseDao;
    private SessionFactory sessionFactory;

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    @TxExceptionRoll
    public Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    @TxExceptionRoll
    public <T extends BaseModel> T save(T obj) throws ObjectPersistenceException {
        T temp = baseDao.save(obj);
        if (temp == null){
            throw new ObjectPersistenceException("数据存储失败，请稍后再试！", -1);
        }
        return temp;
    }

    @Override
    @TxExceptionRoll
    public <T extends BaseModel> T update(T obj) throws ObjectPersistenceException {
        T temp = baseDao.update(obj);
        if (temp == null){
            throw new ObjectPersistenceException("数据修改失败，请稍后再试！", -1);
        }
        return temp;
    }

    @Override
    @TxExceptionRoll
    public <T extends BaseModel> T saveOrUpdate(T obj) throws ObjectPersistenceException {
        T temp = baseDao.saveOrUpdate(obj);
        if (temp == null){
            throw new ObjectPersistenceException("数据修改失败，请稍后再试！", -1);
        }
        return temp;
    }

    @Override
    @TxReadonly
    public <K extends Serializable, T extends BaseModel<K>> T findById(Class<T> clzss, K id) {
        return baseDao.findById(clzss, id);
    }

    @Override
    @TxReadonly
    public <K extends Serializable, T extends BaseModel<K>> T findWhere(Class<T> clzss, Collection<Criterion> criterion) {
        if (criterion == null || criterion.isEmpty()){
            return this.findWhere(clzss);
        }else {
            return this.findWhere(clzss, criterion.toArray(new Criterion[0]));
        }
    }

    @Override
    @TxReadonly
    public <K extends Serializable, T extends BaseModel<K>> T findWhere(Class<T> clzss, Criterion... criterion) {
        return baseDao.findWhere(clzss, criterion);
    }

    @Override
    @TxExceptionRoll
    public <T extends BaseModel<Long>> T deleteById(Class<T> clzss, long id) throws ObjectPersistenceException {
        T rv = baseDao.deleteById(clzss, id, true);
        if (rv == null){
            throw new ObjectPersistenceException("数据移除失败，请稍后再试！", -1);
        }
        return rv;
    }


    @Override
    @TxReadonly
    public <T extends BaseModel> List<T> findAll(Class<T> clzss, PageInfo pageInfo) {
        return baseDao.findAllWhere(clzss, null, null, null, pageInfo);
    }

    @Override
    @TxReadonly
    @SuppressWarnings("unchecked")
    public <T extends BaseModel> List<T> findAllWhere(Class<T> clzss, Collection<Criterion> criterion, Collection<Order> order, PageInfo pageInfo) {
        return this.findAllWhere(clzss, null, criterion, order, pageInfo);
    }

    @Override
    @TxReadonly
    public <T extends BaseModel> List findProjectionAllWhere(Class<T> clzss, Projection projection, Collection<Criterion> whereList) {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(clzss);
        if (!CollectionUtils.isEmpty(whereList)){
            for (Criterion once : whereList) {
                criteria.add(once);
            }
        }
        return criteria.setProjection(projection).list();
    }

    @Override
    public <T extends BaseModel> List<T> findAllWhere(Class<T> clzss, BaseDao.DealCriteria dealCriteria, Collection<Criterion> criterion, Collection<Order> order, PageInfo pageInfo) {
        return baseDao.findAllWhere(clzss, dealCriteria, criterion, order, pageInfo);
    }

    @Override
    @TxReadonly
    public <T extends BaseModel> List<T> findAllWhere(Class<T> clzss, Criterion... criterion) {
        return baseDao.findAllWhere(clzss, criterion);
    }
}
