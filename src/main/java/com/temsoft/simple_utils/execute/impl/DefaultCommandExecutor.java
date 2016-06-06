package com.temsoft.simple_utils.execute.impl;

import com.temsoft.simple_utils.execute.CommandLine;
import com.temsoft.simple_utils.execute.ExecResult;
import com.temsoft.simple_utils.execute.ExecuteInputStreamHandler;
import com.temsoft.simple_utils.execute.PumpStreamHandler;
import com.temsoft.simple_utils.execute.launcher.CommandLauncher;
import com.temsoft.simple_utils.execute.launcher.CommandLauncherFactory;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 命令执行器  非异步实现
 *
 * @author KisChang
 * @version 1.0
 */
public class DefaultCommandExecutor extends CommandExecutorImpl {
    private static Logger logger = LoggerFactory.getLogger(DefaultCommandExecutor.class);

    private static final int BUFFER_SIZE = 1024 * 4;

    private CommandLauncher launcher = null;

    public DefaultCommandExecutor() {
        super();
        //初始化Launcher
        launcher = CommandLauncherFactory.createLauncher();
    }

    public ExecResult execute(CommandLine command, ExecuteInputStreamHandler handler) {
        ExecResult result = new ExecResult(this.exitValues);
        //正常输出
        ByteArrayOutputStream outOs = new ByteArrayOutputStream(BUFFER_SIZE);
        //错误输出
        ByteArrayOutputStream errOs = new ByteArrayOutputStream(BUFFER_SIZE);

        PumpStreamHandler streamHandler = null;
        Process process = null;
        try {
            process = launcher.exec(command);
            streamHandler = new PumpStreamHandler(outOs, errOs);
            streamHandler.setProcessErrorStream(process.getErrorStream());
            streamHandler.setProcessOutputStream(process.getInputStream());
            streamHandler.start();

            try {
                if (handler != null){
                    handler.handlerProcessInputStream(process.getOutputStream());
                }
            }finally {
                IOUtils.closeQuietly(process.getOutputStream());
            }

            result.setExitCode(process.waitFor());
            //将输出信息写入Result
            result.setOutData(
                    new String(outOs.toByteArray())
            );

            if (result.isFailure()){
                result.setErrorMsg(
                        IOUtils.toString(process.getErrorStream())
                );
            }
        } catch (IOException | InterruptedException e) {
            logger.error(e.getMessage(), e);
            result.setErrorMsg(e.getMessage());
            result.setExitCode(EXCEPTION_ERROR);
        } finally {
            try {
                if (process != null){
                    process.destroy();
                }
            } finally {
                if (streamHandler != null){
                    streamHandler.stop();
                }
            }
        }
        return result;
    }

}
