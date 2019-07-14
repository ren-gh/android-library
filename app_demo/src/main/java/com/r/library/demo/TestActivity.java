
package com.r.library.demo;

import android.content.Context;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class TestActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {
    private Context context;
    private MenuView menuView;
    private ImageView otherAdView;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        context = this;

        initViews();

        showContent();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case MenuView.FIRST_BTN_ID: {
                Toast.makeText(context, "关机", Toast.LENGTH_LONG).show();
            }
            break;
            case MenuView.SECOND_BTN_ID: {
                Toast.makeText(context, "延时关机", Toast.LENGTH_LONG).show();
            }
            break;
            case MenuView.THIRD_BTN_ID: {
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
        // WebView 展示时此处失效，故不在此处理
        boolean canClose = false;
        if (otherAdView.isFocusable() && otherAdView.isClickable() && otherAdView.getVisibility() == View.VISIBLE) {
            canClose = true;
        }
        menuView.onFocusChange(v, hasFocus, canClose);

        switch (v.getId()) {
            case R.id.img_pic: {
                if (hasFocus) {
                    otherAdView.setBackgroundResource(R.drawable.focus_highlight_icon);
                    otherAdView.setNextFocusDownId(menuView.getLastFocusBtnId());
                } else {
                    if (Build.VERSION.SDK_INT >= 16) {
                        otherAdView.setBackground(null);
                    } else {
                        otherAdView.setBackgroundDrawable(null);
                    }
                }
            }
            break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // WebView 特殊处理
        if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            if (webView.getVisibility() == View.VISIBLE) {
                menuView.close();
                menuView.disableFocus();
                return true;
            }
            // 按上键直接跳转
            otherAdView.performClick();
        }
        if (keyCode == KeyEvent.KEYCODE_BACK && menuView.isClosed()) {
//            if (webView.canGoBack()) {
//                webView.goBack();
//            } else {
            menuView.revertFocus();
//            }
            return true;
        }
        return super.

                onKeyDown(keyCode, event);

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
        otherAdView = findViewById(R.id.img_pic);
        webView = findViewById(R.id.wv_web);

        webView.setVisibility(View.GONE);
        otherAdView.setVisibility(View.GONE);

        menuView.firstBtn().setImageResouorce(R.drawable.ic_player_rewind);
        menuView.firstBtn().setText("关机");
        menuView.secondBtn().setImageResouorce(R.drawable.ic_player_play_focus);
        menuView.secondBtn().setText("延时5min");
        menuView.thirdBtn().setImageResouorce(R.drawable.ic_player_forward);
        menuView.thirdBtn().setText("息屏");
        menuView.setOnClickListener(this);
        menuView.setOnFocusChangeListener(this);

        menuView.firstBtn().requestFocus();
    }

    private void showContent() {
        boolean showWebView = true;
        if (showWebView) {
            menuView.firstBtn().setNextFocusUpId(R.id.wv_web);
            menuView.secondBtn().setNextFocusUpId(R.id.wv_web);
            menuView.thirdBtn().setNextFocusUpId(R.id.wv_web);
            webView.setNextFocusDownId(menuView.getLastFocusBtnId());

            webView.setVisibility(View.VISIBLE);
            webView.loadUrl("https://www.jianshu.com/");
        } else {
            menuView.firstBtn().setNextFocusUpId(R.id.img_pic);
            menuView.secondBtn().setNextFocusUpId(R.id.img_pic);
            menuView.thirdBtn().setNextFocusUpId(R.id.img_pic);
            otherAdView.setNextFocusDownId(menuView.getLastFocusBtnId());

//            otherAdView.setClickable(true);
//            otherAdView.setOnClickListener(this);
//            otherAdView.setFocusable(true);
//            otherAdView.setOnFocusChangeListener(this);

            otherAdView.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load("http://img5.imgtn.bdimg.com/it/u=8685675,2171717858&fm=26&gp=0.jpg")
                    .into(otherAdView);
        }
    }

}
