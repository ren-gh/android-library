
package com.rengh.app.star.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import com.r.library.common.util.LogUtils;
import com.r.library.common.util.UIUtils;
import com.rengh.app.star.R;
import com.rengh.app.star.bean.AdBean;
import com.rengh.app.star.fragment.SplashFragment;
import com.rengh.app.star.fragment.TabFragment;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseActivity {
    private final String TAG = "MainActivity";

    private Context mContext;
    private AppCompatActivity mActivity;

    private FragmentManager mFragmentManager;
    private SplashFragment mSplashFragment;
    private TabFragment mTabFragment;

    private View mRootView;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LogUtils.i(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UIUtils.setFullStateBar(this, true);

        mActivity = this;
        mContext = this.getApplicationContext();

        mRootView = findViewById(R.id.fl_content);

        initFragment();
        addFragment(mSplashFragment);

        getWindow().getDecorView().post(() -> {
            getWindow().setBackgroundDrawable(getResources().getDrawable(R.color.colorWhite));
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                if (null != mTabFragment) {
                    return mTabFragment.onOptionsItemSelected(item);
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.i(TAG, "onDestroy()");
        cancelDisposable();
    }

    private void initFragment() {
        mSplashFragment = new SplashFragment();
        mSplashFragment.setListener(mSplashListener);

        mTabFragment = new TabFragment();
        mTabFragment.setActivity(mActivity);

        mFragmentManager = getSupportFragmentManager();
    }

    private void replaceFragment(Fragment fragment) {
        try {
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(0, R.anim.slide_out_fade);
            fragmentTransaction.replace(R.id.fl_content, fragment);
            fragmentTransaction.commit();
        } catch (IllegalStateException e) {
            LogUtils.e(TAG, "replaceFragment()", e);
        }
    }

    private void removeFragment(Fragment fragment) {
        try {
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(0, R.anim.slide_out_fade);
            fragmentTransaction.remove(fragment);
            fragmentTransaction.commit();
        } catch (IllegalStateException e) {
            LogUtils.e(TAG, "removeFragment()", e);
        }
    }

    private void addFragment(Fragment fragment) {
        try {
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(0, R.anim.slide_out_fade);
            fragmentTransaction.add(R.id.fl_content, fragment);
            fragmentTransaction.commit();
        } catch (IllegalStateException e) {
            LogUtils.e(TAG, "addFragment()", e);
        }
    }

    private void hideFragment(Fragment fragment) {
        try {
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(0, R.anim.slide_out_fade);
            fragmentTransaction.hide(fragment);
            fragmentTransaction.commit();
        } catch (IllegalStateException e) {
            LogUtils.e(TAG, "hideFragment()", e);
        }
    }

    private SplashFragment.Listener mSplashListener = new SplashFragment.Listener() {
        @Override
        public void onAdReady(AdBean bean) {
            LogUtils.i(TAG, "onReady() Showing ad: " + bean);
        }

        @Override
        public void onAdFinish() {
            LogUtils.i(TAG, "onFinish()");
            onPreviewOk();
        }
    };

    private void onPreviewOk() {
        replaceFragment(mTabFragment);

        UIUtils.setFullStateBar(this, false);

        mTabDisposable = Observable
                .timer(5, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        removeFragment(mTabFragment);
                    }
                });
        mTabDisposable.dispose();
    }

    private Disposable mTabDisposable;

    private void cancelDisposable() {
        if (null != mTabDisposable && !mTabDisposable.isDisposed()) {
            mTabDisposable.dispose();
            mTabDisposable = null;
        }
    }

}
