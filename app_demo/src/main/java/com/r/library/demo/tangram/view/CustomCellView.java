
package com.r.library.demo.tangram.view;

import com.bumptech.glide.Glide;
import com.r.library.common.util.LogUtils;
import com.r.library.demo.R;
import com.tmall.wireless.tangram.structure.BaseCell;
import com.tmall.wireless.tangram.structure.view.ITangramViewLifeCycle;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CustomCellView extends RelativeLayout implements ITangramViewLifeCycle {
    private final String TAG = "CustomCellView";
    private ImageView mImageView;
    private TextView mTextView;

    public CustomCellView(Context context) {
        super(context);
        init(context);
    }

    public CustomCellView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomCellView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomCellView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.item_recyclerview_tangram, this);
        mImageView = findViewById(R.id.iv_icon);
        mTextView = findViewById(R.id.tv_title);
    }

    /**
     * 绑定数据前调用
     */
    @Override
    public void cellInited(BaseCell cell) {
        LogUtils.d(TAG, "cellInited() " + cell.id);
    }

    /**
     * 绑定数据时调用
     * 
     * @param cell
     */
    @Override
    public void postBindView(BaseCell cell) {
        LogUtils.d(TAG, "postBindView() " + cell.id);
    }

    /**
     * 划出屏幕，解绑
     * 
     * @param cell
     */
    @Override
    public void postUnBindView(BaseCell cell) {
        LogUtils.d(TAG, "postUnBindView() " + cell.id);
    }

    public void setImageUrl(String url) {
        Glide.with(this)
                .load(url)
                .placeholder(R.drawable.pic_default)
                .into(mImageView);
    }

    public void setImageId(int resId) {
        mImageView.setImageResource(resId);
    }

    public void setText(String text) {
        mTextView.setText(text);
    }

    public void setTextColor(int color) {
        mTextView.setTextColor(color);
    }
}
