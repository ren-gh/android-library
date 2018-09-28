
package com.rengh.library.common.util;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.view.View;

import com.rengh.library.common.R;

public class RoundFocusUtils {
    private final static float sRadus = 9;
    private final static float[] sRids = {
            sRadus, sRadus, sRadus, sRadus, sRadus, sRadus, sRadus, sRadus
    };

    public static Canvas onDraw(View view, Canvas canvas) {
        // if (view.isFocused()) {
        // float size = view.getResources().getDimension(R.dimen.dp_2);
        // RectF r = new RectF(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        // Paint paint = new Paint();
        // paint.setColor(view.getResources().getColor(R.color.colorRed));
        // canvas.drawRoundRect(r, sRadus, sRadus, paint);
        // Path path = new Path();
        // RectF rectF = new RectF(size, size, view.getMeasuredWidth() - size, view.getMeasuredHeight() - size);
        // path.addRoundRect(rectF, sRids, Path.Direction.CW);
        // canvas.clipPath(path);
        // } else {
        // Path path = new Path();
        // RectF rectF = new RectF(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        // path.addRoundRect(rectF, sRids, Path.Direction.CW);
        // canvas.clipPath(path);
        // }
        if (view.isFocused()) {
            float size = view.getResources().getDimension(R.dimen.dp_2);
            RectF r = new RectF(-size, -size, view.getMeasuredWidth() + size, view.getMeasuredHeight() + size);
            Paint paint = new Paint();
            paint.setColor(view.getResources().getColor(R.color.colorRed));
            canvas.drawRoundRect(r, sRadus, sRadus, paint);
            Path path = new Path();
            RectF rectF = new RectF(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
            path.addRoundRect(rectF, sRids, Path.Direction.CW);
            canvas.clipPath(path);
        } else {
            Path path = new Path();
            RectF rectF = new RectF(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
            path.addRoundRect(rectF, sRids, Path.Direction.CW);
            canvas.clipPath(path);
        }
        return canvas;
    }
}
