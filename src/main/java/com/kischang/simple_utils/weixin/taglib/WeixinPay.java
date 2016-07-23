package com.kischang.simple_utils.weixin.taglib;

import com.kischang.simple_utils.utils.BytesUtils;
import com.kischang.simple_utils.utils.SimpleUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

/**
 * 微信支付JS 代码生成
 *
 * @author KisChang
 * @version 1.0
 */
public class WeixinPay extends TagSupport {

    private String appId;
    private String appKey;
    private String packageStr;
    private String signType = "MD5";
    private boolean format = false;

    @Override
    public int doStartTag() throws JspException {
        String timeStamp = String.valueOf(System.currentTimeMillis() / 1000);
        String nonceStr = SimpleUtils.getRandomString(32);
        String tmpStr = String.format("appId=%s&appKey=%s&timeStamp=%s&nonceStr=%s&package=%s&signType=%s"
                , appId, appKey, timeStamp, nonceStr, packageStr, signType);

        String sign = BytesUtils.getMd5(tmpStr).toUpperCase();
        String content;
        if (format){
            content = "{" +
                      "\n    \"appId\": \"%s\"," +      //公众号名称，由商户传入
                      "\n    \"timeStamp\": %s," +      //时间戳，自1970年以来的秒数
                      "\n    \"nonceStr\": \"%s\"," +   //随机串
                      "\n    \"package\": \"%s\"," +
                      "\n    \"signType\": \"%s\"," +   //微信签名方式
                      "\n    \"paySign\": \"%s\" " +    //微信签名
                      "\n}";
        }else {
            content = "{\"appId\": \"%s\", \"timeStamp\": %s, \"nonceStr\": \"%s\", \"package\": \"%s\", \"signType\": \"%s\", \"paySign\": \"%s\"  }";
        }

        try {
            pageContext.getOut().write(String.format(content
                    , appId, timeStamp, nonceStr, packageStr, signType, sign));
        } catch (IOException ignored) {}

        return Tag.SKIP_BODY;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public void setPackageStr(String packageStr) {
        this.packageStr = packageStr;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public void setFormat(boolean format) {
        this.format = format;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }
}
