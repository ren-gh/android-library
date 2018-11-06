
package com.rengh.rlibrary.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.rengh.library.common.util.LogUtils;

public class NotificationClickReceiver extends BroadcastReceiver {
    private final String TAG = "NotificationClickReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtils.i(TAG, "onReceive");
        LogUtils.i(TAG, "通知ID：" + intent.getIntExtra("notificationId", 0));
        LogUtils.i(TAG, "有线网卡MAC：" + intent.getStringExtra("eth0Mac"));
        LogUtils.i(TAG, "无线网卡MAC：" + intent.getStringExtra("wlan0Mac"));
        LogUtils.i(TAG, "联网网卡：" + intent.getStringExtra("netDev"));
        LogUtils.i(TAG, "联网网卡MAC：" + intent.getStringExtra("netDevMac"));
        LogUtils.i(TAG, "联网网卡IP：" + intent.getStringExtra("netDevIp"));
        LogUtils.i(TAG, "设备IMEI：" + intent.getStringArrayListExtra("imei"));
        LogUtils.i(TAG, "设备MEID：" + intent.getStringArrayListExtra("meid"));
    }
}
