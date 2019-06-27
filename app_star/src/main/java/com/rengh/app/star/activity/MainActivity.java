
package com.rengh.app.star.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import com.r.library.common.util.LogUtils;
import com.r.library.common.util.UIUtils;
import com.rengh.app.star.R;
import com.rengh.app.star.bean.AdBean;
import com.rengh.app.star.fragment.SplashFragment;
import com.rengh.app.star.fragment.TabFragment;

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

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        UIUtils.setTransStateBar(this, getResources().getColor(R.color.colorTrans),
                false);
        UIUtils.setFullStateBar(this, true);

        mActivity = this;
        mContext = this.getApplicationContext();

        mRootView = findViewById(R.id.fl_content);

        initFragment();
        showFragment(mSplashFragment);

        getWindow().getDecorView().setBackgroundResource(R.color.colorWhite);
        getWindow().getDecorView().post(() -> {
        });
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

        mTabFragment = new TabFragment();
        mTabFragment.setActivity(mActivity);

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
        addFragment(mTabFragment);
    }
}
