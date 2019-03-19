
package com.rengh.library.common.player;

import com.rengh.library.common.R;
import com.rengh.library.common.handler.WeakHandler;
import com.rengh.library.common.util.BitmapUtils;
import com.rengh.library.common.util.FileUtils;
import com.rengh.library.common.util.LogUtils;
import com.rengh.library.common.util.TimeUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.InputStream;

public class PlayerController extends RelativeLayout {
    private final String TAG = "PlayerController";

    private Context mContext;
    private WeakHandler mWeakHandler;

    private TextView mTvTitle;
    private ImageView mIvCountDown, mIvStateCenter;
    private LinearLayout mLlBottom;
    private AppCompatSeekBar mSeekBar;
    private TextView mTvTimePlayed, mTvTimeTotal;
    private LinearLayout mLlLoading;

    private Drawable bakDrawable = null;

    private boolean mIsStarted, mIsShowed, mIsAdVideo, mShowLoading;
    private Integer mDuration = -1;

    public PlayerController(Context context) {
        super(context);
        init(context, null, 0);
    }

    public PlayerController(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, null, 0);
    }

    public PlayerController(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, null, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        mContext = context;
        mWeakHandler = new WeakHandler();
        LayoutInflater.from(mContext).inflate(R.layout.layout_player_controller, this);
        mTvTitle = findViewById(R.id.tv_player_title);
        mIvStateCenter = findViewById(R.id.iv_player_center);
        mLlBottom = findViewById(R.id.ll_player_bottom);
        mTvTimePlayed = findViewById(R.id.tv_player_time_played);
        mSeekBar = findViewById(R.id.sb_player_seekbar);
        mTvTimeTotal = findViewById(R.id.tv_player_time_total);
        mLlLoading = findViewById(R.id.ll_loading);
        mIvCountDown = findViewById(R.id.iv_count_down);

        mSeekBar.setMax(0);
        mSeekBar.setProgress(0);

        mIvStateCenter.setVisibility(View.GONE);
        mLlBottom.setVisibility(View.GONE);
        mLlLoading.setVisibility(View.GONE);

        mIsShowed = false;
        mIsStarted = false;
    }

    public PlayerController setVideoType(boolean isAdVideo) {
        mIsAdVideo = isAdVideo;
        if (mIsAdVideo) {
            mTvTitle.setVisibility(View.GONE);
            mIvStateCenter.setVisibility(View.GONE);
            mLlBottom.setVisibility(View.GONE);
        }
        return this;
    }

    public PlayerController setShowLoading(boolean show) {
        mShowLoading = show;
        if (mShowLoading) {
            mLlLoading.setVisibility(View.VISIBLE);
        } else {
            mLlLoading.setVisibility(View.GONE);
        }
        return this;
    }

    public PlayerController setTitle(String title) {
        mTvTitle.setText(title);
        return this;
    }

    public PlayerController setIvStateCenter(int resId) {
        mIvStateCenter.setBackgroundResource(resId);
        return this;
    }

    public PlayerController setIvStateCenter(Drawable drawable) {
        if (Build.VERSION.SDK_INT >= 16) {
            mIvStateCenter.setBackground(drawable);
        } else {
            mIvStateCenter.setBackgroundDrawable(drawable);
        }
        return this;
    }

    public PlayerController setTimePlayed(long currentTime) {
        mTvTimePlayed.setText(TimeUtils.formatDateTime(currentTime));
        return this;
    }

    public PlayerController setTimeTotal(long total) {
        mTvTimeTotal.setText(TimeUtils.formatDateTime(total));
        return this;
    }

    public PlayerController setProgress(int max, int progress) {
        mSeekBar.setMax(max);
        mSeekBar.setProgress(progress);
        return this;
    }

    public PlayerController onStarted() {
        if (!mIsAdVideo) {
            mIvStateCenter.setVisibility(View.VISIBLE);
            mLlBottom.setVisibility(View.VISIBLE);
            mIsShowed = true;
        }
        mLlLoading.setVisibility(View.GONE);
        mIsStarted = true;
        showAndAutoHide();
        return this;
    }

    public PlayerController onPause() {
        mWeakHandler.removeCallbacks(autoHide);
        setIvStateCenter(R.drawable.ic_player_play_focus);
        show();
        return this;
    }

    public PlayerController onStop() {
        setIvStateCenter(R.drawable.ic_player_play_focus);
        show();
        return this;
    }

    private Runnable autoHide = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    public PlayerController onPlaying() {
        setIvStateCenter(R.drawable.ic_player_suspended_focus);
        showAndAutoHide();
        return this;
    }

    private int mCountBak = 0;

    public PlayerController onUpdate(int duration, int current) {
        mDuration = duration;
        setTimeTotal(duration);
        setTimePlayed(current);
        setProgress(duration, current);
        setCountDown(duration, current);
        return this;
    }

    public void rebackCenterImg() {
        if (null != bakDrawable) {
            setIvStateCenter(bakDrawable);
        }
        bakDrawable = null;
        if (-1 == mDuration) {
            show();
        } else {
            showAndAutoHide();
        }
    }

    public PlayerController onFast() {
        if (null == bakDrawable) {
            bakDrawable = mIvStateCenter.getBackground();
        }
        setIvStateCenter(R.drawable.ic_player_fast);
        show();
        return this;
    }

    public PlayerController onRewind() {
        if (null == bakDrawable) {
            bakDrawable = mIvStateCenter.getBackground();
        }
        setIvStateCenter(R.drawable.ic_player_rewind);
        show();
        return this;
    }

    public PlayerController onError() {
        mIvStateCenter.setVisibility(View.VISIBLE);
        mLlBottom.setVisibility(View.VISIBLE);
        mLlLoading.setVisibility(View.GONE);
        setIvStateCenter(R.drawable.ic_player_play_focus);
        show();
        return this;
    }

    public PlayerController onCompleted() {
        setIvStateCenter(R.drawable.ic_player_play_focus);
        show();
        return this;
    }

    private void setCountDown(int duration, int current) {
        if (mIsAdVideo) {
            int down = (duration - current) / 1000;
            if (down != mCountBak) {
                mCountBak = down;
                try {
                    String path = "pic/ad_second/countime" + mCountBak + ".png";
                    InputStream in = FileUtils.getAssetsFileInputStream(mContext, path);
                    Bitmap bmp = BitmapUtils.readBitMap(mContext, in);
                    float height = mContext.getResources().getDimension(R.dimen.dp_20);
                    float width = bmp.getWidth() * (height / bmp.getHeight());
                    ViewGroup.LayoutParams params = mIvCountDown.getLayoutParams();
                    params.width = (int) width;
                    params.height = (int) height;
                    mIvCountDown.setLayoutParams(params);
                    mIvCountDown.setImageBitmap(bmp);
                } catch (Exception e) {
                    LogUtils.e(TAG, "Count: " + mCountBak + ", get ad count down pic error: "
                            + e.getMessage());
                }
            }
        }
    }

    public boolean isShowed() {
        return mIsShowed;
    }

    public PlayerController showOrHide() {
        showOrHide(true);
        return this;
    }

    public PlayerController showOrHide(boolean autoHide) {
        if (mIsShowed) {
            hide();
        } else {
            if (autoHide) {
                showAndAutoHide();
            } else {
                show();
            }
        }
        return this;
    }

    public PlayerController show() {
        return showImpl(false);
    }

    public PlayerController showAndAutoHide() {
        return showImpl(true);
    }

    private PlayerController showImpl(boolean willHide) {
        if (mIsAdVideo) {
            return this;
        }
        mWeakHandler.removeCallbacks(autoHide);
        if (willHide) {
            mWeakHandler.postDelayed(autoHide, 3000);
        }
        if (mIsStarted && !mIsShowed) {
            mIsShowed = true;
            mTvTitle.startAnimation(getAnimation(mContext, R.anim.slide_in_top));
            mIvStateCenter.startAnimation(getAnimation(mContext, R.anim.slide_in_center));
            mLlBottom.startAnimation(getAnimation(mContext, R.anim.slide_in_bottom));
        }
        return this;
    }

    public PlayerController hide() {
        if (mIsStarted && mIsShowed) {
            mIsShowed = false;
            mTvTitle.startAnimation(getAnimation(mContext, R.anim.slide_out_top));
            mIvStateCenter.startAnimation(getAnimation(mContext, R.anim.slide_out_center));
            mLlBottom.startAnimation(getAnimation(mContext, R.anim.slide_out_bottom));
        }
        return this;
    }

    private Animation getAnimation(Context context, int resId) {
        Animation animation = AnimationUtils.loadAnimation(context, resId);
        if (animation != null) {
            animation.setFillAfter(true);
        }
        return animation;
    }
}
