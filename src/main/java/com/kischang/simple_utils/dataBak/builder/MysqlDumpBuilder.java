package com.kischang.simple_utils.dataBak.builder;


import com.kischang.simple_utils.dataBak.DumpType;
import com.kischang.simple_utils.execute.CommandUtils;
import com.kischang.simple_utils.execute.ExecResult;
import com.kischang.simple_utils.utils.OS;

import java.util.LinkedList;
import java.util.List;

/**
 * 数据库备份命令创建工具
 *
 * @author KisChang
 * @version 1.0
 */
public class MysqlDumpBuilder {

    public static void main(String[] args) {
        MysqlDumpBuilder builder = new MysqlDumpBuilder();
        builder
            .setCommand("C:\\Users\\KisChang\\Desktop\\mysqlutils\\mysqldump.exe")
            .setUsername("careerlab")
            .setPassword("tbceo")
            .addDatabase("careerlab")
            /*MySQL8 新增的选项，加上可以不报错*/
            .setOtherArgs("--column-statistics=0 --force --hex-blob ")
            /*配置参数*/
            .setAddDropTable(true)
            .setCompact(false)
            .setBakType(DumpType.DataAndDesc)
            .setOutToFile("C:\\Users\\KisChang\\Desktop\\tmp.sql")
            .setDefChartset("utf8mb4")
        ;
        System.out.println(builder.build());
        ExecResult result = CommandUtils.exec(builder.build());
        String str = result.getOutData();
        System.out.println(str);
    }

    private boolean commandType;        //true mysqldump Linux   ||| false mysqldump.exe Windows
    private String command = null;      //如果不为null， 则忽略commandType
    private String username;
    private String password;
    private String otherArgs = null;    //直接追加在命令尾部
    private String defaultChartset = null;    //直接追加在命令尾部

    private String host = "localhost";
    private int port = 3306;

    private boolean lockAllTables = false;
    private boolean flushLogs = false;

    private List<String> databases;         //备份的数据库       （--databases）
    private List<String> tables;            //备份的数据库中指定表 （--tables）
    private List<String> ignoreTables;      //跳过的数据表 （--ignore-table）

    private boolean hasCreateDB = true;    //是否添加CREATE DATABASE 语句 （false则增加--no-create-db）
    private DumpType bakType = DumpType.DataAndDesc;



    private boolean addDropTable = true;   //默认 false  --skip-add-drop-table  (取消drop语句)
    private boolean compact = false;         //默认 (--compact)输出更少的信息

    //--opt 相当于 --add-drop-table --add-locks --create-options --disable-keys --extended-insert --lock-tables --quick --set-charset
    private boolean opt = false;

    private boolean outType = true; //true流输出   false到文件
    private String outFile;

    public MysqlDumpBuilder() {
        this.commandType = OS.isFamilyUnix();
    }

    public String build(){
        if(checkErr()){
            return null;
        }
        String tmp = command == null ?
                ( commandType ? " mysqldump " : " mysqldump.exe " )
                : command;

        StringBuilder sb = new StringBuilder(tmp);
        if (defaultChartset != null){
            sb.append(" --default-character-set=").append(defaultChartset);
        }

        sb.append(" --user=").append(username);
        sb.append(" --password=").append(password);
        sb.append(" --host=").append(host);
        sb.append(" --port=").append(port);

        sb.append(" --single-transaction ");


        if (!hasCreateDB){
            sb.append(" --no-create-db ");
        }
        switch (bakType){
            case OnlyData:
                sb.append(" --no-create-info ");
                break;
            case OnlyDesc:
                sb.append(" --no-data ");
                break;
            //默认即可
            case DataAndDesc:
            default:
        }
        if (!addDropTable){
            sb.append(" --skip-add-drop-table");
        }
        if (compact){
            sb.append(" --compact ");
        }
        if (opt){
            sb.append(" --opt ");
        }

        if (otherArgs != null){
            sb.append(" ").append(otherArgs).append(" ");
        }

        if (lockAllTables) {
            sb.append(" --lock-all-tables ");
        }
        if (flushLogs) {
            sb.append(" --flush-logs ");
        }

        if (!isNullArr(databases)){
            sb.append(" --databases");
            for (String str : databases){
                sb.append(" ").append(str);
            }
        }
        if (!isNullArr(tables)){
            sb.append(" --tables");
            for (String str : tables){
                sb.append(" ").append(str);
            }
        }
        if (!isNullArr(ignoreTables)){
            for (String str : ignoreTables){
                sb.append(" --ignore-table=").append(str.trim()).append(" ");
            }
        }

        if (this.outType){
            //true流输出
        }else {
            //false到文件，可以忽略一些错误或警告输出，建议采用
            sb.append(" --result-file=").append(this.outFile);
        }
        return sb.toString();
    }

    private boolean checkErr() {
        if (isNullStr(username)){
            return true;
        }
        if (isNullStr(password)){
            return true;
        }
        return false;
    }

    private static boolean isNullStr(String str){
        return str == null || "".equals(str);
    }

    private static boolean isNullArr(List<String> arr){
        return arr == null || arr.isEmpty();
    }

    public MysqlDumpBuilder setCommandType(boolean commandType) {
        this.commandType = commandType;
        return this;
    }

    public MysqlDumpBuilder setCommand(String command) {
        this.command = command;
        return this;
    }

    public MysqlDumpBuilder addDatabase(String db){
        if (databases == null){
            databases = new LinkedList<>();
        }
        databases.add(db);
        return this;
    }

    public MysqlDumpBuilder addTable(String table){
        if (tables == null){
            tables = new LinkedList<>();
        }
        tables.add(table);
        return this;
    }

    public MysqlDumpBuilder addIgnoreTable(String table){
        if (ignoreTables == null){
            ignoreTables = new LinkedList<>();
        }
        ignoreTables.add(table);
        return this;
    }

    public MysqlDumpBuilder setUsername(String username) {
        this.username = username;
        return this;
    }

    public MysqlDumpBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    public MysqlDumpBuilder setHost(String host) {
        this.host = host;
        return this;
    }

    public MysqlDumpBuilder setDefChartset(String chartset) {
        this.defaultChartset = chartset;
        return this;
    }

    public MysqlDumpBuilder setOtherArgs(String otherArgs) {
        this.otherArgs = otherArgs;
        return this;
    }

    public MysqlDumpBuilder setPort(int port) {
        this.port = port;
        return this;
    }

    public MysqlDumpBuilder setHasCreateDB(boolean hasCreateDB) {
        this.hasCreateDB = hasCreateDB;
        return this;
    }

    public MysqlDumpBuilder setBakType(DumpType bakType) {
        this.bakType = bakType;
        return this;
    }

    public MysqlDumpBuilder setAddDropTable(boolean addDropTable) {
        this.addDropTable = addDropTable;
        return this;
    }

    public MysqlDumpBuilder setCompact(boolean compact) {
        this.compact = compact;
        return this;
    }

    public MysqlDumpBuilder setOpt(boolean opt) {
        this.opt = opt;
        return this;
    }

    public MysqlDumpBuilder setLockAllTables(boolean lockAllTables) {
        this.lockAllTables = lockAllTables;
        return this;
    }

    public MysqlDumpBuilder setFlushLogs(boolean flushLogs) {
        this.flushLogs = flushLogs;
        return this;
    }

    public MysqlDumpBuilder setOutToFile(String file) {
        this.outType = false;
        this.outFile = file;
        return this;
    }
    public MysqlDumpBuilder setOutToCmd() {
        this.outType = true;
        return this;
    }
}