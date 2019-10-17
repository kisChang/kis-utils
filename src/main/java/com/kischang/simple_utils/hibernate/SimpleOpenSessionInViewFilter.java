package com.kischang.simple_utils.hibernate;

import com.kischang.simple_utils.utils.SpringUtils;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate5.support.OpenSessionInViewFilter;

import javax.servlet.http.HttpServletRequest;

/**
 * 在当前模式下，没有webContext，原代码无法获取到SessionFactory
 *
 * @author KisChang
 * @date 2019-10-17
 */
public class SimpleOpenSessionInViewFilter extends OpenSessionInViewFilter {

    @Override
    protected SessionFactory lookupSessionFactory(HttpServletRequest request) {
        return SpringUtils.getBean(SessionFactory.class);
    }

}
