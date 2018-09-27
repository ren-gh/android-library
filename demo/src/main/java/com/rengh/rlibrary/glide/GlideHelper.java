
package com.rengh.rlibrary.glide;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.rengh.library.common.glide.GlideCircleTransform;
import com.rengh.rlibrary.R;

import android.content.Context;
import android.widget.ImageView;

public class GlideHelper {

    public static void load2View(Context context, ImageView imageView, String url) {
        RequestOptions requestOptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.color.colorDefaultBg)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.NORMAL);
        Glide.with(context)
                .load(url)
                .apply(requestOptions)
                .transition(new DrawableTransitionOptions().transition(android.R.anim.fade_in))
                .into(imageView);
    }

    public static void load2ViewRoundTop(Context context, final ImageView imageView, String url) {
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.color.colorTrans)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transform(new GlideCircleTransform(context, 9)) // 会覆盖centerCrop，使用RoundImageView替换
                .priority(Priority.NORMAL);
        Glide.with(context)
                .load(url)
                .apply(requestOptions)
                .transition(new DrawableTransitionOptions().transition(android.R.anim.fade_in))
                .into(imageView);
    }

    public static void load2ViewRoundTop(Context context, final ImageView imageView, int resId) {
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.color.colorTrans)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transform(new GlideCircleTransform(context, 9)) // 会覆盖centerCrop，使用RoundImageView替换
                .priority(Priority.NORMAL);
        Glide.with(context)
                .load(resId)
                .apply(requestOptions)
                .transition(new DrawableTransitionOptions().transition(android.R.anim.fade_in))
                .into(imageView);
    }

}
