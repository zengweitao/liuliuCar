package com.cheweibao.liuliu.common;

//防止按钮重复点击
public class ButCommonUtils {
    private static long lastClickTime;
    private static long lastClick3sTime;

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 900) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    public static boolean isFastDoubleClick1s() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 1500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    public static boolean isFastDoubleClick3s() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClick3sTime;
        if (0 < timeD && timeD < 3000) {
            return true;
        }
        lastClick3sTime = time;
        return false;
    }

}
