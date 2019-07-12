
package com.r.library.demo;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.r.library.common.util.LogUtils;

import org.jetbrains.annotations.NotNull;

public class MenuView extends RelativeLayout {
    private final String TAG = "MenuView";
    private AnimatorSet animatorSetOpen, animatorSetClose;
    private MenuBtn mbtnShutdown, mbtnDelay, mbtnScreen;
    private ImageView imgDown;
    private int height, lowHeight;
    private boolean closed = false;

    public static final int SHUTDOWN_BTN_ID = R.id.mbtn_shutdown;
    public static final int DELAY_BTN_ID = R.id.mbtn_delay;
    public static final int SCREEN_BTN_ID = R.id.mbtn_screen;

    public MenuView(Context context) {
        super(context);
        init(context);
    }

    public MenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public MenuBtn shutdownBtn() {
        return mbtnShutdown;
    }

    public MenuBtn delayBtn() {
        return mbtnDelay;
    }

    public MenuBtn screenBtn() {
        return mbtnScreen;
    }

    public void open(int duration) {
        LogUtils.i(TAG, "open() " + duration + ", closed: " + closed);
        if (!closed) {
            return;
        }
        if (!initHeight()) {
            return;
        }
        initAnimatorSet();
        closed = false;
        LogUtils.i(TAG, "open() lowHeight=" + lowHeight + ", height=" + height);
        animatorSetOpen.setDuration(duration).start();
    }

    public void close(int duration) {
        LogUtils.i(TAG, "close() " + duration + ", closed: " + closed);
        if (closed) {
            return;
        }
        if (!initHeight()) {
            return;
        }
        initAnimatorSet();
        closed = true;
        imgDown.setVisibility(GONE);
        LogUtils.i(TAG, "close() lowHeight=" + lowHeight + ", height=" + height);
        animatorSetClose.setDuration(duration).start();
    }

    public void resumeAnimators() {
        if (Build.VERSION.SDK_INT >= 19) {
            if (null != animatorSetOpen && animatorSetOpen.isPaused()) {
                animatorSetOpen.resume();
            }
            if (null != animatorSetClose && animatorSetClose.isPaused()) {
                animatorSetClose.resume();
            }
        }
    }

    public void pauseAnimators() {
        if (null != animatorSetOpen && animatorSetOpen.isRunning()) {
            if (Build.VERSION.SDK_INT >= 19) {
                animatorSetOpen.pause();
            } else {
                animatorSetOpen.cancel();
            }
        }
        if (null != animatorSetClose && animatorSetClose.isRunning()) {
            if (Build.VERSION.SDK_INT >= 19) {
                animatorSetClose.pause();
            } else {
                animatorSetClose.cancel();
            }
        }
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_menu, this);

        mbtnShutdown = findViewById(SHUTDOWN_BTN_ID);
        mbtnDelay = findViewById(DELAY_BTN_ID);
        mbtnScreen = findViewById(SCREEN_BTN_ID);
        imgDown = findViewById(R.id.img_down);

        mbtnShutdown.setImageResouorce(R.drawable.ic_player_rewind);
        mbtnShutdown.setText("测试");

        mbtnDelay.setImageResouorce(R.drawable.ic_player_play_focus);
        mbtnDelay.setText("测试");

        mbtnScreen.setImageResouorce(R.drawable.ic_player_forward);
        mbtnScreen.setText("测试");
    }

    private boolean initHeight() {
        if (0 == height) {
            height = getHeight();
        }
        if (0 != height) {
            lowHeight = height - (int) getResources().getDimension(R.dimen.dp_100);
            return true;
        }
        return false;
    }

    private void initAnimatorSet() {
        if (null == animatorSetOpen) {
            // open
            ValueAnimator valueAnimator = getViewOpenAnimator();
            ObjectAnimator[] shutdownBtnAnimators = getBtnOpenAnimators(mbtnShutdown);
            ObjectAnimator[] delayBtnAnimators = getBtnOpenAnimators(mbtnDelay);
            ObjectAnimator[] screenBtnAnimators = getBtnOpenAnimators(mbtnScreen);
            Animator[] animators = new Animator[] {
                    valueAnimator,
                    shutdownBtnAnimators[0], shutdownBtnAnimators[1], shutdownBtnAnimators[2],
                    delayBtnAnimators[0], delayBtnAnimators[1], delayBtnAnimators[2],
                    screenBtnAnimators[0], screenBtnAnimators[1], screenBtnAnimators[2]
            };
            animatorSetOpen = new AnimatorSet();
            animatorSetOpen.playTogether(animators);
        }
        if (null == animatorSetClose) {
            // close
            ValueAnimator valueAnimator = getViewCloseAnimator();
            ObjectAnimator[] shutdownBtnAnimators = getBtnCloseAnimators(mbtnShutdown);
            ObjectAnimator[] delayBtnAnimators = getBtnCloseAnimators(mbtnDelay);
            ObjectAnimator[] screenBtnAnimators = getBtnCloseAnimators(mbtnScreen);
            Animator[] animators = new Animator[] {
                    valueAnimator,
                    shutdownBtnAnimators[0], shutdownBtnAnimators[1], shutdownBtnAnimators[2],
                    delayBtnAnimators[0], delayBtnAnimators[1], delayBtnAnimators[2],
                    screenBtnAnimators[0], screenBtnAnimators[1], screenBtnAnimators[2]
            };
            animatorSetClose = new AnimatorSet();
            animatorSetClose.playTogether(animators);
        }
    }

    @NotNull
    private ValueAnimator getViewOpenAnimator() {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(lowHeight, height);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int height = (int) animation.getAnimatedValue();
                LogUtils.i(TAG, "open() onAnimationUpdate() height=" + height);
                ViewGroup.LayoutParams layoutParams = getLayoutParams();
                layoutParams.height = height;
                setLayoutParams(layoutParams);
                if (height == MenuView.this.height) {
                    imgDown.setVisibility(VISIBLE);
                }
            }
        });
        return valueAnimator;
    }

    @NotNull
    private ValueAnimator getViewCloseAnimator() {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(height, lowHeight);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int height = (int) animation.getAnimatedValue();
                LogUtils.i(TAG, "close() onAnimationUpdate() height=" + height);
                ViewGroup.LayoutParams layoutParams = getLayoutParams();
                layoutParams.height = height;
                setLayoutParams(layoutParams);
            }
        });
        return valueAnimator;
    }

    private ObjectAnimator[] getBtnOpenAnimators(MenuBtn menuBtn) {
        ObjectAnimator[] objectAnimators = new ObjectAnimator[3];
        ObjectAnimator textAnimationX = ObjectAnimator.ofFloat(menuBtn.text(),
                "x",
                menuBtn.text().getX(), menuBtn.text().getX() + 100);

        ObjectAnimator textAnimationAlpha = ObjectAnimator.ofFloat(menuBtn.text(),
                "alpha",
                0, 1);

        ObjectAnimator iconAnimationX = ObjectAnimator.ofFloat(menuBtn.icon(),
                "x",
                menuBtn.icon().getX(), menuBtn.icon().getX() - 50);

        objectAnimators[0] = textAnimationX;
        objectAnimators[1] = textAnimationAlpha;
        objectAnimators[2] = iconAnimationX;

        return objectAnimators;
    }

    private ObjectAnimator[] getBtnCloseAnimators(MenuBtn menuBtn) {
        ObjectAnimator[] objectAnimators = new ObjectAnimator[3];
        ObjectAnimator textAnimationX = ObjectAnimator.ofFloat(menuBtn.text(),
                "x",
                menuBtn.text().getX(), menuBtn.text().getX() - 100);

        ObjectAnimator textAnimationAlpha = ObjectAnimator.ofFloat(menuBtn.text(),
                "alpha",
                1, 0);

        ObjectAnimator iconAnimationX = ObjectAnimator.ofFloat(menuBtn.icon(),
                "x",
                menuBtn.icon().getX(), menuBtn.icon().getX() + 50);

        objectAnimators[0] = textAnimationX;
        objectAnimators[1] = textAnimationAlpha;
        objectAnimators[2] = iconAnimationX;

        return objectAnimators;
    }
}
