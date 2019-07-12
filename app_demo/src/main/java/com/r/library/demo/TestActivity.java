
package com.r.library.demo;

import android.content.Context;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class TestActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {
    private Context context;
    private MenuView menuView;
    private ImageView picView;
    private int lastFocusBtnId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        context = this;

        initViews();

        Glide.with(context)
                .load("http://img5.imgtn.bdimg.com/it/u=8685675,2171717858&fm=26&gp=0.jpg")
                .into(picView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case MenuView.SHUTDOWN_BTN_ID: {
                Toast.makeText(context, "关机", Toast.LENGTH_LONG).show();
            }
                break;
            case MenuView.DELAY_BTN_ID: {
                Toast.makeText(context, "延时关机", Toast.LENGTH_LONG).show();
            }
                break;
            case MenuView.SCREEN_BTN_ID: {
                Toast.makeText(context, "息屏", Toast.LENGTH_LONG).show();
            }
                break;
            case R.id.img_pic: {
                Toast.makeText(context, "查看详情", Toast.LENGTH_LONG).show();
            }
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case MenuView.SHUTDOWN_BTN_ID: {
                if (hasFocus) {
                    lastFocusBtnId = v.getId();
                    menuView.open(500);
                }
            }
                break;
            case MenuView.DELAY_BTN_ID: {
                if (hasFocus) {
                    lastFocusBtnId = v.getId();
                    menuView.open(500);
                }
            }
                break;
            case MenuView.SCREEN_BTN_ID: {
                if (hasFocus) {
                    lastFocusBtnId = v.getId();
                    menuView.open(500);
                }
            }
                break;
            case R.id.img_pic: {
                if (hasFocus) {
                    picView.setBackgroundResource(R.drawable.focus_highlight_icon);
                    picView.setNextFocusDownId(lastFocusBtnId);
                    menuView.close(500);
                } else {
                    if (Build.VERSION.SDK_INT >= 16) {
                        picView.setBackground(null);
                    } else {
                        picView.setBackgroundDrawable(null);
                    }
                }
            }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onResume() {
        super.onResume();
        menuView.resumeAnimators();
    }

    @Override
    public void onPause() {
        super.onPause();
        menuView.pauseAnimators();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void initViews() {
        menuView = findViewById(R.id.mv_delay);
        picView = findViewById(R.id.img_pic);

        menuView.shutdownBtn().setText("关机");
        menuView.delayBtn().setText("延时5min");
        menuView.screenBtn().setText("息屏");

        menuView.shutdownBtn().setOnClickListener(this);
        menuView.delayBtn().setOnClickListener(this);
        menuView.screenBtn().setOnClickListener(this);

        menuView.shutdownBtn().setOnFocusChangeListener(this);
        menuView.delayBtn().setOnFocusChangeListener(this);
        menuView.screenBtn().setOnFocusChangeListener(this);

        picView.setFocusable(true);
        picView.setOnFocusChangeListener(this);

        menuView.shutdownBtn().setNextFocusUpId(R.id.img_pic);
        menuView.delayBtn().setNextFocusUpId(R.id.img_pic);
        menuView.screenBtn().setNextFocusUpId(R.id.img_pic);
        picView.setNextFocusDownId(MenuView.SHUTDOWN_BTN_ID);

        menuView.shutdownBtn().requestFocus();
    }

}
