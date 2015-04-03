package com.gztoucher.sensortest.helper;

import android.app.Activity;
import android.content.Context;
import android.os.PowerManager;
import android.view.WindowManager;

/**
 * 保持CPU一直运行或屏幕长亮。需要权限：android.permission.WAKE_LOCK
 *
 * @author 李玉江[QQ:1023694760]
 * @version 2015/4/3
 *          Created by IntelliJ IDEA 14.1
 */
public class WakeLocker {
    private static final String TAG = "wake_locker";
    private PowerManager powerManager;
    private PowerManager.WakeLock wakeLock;
    private Type type;

    public WakeLocker(Context context, Type type) {
        this.type = type;
        powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
    }

    public void acquire() {
        int level;
        if (type.equals(Type.KEEP_SCREEN_ON)) {
            level = PowerManager.SCREEN_BRIGHT_WAKE_LOCK;
        } else {
            level = PowerManager.PARTIAL_WAKE_LOCK;
        }
        wakeLock = powerManager.newWakeLock(level | PowerManager.ON_AFTER_RELEASE, TAG);
        wakeLock.acquire();
    }

    public void release() {
        if (null != wakeLock) {
            wakeLock.release();
        }
    }

    public static void keepScreenBright(Activity activity) {
        //必须在setContentView()之前调用
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    public enum Type {
        KEEP_CPU_RUN, KEEP_SCREEN_ON
    }

}
