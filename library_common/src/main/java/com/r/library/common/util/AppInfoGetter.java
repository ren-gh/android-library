package com.r.library.common.util;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

public class AppInfoGetter {
    private final static String TAG = "AppInfoGetter";

    public static List<AppInfo> getAllAppInfo(Context context) {
        ArrayList<AppInfo> appList = new ArrayList<>();
        try {
            PackageManager packageManager = context.getPackageManager();
            List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
            if (null != packageInfos && packageInfos.size() > 0) {
                for (int i = 0; i < packageInfos.size(); i++) {
                    String pkg = packageInfos.get(i).packageName;
                    LogUtils.d(TAG, "pkg: " + pkg);
                    AppInfo appInfo = new AppInfo(context);
                    appInfo.setOtherPackageName(pkg);
                    appList.add(appInfo);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(TAG, "getAllAppInfo exception " + e.getMessage());
        }
        return appList;
    }

    public static List<AppInfo> getMainAppInfo(Context context) {
        ArrayList<AppInfo> appList = new ArrayList<>();
        try {
            PackageManager packageManager = context.getPackageManager();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(intent, 0);
            if (resolveInfoList.size() > 0) {
                for (int i = 0; i < resolveInfoList.size(); i++) {
                    ActivityInfo activityInfo = resolveInfoList.get(i).activityInfo;
                    String app = activityInfo.applicationInfo.loadLabel(packageManager).toString();
                    String pkg = activityInfo.packageName;
                    LogUtils.d(TAG, " appName " + app + " pkg " + pkg);
                    AppInfo appInfo = new AppInfo(context);
                    appInfo.setOtherPackageName(pkg);
                    appList.add(appInfo);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(TAG, "getMainAppInfo exception " + e.getMessage());
        }
        return appList;
    }
}
