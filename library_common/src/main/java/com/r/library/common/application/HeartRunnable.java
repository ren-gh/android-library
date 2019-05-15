
package com.r.library.common.application;

import java.util.concurrent.TimeUnit;

import com.r.library.common.util.LogUtils;
import com.r.library.common.util.ThreadManager;

import android.text.TextUtils;

/**
 * Created by rengh on 17-5-29.
 */
public class HeartRunnable implements Runnable {
    private final String TAG = "HeartRunnable";
    private String mHeartType;

    public HeartRunnable() {
    }

    public HeartRunnable(String type) {
        mHeartType = type;
    }

    /**
     * 启动心跳
     *
     * @param delay 首次执行延迟时间，单位：s
     * @param reRun 后续执行间隔时间，单位：s
     */
    public static void init(int delay, int reRun) {
        ThreadManager.getInstance().excuteScheduledSingle(new HeartRunnable(), delay, reRun, TimeUnit.SECONDS);
    }

    /**
     * 启动心跳
     * 
     * @param type 心跳类型，若设置，则每次心跳都打印
     * @param delay 首次执行延迟时间，单位：s
     * @param reRun 后续执行间隔时间，单位：s
     */
    public static void init(String type, int delay, int reRun) {
        ThreadManager.getInstance().excuteScheduledSingle(new HeartRunnable(type), delay, reRun, TimeUnit.SECONDS);
    }

    @Override
    public void run() {
        if (TextUtils.isEmpty(mHeartType)) {
            LogUtils.d(TAG, "PID: " + android.os.Process.myPid() + " TID: " + Thread.currentThread().getId() + " Heart...");
        } else {
            LogUtils.d(TAG, "TYPE: " + mHeartType + " PID: " + android.os.Process.myPid() + " TID: " + Thread.currentThread().getId() + " Heart...");
        }
    }
}
