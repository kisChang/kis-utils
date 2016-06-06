package com.temsoft.simple_utils.dataBak;


import com.temsoft.simple_utils.dataBak.builder.MysqlDumpBuilder;
import com.temsoft.simple_utils.dataBak.builder.MysqlRecoveryBuilder;
import com.temsoft.simple_utils.execute.CommandUtils;
import com.temsoft.simple_utils.execute.ExecResult;
import com.temsoft.simple_utils.execute.ExecuteInputStreamHandler;
import com.temsoft.simple_utils.utils.OS;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.regex.Pattern;

/**
 * Created by KisChang on 2015-05-15.
 * 备份恢复mysql工具类
 * Windows未测试
 */
public class BakUpMysqlUtils {

    private static final Pattern USE_PATTERN = Pattern.compile("USE `\\w+`;", Pattern.UNIX_LINES);
    private static final String EMPTY = "";
    /**
     * 自动检测系统版本进行相应的备份操作
     */
    public static String mysqlBak(MysqlDumpBuilder builder) {
        String cmd = builder.build();
        ExecResult result = CommandUtils.exec(cmd);
        String str = result.getOutData();
        if (str != null){
            str = USE_PATTERN.matcher(str).replaceAll(EMPTY);
        }
        return str;
    }

    /**
     * 全备份
     */
    public static String mysqlBak(String databaseName, String username, String password) {
        MysqlDumpBuilder builder = new MysqlDumpBuilder();
        builder.setCommandType(OS.isFamilyUnix())
                .setUsername(username)
                .setPassword(password)
                .addDatabase(databaseName)
        ;
        return mysqlBak(builder);
    }

    /**
     * 检测系统版本并进行对应的恢复操作
     * @return -1运行异常  0 正常  1执行参数问题（账号密码不对或数据库不存在） other执行过程问题（备份文件问题）
     */
    public static int mysqlRecovery(final MysqlRecoveryBuilder builder, final String data){
        ExecResult result = CommandUtils.exec(builder.build(), new ExecuteInputStreamHandler() {
            @Override
            public void handlerProcessInputStream(OutputStream os) throws IOException {
                IOUtils.write(data, os, builder.getCharset());
                os.flush();
                os.close();
            }
        });
        return result.getExitCode();
    }

    public static int mysqlRecovery(String data, String databaseName, String username, String password) {
        MysqlRecoveryBuilder builder = new MysqlRecoveryBuilder();
        builder.setCommandType(OS.isFamilyUnix())
                .setUsername(username)
                .setPassword(password)
                .setDatabase(databaseName)
        ;
        return mysqlRecovery(builder, data);
    }

}
