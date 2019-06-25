
package com.rengh.app.star.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
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
import android.view.ViewStub;
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
import com.rengh.app.star.fragment.LoginFragment;
import com.rengh.app.star.fragment.PictureFragment;
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
    private LoginFragment mLoginFragment;
    private PictureFragment mPicFragment;

    private Fragment mLastFragment = null;

    private View mRootView;
    private ViewStub mViewStub;
    private ImageView mImgPic;

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
        mViewStub = findViewById(R.id.vs_pic);

        initFragment();
        showSplash();
        getWindow().getDecorView().setBackgroundResource(R.color.colorWhite);

        getWindow().getDecorView().post(() -> {
            // mViewStub.inflate();
            // mImgPic = findViewById(R.id.img_pic);
            // setImageUrl();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    onPicLoaded();
                }
            }, 3000L);
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
        Glide.with(mActivity).onDestroy();
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

    private void showSplash() {
        showFragment(mSplashFragment);
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
                    showFragment(mPicFragment);
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            hideFragment();
                        }
                    }, 5000L);
                }
            }

            @Override
            public void onLoginFailed() {
                if (null != mHandler) {
                    Snackbar.make(mRootView, "登录失败", Snackbar.LENGTH_LONG).show();
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
                    Snackbar.make(mRootView, "图片加载成功", Snackbar.LENGTH_LONG).show();
                }
            }
        });
        mFragmentManager = getSupportFragmentManager();
    }

    private void showFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(0, R.anim.slide_out_fade);
        fragmentTransaction.replace(R.id.fl_content, fragment);
        fragmentTransaction.commit();
        mLastFragment = fragment;
    }

    private void hideFragment() {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(0, R.anim.slide_out_fade);
        if (null != mLastFragment) {
            fragmentTransaction.remove(mLastFragment);
        }
        fragmentTransaction.commit();
        mLastFragment = null;
    }

    private void setImageUrl() {
        final String url = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1561451242506&di=ea34c472cdcbce97251f0cdb06bc9776&imgtype=0&src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F01cffe5b485d25a8012036be383706.gif";
        Glide.with(mActivity)
                .load(url)
                .placeholder(R.drawable.pic_default)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model,
                            Target<Drawable> target,
                            boolean isFirstResource) {
                        LogUtils.e(TAG, "onLoadFailed()");
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model,
                            Target<Drawable> target,
                            DataSource dataSource,
                            boolean isFirstResource) {
                        LogUtils.i(TAG, "onResourceReady()");
                        onPicLoaded();
                        return false;
                    }
                })
                .into(mImgPic);
    }

    private void onPicLoaded() {
        UIUtils.setTransStateBar(mActivity, getResources().getColor(R.color.colorTrans),
                true);
        LogUtils.i(TAG, "Update state bar's color.");
        showFragment(mLoginFragment);
    }

}
