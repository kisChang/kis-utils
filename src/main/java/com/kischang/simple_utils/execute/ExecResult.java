package com.kischang.simple_utils.execute;

import java.io.InputStream;
import java.util.Set;

/**
 * 执行结果
 *
 * @author KisChang
 * @version 1.0
 */
public class ExecResult {

    //默认为 Integer.MIN_VALUE 相当于出错
    private int exitCode = Integer.MIN_VALUE;

    //错误信息
    private String errorMsg;
    //输出信息
    private InputStream outStream;
    private String outData;
    private Set<Integer> exitValues;

    public ExecResult(Set<Integer> exitValues) {
        this.exitValues = exitValues;
    }

    public ExecResult(Set<Integer> exitValues, InputStream outStream) {
        this.outStream = outStream;
        this.exitValues = exitValues;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public InputStream getOutStream() {
        return outStream;
    }

    public void setOutStream(InputStream outStream) {
        this.outStream = outStream;
    }

    public String getOutData() {
        return outData;
    }

    public void setOutData(String outData) {
        this.outData = outData;
    }

    public int getExitCode() {
        return exitCode;
    }

    public void setExitCode(int exitCode) {
        this.exitCode = exitCode;
    }

    public boolean isFailure() {
        return !exitValues.contains(exitCode);
    }

    @Override
    public String toString() {
        return "ExecResult{" +
                "exitCode=" + exitCode +
                '}';
    }
}
