package com.temsoft.simple_utils.execute.launcher;

import com.temsoft.simple_utils.execute.CommandLine;

import java.io.IOException;

/**
 * 默认的执行器（仅作描述）
 *
 * @author KisChang
 * @version 1.0
 */
public class DefaultCommandLauncher extends CommandLauncherImpl {

    @Override
    public Process exec(CommandLine cmd) throws IOException {
        return super.exec(cmd);
    }

}
