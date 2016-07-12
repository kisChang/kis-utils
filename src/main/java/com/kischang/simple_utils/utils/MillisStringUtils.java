package com.kischang.simple_utils.utils;

import java.util.Date;

/**
 * @author KisChang
 * @version 0.1
 */
public class MillisStringUtils {

    public static long afterMillis(Date date) {
        return afterMillis(date.getTime());
    }

    public static long afterMillis(long timestamp) {
        return (System.currentTimeMillis() - timestamp) / (1000 * 60);//转化minute
    }

    public static MillisString millisToStr(long millis) {
        if (millis < 60) {
            return new MillisString(millis, MillisString.Type.Minute);
        } else if (millis >= 60 && millis < 24 * 60) {
            return new MillisString(millis / 60, MillisString.Type.Hour);
        } else {
            return new MillisString(millis / (24 * 60), MillisString.Type.Day);
        }
    }

    public static MillisString after(Date date) {
        return after(date.getTime());
    }

    public static MillisString after(long timestamp) {
        return millisToStr(afterMillis(timestamp));
    }

    /**
     * 将时间转换成 类似：
     * > 1分钟
     * > 10小时
     * > 2天
     * 返回值
     * @author KisChang
     * @version 1.0
     */
    public static class MillisString {

        private long num;
        private Type type;

        public enum Type {
            Minute("分钟", Type.Minute_Val), Hour("小时", Type.Hour_Val), Day("天", Type.Day_Val);

            public static final int Minute_Val  = 1;
            public static final int Hour_Val    = 2;
            public static final int Day_Val     = 3;

            private int value;
            private String desc;

            Type(String desc, int value) {
                this.value = value;
                this.desc = desc;
            }

            public int getValue() {
                return value;
            }

            public String getDesc() {
                return desc;
            }
        }

        public MillisString(long num, Type type) {
            this.num = num;
            this.type = type;
        }

        public long getNum() {
            return num;
        }

        public int getType() {
            return type.getValue();
        }

        public Type getTypeEnum() {
            return type;
        }

        @Override
        public String toString() {
            return num + type.desc;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            MillisString that = (MillisString) o;

            if (num != that.num) return false;
            if (type != that.type) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = (int) (num ^ (num >>> 32));
            result = 31 * result + (type != null ? type.hashCode() : 0);
            return result;
        }
    }
}
