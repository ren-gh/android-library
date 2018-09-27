
package com.rengh.rlibrary.view;

import com.rengh.library.common.util.RoundFocusUtils;
import com.rengh.library.common.view.MarqueeTextView;
import com.rengh.rlibrary.R;
import com.rengh.rlibrary.glide.GlideHelper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AliButtonCommodity extends RelativeLayout {
    private final String TAG = "AliButtonCommodity";
    private Context mContext;
    private TextView mTvBrand, mTvRedTitle, mTvPricIcon, mTvPric;
    private MarqueeTextView mTvTitle;
    private ImageView mImgViewPic, mImgViewShopIcon;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(RoundFocusUtils.onDraw(this, canvas));
    }

    public AliButtonCommodity(Context context) {
        this(context, null);
    }

    public AliButtonCommodity(Context context, AttributeSet attrs) {
        this(context, attrs, android.support.v7.appcompat.R.attr.buttonStyle);
    }

    public AliButtonCommodity(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = getContext();
        View view = inflate(mContext, R.layout.view_custom_commodity, this);
        mTvBrand = view.findViewById(R.id.tv_commodity_brand);
        mTvTitle = view.findViewById(R.id.tv_commodity_title);
        mTvRedTitle = view.findViewById(R.id.tv_commodity_red_title);
        mTvPricIcon = view.findViewById(R.id.tv_commodity_pric_icon);
        mTvPric = view.findViewById(R.id.tv_commodity_pric);
        mImgViewPic = view.findViewById(R.id.img_commodity_pic);
        mImgViewShopIcon = view.findViewById(R.id.img_commodity_shop_icon);
        setFocusable(true);
        setClickable(true);
    }

    public AliButtonCommodity setScroll(boolean scroll) {
        TextPaint textPaint = mTvTitle.getPaint();
        float textWidth = textPaint.measureText(mTvTitle.getText().toString());
        if (textWidth > mTvTitle.getWidth()) {
            if (scroll) {
                mTvTitle.startScroll();
            } else {
                mTvTitle.stopScroll();
            }
        }
        return this;
    }

    public AliButtonCommodity setBrand(String name) {
        mTvBrand.setText(name);
        return this;
    }

    public AliButtonCommodity setTitle(String name) {
        mTvTitle.setText(name);
        return this;
    }

    public AliButtonCommodity setRedTitle(String name) {
        mTvRedTitle.setText(name);
        return this;
    }

    public AliButtonCommodity setPricIcon(String name) {
        mTvPricIcon.setText(name);
        return this;
    }

    public AliButtonCommodity setPric(String name) {
        LayoutParams params = (LayoutParams) mTvPric.getLayoutParams();
        // if (name.length() > 4) {
        // params.setMargins(0, 0,
        // (int) getResources().getDimension(R.dimen.dp_32),
        // (int) getResources().getDimension(R.dimen.dp_4));
        // mTvPric.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.sp_12));
        // } else {
        // params.setMargins(0, 0,
        // (int) getResources().getDimension(R.dimen.dp_32),
        // (int) getResources().getDimension(R.dimen.dp_2));
        // mTvPric.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.sp_20));
        // }
        mTvPric.setLayoutParams(params);
        mTvPric.setText(name);
        return this;
    }

    public AliButtonCommodity setImageBitmap(Bitmap bitmap) {
        mImgViewPic.setImageBitmap(bitmap);
        return this;
    }

    public AliButtonCommodity setImageResource(int resId) {
        mImgViewPic.setImageResource(resId);
        return this;
    }

    public AliButtonCommodity setPic(String url) {
        GlideHelper.load2View(mContext, mImgViewPic, url);
        return this;
    }

}
