package com.kischang.simple_utils.execute.launcher;

import com.kischang.simple_utils.execute.CommandLine;

import java.io.IOException;

/**
 * 执行器实现
 *
 * @author KisChang
 * @version 1.0
 */
public class CommandLauncherImpl implements CommandLauncher{

    @Override
    public Process exec(CommandLine cmd) throws IOException {

//        System.out.println("CommandLauncherImpl >>" + java.util.Arrays.toString(cmd.toStrings()));

//        return Runtime.getRuntime().exec(cmd.toStrings());

        ProcessBuilder builder = new ProcessBuilder(cmd.toStrings());
        builder.redirectErrorStream(true);
        return builder.start();
    }

}
