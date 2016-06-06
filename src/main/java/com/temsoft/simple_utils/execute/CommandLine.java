package com.temsoft.simple_utils.execute;

import java.io.File;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Vector;


/**
 * 执行的命令描述
 */
public class CommandLine {

    //执行的程序
    private String executable;
    //参数
    private final Vector<String> arguments = new Vector<String>();

    public CommandLine(String executable) {
        this.executable = toCleanExecutable(executable);
    }

    public CommandLine(final File executable) {
        this.executable = toCleanExecutable(executable.getAbsolutePath());
    }

    public static CommandLine parse(final String line) {
        if (line == null) {
            throw new IllegalArgumentException("Command line can not be null");
        } else if (line.trim().length() == 0) {
            throw new IllegalArgumentException("Command line can not be empty");
        } else {
            String[] ls = translateCommandline(line);
            final CommandLine cl = new CommandLine(ls[0]);
            for (int i = 1; i < ls.length; i++) {
                cl.addArgument(ls[i]);
            }
            return cl;
        }
    }

    public String getExecutable() {
        return executable;
    }

    public void addArgument(String arg) {
        arguments.add(arg);
    }

    public void addArguments(String[] strings) {
        for (String str : strings) {
            arguments.add(str);
        }
    }

    public String[] getArguments() {
        return arguments.toArray(new String[arguments.size()]);
    }

    /**
     * 返回参数数组
     */
    public String[] toStrings() {
        final String[] result = new String[arguments.size() + 1];
        result[0] = this.getExecutable();
        System.arraycopy(getArguments(), 0, result, 1, result.length - 1);
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String s : toStrings()){
            sb.append(" ");
            sb.append(s);
        }
        return sb.toString();
    }

    private static String[] translateCommandline(final String toProcess) {
        if (toProcess == null || toProcess.length() == 0) {
            // no command? no string
            return new String[0];
        }

        // parse with a simple finite state machine
        final int normal = 0;
        final int inQuote = 1;
        final int inDoubleQuote = 2;
        int state = normal;
        final StringTokenizer tok = new StringTokenizer(toProcess, "\"\' ", true);
        final ArrayList<String> list = new ArrayList<String>();
        StringBuilder current = new StringBuilder();
        boolean lastTokenHasBeenQuoted = false;

        while (tok.hasMoreTokens()) {
            final String nextTok = tok.nextToken();
            switch (state) {
                case inQuote:
                    if ("\'".equals(nextTok)) {
                        lastTokenHasBeenQuoted = true;
                        state = normal;
                    } else {
                        current.append(nextTok);
                    }
                    break;
                case inDoubleQuote:
                    if ("\"".equals(nextTok)) {
                        lastTokenHasBeenQuoted = true;
                        state = normal;
                    } else {
                        current.append(nextTok);
                    }
                    break;
                default:
                    if ("\'".equals(nextTok)) {
                        state = inQuote;
                    } else if ("\"".equals(nextTok)) {
                        state = inDoubleQuote;
                    } else if (" ".equals(nextTok)) {
                        if (lastTokenHasBeenQuoted || current.length() != 0) {
                            list.add(current.toString());
                            current = new StringBuilder();
                        }
                    } else {
                        current.append(nextTok);
                    }
                    lastTokenHasBeenQuoted = false;
                    break;
            }
        }

        if (lastTokenHasBeenQuoted || current.length() != 0) {
            list.add(current.toString());
        }

        if (state == inQuote || state == inDoubleQuote) {
            throw new IllegalArgumentException("Unbalanced quotes in "
                    + toProcess);
        }

        final String[] args = new String[list.size()];
        return list.toArray(args);
    }

    /**
     * 清理被执行程序String（替换文件分隔符）
     */
    private String toCleanExecutable(final String dirtyExecutable) {
        if (dirtyExecutable == null) {
            throw new IllegalArgumentException("Executable can not be null");
        } else if (dirtyExecutable.trim().length() == 0) {
            throw new IllegalArgumentException("Executable can not be empty");
        } else {
            return fixFileSeparatorChar(dirtyExecutable);
        }
    }

    private static final char SLASH_CHAR = '/';
    private static final char BACKSLASH_CHAR = '\\';
    private static String fixFileSeparatorChar(final String arg) {
        return arg.replace(SLASH_CHAR, File.separatorChar).replace(
                BACKSLASH_CHAR, File.separatorChar);
    }

}
