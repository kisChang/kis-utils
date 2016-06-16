package com.kischang.simple_utils.dataBak.builder;

/**
 * 数据恢复命令创建工具
 *
 * @author KisChang
 * @version 1.0
 */
public class MysqlRecoveryBuilder {

    public static void main(String[] args) {
        MysqlRecoveryBuilder builder = new MysqlRecoveryBuilder();
        builder.setCommandType(false)
                .setUsername("root")
                .setPassword("root")
                .setDatabase("CareerAssess");

        System.out.println(builder.build());
    }

    private boolean commandType = true;  //true mysql Linux   ||| false mysql.exe Windows
    private String command = null;      //如果不为null， 则忽略commandType
    private String username;
    private String password;

    private String host = "localhost";
    private int port = 3306;

    private String charset = "utf8";    //设置编码

    private String database;    //目标数据库

    public String build() {
        if (checkErr()) {
            return null;
        }
        String tmp = command == null ?
                ( commandType ? " mysql " : " mysql.exe " )
                : command;

        StringBuilder sb = new StringBuilder(tmp);
        sb.append(" ").append(database);
        sb.append(" --user=").append(username);
        sb.append(" --password=").append(password);
        sb.append(" --host=").append(host);
        sb.append(" --port=").append(port);
        sb.append(" --default-character-set=").append(charset);
        //忽略错误
        // （否则调用过程中可能会出现类似异常：【IOException: 断开的管道断开的管道】）
        // （原因：因为调用过程中有错误输出而导致输入流异常）
        sb.append(" --force ");

        return sb.toString();
    }

    private boolean checkErr() {
        if (isNullStr(username)){
            return true;
        }
        if (isNullStr(password)){
            return true;
        }
        if (isNullStr(database)){
            return true;
        }
        return false;
    }

    private static boolean isNullStr(String str){
        return str == null || "".equals(str);
    }

    public String getCharset() {
        return charset;
    }

    public MysqlRecoveryBuilder setCharset(String charset) {
        this.charset = charset;
        return this;
    }

    public MysqlRecoveryBuilder setCommandType(boolean commandType) {
        this.commandType = commandType;
        return this;
    }

    public MysqlRecoveryBuilder setCommand(String command) {
        this.command = command;
        return this;
    }

    public MysqlRecoveryBuilder setUsername(String username) {
        this.username = username;
        return this;
    }

    public MysqlRecoveryBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    public MysqlRecoveryBuilder setHost(String host) {
        this.host = host;
        return this;
    }

    public MysqlRecoveryBuilder setPort(int port) {
        this.port = port;
        return this;
    }

    public MysqlRecoveryBuilder setDatabase(String database) {
        this.database = database;
        return this;
    }
}
