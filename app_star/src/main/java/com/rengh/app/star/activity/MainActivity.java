
package com.rengh.app.star.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewStub;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.r.library.common.handler.WeakHandler;
import com.r.library.common.handler.WeakHandlerListener;
import com.r.library.common.util.LogUtils;
import com.r.library.common.util.UIUtils;
import com.rengh.app.star.R;
import com.rengh.app.star.bean.AdBean;
import com.rengh.app.star.fragment.SplashFragment;

public class MainActivity extends AppCompatActivity implements WeakHandlerListener {
    private final String TAG = "MainActivity";

    private final int MSG_WHAT_FINISH = 0;

    private final long TOAST_LENGTH = 2000L;

    private Context mContext;
    private AppCompatActivity mActivity;
    private WeakHandler mHandler;

    private FragmentManager mFragmentManager;
    private SplashFragment mSplashFragment;

    private View mRootView;
    private ViewStub mViewStub;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LogUtils.i(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        UIUtils.setTransStateBar(this, getResources().getColor(R.color.colorTrans),
                false);
        UIUtils.setFullStateBar(this, true);

        mActivity = this;
        mContext = this.getApplicationContext();
        mHandler = new WeakHandler(this);

        mRootView = findViewById(R.id.fl_content);
        mViewStub = findViewById(R.id.vs_pic);

        initFragment();
        showFragment(mSplashFragment);

        getWindow().getDecorView().setBackgroundResource(R.color.colorWhite);
        getWindow().getDecorView().post(() -> {
            mViewStub.inflate();
        });
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        LogUtils.i(TAG, "onPostCreate()");
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    protected void onRestart() {
        LogUtils.i(TAG, "onRestart()");
        super.onRestart();
    }

    @Override
    protected void onStart() {
        LogUtils.i(TAG, "onStart()");
        super.onStart();
    }

    @Override
    protected void onResume() {
        LogUtils.i(TAG, "onResume()");
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        LogUtils.i(TAG, "onSaveInstanceState()");
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onPostResume() {
        LogUtils.i(TAG, "onPostResume()");
        super.onPostResume();
    }

    @Override
    protected void onPause() {
        LogUtils.i(TAG, "onPause()");
        super.onPause();
    }

    @Override
    protected void onStop() {
        LogUtils.i(TAG, "onStop()");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        LogUtils.i(TAG, "onDestroy()");
        mHandler.removeCallbacksAndMessages(null);
        mHandler = null;
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        LogUtils.i(TAG, "onConfigurationChanged()");
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        LogUtils.i(TAG, "onNewIntent()");
        super.onNewIntent(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        LogUtils.i(TAG, "onKeyDown()");
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        LogUtils.i(TAG, "onBackPressed()");
        super.onBackPressed();
    }

    @Override
    public void onLowMemory() {
        LogUtils.i(TAG, "onLowMemory()");
        super.onLowMemory();
    }

    @Override
    public void process(Message msg) {
        LogUtils.i(TAG, "process() what: " + msg.what);
        switch (msg.what) {
            case MSG_WHAT_FINISH: {
                finish();
            }
                break;
        }
    }

    private void initFragment() {
        mSplashFragment = new SplashFragment();
        mSplashFragment.setListener(new SplashFragment.Listener() {
            @Override
            public void onAdReady(AdBean bean) {
                LogUtils.i(TAG, "onReady() Showing ad: " + bean);
            }

            @Override
            public void onAdFinish() {
                LogUtils.i(TAG, "onFinish()");
                onPreviewOk();
            }
        });
        mFragmentManager = getSupportFragmentManager();
    }

    private void showFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(0, R.anim.slide_out_fade);
        fragmentTransaction.replace(R.id.fl_content, fragment);
        fragmentTransaction.commit();
    }

    private void removeFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(0, R.anim.slide_out_fade);
        fragmentTransaction.remove(fragment);
        fragmentTransaction.commit();
    }

    private void addFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(0, R.anim.slide_out_fade);
        fragmentTransaction.add(R.id.fl_content, fragment);
        fragmentTransaction.commit();
    }

    private void hideFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(0, R.anim.slide_out_fade);
        fragmentTransaction.hide(fragment);
        fragmentTransaction.commit();
    }

    private void onPreviewOk() {
        // 设置状态栏背景颜色和亮/黑主题
        UIUtils.setFullStateBar(this, false);
        UIUtils.setTransStateBar(mActivity, getResources().getColor(R.color.colorTrans),
                true);
        LogUtils.i(TAG, "onPreviewOk() Update state bar's color.");
        removeFragment(mSplashFragment);
    }

}
