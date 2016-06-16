package com.kischang.simple_utils.formbean;

import com.kischang.simple_utils.utils.JacksonUtils;

import java.io.Serializable;

/**
 * 返回Json
 *
 * Created by KisChang on 2015-05-05.
 */
public class ResponseData<T> implements Serializable{

    private boolean stat;       //处理结果成功与否
    private String msg;         //错误信息
    private int errorCode = 0;  //0为没有报错

    private T content;

    public ResponseData() {
    }

    public ResponseData(boolean stat, String msg) {
        this.stat = stat;
        this.msg = msg;
    }

    public ResponseData(boolean stat, String msg, int errorCode) {
        this.stat = stat;
        this.msg = msg;
        this.errorCode = errorCode;
    }

    private static final ResponseData OK_INS = new ResponseData(true,null,0);
    public static ResponseData mkOK(){
        return OK_INS;
    }

    public boolean isStat() {
        return stat;
    }

    public ResponseData<T> setStat(boolean stat) {
        this.stat = stat;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public ResponseData<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public ResponseData<T> setErrorCode(int errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public T getContent() {
        return content;
    }

    public ResponseData<T> setContent(T content) {
        this.content = content;
        return this;
    }

    @Override
    public String toString() {
        return JacksonUtils.obj2Json(this);
    }
}
