
package com.r.library.demo.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

import com.r.library.common.util.BitmapUtils;
import com.r.library.common.util.FileUtils;

import com.r.library.common.util.LogUtils;
import com.r.library.demo.preference.PreferenceManager;

import java.io.InputStream;

public class BgUtils {
    private static final String TAG = "BgUtils";

    public static void autoUpdateBackground(Context context, View view) {
        int bgIndex = PreferenceManager.getInstance(context).getBgIndex();
        LogUtils.i(TAG, "autoUpdateBackground() bg index: " + bgIndex);
        if (bgIndex != 0) {
            String path = getPath(bgIndex);
            InputStream inputStream = FileUtils.getAssetsFileInputStream(context, path);
            Bitmap bgBmp = BitmapUtils.readBitMap(context, inputStream);
            if (null != bgBmp) {
                if (Build.VERSION.SDK_INT >= 16) {
                    view.setBackground(new BitmapDrawable(context.getResources(), bgBmp));
                } else {
                    view.setBackgroundDrawable(new BitmapDrawable(context.getResources(), bgBmp));
                }
            }
        }
    }

    public static void nextBackground(Context context, View view) {
        int bgIndex = PreferenceManager.getInstance(context).getBgIndex();
        if (bgIndex != 0) {
            if (20 == bgIndex) {
                bgIndex = 1;
            } else {
                bgIndex += 1;
            }
            PreferenceManager.getInstance(context).putBgIndex(bgIndex);
            bgIndex = PreferenceManager.getInstance(context).getBgIndex();
            LogUtils.i(TAG, "nextBackground() bg index: " + bgIndex);
            String path = getPath(bgIndex);
            InputStream inputStream = FileUtils.getAssetsFileInputStream(context, path);
            Bitmap bgBmp = BitmapUtils.readBitMap(context, inputStream);
            if (null != bgBmp) {
                if (Build.VERSION.SDK_INT >= 16) {
                    view.setBackground(new BitmapDrawable(context.getResources(), bgBmp));
                } else {
                    view.setBackgroundDrawable(new BitmapDrawable(context.getResources(), bgBmp));
                }
            }
        }
    }

    public static Drawable getCoverDrawable(Context context) {
        Bitmap coverDrawable = null;
        int bgIndex = PreferenceManager.getInstance(context).getBgIndex();
        if (bgIndex != 0) {
            if (20 == bgIndex) {
                bgIndex = 1;
            } else {
                bgIndex += 1;
            }
            LogUtils.i(TAG, "getCoverDrawable() bg index: " + bgIndex);
            String path = getPath(bgIndex);
            InputStream inputStream = FileUtils.getAssetsFileInputStream(context, path);
            coverDrawable = BitmapUtils.readBitMap(context, inputStream);
        }
        return new BitmapDrawable(context.getResources(), coverDrawable);
    }

    private static String getPath(int bgIndex) {
        return "background/bg_jianyue_" + bgIndex + ".jpeg";
    }
}
