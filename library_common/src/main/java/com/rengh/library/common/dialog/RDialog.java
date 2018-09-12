
package com.rengh.library.common.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.rengh.library.common.R;
import com.rengh.library.common.util.UIUtils;

public class RDialog extends Dialog implements View.OnClickListener, View.OnFocusChangeListener {
    private final String TAG = "RDialog";
    private Context mContext;

    private TextView textView;
    private Button buttonYes, buttonNo;

    public RDialog(Activity activity) {
        super(activity, R.style.DialogStyle);
        setContentView(R.layout.dialog_r_layout);
        mContext = activity;

        textView = findViewById(R.id.tv_msg);
        buttonYes = findViewById(R.id.btn_yes);
        buttonNo = findViewById(R.id.btn_no);

        buttonYes.setOnClickListener(this);
        buttonYes.setOnFocusChangeListener(this);

        buttonNo.setOnClickListener(this);
        buttonNo.setOnFocusChangeListener(this);
    }

    public RDialog setContent(String text) {
        textView.setText(text);
        return this;
    }

    public RDialog setButtonYesClick(View.OnClickListener listener) {
        buttonYes.setOnClickListener(listener);
        return this;
    }

    public RDialog setButtonNoClick(View.OnClickListener listener) {
        buttonNo.setOnClickListener(listener);
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void show() {
        super.show();

        // 设置为全屏
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getWindow().setAttributes(layoutParams);

        buttonYes.requestFocus();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_yes) {
            dismiss();
        } else if (i == R.id.btn_no) {
            dismiss();
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            UIUtils.scaleView(v, 1.0f, 100);
        } else {
            UIUtils.scaleView(v, 1.0f, 100);
        }
    }
}
