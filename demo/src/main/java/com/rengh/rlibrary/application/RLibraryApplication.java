
package com.rengh.rlibrary.application;

import android.app.Application;
import android.content.res.Configuration;
import android.content.res.Resources;

import com.rengh.library.common.notification.NotificationHelper;
import com.rengh.library.common.util.LogUtils;
import com.rengh.rlibrary.R;

public class RLibraryApplication extends Application {
    private final String TAG = "RLibraryApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.setAutoSave(false);
        LogUtils.setRootTag("RLibraryDemo");
        LogUtils.i(TAG, "onCreate()");

        NotificationHelper.getInstance().init(this).initChannelId(getString(R.string.app_name));
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
