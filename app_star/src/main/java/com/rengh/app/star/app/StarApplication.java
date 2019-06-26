
package com.rengh.app.star.app;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.support.multidex.MultiDex;

import com.bumptech.glide.Glide;
import com.r.library.common.util.LogUtils;
import com.rengh.app.star.service.StarIntentService;

public class StarApplication extends Application {
    private final String TAG = "StarApplication";
    private Context mContext;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
        initLogUtil();
        LogUtils.i(TAG, "attachBaseContext()");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.i(TAG, "onCreate()");

        mContext = this;

        StarIntentService.startIntentService(mContext, StarIntentService.ACTION_INIT, null);
    }

    private void initLogUtil() {
        LogUtils.setRootTag(TAG);
        LogUtils.setAutoSave(true);
        LogUtils.setSaveRuntimeInfo(true);
        LogUtils.setLogSaveDay(7);
        LogUtils.setLogPath(getFilesDir().getPath() + "/Logs");
        LogUtils.i(TAG, "Init log completed.");
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        LogUtils.i(TAG, "onTerminate()");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LogUtils.i(TAG, "onConfigurationChanged()");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        LogUtils.i(TAG, "onLowMemory()");
        Glide.get(this).clearMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        Glide.get(this).trimMemory(level);
        switch (level) {
            case TRIM_MEMORY_UI_HIDDEN: {
                LogUtils.i(TAG, "onTrimMemory() TRIM_MEMORY_UI_HIDDEN");
                Glide.get(this).clearMemory();
            }
                break;
            case TRIM_MEMORY_BACKGROUND: {
                LogUtils.i(TAG, "onTrimMemory() TRIM_MEMORY_BACKGROUND");
            }
                break;
            case TRIM_MEMORY_COMPLETE: {
                LogUtils.i(TAG, "onTrimMemory() TRIM_MEMORY_COMPLETE");
            }
                break;
            case TRIM_MEMORY_MODERATE: {
                LogUtils.i(TAG, "onTrimMemory() TRIM_MEMORY_MODERATE");
            }
                break;
            case TRIM_MEMORY_RUNNING_CRITICAL: {
                LogUtils.i(TAG, "onTrimMemory() TRIM_MEMORY_RUNNING_CRITICAL");
            }
                break;
            case TRIM_MEMORY_RUNNING_LOW: {
                LogUtils.i(TAG, "onTrimMemory() TRIM_MEMORY_RUNNING_LOW");
            }
                break;
            case TRIM_MEMORY_RUNNING_MODERATE: {
                LogUtils.i(TAG, "onTrimMemory() TRIM_MEMORY_RUNNING_MODERATE");
            }
                break;
            default:
                LogUtils.i(TAG, "onTrimMemory() Unknow: " + level);
                break;
        }
    }
}
