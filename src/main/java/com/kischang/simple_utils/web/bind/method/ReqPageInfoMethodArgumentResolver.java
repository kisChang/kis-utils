package com.kischang.simple_utils.web.bind.method;

import com.kischang.simple_utils.page.PageInfo;
import com.kischang.simple_utils.web.bind.annotation.ReqPageInfo;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;


/**
 * <p>用于绑定@FormModel的方法参数解析器
 * 注入分页对象
 */
public class ReqPageInfoMethodArgumentResolver implements HandlerMethodArgumentResolver {

    public ReqPageInfoMethodArgumentResolver() {
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        if (parameter.hasParameterAnnotation(ReqPageInfo.class)) {
            return true;
        }
        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        ReqPageInfo reqPageInfoAnnotation = parameter.getParameterAnnotation(ReqPageInfo.class);
        PageInfo pageInfo = new PageInfo();
        //URL
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        if("".equals(reqPageInfoAnnotation.url())){
            String url = request.getServletPath();
            String queryString = request.getQueryString();
            if(queryString != null){
                queryString = queryString.replaceAll(reqPageInfoAnnotation.pageNowParam() + "=[0-9]+[&]?","");
                if(queryString != null && !"".equals(queryString) && !"null".equalsIgnoreCase(queryString)){
                    url = request.getServletPath() + "?" + queryString;
                }
            }

            pageInfo.setUrl(url);
        }else{
            pageInfo.setUrl(reqPageInfoAnnotation.url());
        }

        //pageSize And PageNow
        pageInfo.setPageSize(reqPageInfoAnnotation.pageSize());
        pageInfo.setPageNow(1);

        //update PageNow
        String pageNow = webRequest.getParameter(reqPageInfoAnnotation.pageNowParam());
        try{
            if(pageNow != null){
                pageInfo.setPageNow(Integer.parseInt(pageNow));
            }
        }catch (Exception ignored){
        }
        request.setAttribute("page",pageInfo);
        mavContainer.getModel().addAttribute("page", pageInfo);
        return pageInfo;
    }
}
