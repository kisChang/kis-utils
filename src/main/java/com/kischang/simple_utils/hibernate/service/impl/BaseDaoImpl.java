package com.kischang.simple_utils.hibernate.service.impl;

import com.kischang.simple_utils.hibernate.model.BaseModel;
import com.kischang.simple_utils.page.PageInfo;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 增删改查基础dao实现
 *
 * @author KisChang
 * @version 1.0
 */
public class BaseDaoImpl implements BaseDao {

    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends BaseModel> T save(T obj) {
        Session session = sessionFactory.getCurrentSession();
        save(obj, session);
        return obj;
    }

    private <T extends BaseModel> void save(T obj, Session session) {
        obj.setId(session.save(obj));
    }

    @Override
    public <T extends BaseModel> T update(T obj) {
        Session session = sessionFactory.getCurrentSession();
        return (T) session.merge(obj);
    }

    @Override
    public <T extends BaseModel> T saveOrUpdate(T obj) {
        Session session = sessionFactory.getCurrentSession();
        if (obj.getId() == null) {
            save(obj, session);
        } else {
            Object find = this.findById(obj.getClass(), obj.getId());
            if (find == null) {
                save(obj, session);
            } else {
                obj = (T) session.merge(obj);
            }
        }
        return obj;
    }

    @Override
    public <K extends Serializable, T extends BaseModel<K>> T findById(Class<T> clzss, K id) {
        return sessionFactory.getCurrentSession()
                .get(clzss, id);
    }

    @Override
    public <K extends Serializable, T extends BaseModel<K>> T deleteById(Class<T> clzss, K id) {
        return this.deleteById(clzss, id, true);
    }

    @Override
    public <K extends Serializable, T extends BaseModel<K>> T deleteById(Class<T> clzss, K id, boolean hard) {
        Session session = sessionFactory.getCurrentSession();
        T rv = this.findById(clzss, id);
        if (rv != null) {
            //执行物理删除
            session.delete(rv);
        }
        return rv;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends BaseModel> List<T> findAllWhere(Class<T> clzss, DealCriteria dealCriteria, Collection<Criterion> criterion, Collection<Order> orderList, PageInfo pageInfo) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(clzss);
        if (!CollectionUtils.isEmpty(criterion)) {
            for (Criterion once : criterion) {
                criteria.add(once);
            }
        }
        if (!CollectionUtils.isEmpty(orderList)) {
            for (Order onceOrder : orderList) {
                criteria.addOrder(onceOrder);
            }
        }

        if (dealCriteria != null){
            criteria = dealCriteria.deal(criteria);
        }

        if (pageInfo != null) {
            Criteria count = session.createCriteria(clzss)
                    .setProjection(Projections.rowCount());
            if (!CollectionUtils.isEmpty(criterion)) {
                for (Criterion once : criterion) {
                    count.add(once);
                }
            }
            pageInfo.setTotalPage((Number) count.uniqueResult());
            criteria.setFirstResult(pageInfo.getLimitStart());
            criteria.setMaxResults(pageInfo.getPageSize());
        }
        return criteria.list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <K extends Serializable, T extends BaseModel<K>> T findWhere(Class<T> clzss, Criterion... criterion) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(clzss);
        if (criterion != null && criterion.length >= 1) {
            criteria.add(Restrictions.and(criterion));
        }
        List<T> list = criteria.list();
        return (list == null || list.isEmpty()) ? null : list.get(0);
    }

    @Override
    public <T extends BaseModel> List<T> findAllWhere(Class<T> clzss, Criterion... criterion) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(clzss);
        if (criterion != null && criterion.length >= 1) {
            criteria.add(Restrictions.and(criterion));
        }
        return criteria.list();
    }
}
