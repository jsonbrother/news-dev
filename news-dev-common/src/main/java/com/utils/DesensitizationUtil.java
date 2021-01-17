package com.utils;

/**
 * 通用脱敏工具类
 * <p>可用于：用户名 手机号 邮箱 地址等
 * Created by TongHaiJun
 * 2021/1/17 10:53
 */
public class DesensitizationUtil {

    private static final int SIZE = 6;
    private static final String SYMBOL = "*";

    /***
     * 通用脱敏方法
     * @param value 值
     * @return str
     */
    public static String commonDisplay(String value) {
        if (null == value || "".equals(value)) {
            return value;
        }
        int len = value.length();
        int pamOne = len / 2;
        int pamTwo = pamOne - 1;
        int pamThree = len % 2;
        StringBuilder stringBuilder = new StringBuilder();
        if (len <= 2) {
            if (pamThree == 1) {
                return SYMBOL;
            }
            stringBuilder.append(SYMBOL);
            stringBuilder.append(value.charAt(len - 1));
        } else {
            if (pamTwo <= 0) {
                stringBuilder.append(value, 0, 1);
                stringBuilder.append(SYMBOL);
                stringBuilder.append(value, len - 1, len);

            } else if (pamTwo >= SIZE / 2 && SIZE + 1 != len) {
                int pamFive = (len - SIZE) / 2;
                stringBuilder.append(value, 0, pamFive);
                for (int i = 0; i < SIZE; i++) {
                    stringBuilder.append(SYMBOL);
                }
                stringBuilder.append(value, len - (pamFive + 1), len);
            } else {
                int pamFour = len - 2;
                stringBuilder.append(value, 0, 1);
                for (int i = 0; i < pamFour; i++) {
                    stringBuilder.append(SYMBOL);
                }
                stringBuilder.append(value, len - 1, len);
            }
        }
        return stringBuilder.toString();
    }
}
