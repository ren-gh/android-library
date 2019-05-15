
package com.r.library.common.util;

import java.util.concurrent.TimeUnit;

import android.app.Instrumentation;
import android.content.Context;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.WindowManager;

public class KeyEventUtils {
    public static void sendKeyEvent(final int keyCode, int delayMills) {
        ThreadManager.getInstance().excuteScheduled(new Runnable() {
            @Override
            public void run() {
                Instrumentation inst = new Instrumentation();
                inst.sendKeyDownUpSync(keyCode);
            }
        }, delayMills, TimeUnit.MILLISECONDS);
    }

    public static void clickCenterOfScreen(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        final int x = dm.widthPixels / 2;
        final int y = dm.heightPixels / 2;
        ThreadManager.getInstance().excuteScheduled(new Runnable() {
            @Override
            public void run() {
                Instrumentation inst = new Instrumentation();
                inst.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
                        MotionEvent.ACTION_DOWN, x, y, 0));
                inst.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
                        MotionEvent.ACTION_UP, x, y, 0));
            }
        }, 1, TimeUnit.SECONDS);
    }
}
