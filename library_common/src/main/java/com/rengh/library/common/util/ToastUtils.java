
package com.rengh.library.common.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {
    private static Toast toast;

    public static Toast getToast(Context context) {
        if (null == toast) {
            toast = new Toast(context);
        }
        return toast;
    }

    public static void showToast(Context context, int resId) {
        showToast(context, context.getResources().getString(resId), Toast.LENGTH_LONG);
    }

    public static void showToast(Context context, int resId, int duration) {
        showToast(context, context.getResources().getString(resId), duration);
    }

    public static void showToast(Context context, String content) {
        showToast(context, content, Toast.LENGTH_LONG);
    }

    public static void showToast(Context context, String content, int duration) {
        if (toast == null) {
            toast = getToast(context);
        }
        toast.setDuration(duration);
        toast.setText(content);
        toast.show();
    }
}
