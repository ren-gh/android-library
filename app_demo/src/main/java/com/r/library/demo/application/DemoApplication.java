
package com.r.library.demo.application;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.r.library.common.application.CrashHandler;
import com.r.library.common.application.HeartRunnable;
import com.r.library.common.notification.NotificationHelper;
import com.r.library.common.util.LogUtils;
import com.r.library.demo.preference.PreferenceManager;
import com.r.library.demo.tangram.manager.TangramDemoManager;

public class DemoApplication extends MultiDexApplication {
    private final String TAG = "DemoApplication";

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        String rootTag = "RLibraryDemo";
        String heartTag = "RLibraryDemo";

        LogUtils.setAutoSave(false);
        LogUtils.setRootTag(rootTag);
        LogUtils.i(TAG, "onCreate()");

        NotificationHelper.getInstance().init(this).initChannelId(heartTag);
        CrashHandler.getInstance().init(this);
        HeartRunnable.init(heartTag, 0, 30);

        int bgIndex = (int) (1 + Math.random() * (20 - 1 + 1));
        PreferenceManager.getInstance(this).putBgIndex(bgIndex);

        TangramDemoManager.init(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    @Override
    public void setTheme(int resid) {
        super.setTheme(resid);
    }

    @Override
    public Resources.Theme getTheme() {
        return super.getTheme();
    }
}
