package com.r.library.common.apk;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.r.library.common.util.FileUtils;

import java.io.File;

public class ApkInstaller {
    private static boolean hasSDCard = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);

    public static String getSdcardDirPath(String appDirName) {
        String appDir = null;
        if (hasSDCard) { // SD卡根目录的hello.text
            appDir = Environment.getExternalStorageDirectory().getPath() + "/" + appDirName;
        } else {  // 系统下载缓存根目录的hello.text
            appDir = Environment.getDownloadCacheDirectory().getPath() + "/" + appDirName;
        }
        String dir = appDir + "/";
        return mkdirs(dir);
    }

    public static String getCacheDirPath(AppCompatActivity activity) {
        String dir = activity.getCacheDir().getPath() + "/";
        return mkdirs(dir);
    }

    public static String getFileDirPath(AppCompatActivity activity) {
        String dir = activity.getFilesDir().getPath() + "/";
        return mkdirs(dir);
    }

    public static void installApk(AppCompatActivity activity, String apkPath) {
        if (activity == null || TextUtils.isEmpty(apkPath)) {
            return;
        }
        File file = new File(apkPath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String type = "application/vnd.android.package-archive";
        if (Build.VERSION.SDK_INT >= 24) {
            Uri apkUri = FileProvider.getUriForFile(activity, activity.getPackageName()+".FileProvider", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(apkUri, type);
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(file), type);
        }
        activity.startActivity(intent);
    }

    public static boolean canRequestPackageInstalls(AppCompatActivity activity, int resultCode) {
        if (Build.VERSION.SDK_INT >= 26) {
            boolean b = activity.getPackageManager().canRequestPackageInstalls();
            if (!b) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                activity.startActivityForResult(intent, resultCode);
                return false;
            }
        }
        return true;
    }

    private static String mkdirs(String dir) {
        if (!FileUtils.isExists(dir)) {
            FileUtils.createDir(dir);
        }
        return dir;
    }

}
