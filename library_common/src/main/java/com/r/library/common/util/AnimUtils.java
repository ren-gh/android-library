
package com.r.library.common.util;

import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.view.View;

public class AnimUtils {
    public static void startShakeXByProperty(View view, float shakeDegrees, long duration) {
        if (0 > shakeDegrees || 0 > duration) {
            return;
        }
        if (0 == shakeDegrees) {
            shakeDegrees = 3.0f;
        }
        if (0 == duration) {
            duration = 800;
        }
        PropertyValuesHolder x = PropertyValuesHolder.ofKeyframe(View.TRANSLATION_X,
                Keyframe.ofFloat(0f, 0f),
                Keyframe.ofFloat(0.1f, -shakeDegrees),
                Keyframe.ofFloat(0.2f, shakeDegrees),
                Keyframe.ofFloat(0.3f, -shakeDegrees),
                Keyframe.ofFloat(0.4f, shakeDegrees),
                Keyframe.ofFloat(0.5f, -shakeDegrees),
                Keyframe.ofFloat(0.6f, shakeDegrees),
                Keyframe.ofFloat(0.7f, -shakeDegrees),
                Keyframe.ofFloat(0.8f, shakeDegrees),
                Keyframe.ofFloat(0.9f, -shakeDegrees),
                Keyframe.ofFloat(1.0f, 0f));
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(view, x);
        objectAnimator.setDuration(duration);
        objectAnimator.start();
    }

    public static void startShakeYByProperty(View view, float shakeDegrees, long duration) {
        if (0 > shakeDegrees || 0 > duration) {
            return;
        }
        if (0 == shakeDegrees) {
            shakeDegrees = 3.0f;
        }
        if (0 == duration) {
            duration = 800;
        }
        PropertyValuesHolder y = PropertyValuesHolder.ofKeyframe(View.TRANSLATION_Y,
                Keyframe.ofFloat(0f, 0f),
                Keyframe.ofFloat(0.1f, -shakeDegrees),
                Keyframe.ofFloat(0.2f, shakeDegrees),
                Keyframe.ofFloat(0.3f, -shakeDegrees),
                Keyframe.ofFloat(0.4f, shakeDegrees),
                Keyframe.ofFloat(0.5f, -shakeDegrees),
                Keyframe.ofFloat(0.6f, shakeDegrees),
                Keyframe.ofFloat(0.7f, -shakeDegrees),
                Keyframe.ofFloat(0.8f, shakeDegrees),
                Keyframe.ofFloat(0.9f, -shakeDegrees),
                Keyframe.ofFloat(1.0f, 0f));
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(view, y);
        objectAnimator.setDuration(duration);
        objectAnimator.start();
    }
}
