
package com.r.library.common.player;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.r.library.common.R;
import com.r.library.common.handler.WeakHandler;
import com.r.library.common.handler.WeakHandlerListener;
import com.r.library.common.util.LogUtils;
import com.r.library.common.util.ThreadManager;
import com.r.library.common.util.ThreadUtils;
import com.r.library.common.util.ToastUtils;

public class PlayerView extends RelativeLayout implements WeakHandlerListener {
    private final static String TAG = "PlayerView";
    private Context mContext;
    private WeakHandler mWeakHandler;
    private Window mWindow;
    private PlayerViewListener mViewListener; // 当前界面监听触摸滑动进度条实现快进/快退

    private RelativeLayout mParent;
    private VideoView mVideoView;
    private FrameLayout mFlFloatView, mFlFloatView2;
    private PlayerController mPlayerController;
    private MediaPlayer.OnPreparedListener mOnPreparedListener;
    private MediaPlayer.OnErrorListener mOnErrorListener;
    private MediaPlayer.OnCompletionListener mOnCompletionListener;
    private MediaPlayer.OnSeekCompleteListener mOnSeekCompletionListener;
    private MediaPlayer.OnVideoSizeChangedListener mOnVideoSizeChangeListener;
    private MediaPlayer.OnBufferingUpdateListener mOnBufferingUpdateListener;
    private MediaPlayer.OnInfoListener mOnInfoListener;

    private final int MSG_WHAT_ON_PREPARED = 1;
    private final int MSG_WHAT_ON_ERROR = 2;
    private final int MSG_WHAT_ON_COMPLETION = 3;
    private final int MSG_WHAT_ON_UPDATE = 4;
    private final int MSG_WHAT_ON_CLICKED = 5;
    private final int MSG_WHAT_ON_FINISH = 6;

    private final int FAST_REWIND_DELAY = 1000;
    private final int TIME_FORWORD_OR_REWIND = 10000; // 快进快退的时间间隔
    private final int TIME_DOUBLE_CLICK_DELAY = 600; // 双击事件的间隔时间
    private final int TIME_UPDATE_PROGRESS = 100; // 更新播放时间的间隔时间

    private int mSavedTime = 0;
    private boolean mPlayFinished = false;
    private boolean mUpdateThreadRunning = false;

    private int mDuration, mCurrentPosition, mOldCurrentPosition;
    private Runnable mLoadingRunnable = new Runnable() {
        @Override
        public void run() {
            if (null == mVideoView) {
                return;
            }
            if (mOldCurrentPosition == mCurrentPosition && mVideoView.isPlaying()) {
                mPlayerController.showLoading(true);
                if (null != mPlayerListener) {
                    mPlayerListener.onLoadingShow();
                }
            } else {
                mPlayerController.showLoading(false);
                if (null != mPlayerListener) {
                    mPlayerListener.onLoadingClose();
                }
            }
            mOldCurrentPosition = mCurrentPosition;
            mWeakHandler.postDelayed(mLoadingRunnable, 2000);
        }
    };

    private boolean mBackKeyPressed = false; // 记录是否有首次按键
    private Runnable mBackKeyRunnable = new Runnable() {
        @Override
        public void run() {
            mBackKeyPressed = false;
            if (null != mPlayerListener) {
                mPlayerListener.onBackClicked();
            }
            ToastUtils.showToast(mContext, R.string.player_toast_double_click_to_exit);
        }
    };
    private boolean mCenterKeyPressed = false; // 记录是否有首次按键
    private Runnable mCenterKeyRunnable = new Runnable() {
        @Override
        public void run() {
            mCenterKeyPressed = false;
            if (null != mPlayerListener) {
                mPlayerListener.onClick();
            }
            if (mShowCenterClickToast) {
                if (mVideoView.isPlaying()) {
                    ToastUtils.showToast(mContext, R.string.player_toast_double_click_to_pause);
                } else {
                    ToastUtils.showToast(mContext, R.string.player_toast_double_click_to_play);
                }
            }
        }
    };
    private boolean mControClicked = false; // 记录是否有首次按键
    private Runnable mControClickRunnable = new Runnable() {
        @Override
        public void run() {
            mControClicked = false;
            if (null != mPlayerListener) {
                mPlayerListener.onClick();
            }
            mPlayerController.showOrHide();
        }
    };

    private boolean mNeedUpdateProgress = true;
    private ForwardRewindRunnable mForwardOrRewindRunnable = new ForwardRewindRunnable();

    private class ForwardRewindRunnable implements Runnable {
        private int mSeek;

        public void setSeek(int seek) {
            mSeek = seek;
        }

        @Override
        public void run() {
            mVideoView.seekTo(mSeek);
            if (null != mPlayerListener) {
                mPlayerListener.onFast();
            }
            mNeedUpdateProgress = true;
        }
    }

    /**
     * Start: 需要调用方设置的参数
     */

    private PlayerListener mPlayerListener; // 播放状态监听
    private boolean mDoubleClickPause = false; // 双击暂停模式
    private boolean mAutoFinish = false; // 播放完成关闭当前界面
    private int mAutoFinishDelay = 0; // 播放完成或者出错时，延时自动关闭当前界面
    private boolean mIsAdVideo = false; // 是否是广告
    private boolean mDisableCountDown = false; // 禁用广告倒计时
    private boolean mShowLoading = false; // 显示加载图
    private boolean mShowCenterClickToast = true; // 双击模式下，单击是否提示
    private int mNumber = 0;
    private String mVideoName = null; // 视频名称
    private Uri mVideoUri = null; // 视频地址
    private Drawable mCoverDrawable = null; // 播放前封面
    private Boolean mIgnoreBackKey = false;

    /**
     * End: 需要调用方设置的参数
     */

    public PlayerView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public PlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, null, 0);
    }

    public PlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, null, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        mContext = context;
        mWeakHandler = new WeakHandler(this);
        LayoutInflater.from(mContext).inflate(R.layout.view_player, this);
        LogUtils.i(TAG, "init()");

        mPlayFinished = false;

        mParent = findViewById(R.id.rl_parent);
        mVideoView = findViewById(R.id.videoView);
        mFlFloatView = findViewById(R.id.fl_float_view);
        mFlFloatView2 = findViewById(R.id.fl_float_view_2);
        mPlayerController = findViewById(R.id.playerController);

        initVideoViewListener();
    }

    public void setWindow(Window window) {
        mWindow = window;
    }

    public void onRestart() {
        LogUtils.i(TAG, "onRestart()");
    }

    public void onStart() {
        LogUtils.i(TAG, "onStart() saved time: " + mSavedTime);
        if (!mPlayFinished) {
            playVideo();
        }
    }

    public void onResume() {
        LogUtils.i(TAG, "onResume()");
    }

    public void onPause() {
        LogUtils.i(TAG, "onPause()");
    }

    public void onStop(boolean isFinishing) {
        LogUtils.i(TAG, "onStop() isFinishing: " + isFinishing + " saved time: " + mSavedTime);
        pauseVideo();
        if (isFinishing) {
            stopVideo();
            mSavedTime = 0;
            mVideoView = null;
        }
    }

    public void onDestroy() {
        LogUtils.i(TAG, "onDestroy()");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_MENU == keyCode
                || KeyEvent.KEYCODE_BACK == keyCode
                || KeyEvent.KEYCODE_DPAD_CENTER == keyCode
                || KeyEvent.KEYCODE_DPAD_UP == keyCode
                || KeyEvent.KEYCODE_DPAD_DOWN == keyCode
                || KeyEvent.KEYCODE_DPAD_LEFT == keyCode
                || KeyEvent.KEYCODE_DPAD_RIGHT == keyCode
                || KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE == keyCode
                || KeyEvent.KEYCODE_MEDIA_PLAY == keyCode
                || KeyEvent.KEYCODE_MEDIA_PAUSE == keyCode
                || KeyEvent.KEYCODE_MEDIA_STOP == keyCode
                || KeyEvent.KEYCODE_MEDIA_FAST_FORWARD == keyCode
                || KeyEvent.KEYCODE_MEDIA_REWIND == keyCode) {
            Message msg = new Message();
            msg.what = MSG_WHAT_ON_CLICKED;
            msg.arg1 = keyCode;
            msg.obj = event;
            mWeakHandler.sendMessage(msg);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mPlayerController.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    /*
     * 设置屏幕亮度 0 最暗 1 最亮
     */
    private void setBrightness(float brightness) {
        if (null == mWindow) {
            return;
        }
        WindowManager.LayoutParams lp = mWindow.getAttributes();
        lp.screenBrightness = lp.screenBrightness + brightness / 255.0f;
        if (lp.screenBrightness > 1) {
            lp.screenBrightness = 1;
        } else if (lp.screenBrightness < 0.1) {
            lp.screenBrightness = (float) 0.1;
        }
        mWindow.setAttributes(lp);
        float sb = lp.screenBrightness;
        LogUtils.d(TAG, "屏幕亮度：" + (int) Math.ceil(sb * 100) + "%");
    }

    @Override
    public void process(Message msg) {
        switch (msg.what) {
            case MSG_WHAT_ON_PREPARED: {
                updateVideoViewInfo();
                mPlayerController.onStarted();
                mWeakHandler.postDelayed(mLoadingRunnable, 3000);
                if (null != mPlayerListener) {
                    mPlayerListener.onStart(mFlFloatView, mFlFloatView2);
                }
                if (!mIsAdVideo && mDoubleClickPause) {
                    ToastUtils.showToast(mContext,
                            R.string.player_toast_double_click_to_play_or_pause);
                }
            }
                break;
            case MSG_WHAT_ON_ERROR: {
                mPlayFinished = true;
                mVideoView.stopPlayback();
                ToastUtils.showToast(mContext, mContext.getString(R.string.player_toast_error_with_code,
                        msg.arg1, msg.arg2));
                mPlayerController.onError();
                if (null != mPlayerListener) {
                    mPlayerListener.onError();
                }
                if (mAutoFinish) {
                    mWeakHandler.sendEmptyMessageDelayed(MSG_WHAT_ON_FINISH,
                            mAutoFinishDelay);
                }
            }
                break;
            case MSG_WHAT_ON_COMPLETION: {
                mPlayFinished = true;
                if (!mIsAdVideo) {
                    ToastUtils.showToast(mContext, R.string.player_toast_completed);
                }
                mPlayerController.onCompleted();
                if (null != mPlayerListener) {
                    mPlayerListener.onCompleted();
                }
                if (mAutoFinish) {
                    mWeakHandler.sendEmptyMessageDelayed(MSG_WHAT_ON_FINISH,
                            mAutoFinishDelay);
                }
            }
                break;
            case MSG_WHAT_ON_UPDATE: {
                if (msg.arg2 > 0) {
                    if (Build.VERSION.SDK_INT >= 16) {
                        mPlayerController.setBackground(null);
                    } else {
                        mPlayerController.setBackgroundDrawable(null);
                    }
                    if (null != mPlayerListener) {
                        mPlayerListener.onUpdate(msg.arg2);
                    }
                }
                // 切入后台后，部分机型长度大于0，Current值非常小，如小米6：116
                if (0 < msg.arg1 && 1000 < msg.arg2) {
                    mSavedTime = msg.arg2;
                }
                mPlayerController.onUpdate(msg.arg1, msg.arg2);
            }
                break;
            case MSG_WHAT_ON_CLICKED: {
                // 双击退出
                if (KeyEvent.KEYCODE_BACK == msg.arg1 && !mIgnoreBackKey) { // 4
                    doubleClickBackToFinish();
                } else {
                    processMediaOnClick(msg);
                }
            }
                break;
            case MSG_WHAT_ON_FINISH: {
                if (null != mPlayerListener) {
                    mPlayerListener.onFinish();
                }
                PlayerHelper.clearAll();
            }
                break;
        }
    }

    private void initVideoViewListener() {
        LogUtils.i(TAG, "initVideoViewListener()");
        mOnVideoSizeChangeListener = new MediaPlayer.OnVideoSizeChangedListener() {
            @Override
            public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                LayoutParams params = changeVideoSize(mp);
                params.addRule(RelativeLayout.CENTER_IN_PARENT, mParent.getId());
                mVideoView.setLayoutParams(params);
            }
        };
        mOnSeekCompletionListener = new MediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MediaPlayer mp) {
                LogUtils.i(TAG, "onSeekComplete()");
                mPlayerController.rebackCenterImg();
                mSavedTime = 0;
                if (null != mPlayerListener) {
                    mPlayerListener.onSeekCommpleted();
                }
            }
        };
        mOnBufferingUpdateListener = new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                mPlayerController.setSecondProgress(percent);
            }
        };
        mOnInfoListener = new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                return false;
            }
        };
        mOnPreparedListener = new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                LogUtils.i(TAG, "onPrepared()");
                mp.setOnBufferingUpdateListener(mOnBufferingUpdateListener);
                mp.setOnInfoListener(mOnInfoListener);
                mp.setOnVideoSizeChangedListener(mOnVideoSizeChangeListener);
                mp.setOnSeekCompleteListener(mOnSeekCompletionListener);
                mp.setScreenOnWhilePlaying(true);

                Message msg = new Message();
                msg.what = MSG_WHAT_ON_PREPARED;
                msg.obj = mp;
                mWeakHandler.sendMessage(msg);
            }
        };
        mOnErrorListener = new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                LogUtils.i(TAG, "onError()");
                Message msg = new Message();
                msg.what = MSG_WHAT_ON_ERROR;
                msg.obj = mp;
                msg.arg1 = what;
                msg.arg2 = extra;
                mWeakHandler.sendMessage(msg);
                return false;
            }
        };
        mOnCompletionListener = new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                LogUtils.i(TAG, "onCompleted()");
                Message msg = new Message();
                msg.what = MSG_WHAT_ON_COMPLETION;
                msg.obj = mp;
                mWeakHandler.sendMessage(msg);
            }
        };
        mViewListener = new PlayerViewListener() {
            @Override
            public boolean onTouchEvent(MotionEvent event) {
                return false;
            }

            @Override
            public void onCenterClicked() {
                centerClicked();
            }

            @Override
            public void onClick() {
                doubleClickControToPause();
            }

            @Override
            public void onSeekBarChanged(int progress, boolean fromUser) {
                if (!fromUser) {
                    return;
                }
                onSeekBarChangedByUser(progress);
            }
        };
    }

    public void initValues() {
        LogUtils.i(TAG, "initValues()");
        PlayerParams params = PlayerHelper.getPlayerParams();
        if (null != params) {
            mNumber = params.getNumber();
            mVideoUri = params.getVideoUri();
            mVideoName = params.getVideoTitle();
            mPlayerListener = params.getPlayerListener();
            mDoubleClickPause = params.getDoubleClickPause();
            mAutoFinish = params.getAutoFinish();
            mAutoFinishDelay = params.getAutoFinishDelay();
            mIsAdVideo = params.isAdVideo();
            mDisableCountDown = params.isDisableCountDown();
            mCoverDrawable = params.getCoverDrawable();
            mShowLoading = params.getShowLoading();
            mShowCenterClickToast = params.getShowCenterClickToast();
            mIgnoreBackKey = params.getIgnoreBackKey();
        }
        PlayerHelper.clearAll();
        LogUtils.i(TAG, "title: " + mVideoName + ", uri: " + mVideoUri);
        initVideoView();
    }

    private void initVideoView() {
        LogUtils.i(TAG, "initVideoView()");
        if (null == mVideoUri) {
            ToastUtils.showToast(mContext, R.string.player_toast_uri_be_null);
            mWeakHandler.sendEmptyMessage(MSG_WHAT_ON_FINISH);
        } else {
            mVideoView.setOnPreparedListener(mOnPreparedListener);
            mVideoView.setOnErrorListener(mOnErrorListener);
            mVideoView.setOnCompletionListener(mOnCompletionListener);
            if (TextUtils.isEmpty(mVideoName)) {
                mPlayerController.setTitle(mContext.getString(R.string.player_text_left_top_tip,
                        mContext.getString(R.string.player_text_video_title_unknow)));
            } else {
                String title = mContext.getString(R.string.player_text_left_top_tip, mVideoName);
                if (0 != mNumber) {
                    title = mNumber + "." + title;
                }
                mPlayerController.setTitle(title);
            }
            mPlayerController.setVideoType(mIsAdVideo);
            mPlayerController.disableCountdown(mDisableCountDown);
            mPlayerController.setShowLoading(mShowLoading);
            mPlayerController.setViewListener(mViewListener);
            if (Build.VERSION.SDK_INT >= 16) {
                mPlayerController.setBackground(mCoverDrawable);
            } else {
                mPlayerController.setBackgroundDrawable(mCoverDrawable);
            }
            mVideoView.setVideoURI(mVideoUri);
        }
    }

    private LayoutParams changeVideoSize(MediaPlayer mp) {
        LogUtils.i(TAG, "changeVideoSize()");
        // 改变视频的尺寸自适应。
        int videoWidth = mp.getVideoWidth();
        int videoHeight = mp.getVideoHeight();

        int surfaceWidth = mVideoView.getWidth();
        int surfaceHeight = mVideoView.getHeight();

        // 根据视频尺寸去计算->视频可以在sufaceView中放大的最大倍数。
        float max;
        if (getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            // 竖屏模式下按视频宽度计算放大倍数值
            max = Math.max((float) videoWidth / (float) surfaceWidth, (float) videoHeight / (float) surfaceHeight);
        } else {
            // 横屏模式下按视频高度计算放大倍数值
            max = Math.max(((float) videoHeight / (float) surfaceHeight), (float) videoWidth / (float) surfaceWidth);
        }

        // 视频宽高分别/最大倍数值 计算出放大后的视频尺寸
        videoWidth = (int) Math.ceil((float) videoWidth / max);
        videoHeight = (int) Math.ceil((float) videoHeight / max);

        // 无法直接设置视频尺寸，将计算出的视频尺寸设置到surfaceView 让视频自动填充。
        LayoutParams params = new LayoutParams(videoWidth, videoHeight);
        return params;
    }

    private void updateVideoViewInfo() {
        if (mUpdateThreadRunning) {
            return;
        }
        LogUtils.i(TAG, "updateVideoViewInfo()");
        mUpdateThreadRunning = true;
        ThreadManager.getInstance().excuteCached(new Runnable() {
            @Override
            public void run() {
                while (null != mVideoView) {
                    if (mNeedUpdateProgress) {
                        try {
                            mDuration = mVideoView.getDuration();
                            mCurrentPosition = mVideoView.getCurrentPosition();
                            Message msg = new Message();
                            msg.what = MSG_WHAT_ON_UPDATE;
                            msg.arg1 = mDuration;
                            msg.arg2 = mCurrentPosition;
                            mWeakHandler.sendMessage(msg);
                        } catch (Exception e) {
                            LogUtils.d(TAG, "update exception: " + e.getMessage());
                        }
                    }
                    ThreadUtils.sleep(TIME_UPDATE_PROGRESS);
                }
                mUpdateThreadRunning = false;
            }
        });
    }

    private void processMediaOnClick(Message msg) {
        LogUtils.i(TAG, "updateVideoViewInfo()");
        if (mIsAdVideo) {
            return;
        }
        // 显示/隐藏控制器
        if (KeyEvent.KEYCODE_MENU == msg.arg1 // 82
                || KeyEvent.KEYCODE_DPAD_UP == msg.arg1 // 19
                || KeyEvent.KEYCODE_DPAD_DOWN == msg.arg1) { // 20
            showOrHideController();
        }
        // 播放/暂停
        else if (KeyEvent.KEYCODE_DPAD_CENTER == msg.arg1) { // 23
            centerClicked();
        }
        // 播放/暂停
        else if (KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE == msg.arg1) { // 85
            mPlayFinished = false;
            playOrPauseVideo();
        }
        // 播放
        else if (KeyEvent.KEYCODE_MEDIA_PLAY == msg.arg1) { // 126
            mPlayFinished = false;
            playVideo();
        }
        // 暂停
        else if (KeyEvent.KEYCODE_MEDIA_PAUSE == msg.arg1) { // 127
            pauseVideo();
        }
        // 快进
        else if (KeyEvent.KEYCODE_DPAD_RIGHT == msg.arg1 // 22
                || KeyEvent.KEYCODE_MEDIA_FAST_FORWARD == msg.arg1) { // 90
            fastForWardVideo();
        }
        // 快退
        else if (KeyEvent.KEYCODE_DPAD_LEFT == msg.arg1 // 21
                || KeyEvent.KEYCODE_MEDIA_REWIND == msg.arg1) { // 89
            rewindVideo();
        }
        // 停止
        else if (KeyEvent.KEYCODE_MEDIA_STOP == msg.arg1) { // 86
            stopVideo();
        }
    }

    private void showOrHideController() {
        LogUtils.i(TAG, "showOrHideController()");
        if (mVideoView.isPlaying()) {
            mPlayerController.showOrHide(true);
        } else {
            mPlayerController.showOrHide(false);
        }
    }

    private void centerClicked() {
        LogUtils.i(TAG, "centerClicked()");
        Message msg;
        if (mDoubleClickPause) {
            doubleClickCenterToPause();
        } else {
            msg = new Message();
            msg.what = MSG_WHAT_ON_CLICKED;
            msg.arg1 = KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE;
            processMediaOnClick(msg);
        }
    }

    private void playOrPauseVideo() {
        LogUtils.i(TAG, "playOrPauseVideo()");
        if (mPlayFinished) {
            return;
        }
        if (mVideoView.isPlaying()) {
            mVideoView.pause();
            mPlayerController.onPause();
            if (null != mPlayerListener) {
                mPlayerListener.onPause();
            }
        } else {
            mVideoView.start();
            mPlayerController.onPlaying();
            if (null != mPlayerListener) {
                mPlayerListener.onPlaying();
            }
        }
    }

    private void playVideo() {
        LogUtils.i(TAG, "playVideo()");
        if (mPlayFinished) {
            return;
        }
        if (!mVideoView.isPlaying()) {
            if (0 < mSavedTime) {
                mVideoView.seekTo(mSavedTime);
            }
            mVideoView.start();
            mPlayerController.onPlaying();
            if (null != mPlayerListener) {
                mPlayerListener.onPlaying();
            }
        }
    }

    private boolean pauseVideo() {
        LogUtils.i(TAG, "pauseVideo()");
        if (null != mVideoView && mVideoView.isPlaying()) {
            mVideoView.pause();
            mPlayerController.onPause();
            if (null != mPlayerListener) {
                mPlayerListener.onPause();
            }
            return true;
        } else {
            return false;
        }
    }

    private void fastForWardVideo() {
        if (!mPlayerController.isShowed()) {
            mPlayerController.showAndAutoHide();
        } else {
            LogUtils.i(TAG, "fastForWardVideo()");
            if (-1 != mDuration && mVideoView.canSeekForward()) {
                mCurrentPosition += TIME_FORWORD_OR_REWIND;
                if (mCurrentPosition > mDuration) {
                    mCurrentPosition = mDuration;
                }
                LogUtils.d(TAG, "time length: " + mCurrentPosition);
                Message msg = new Message();
                msg.what = MSG_WHAT_ON_UPDATE;
                msg.arg1 = mDuration;
                msg.arg2 = mCurrentPosition;
                mWeakHandler.sendMessage(msg);
                mNeedUpdateProgress = false;
                mPlayerController.onForward();
                mWeakHandler.removeCallbacks(mForwardOrRewindRunnable);
                mForwardOrRewindRunnable.setSeek(mCurrentPosition);
                mWeakHandler.postDelayed(mForwardOrRewindRunnable, FAST_REWIND_DELAY);
            } else {
                mPlayerController.showAndAutoHide();
                ToastUtils.showToast(mContext, R.string.player_toast_not_support_forward);
            }
        }
    }

    private void rewindVideo() {
        if (!mPlayerController.isShowed()) {
            mPlayerController.showAndAutoHide();
        } else {
            LogUtils.i(TAG, "rewindVideo()");
            if (-1 != mDuration && mVideoView.canSeekBackward()) {
                mCurrentPosition -= TIME_FORWORD_OR_REWIND;
                if (mCurrentPosition < 0) {
                    mCurrentPosition = 0;
                }
                LogUtils.d(TAG, "time length: " + mCurrentPosition);
                Message msg = new Message();
                msg.what = MSG_WHAT_ON_UPDATE;
                msg.arg1 = mDuration;
                msg.arg2 = mCurrentPosition;
                mWeakHandler.sendMessage(msg);
                mNeedUpdateProgress = false;
                mPlayerController.onRewind();
                mWeakHandler.removeCallbacks(mForwardOrRewindRunnable);
                mForwardOrRewindRunnable.setSeek(mCurrentPosition);
                mWeakHandler.postDelayed(mForwardOrRewindRunnable, FAST_REWIND_DELAY);
            } else {
                mPlayerController.showAndAutoHide();
                ToastUtils.showToast(mContext, R.string.player_toast_not_support_backward);
            }
        }
    }

    private void stopVideo() {
        LogUtils.i(TAG, "stopVideo()");
        mVideoView.stopPlayback();
        mPlayerController.onStop();
        if (null != mPlayerListener) {
            mPlayerListener.onStop();
        }
    }

    private void onSeekBarChangedByUser(int progress) {
        LogUtils.i(TAG, "onSeekBarChangedByUser()");
        if (-1 != mDuration && mVideoView.canSeekForward()) {
            if (progress > mDuration) {
                progress = mDuration;
            }
            mPlayFinished = false;
            mVideoView.seekTo(progress);
            if (null != mPlayerListener) {
                mPlayerListener.onSeekChanged(progress);
            }
        } else {
            mPlayerController.showAndAutoHide();
            ToastUtils.showToast(mContext, R.string.player_toast_not_support_forward);
        }
    }

    private void doubleClickBackToFinish() {
        LogUtils.i(TAG, "doubleClickBackToFinish()");
        if (!mBackKeyPressed) {
            mBackKeyPressed = true;
            mWeakHandler.postDelayed(mBackKeyRunnable, TIME_DOUBLE_CLICK_DELAY);
        } else {
            mBackKeyPressed = false;
            mWeakHandler.removeCallbacks(mBackKeyRunnable);
            mWeakHandler.sendEmptyMessage(MSG_WHAT_ON_FINISH);
        }
    }

    private void doubleClickCenterToPause() {
        LogUtils.i(TAG, "doubleClickCenterToPause()");
        if (!mCenterKeyPressed) {
            mCenterKeyPressed = true;
            mWeakHandler.postDelayed(mCenterKeyRunnable, TIME_DOUBLE_CLICK_DELAY);
        } else {
            mCenterKeyPressed = false;
            mWeakHandler.removeCallbacks(mCenterKeyRunnable);
            Message msg = new Message();
            msg.what = MSG_WHAT_ON_CLICKED;
            msg.arg1 = KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE;
            processMediaOnClick(msg);
        }
    }

    private void doubleClickControToPause() {
        LogUtils.i(TAG, "doubleClickCenterToPause()");
        if (!mControClicked) {
            mControClicked = true;
            mWeakHandler.postDelayed(mControClickRunnable, TIME_DOUBLE_CLICK_DELAY);
        } else {
            mControClicked = false;
            mWeakHandler.removeCallbacks(mControClickRunnable);
            Message msg = new Message();
            msg.what = MSG_WHAT_ON_CLICKED;
            msg.arg1 = KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE;
            processMediaOnClick(msg);
        }
    }
}
