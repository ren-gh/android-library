
package com.rengh.app.star.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Message;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;

import com.r.library.common.handler.WeakHandler;
import com.r.library.common.handler.WeakHandlerListener;
import com.r.library.common.util.LogUtils;
import com.r.library.common.util.UIUtils;
import com.rengh.app.star.R;
import com.rengh.app.star.fragment.LoginFragment;
import com.rengh.app.star.fragment.PictureFragment;
import com.rengh.app.star.fragment.SplashFragment;

public class MainActivity extends AppCompatActivity implements WeakHandlerListener {
    private final String TAG = "MainActivity";

    private final int MSG_WHAT_FINISH = 0;
    private final int MSG_WHAT_SHOW_SPLASH = 1;
    private final int MSG_WHAT_SHOW_LOGIN = 2;
    private final int MSG_WHAT_SHOW_PIC = 3;

    private final long TOAST_LENGTH = 2000L;

    private Context mContext;
    private AppCompatActivity mActivity;
    private WeakHandler mHandler;

    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;

    private SplashFragment mSplashFragment;
    private LoginFragment mLoginFragment;
    private PictureFragment mPicFragment;

    private View mRootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LogUtils.i(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UIUtils.setTransStateBar(this, getResources().getColor(R.color.colorTrans),
                false);

        mActivity = this;
        mContext = this.getApplicationContext();
        mHandler = new WeakHandler(this);

        mRootView = findViewById(R.id.fl_content);

        getWindow().getDecorView().post(() -> {
            initFragment();
            showSplash();
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
        // UIUtils.setFullStateBar(this, hasFocus);
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
            case MSG_WHAT_SHOW_SPLASH: {
                showFragment(mSplashFragment);
                LogUtils.i(TAG, "Show splash fragment.");
            }
                break;
            case MSG_WHAT_SHOW_LOGIN: {
                showFragment(mLoginFragment);
                LogUtils.i(TAG, "Show login fragment.");
            }
                break;
            case MSG_WHAT_SHOW_PIC: {
                showFragment(mPicFragment);
                LogUtils.i(TAG, "Show pic fragment.");
            }
                break;
        }
    }

    private void showSplash() {
        mHandler.sendEmptyMessage(MSG_WHAT_SHOW_SPLASH);
    }

    private void initFragment() {
        mSplashFragment = new SplashFragment();
        mLoginFragment = new LoginFragment();
        mPicFragment = new PictureFragment();
        mLoginFragment.setListener(new LoginFragment.LoginListener() {
            @Override
            public void onLogin() {
                if (null != mHandler) {
                    Snackbar.make(mRootView, "登录成功", Snackbar.LENGTH_LONG).show();
                    mHandler.sendEmptyMessage(MSG_WHAT_SHOW_PIC);
                }
            }

            @Override
            public void onLoginFailed() {
                if (null != mHandler) {
                    Snackbar.make(mRootView, "登录失败", Snackbar.LENGTH_LONG).show();
                    mHandler.sendEmptyMessage(MSG_WHAT_SHOW_PIC);
                }
            }
        });
        mPicFragment.setListener(new PictureFragment.Listener() {
            @Override
            public void onLoadFailed() {
                if (null != mHandler) {
                    Snackbar.make(mRootView, "图片加载失败", Snackbar.LENGTH_LONG).show();
                    mHandler.sendEmptyMessageDelayed(MSG_WHAT_FINISH, TOAST_LENGTH);
                }
            }

            @Override
            public void onResourceReady() {
                if (null != mHandler) {
                    UIUtils.setTransStateBar(mActivity, getResources().getColor(R.color.colorTrans),
                            true);
                    LogUtils.i(TAG, "Update state bar's color.");

                    mActivity.getWindow().getDecorView().setBackgroundResource(R.color.colorWhite);

                    Snackbar.make(mRootView, "图片加载成功", Snackbar.LENGTH_LONG).show();
                    mHandler.sendEmptyMessage(MSG_WHAT_SHOW_LOGIN);
                }
            }
        });
        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.setCustomAnimations(R.anim.slide_in_fade, R.anim.slide_out_fade);
        mFragmentTransaction.add(R.id.fl_content, mPicFragment);
        mFragmentTransaction.add(R.id.fl_content, mLoginFragment);
        mFragmentTransaction.add(R.id.fl_content, mSplashFragment);
        mFragmentTransaction.commit();

        showFragment(mSplashFragment);
    }

    private Fragment lastFragment = null;

    private void showFragment(Fragment fragment) {
        if (null != lastFragment) {
            mFragmentTransaction.hide(lastFragment);
        }
        mFragmentTransaction.show(fragment);
        lastFragment = fragment;
    }

}
