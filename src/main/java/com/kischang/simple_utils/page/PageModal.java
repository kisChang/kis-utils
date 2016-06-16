package com.kischang.simple_utils.page;

import java.util.List;

/**
 * 分页实体类描述
 *
 * @author KisChang
 * @version 1.0
 * @date 2015年11月03日
 * @see PageInfo
 * @since 1.0
 */
public class PageModal<T> implements java.io.Serializable {
    private PageInfo pageInfo;
    private List<T> data;

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public PageModal<T> setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
        return this;
    }

    public List<T> getData() {
        return data;
    }

    public PageModal<T> setData(List<T> data) {
        this.data = data;
        return this;
    }

    @Override
    public String toString() {
        return "{" +
                "pageInfo=" + pageInfo +
                ", data=" + data +
                '}';
    }
}
