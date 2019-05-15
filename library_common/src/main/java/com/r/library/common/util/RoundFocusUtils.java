
package com.r.library.common.util;

import android.graphics.Canvas;
import android.graphics.DrawFilter;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.RectF;
import android.view.View;

import com.r.library.common.R;

public class RoundFocusUtils {
    private static DrawFilter drawFilter = new PaintFlagsDrawFilter(0, Paint.FILTER_BITMAP_FLAG);

    public static Canvas onDrawWithLine(View view, boolean hasFocus, Canvas canvas) {
        return onDrawWithLine(view, hasFocus, canvas, 9);
    }

    public static Canvas onDrawWithLine(View view, boolean hasFocus, Canvas canvas, float radiu) {
        try {
            float radius = radiu;
            if (hasFocus) {
                float size = view.getResources().getDimension(R.dimen.dp_1_5);
                float x = 0.4f;
                RectF r = new RectF(-size * x, -size * x, view.getMeasuredWidth() + size * x, view.getMeasuredHeight() + size * x);
                Paint paint = new Paint();
                paint.setFlags(Paint.ANTI_ALIAS_FLAG);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(size);
                paint.setColor(view.getResources().getColor(R.color.colorWhite));
                paint.setFilterBitmap(true);
                paint.setAntiAlias(true);
                if (0 == radius) {
                    canvas.drawRect(r, paint);
                } else {
                    canvas.drawRoundRect(r, radius, radius, paint);
                }
            }
        } catch (UnsupportedOperationException e) {
        }
        onDraw(view, canvas, radiu);
        return canvas;
    }

    public static Canvas onDraw(View view, Canvas canvas) {
        return onDraw(view, canvas, 9);
    }

    public static Canvas onDraw(View view, Canvas canvas, float radiu) {
        try {
            float radius;
            if (radiu <= 0) {
                return canvas;
            } else {
                radius = radiu;
            }
            float[] sRids = {
                    radius, radius, radius, radius, radius, radius, radius, radius
            };
            canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
            Path path = new Path();
            RectF rectF = new RectF(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
            path.addRoundRect(rectF, sRids, Path.Direction.CW);
            canvas.clipPath(path);
            if (canvas.getDrawFilter() != drawFilter) {
                canvas.setDrawFilter(drawFilter);
            }
        } catch (UnsupportedOperationException e) {
        }
        return canvas;
    }
}
