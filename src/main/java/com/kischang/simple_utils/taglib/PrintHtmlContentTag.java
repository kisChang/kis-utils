package com.kischang.simple_utils.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

public class PrintHtmlContentTag extends SimpleTagSupport{
    private String var;
    private String suffix = "...";
    private int len = 10;

	public void setVar(String var) {
		this.var = var;
	}
    public void setLen(int len) {
        this.len = len;
    }
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    @Override
	public void doTag() throws JspException, IOException {
		boolean con = false;
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<var.length();i++ ){
			char b = var.charAt(i);
			if (con) {
				if (b == '>') {
					con = false;
				}
				continue;
			}
			if (b == '<') {
				con = true;
				continue;
			}
			sb.append(b);
			if(sb.toString().replaceAll("&nbsp;","").length() >= len){
				sb.append(suffix);
				break;
			}
		}
		this.getJspContext().getOut().write(sb.toString());
	}
}