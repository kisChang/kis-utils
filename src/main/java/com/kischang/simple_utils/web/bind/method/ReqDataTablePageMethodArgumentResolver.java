package com.kischang.simple_utils.web.bind.method;

import com.kischang.simple_utils.page.PageInfo;
import com.kischang.simple_utils.web.bind.annotation.ReqDataTablePage;
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
public class ReqDataTablePageMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        if (parameter.hasParameterAnnotation(ReqDataTablePage.class)) {
            return true;
        }
        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        ReqDataTablePage anno = parameter.getParameterAnnotation(ReqDataTablePage.class);

        PageInfo pageInfo = new PageInfo();
        //URL
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        //update PageNow
        int start = Integer.parseInt(webRequest.getParameter(anno.start()));
        int length = Integer.parseInt(webRequest.getParameter(anno.length()));

        pageInfo.setPageSize(length);
        pageInfo.setPageNow(start / length + 1);

        request.setAttribute("page", pageInfo);
        mavContainer.getModel().addAttribute("page", pageInfo);
        return pageInfo;
    }
}