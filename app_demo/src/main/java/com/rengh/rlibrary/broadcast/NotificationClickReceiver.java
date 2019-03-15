
package com.rengh.rlibrary.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.rengh.library.common.net.LocalNetHelper;
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

        StringBuffer sb = new StringBuffer();
        sb.append("Base Info: ");
        sb.append("product=").append(Build.PRODUCT);
        sb.append("&brand=").append(Build.BRAND);
        sb.append("&board=").append(Build.BOARD);
        sb.append("&model=").append(Build.MODEL);
        sb.append("&user=").append(Build.USER);
        sb.append("&type=").append(Build.TYPE);
        sb.append("&id=").append(Build.ID);
        sb.append("&sdk=").append(Build.VERSION.SDK_INT);
        sb.append("&eth=").append(LocalNetHelper.getMac(LocalNetHelper.TYPE_ETH_0));
        sb.append("&wlan=").append(LocalNetHelper.getMac(LocalNetHelper.TYPE_WLAN_0));
        sb.append("&netDev=").append(LocalNetHelper.getIpName());
        sb.append("&netMac=").append(LocalNetHelper.getMac(LocalNetHelper.getIpName()));
        sb.append("$ip=").append(LocalNetHelper.getIp());
        sb.append("&imei=").append(LocalNetHelper.getIMEI(context));
        sb.append("&meid=").append(LocalNetHelper.getMEID(context));
        sb.append(" ");
        LogUtils.i(TAG, sb.toString());
    }
}
