
package com.r.library.demo.preference;

import android.content.Context;

import com.r.library.common.util.PreferenceUtils;

public class PreferenceManager {
    private final String PREFERENCE_NAME = "demo";
    private final String KEY_BG_INDEX = "BgIndex";

    private static PreferenceManager instance;

    private PreferenceUtils preferenceUtils;

    public static PreferenceManager getInstance(Context context) {
        if (null == instance) {
            synchronized (PreferenceManager.class) {
                if (null == instance) {
                    instance = new PreferenceManager(context);
                }
            }
        }
        return instance;
    }

    private PreferenceManager(Context context) {
        preferenceUtils = new PreferenceUtils(context, PREFERENCE_NAME);
    }

    public void putBgIndex(int index) {
        preferenceUtils.putInt(KEY_BG_INDEX, index);
    }

    public int getBgIndex() {
        return preferenceUtils.getInt(KEY_BG_INDEX, 0);
    }
}
