package com.first.myapp.com.myapplication.util;

import java.util.Formatter;

/**
 * Created by Jayda on 5/8/17.
 */

public class MathUtil {

    public static String floatFormat(float value, int decimalPoint) {
        StringBuilder sb = new StringBuilder();
        sb.append("%");
        sb.append(".");
        sb.append(decimalPoint);
        sb.append("f");
    /*
     * %.2f % 表示 小数点前任意位数 2 表示两位小数 格式后的结果为 f 表示浮点型
     */
        return new Formatter().format(sb.toString(), value).toString();
    }
}
