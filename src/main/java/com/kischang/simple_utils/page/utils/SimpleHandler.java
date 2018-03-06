package com.kischang.simple_utils.page.utils;

import java.util.Collection;

/**
 * 分页查询处理函数简单实现
 *
 * @author KisChang
 * @version 3.1.2
 */
public abstract class SimpleHandler<T> implements Handler<T> {

    private int pageNowInt;

    public int getNowPage() {
        return this.pageNowInt + 1;
    }

    public abstract void execBatch(Collection<T> data) throws Exception;

    public void execBatch(Collection<T> data, int pageNow, int totalPage) throws Exception {
        this.pageNowInt = pageNow;
        execBatch(data);
    }

}