package com.gztoucher.sensortest.helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PowerManager;

/**
 * 屏幕状态监听。参见：http://blog.csdn.net/mengweiqi33/article/details/18094221
 *
 * @author 李玉江[QQ:1023694760]
 * @version 2015/4/13
 *          Created by IntelliJ IDEA 14.1
 */
public class ScreenListener {
    private Context mContext;
    private ScreenBroadcastReceiver mScreenReceiver;
    private ScreenStateListener mScreenStateListener;

    public ScreenListener(Context context) {
        mContext = context;
        mScreenReceiver = new ScreenBroadcastReceiver();
    }

    /**
     * 开始监听屏幕状态
     *
     * @param listener
     */
    public void start(ScreenStateListener listener) {
        mScreenStateListener = listener;
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        mContext.registerReceiver(mScreenReceiver, filter);
        initScreenState();
    }

    /**
     * 初始化屏幕状态
     */
    private void initScreenState() {
        if (mScreenStateListener == null) {
            throw new IllegalArgumentException("listener is null");
        }
        PowerManager manager = (PowerManager) mContext
                .getSystemService(Context.POWER_SERVICE);
        if (manager.isScreenOn()) {
            mScreenStateListener.onScreenOn();
        } else {
            mScreenStateListener.onScreenOff();
        }
    }

    /**
     * 停止screen状态监听
     */
    public void stop() {
        mContext.unregisterReceiver(mScreenReceiver);
    }

    /**
     * 屏幕状态广播接收者
     */
    private class ScreenBroadcastReceiver extends BroadcastReceiver {
        private String action = null;

        @Override
        public void onReceive(Context context, Intent intent) {
            action = intent.getAction();
            if (Intent.ACTION_SCREEN_ON.equals(action)) { // 开屏
                mScreenStateListener.onScreenOn();
            } else if (Intent.ACTION_SCREEN_OFF.equals(action)) { // 锁屏
                mScreenStateListener.onScreenOff();
            } else if (Intent.ACTION_USER_PRESENT.equals(action)) { // 解锁
                mScreenStateListener.onUserPresent();
            }
        }

    }

    /**
     * 回调接口
     */
    public interface ScreenStateListener {

        /**
         * 屏幕已点亮
         */
        void onScreenOn();

        /**
         * 屏幕已熄灭
         */
        void onScreenOff();

        /**
         * 屏幕已解锁
         */
        void onUserPresent();

    }

}

