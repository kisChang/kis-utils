package com.kischang.simple_utils.formbean;

import com.kischang.simple_utils.page.PageInfo;

import java.util.List;

/**
 * JQuery DataTables 响应
 *
 * @author KisChang
 * @date 2017-03-10
 */
public class DataTableRv<T> implements java.io.Serializable{

    private Integer draw = 0;
    private int recordsTotal = 0;
    private int recordsFiltered = 0;
    private List<T> data;

    public DataTableRv() {
    }

    public DataTableRv(List<T> data, PageInfo pageInfo) {
        this(data, pageInfo == null ? 1 : pageInfo.getTotalNum());
    }

    public DataTableRv(List<T> data, int totalPage) {
        this.data = data;
        this.recordsFiltered = this.recordsTotal = totalPage;
    }

    public Integer getDraw() {
        return draw;
    }

    public DataTableRv setDraw(Integer draw) {
        this.draw = draw;
        return this;
    }

    public int getRecordsTotal() {
        return recordsTotal;
    }

    public void setRecordsTotal(int recordsTotal) {
        this.recordsTotal = recordsTotal;
    }

    public int getRecordsFiltered() {
        return recordsFiltered;
    }

    public void setRecordsFiltered(int recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
