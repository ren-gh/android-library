
package com.r.library.demo.tangram.view;

import com.bumptech.glide.Glide;
import com.r.library.common.util.LogUtils;
import com.r.library.common.util.ToastUtils;
import com.r.library.demo.R;
import com.tmall.wireless.tangram.structure.BaseCell;
import com.tmall.wireless.tangram.structure.view.ITangramViewLifeCycle;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CustomInterfaceView extends RelativeLayout implements ITangramViewLifeCycle {
    private final String TAG = "CustomInterfaceView";
    private ImageView mImageView;
    private TextView mTextView;

    public CustomInterfaceView(Context context) {
        super(context);
        init(context);
    }

    public CustomInterfaceView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomInterfaceView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomInterfaceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.item_recyclerview_tangram, this);
        mImageView = findViewById(R.id.iv_icon);
        mTextView = findViewById(R.id.tv_title);
        setFocusable(true);
        setClickable(true);
    }

    /**
     * 绑定数据前调用
     */
    @Override
    public void cellInited(BaseCell cell) {
        LogUtils.d(TAG, "cellInited() " + cell.id);
        setBackgroundResource(R.drawable.view_bg);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.d(TAG, "onClick() pos=" + cell.pos);
                ToastUtils.showToast(getContext(),
                        "您点击了组件，type=" + cell.stringType + ", pos=" + cell.pos);
            }
        });
    }

    /**
     * 绑定数据时调用
     * 
     * @param cell
     */
    @Override
    public void postBindView(BaseCell cell) {
        LogUtils.d(TAG, "postBindView() " + cell.id);

        setImageUrl(cell.optStringParam("imageUrl"));
        setText(cell.optStringParam("text"));
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
