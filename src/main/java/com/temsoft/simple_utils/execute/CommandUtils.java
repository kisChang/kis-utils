package com.temsoft.simple_utils.execute;


import com.temsoft.simple_utils.execute.impl.DefaultCommandExecutor;

/**
 * 执行本地命令工具类
 *
 * @author KisChang
 * @version 1.0
 */
public class CommandUtils {
//    private static final CommandExecutor defExecutor = new ZTCommandExecutor();
    private static final CommandExecutor defExecutor = new DefaultCommandExecutor();

    public static ExecResult exec(String cmd){
        return exec(defExecutor, cmd);
    }

    public static ExecResult exec(CommandExecutor executor, String cmd){
        return executor.execute(CommandLine.parse(cmd), null);
    }

    public static ExecResult exec(String cmd, ExecuteInputStreamHandler handler){
        return exec(defExecutor, cmd, handler);
    }

    public static ExecResult exec(CommandExecutor executor, String cmd, ExecuteInputStreamHandler handler){
        return executor.execute(CommandLine.parse(cmd), handler);
    }

    public static void printResult(ExecResult result) {
        System.out.println("ExitCode : " + result.getExitCode());
        System.out.println("ErrorMsg : " + result.getErrorMsg());
        System.out.println("isFailure : " + result.isFailure());
        System.out.println("-------------------------------");
        System.out.println(result.getOutData());
    }

    public static void main(String[] args) {
        test1();
    }

    private static void test1() {
        ExecResult result = exec("ls -al");
        printResult(result);
    }

}
