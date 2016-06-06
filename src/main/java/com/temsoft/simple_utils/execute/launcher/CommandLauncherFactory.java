package com.temsoft.simple_utils.execute.launcher;


import com.temsoft.simple_utils.utils.OS;

/**
 * 创建一个OS与JVM 的 Command Launcher
 *
 * @author KisChang
 * @version 1.0
 */
public class CommandLauncherFactory {


    public static CommandLauncher createLauncher() {
        CommandLauncher launcher = null;

        if (OS.isFamilyUnix()) {
            launcher = new UnixCommandLauncher();
        } else if (OS.isFamilyWindows() || OS.isFamilyWin9x() || OS.isFamilyDOS()){
            launcher = new WinNTCommandLauncher();
        } else {
            launcher = new DefaultCommandLauncher();
        }

        return launcher;
    }

}
