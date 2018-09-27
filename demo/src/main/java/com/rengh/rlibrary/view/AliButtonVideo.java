
package com.rengh.rlibrary.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rengh.library.common.util.RoundFocusUtils;
import com.rengh.rlibrary.R;
import com.rengh.rlibrary.glide.GlideHelper;

public class AliButtonVideo extends RelativeLayout {
    private final String TAG = "AliItemVideoButton";
    private Context mContext;
    private ImageView mImgViewThumbnail;
    private TextView mTvName;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(RoundFocusUtils.onDraw(this, canvas));
    }

    public AliButtonVideo(Context context) {
        this(context, null);
    }

    public AliButtonVideo(Context context, AttributeSet attrs) {
        this(context, attrs, android.support.v7.appcompat.R.attr.buttonStyle);
    }

    public AliButtonVideo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = getContext();
        View view = inflate(mContext, R.layout.view_custom_video, this);
        mImgViewThumbnail = view.findViewById(R.id.img_video_thumbnail);
        mTvName = view.findViewById(R.id.tv_video_name);
        setFocusable(true);
        setClickable(true);
    }

    public AliButtonVideo setImageBitmap(Bitmap bitmap) {
        mImgViewThumbnail.setImageBitmap(bitmap);
        return this;
    }

    public AliButtonVideo setImageResource(int resId) {
        mImgViewThumbnail.setImageResource(resId);
        return this;
    }

    public AliButtonVideo setPic(String url) {
        GlideHelper.load2View(mContext, mImgViewThumbnail, url);
        return this;
    }

    public AliButtonVideo setName(String name) {
        mTvName.setText(name);
        return this;
    }
}
