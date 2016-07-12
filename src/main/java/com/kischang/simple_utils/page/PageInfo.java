package com.kischang.simple_utils.page;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.ui.Model;

import java.io.Serializable;

/**
 * 分页对象
 *
 * @author KisChang
 * @version 1.0
 * @since 1.0
 */
public class PageInfo implements Serializable {
    private int pageNow = 1;    //当前页，默认为1
    private int pageSize = 10;  //每页显示的条数
    private int totalPage;      //总页数
    private String url;         //页面的完整url地址

    public static void setToModal(PageInfo pageInfo, Model modal){
        modal.addAttribute("page", pageInfo);
    }

    public PageInfo() {
        super();
    }

    public PageInfo(String url) {
        super();
        this.url = url;
    }

    public PageInfo(int pageSize) {
        super();
        this.pageSize = pageSize;
    }

    public PageInfo(int pageSize, String url) {
        super();
        this.pageSize = pageSize;
        this.url = url;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        if (pageSize > 0){
            this.pageSize = pageSize;
        }else{
            this.pageSize = 1;
        }
    }

    public int getPageNow() {
        return pageNow;
    }

    public PageInfo setPageNow(int pageNow) {
        if (pageNow <= 0){
            this.pageNow = 1;
        }else {
            this.pageNow = pageNow;
        }
        return this;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Number totalCount) {
        int value = 0;
        if (totalCount != null){
            value = totalCount.intValue();
        }
        if (value % pageSize == 0) {
            this.totalPage = value / pageSize;
        } else {
            this.totalPage = value / pageSize + 1;
        }
        if (getPageNow() > this.totalPage){
            setPageNow(this.totalPage);
        }
    }

    @JsonIgnore
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @JsonIgnore
    public int getLimitStart() {
        return (pageNow - 1) * pageSize;
    }
}