package com.gztoucher.sensortest.helper;

/**
 * 判断是否重复的动作，防止短时间大量重复或者也可是说是无效的操作
 *
 * @author 李玉江[QQ:1023694760]
 * @version 2015/4/3
 *          Created by IntelliJ IDEA 14.1
 */
public class RepeatHelper {
    private static final long DEFAULT_TIME_MILLIS = 800;//毫秒
    private static long lastTimeMillis = 0L;

    public static boolean isFastDoubleAction(long maxTimeMillis) {
        long currentTimeMillis = System.currentTimeMillis();
        long diff = currentTimeMillis - lastTimeMillis;
        if (diff < maxTimeMillis) {
            return true;
        } else {
            lastTimeMillis = currentTimeMillis;
            return false;
        }
    }

    public static boolean isFastDoubleAction() {
        //间隔时间视具体项目中需求情况而定
        return isFastDoubleAction(DEFAULT_TIME_MILLIS);
    }

}
