
package com.r.library.demo.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

import com.r.library.common.util.BitmapUtils;
import com.r.library.common.util.FileUtils;
import com.r.library.demo.preference.PreferenceManager;

import java.io.InputStream;

public class BackgroundUtils {
    public static void autoUpdateBackground(Context context, View view) {
        int bgIndex = PreferenceManager.getInstance(context).getBgIndex();
        if (bgIndex != 0) {
            String path = "background/bg_jianyue_" + bgIndex + ".jpeg";
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
            String path = "background/bg_jianyue_" + bgIndex + ".jpeg";
            InputStream inputStream = FileUtils.getAssetsFileInputStream(context, path);
            coverDrawable = BitmapUtils.readBitMap(context, inputStream);
        }
        return new BitmapDrawable(context.getResources(), coverDrawable);
    }
}
