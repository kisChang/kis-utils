package com.kischang.simple_utils.execute.impl;


import com.kischang.simple_utils.execute.CommandExecutor;

import java.util.HashSet;
import java.util.Set;

/**
 * 命令执行器基础功能实现
 *
 * @author KisChang
 * @version 1.0
 */
public abstract class CommandExecutorImpl implements CommandExecutor {

    //默认0 为正确结果
    public static final int DEF_EXIT_VALUE  = 0;
    public static int COMMAND_ERROR         = -100;
    public static int EXCEPTION_ERROR       = -200;

    protected Set<Integer> exitValues = null;

    public CommandExecutorImpl() {
        //相当于初始化
        cleanExitValue();
    }

    public void setExitValue(final int value) {
        exitValues.add(value);
    }

    public void setExitValues(final int[] values) {
        for (int v : values) {
            setExitValue(v);
        }
    }

    public void cleanExitValue() {
        exitValues = new HashSet<Integer>();
        exitValues.add(CommandExecutorImpl.DEF_EXIT_VALUE);
    }

}
