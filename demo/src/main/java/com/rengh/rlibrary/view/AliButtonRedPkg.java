
package com.rengh.rlibrary.view;

import com.rengh.library.common.util.RoundFocusUtils;
import com.rengh.rlibrary.R;
import com.rengh.rlibrary.glide.GlideHelper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class AliButtonRedPkg extends RelativeLayout {
    private final String TAG = "AliButtonRedPkg";
    private Context mContext;
    private ImageView mImgViewPkg;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(RoundFocusUtils.onDraw(this, canvas));
    }

    public AliButtonRedPkg(Context context) {
        this(context, null);
    }

    public AliButtonRedPkg(Context context, AttributeSet attrs) {
        this(context, attrs, android.support.v7.appcompat.R.attr.buttonStyle);
    }

    public AliButtonRedPkg(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = getContext();
        View view = inflate(mContext, R.layout.view_custom_red_pkg, this);
        mImgViewPkg = view.findViewById(R.id.img_red_pkg);
        setFocusable(true);
        setClickable(true);
    }

    public AliButtonRedPkg setImageBitmap(Bitmap bitmap) {
        mImgViewPkg.setImageBitmap(bitmap);
        return this;
    }

    public AliButtonRedPkg setImageResource(int resId) {
        mImgViewPkg.setImageResource(resId);
        return this;
    }

    public AliButtonRedPkg setPic(String url) {
        GlideHelper.load2View(mContext, mImgViewPkg, url);
        return this;
    }
}
