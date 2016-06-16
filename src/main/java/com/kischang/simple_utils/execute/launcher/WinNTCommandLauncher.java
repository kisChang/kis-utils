package com.kischang.simple_utils.execute.launcher;

import com.kischang.simple_utils.execute.CommandLine;

import java.io.IOException;

/**
 * A command launcher for Win/2000/NT that uses 'cmd.exe ' when launching
 *
 * @author KisChang
 * @version 1.0
 */
public class WinNTCommandLauncher extends CommandLauncherImpl {

    @Override
    public Process exec(CommandLine cmd) throws IOException {
        final CommandLine newCmd = new CommandLine("cmd");
        newCmd.addArgument("/c");
        newCmd.addArguments(cmd.toStrings());

        return super.exec(newCmd);
    }

}
