
package com.r.library.common.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {
    private volatile static Toast toast;

    public static void showToast(Context context, int resId) {
        show(context, context.getResources().getString(resId), Toast.LENGTH_LONG);
    }

    public static void showToast(Context context, int resId, int duration) {
        show(context, context.getResources().getString(resId), duration);
    }

    public static void showToast(Context context, String content) {
        show(context, content, Toast.LENGTH_LONG);
    }

    public static void showToast(Context context, String content, int duration) {
        show(context, content, duration);
    }

    private static synchronized void show(Context context, String content, int duration) {
        if (null == toast) {
            toast = Toast.makeText(context.getApplicationContext(), content, duration);
        } else {
            toast.setDuration(duration);
            toast.setText(content);
        }
        try {
            toast.show();
        } catch (Exception e) {
        }
    }

    public static synchronized void destory(Context context) {
        if (null != toast) {
            toast.cancel();
            toast = null;
        }
    }

}
