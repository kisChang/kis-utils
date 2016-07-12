package com.kischang.simple_utils.taglib;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

/**
 * 获取url
 *
 * @author KisChang
 * @version 1.0
 */
public class TaglibGetUrl extends TagSupport {

    private boolean forward = true;
    private String var = null;

    @Override
    public int doStartTag() throws JspException {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        String uri = null;
        if (forward){
            uri = String.valueOf(request.getAttribute("javax.servlet.forward.request_uri"));
        }else {
            uri = request.getRequestURI();
        }

        String host = request.getHeader("Host");
        String proto = request.getHeader("x-forwarded-proto");
        if (proto == null || "".equals(proto) || "null".equals(proto)){
            proto = "http";
        }

        String url = proto + "://" + host + uri;
        if (var != null){
            request.setAttribute(var, url);
        }else {
            try {
                pageContext.getOut().write(url);
            } catch (IOException ignored) {}
        }
        return Tag.SKIP_BODY;
    }

    public void setForward(boolean forward) {
        this.forward = forward;
    }

    public void setVar(String var) {
        this.var = var;
    }
}
