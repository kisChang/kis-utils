package com.kischang.simple_utils.page.utils;


import com.kischang.simple_utils.page.PageInfo;

/**
 * 分页查询处理
 *
 * @author KisChang
 * @version 3.1.2
 */
public class PageHandler {

    public static <T> void pageBatch(int pageSize, Handler<T> handler) throws Exception {
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
