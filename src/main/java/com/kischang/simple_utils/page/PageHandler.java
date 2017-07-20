package com.kischang.simple_utils.page;


import java.util.Collection;

/**
 * 分页查询处理
 *
 * @author KisChang
 * @version 1.0
 * @date 2016年03月01日
 * @since 1.0
 */
public class PageHandler {

    //处理函数
    public static interface Handler<T>{

        int getNowPage();

        Collection<T> execFind(PageInfo pageInfo) throws Exception;

        void execBatch(Collection<T> data, int pageNow, int totalPage) throws Exception;
    }

    public static <T> void pageBatch(int pageSize, Handler<T> handler) throws Exception{
        PageInfo pageInfo = new PageInfo(pageSize);
        do {
            pageInfo.setPageNow(
                    handler.getNowPage()
            );

            handler.execBatch(
                    handler.execFind(pageInfo)
                    , pageInfo.getPageNow()
                    , pageInfo.getTotalPage()
            );
        } while (pageInfo.getPageNow() < pageInfo.getTotalPage());
    }

}
