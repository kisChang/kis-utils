package com.kischang.simple_utils.weixin.taglib;

import com.kischang.simple_utils.utils.BytesUtils;
import com.kischang.simple_utils.utils.SimpleUtils;
import com.kischang.simple_utils.weixin.utils.WeixinJSSDKUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.List;

/**
 * @author KisChang
 * @version 1.0
 */
public class WeixinJSUrl extends TagSupport {

    private String url;
    private String jsUrl = "//res.wx.qq.com/open/js/jweixin-1.1.0.js";
    private String appId;
    private String appSecret;
    private String ticket = null;
    private boolean debug = false;
    private boolean format = false;

    private String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
    private String nonceStr = SimpleUtils.getRandomString(15);

    private List<String> jsApiList;

    @Override
    public int doStartTag() throws JspException {

        //不需要# 后面的内容
        if (url.contains("#")){
            url = url.substring(0, url.indexOf("#"));
        }

        StringBuilder sb = new StringBuilder();
        for (String str : jsApiList){
            if (sb.length() != 0){
                sb.append(", ");
            }
            sb.append("'");
            sb.append(str);
            sb.append("'");
        }

        //第一步：获取 access_token（需要缓存） 后加载 js api ticket
        if (ticket == null){
            ticket = WeixinJSSDKUtils.getJsapiTicket(appId, appSecret);
        }
        if (ticket == null){
            return Tag.SKIP_BODY;
        }

        //第二步：生成签名
        String tmp = String.format("jsapi_ticket=%s&noncestr=%s&timestamp=%s&url=%s", ticket, nonceStr, timestamp, url);
        String signature = BytesUtils.getSha1(tmp);

        String content;
        if (format){
            content = "<script type=\"text/javascript\" src=\"%s\"></script>\n" +
                    "<script>\n" +
                    "   wx.config({\n" +
                    "       %s" +
                    "       appId: '%s',\n" +
                    "       timestamp: %s,\n" +
                    "       nonceStr: '%s',\n" +
                    "       signature: '%s',\n" +
                    "       jsApiList: [%s] \n" +
                    "   });\n" +
                    "</script>\n";
        }else {
            content = "<script type=\"text/javascript\" src=\"%s\"></script>\n" +
                    "<script> wx.config({%s appId: '%s', timestamp: %s, nonceStr: '%s', signature: '%s', jsApiList: [%s]  });</script>\n";
        }

        try {
            pageContext.getOut().write(String.format(content
                    , jsUrl, debug ? "debug: true,\n" : "", appId, timestamp, nonceStr, signature, sb.toString()));
        } catch (IOException ignored) {}


        return Tag.SKIP_BODY;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setJsUrl(String jsUrl) {
        this.jsUrl = jsUrl;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public void setFormat(boolean format) {
        this.format = format;
    }

    public void setJsApiList(List<String> jsApiList) {
        this.jsApiList = jsApiList;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }
}
