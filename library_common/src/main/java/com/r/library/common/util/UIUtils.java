
package com.r.library.common.util;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by rengh on 17-5-29.
 */
public class UIUtils {
    public static Resources getResources(Context context) {
        return context.getResources();
    }

    public static int dip2px(Context context, float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources(context).getDisplayMetrics());
    }

    public static int sip2px(Context context, float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources(context).getDisplayMetrics());
    }

    public static void scaleView(View view, float value) {
        scaleView(view, value, 300);
    }

    public static void scaleView(View view, float value, int duration) {
        if (view != null) {
            ObjectAnimator animx = ObjectAnimator.ofFloat(view, "scaleX", value);
            ObjectAnimator animy = ObjectAnimator.ofFloat(view, "scaleY", value);
            AnimatorSet set = new AnimatorSet();
            set.setDuration(duration);
            set.setInterpolator(new DecelerateInterpolator());
            set.play(animx).with(animy);
            set.start();
        }
    }

    /**
     * 设置沉侵式状态栏，从边缘划入时才显示状态栏和导航栏等。onWindowFocusChanged()
     *
     * @param appCompatActivity
     * @param hasFocus
     */
    public static void setFullStateBar(AppCompatActivity appCompatActivity, boolean hasFocus) {
        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
            View decorView = appCompatActivity.getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    /**
     * 设置透明状态栏和导航栏，始终显示。
     *
     * @param appCompatActivity
     */
    public static void setTransStateBar(AppCompatActivity appCompatActivity) {
        setTransStateBar(appCompatActivity, Color.TRANSPARENT, null);
    }

    public static void setTransStateBar(AppCompatActivity appCompatActivity, int color, Boolean light) {
        ActionBar actionBar = appCompatActivity.getSupportActionBar();
        if (null == actionBar) {
            return;
        }
        int option = getOption(color, light);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = appCompatActivity.getWindow().getDecorView();
            decorView.setSystemUiVisibility(option);
            appCompatActivity.getWindow().setNavigationBarColor(color);
            appCompatActivity.getWindow().setStatusBarColor(color);
        }
        actionBar.hide();
    }

    private static int getOption(int color, Boolean light) {
        int option;
        if (light != null) {
            if (light) {
                option = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                        | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
            } else {
                option = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            }
        } else {
            if (ColorUtils.calculateLuminance(color) >= 0.5) {
                option = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                        | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
            } else {
                option = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            }
        }
        return option;
    }
}
