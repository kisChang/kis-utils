package com.kischang.simple_utils.page;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.ui.Model;

import java.io.Serializable;
import java.util.Objects;

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
    private int totalNum;       //总数量
    private String url;         //页面的完整url地址

    public static void setToModal(PageInfo pageInfo, Model modal){
        modal.addAttribute("page", pageInfo);
    }

    public static void setToModal(PageModal pageModal, Model modal){
        if (pageModal != null){
            modal.addAttribute("page", pageModal.getPageInfo());
        }
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
        if (pageSize > 0){
            return this.pageSize;
        }else{
            return  1;
        }
    }

    public int getPageSizeReal() {
        return this.pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
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

    public int getTotalNum() {
        return totalNum;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Number totalCount) {
        int value = 0;
        if (totalCount != null){
            value = totalCount.intValue();
        }
        this.totalNum = value;
        if (value % getPageSize() == 0) {
            this.totalPage = value / getPageSize();
        } else {
            this.totalPage = value / getPageSize() + 1;
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
        return (pageNow - 1) * getPageSize();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PageInfo pageInfo = (PageInfo) o;
        return pageNow == pageInfo.pageNow &&
                pageSize == pageInfo.pageSize &&
                totalPage == pageInfo.totalPage &&
                totalNum == pageInfo.totalNum &&
                Objects.equals(url, pageInfo.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pageNow, pageSize, totalPage, totalNum, url);
    }

    @Override
    public String toString() {
        return "PageInfo{" +
                "pageNow=" + pageNow +
                ", pageSize=" + pageSize +
                ", totalPage=" + totalPage +
                ", totalNum=" + totalNum +
                ", url='" + url + '\'' +
                '}';
    }
}