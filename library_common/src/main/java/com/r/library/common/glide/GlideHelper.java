
package com.r.library.common.glide;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.r.library.common.R;
import com.r.library.common.util.BitmapUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class GlideHelper {

    public static void load2ViewBgByUrl(Context context, final View view, String url) {
        Glide.with(context).load(url).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                view.setBackgroundDrawable(resource);
            }
        });
    }

    public static void load2ViewBgRes(Context context, final View view, int resId) {
        Bitmap bmp = BitmapUtils.readBitMap(context, resId);
        Drawable drawable;
        if (bmp == null) {
            drawable = context.getResources().getDrawable(resId);
        } else {
            drawable = new BitmapDrawable(context.getResources(), bmp);
        }
        if (null != drawable) {
            view.setBackgroundDrawable(drawable);
        } else {
            view.setBackgroundColor(context.getResources().getColor(resId));
        }
        // Glide.with(context).load(resId).into(new SimpleTarget<Drawable>() {
        // @Override
        // public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
        // view.setBackgroundDrawable(resource);
        // }
        // });
    }

    public static void load2ViewFitCenter(Context context, ImageView imageView, String url) {
        RequestOptions requestOptions = new RequestOptions()
                .fitCenter()
                .placeholder(R.color.colorTrans)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.NORMAL);
        Glide.with(context)
                .load(url)
                .apply(requestOptions)
                .transition(new DrawableTransitionOptions().transition(android.R.anim.fade_in))
                .into(imageView);
    }

    public static void load2ViewCenterCrop(Context context, final ImageView imageView, String url) {
        RequestOptions requestOptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.color.colorTrans)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.NORMAL);
        Glide.with(context)
                .load(url)
                .apply(requestOptions)
                .transition(new DrawableTransitionOptions().transition(android.R.anim.fade_in))
                .into(imageView);
    }

    public static void load2ViewCenterCropWithRadiu(Context context, final ImageView imageView, String url, int piexls) {
        RequestOptions requestOptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.color.colorTrans)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transform(new GlideCircleTopTransform(context, piexls))
                .priority(Priority.NORMAL);
        Glide.with(context)
                .load(url)
                .apply(requestOptions)
                .transition(new DrawableTransitionOptions().transition(android.R.anim.fade_in))
                .into(imageView);
    }

}
