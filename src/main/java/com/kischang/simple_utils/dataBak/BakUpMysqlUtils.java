package com.kischang.simple_utils.dataBak;


import com.kischang.simple_utils.dataBak.builder.MysqlDumpBuilder;
import com.kischang.simple_utils.dataBak.builder.MysqlRecoveryBuilder;
import com.kischang.simple_utils.execute.CommandUtils;
import com.kischang.simple_utils.execute.ExecResult;
import com.kischang.simple_utils.utils.OS;
import org.apache.commons.io.IOUtils;

import java.util.regex.Pattern;

/**
 * Created by KisChang
 *
 * 备份恢复mysql工具类
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
    public static int mysqlRecovery(final MysqlRecoveryBuilder builder){
        ExecResult result = null;
        if (builder.isInType()){
            result = CommandUtils.exec(builder.build(), os -> {
                IOUtils.write(builder.getData(), os, builder.getCharset());
                os.flush();
                os.close();
            });
        }else {
            result = CommandUtils.exec(builder.build());
        }
        return result != null ? result.getExitCode() : -1;
    }

    public static int mysqlRecovery(String data, String databaseName, String username, String password) {
        MysqlRecoveryBuilder builder = new MysqlRecoveryBuilder();
        builder.setCommandType(OS.isFamilyUnix())
                .setUsername(username)
                .setPassword(password)
                .setDatabase(databaseName)
                .setUseData(data)
        ;
        return mysqlRecovery(builder);
    }

}
