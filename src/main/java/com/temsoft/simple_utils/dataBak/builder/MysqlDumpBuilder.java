package com.temsoft.simple_utils.dataBak.builder;


import com.temsoft.simple_utils.dataBak.DumpType;

import java.util.ArrayList;
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
        builder.setCommandType(true)
            .setUsername("root")
            .setPassword("root")
            .setBakType(DumpType.DataAndDesc)
            .addDatabase("CareerAssess")
            .addTable("sys_role")
            .setOpt(true);
        System.out.println(builder.build());
    }

    private boolean commandType = true;  //true mysqldump Linux   ||| false mysqldump.exe Windows
    private String command = null;      //如果不为null， 则忽略commandType
    private String username;
    private String password;

    private String host = "localhost";
    private int port = 3306;

    private List<String> databases; //备份的数据库       （--databases）
    private List<String> tables;    //备份的数据库中指定表 （--tables）

    private boolean hasCreateDB = true;    //是否添加CREATE DATABASE 语句 （false则增加--no-create-db）
    private DumpType bakType = DumpType.DataAndDesc;



    private boolean addDropTable = false;   //默认 false  --skip-add-drop-table  (取消drop语句)
    private boolean compact = true;         //默认 (--compact)输出更少的信息

    //--opt 相当于 --add-drop-table --add-locks --create-options --disable-keys --extended-insert --lock-tables --quick --set-charset
    private boolean opt = true;

    public String build(){
        if(checkErr()){
            return null;
        }
        String tmp = command == null ?
                ( commandType ? " mysqldump " : " mysqldump.exe " )
                : command;

        StringBuilder sb = new StringBuilder(tmp);
        sb.append(" --user=").append(username);
        sb.append(" --password=").append(password);
        sb.append(" --host=").append(host);
        sb.append(" --port=").append(port);


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
            databases = new ArrayList<>();
        }
        databases.add(db);
        return this;
    }

    public MysqlDumpBuilder addTable(String table){
        if (tables == null){
            tables = new ArrayList<>();
        }
        tables.add(table);
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
}