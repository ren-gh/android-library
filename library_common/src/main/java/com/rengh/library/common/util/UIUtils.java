
package com.rengh.library.common.util;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by rengh on 17-5-29.
 */
public class UIUtils {
    public static Resources getResources(Context context) {
        return context.getResources();
    }

    public static int dip2px(Context context, float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources(context).getDisplayMetrics());
    }

    public static int sip2px(Context context, float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources(context).getDisplayMetrics());
    }

    public static void scaleView(View view, float value) {
        scaleView(view, value, 300);
    }

    public static void scaleView(View view, float value, int duration) {
        if (view != null) {
            ObjectAnimator animx = ObjectAnimator.ofFloat(view, "scaleX", value);
            ObjectAnimator animy = ObjectAnimator.ofFloat(view, "scaleY", value);
            AnimatorSet set = new AnimatorSet();
            set.setDuration(duration);
            set.setInterpolator(new DecelerateInterpolator());
            set.play(animx).with(animy);
            set.start();
        }
    }
}
