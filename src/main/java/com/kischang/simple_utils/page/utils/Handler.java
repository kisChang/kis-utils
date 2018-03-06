package com.kischang.simple_utils.page.utils;

import com.kischang.simple_utils.page.PageInfo;

import java.util.Collection;

/**
 * 分页查询处理函数
 *
 * @author KisChang
 * @version 3.1.2
 */
public interface Handler<T> {

    int getNowPage();

    Collection<T> execFind(PageInfo pageInfo) throws Exception;

    void execBatch(Collection<T> data, int pageNow, int totalPage) throws Exception;

}