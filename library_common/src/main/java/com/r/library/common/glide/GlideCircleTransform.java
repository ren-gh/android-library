
package com.r.library.common.glide;

import java.security.MessageDigest;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.r.library.common.util.BitmapUtils;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * Created by rengh on 17-6-27.
 */
public class GlideCircleTransform extends BitmapTransformation {
    private int mWidth = 0;
    private int mHeight = 0;
    private int mPixels = 0;

    public GlideCircleTransform(Context context, int pixels) {
        mPixels = pixels;
    }

    public GlideCircleTransform(Context context, int width, int height, int pixels) {
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
            return BitmapUtils.toRoundCornerImage(source, mPixels);
        } else {
            return BitmapUtils.toRoundCornerImage(source, mWidth, mHeight, mPixels);
        }
    }

    @Override
    public void updateDiskCacheKey(MessageDigest digest) {
    }
}
