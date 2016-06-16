package com.kischang.simple_utils.execute.launcher;


import com.kischang.simple_utils.execute.CommandLine;

import java.io.IOException;

/**
 * A command launcher for Unix-like that uses 'sh -c ' when launching
 *
 * @author KisChang
 * @version 1.0
 */
public class UnixCommandLauncher extends CommandLauncherImpl {

    @Override
    public Process exec(CommandLine cmd) throws IOException {
        final CommandLine newCmd = new CommandLine("sh");
        newCmd.addArgument("-c");
        newCmd.addArgument(cmd.toString());
        return super.exec(newCmd);

//        return super.exec(cmd);
    }

}
