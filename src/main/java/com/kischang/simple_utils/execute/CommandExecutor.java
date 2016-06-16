package com.kischang.simple_utils.execute;

/**
 * 命令执行器接口
 *
 * @author KisChang
 * @version 1.0
 */
public interface CommandExecutor {

    void setExitValue(final int value);

    void setExitValues(final int[] values);

    void cleanExitValue();

    ExecResult execute(CommandLine command, ExecuteInputStreamHandler handler);

}
