
package com.rengh.library.common.util;

import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Created by rengh on 17-5-29.
 */
public class PreferenceUtils {
    private static final String TAG = "PreferenceUtils";
    private SharedPreferences mSharedPreferences = null;
    private Editor mEditor = null;

    public PreferenceUtils(Context context, String name) {
        mSharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }

    public boolean putString(String key, String value) {
        if (null != mEditor) {
            return mEditor.putString(key, value).commit();
        } else {
            LogUtils.e(TAG, "putString() error. Editor is null.");
        }
        return false;
    }

    public boolean putBoolean(String key, boolean value) {
        if (null != mEditor) {
            return mEditor.putBoolean(key, value).commit();
        } else {
            LogUtils.e(TAG, "putBoolean() error. Editor is null.");
        }
        return false;
    }

    public boolean putInt(String key, int value) {
        if (null != mEditor) {
            return mEditor.putInt(key, value).commit();
        } else {
            LogUtils.e(TAG, "putInt() error. Editor is null.");
        }
        return false;
    }

    public boolean putLong(String key, long value) {
        if (null != mEditor) {
            return mEditor.putLong(key, value).commit();
        } else {
            LogUtils.e(TAG, "putLong() error. Editor is null.");
        }
        return false;
    }

    public boolean putFloat(String key, float value) {
        if (null != mEditor) {
            return mEditor.putFloat(key, value).commit();
        } else {
            LogUtils.e(TAG, "putFloat() error. Editor is null.");
        }
        return false;
    }

    public boolean putStringSet(String key, Set<String> values) {
        if (null != mEditor) {
            return mEditor.putStringSet(key, values).commit();
        } else {
            LogUtils.e(TAG, "putStringSet() error. Editor is null.");
        }
        return false;
    }

    public Map<String, ?> getAll() {
        if (null != mSharedPreferences) {
            return mSharedPreferences.getAll();
        } else {
            LogUtils.e(TAG, "getAll() error. Preference is null.");
        }
        return null;
    }

    public boolean getBoolean(String key, boolean defValue) {
        if (null != mSharedPreferences) {
            return mSharedPreferences.getBoolean(key, defValue);
        } else {
            LogUtils.e(TAG, "getBoolean() error. Preference is null.");
        }
        return defValue;
    }

    public int getInt(String key, int defValue) {
        if (null != mSharedPreferences) {
            return mSharedPreferences.getInt(key, defValue);
        } else {
            LogUtils.e(TAG, "getInt() error. Preference is null.");
        }
        return defValue;
    }

    public long getLong(String key, long defValue) {
        if (null != mSharedPreferences) {
            return mSharedPreferences.getLong(key, defValue);
        } else {
            LogUtils.e(TAG, "getLong() error. Preference is null.");
        }
        return defValue;
    }

    public float getFloat(String key, long defValue) {
        if (null != mSharedPreferences) {
            return mSharedPreferences.getFloat(key, defValue);
        } else {
            LogUtils.e(TAG, "getFloat() error. Preference is null.");
        }
        return defValue;
    }

    public String getString(String key, String defValue) {
        if (null != mSharedPreferences) {
            return mSharedPreferences.getString(key, defValue);
        } else {
            LogUtils.e(TAG, "getString() error. Preference is null.");
        }
        return defValue;
    }

    public Set<String> getStringSet(String key, Set<String> defValues) {
        if (null != mSharedPreferences) {
            return mSharedPreferences.getStringSet(key, defValues);
        } else {
            LogUtils.e(TAG, "getStringSet() error. Preference is null.");
        }
        return defValues;
    }

    public boolean contains(String key, boolean defValues) {
        if (null != mSharedPreferences) {
            return mSharedPreferences.contains(key);
        } else {
            LogUtils.e(TAG, "getStringSet() error. Preference is null.");
        }
        return defValues;
    }

    public boolean registerOnSharedPreferenceChangeListener(
            SharedPreferences.OnSharedPreferenceChangeListener listener) {
        if (null != mSharedPreferences) {
            mSharedPreferences.registerOnSharedPreferenceChangeListener(listener);
            return true;
        } else {
            LogUtils.e(TAG, "getStringSet() error. Preference is null.");
        }
        return false;
    }

    public boolean unregisterOnSharedPreferenceChangeListener(
            SharedPreferences.OnSharedPreferenceChangeListener listener) {
        if (null != mSharedPreferences) {
            mSharedPreferences.unregisterOnSharedPreferenceChangeListener(listener);
            return true;
        } else {
            LogUtils.e(TAG, "getStringSet() error. Preference is null.");
        }
        return false;
    }
}
