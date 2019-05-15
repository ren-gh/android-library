package com.r.library.demo.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import com.r.library.common.util.LogUtils;
import com.r.library.common.util.ToastUtils;

public class AppInstallReceiver extends BroadcastReceiver {
    private final String TAG = "AppInstallReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        LogUtils.i(TAG,"action: " + action);
        PackageManager manager = context.getPackageManager();
        if (action.equals(Intent.ACTION_PACKAGE_ADDED)) {
            String packageName = intent.getData().getSchemeSpecificPart();
            ToastUtils.showToast(context, "安装成功：" + packageName);
        } else if (action.equals(Intent.ACTION_PACKAGE_REMOVED)) {
            String packageName = intent.getData().getSchemeSpecificPart();
            ToastUtils.showToast(context, "卸载成功：" + packageName);
        } else if (action.equals(Intent.ACTION_PACKAGE_REPLACED)) {
            String packageName = intent.getData().getSchemeSpecificPart();
            ToastUtils.showToast(context, "替换成功：" + packageName);
        } else {
            LogUtils.e(TAG,"Unknow action.");
        }
    }
}
