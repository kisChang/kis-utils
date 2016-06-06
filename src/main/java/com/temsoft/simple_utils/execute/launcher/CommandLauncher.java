package com.temsoft.simple_utils.execute.launcher;


import com.temsoft.simple_utils.execute.CommandLine;

import java.io.IOException;


/**
 * 接口用来屏蔽不同平台
 *
 * @author KisChang
 * @version 1.0
 */
public interface CommandLauncher {

    Process exec(final CommandLine cmd) throws IOException;

}
