
package com.rengh.library.common.glide;

import java.security.MessageDigest;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.rengh.library.common.util.BitmapFillet;
import com.rengh.library.common.util.BitmapUtils;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * Created by rengh on 17-6-27.
 */
public class GlideCircleTopTransform extends BitmapTransformation {
    private int mWidth = 0;
    private int mHeight = 0;
    private int mPixels = 0;

    public GlideCircleTopTransform(Context context, int pixels) {
        mPixels = pixels;
    }

    public GlideCircleTopTransform(Context context, int width, int height, int pixels) {
        mWidth = width;
        mHeight = height;
        mPixels = pixels;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return circleCrop(pool, toTransform);
    }

    private Bitmap circleCrop(BitmapPool pool, Bitmap source) {
        if (source == null)
            return null;

        if (0 == mHeight && 0 == mWidth) {
            source = Bitmap.createBitmap(source, 0, 0, source.getWidth(),
                    source.getWidth() * 238 / 714);
            return BitmapFillet.fillet(source, mPixels, BitmapFillet.CORNER_TOP);
        } else {
            source = BitmapUtils.resizeBitmap(source, mWidth, mHeight);
            return BitmapFillet.fillet(source, mPixels, BitmapFillet.CORNER_TOP);
        }
    }

    @Override
    public void updateDiskCacheKey(MessageDigest digest) {
    }
}
