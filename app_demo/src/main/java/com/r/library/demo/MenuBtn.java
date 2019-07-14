
package com.r.library.demo;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MenuBtn extends LinearLayout implements View.OnFocusChangeListener {
    private ImageView icon;
    private TextView text;
    private OnFocusChangeListener onFocusChangeListener;

    public MenuBtn(Context context) {
        super(context);
        init(context);
    }

    public MenuBtn(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MenuBtn(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_menu_button, this);
        setGravity(Gravity.CENTER);
        setOrientation(LinearLayout.HORIZONTAL);
        setClickable(true);
        setFocusable(true);
        setOnFocusChangeListener(this);
        icon = findViewById(R.id.img_btn_icon);
        text = findViewById(R.id.tv_btn_text);
        icon.setImageDrawable(null);
        text.setText("");
    }

    @Override
    public void setOnFocusChangeListener(OnFocusChangeListener onFocusChangeListener) {
        this.onFocusChangeListener = onFocusChangeListener;
        super.setOnFocusChangeListener(this);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            icon.setVisibility(VISIBLE);
            text.setTextColor(0xae000000);
            setBackgroundResource(R.drawable.btn_bg_focus);
        } else {
            icon.setVisibility(VISIBLE);
            text.setTextColor(0xaeffffff);
            if (Build.VERSION.SDK_INT >= 16) {
                setBackground(null);
            } else {
                setBackgroundDrawable(null);
            }
        }
        if (null != this.onFocusChangeListener) {
            this.onFocusChangeListener.onFocusChange(v, hasFocus);
        }
    }

    public ImageView icon() {
        return icon;
    }

    public TextView text() {
        return text;
    }

    public void setImageResouorce(int resId) {
        icon.setImageResource(resId);
    }

    public void setIconDrawable(Drawable drawable) {
        icon.setImageDrawable(drawable);
    }

    public void setText(int resId) {
        text.setText(resId);
    }

    public void setText(CharSequence charSequence) {
        text.setText(charSequence);
    }
}
