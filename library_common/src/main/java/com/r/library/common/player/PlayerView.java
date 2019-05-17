
package com.r.library.common.player;

import com.r.library.common.R;
import com.r.library.common.handler.WeakHandler;
import com.r.library.common.handler.WeakHandlerListener;
import com.r.library.common.util.LogUtils;
import com.r.library.common.util.ThreadManager;
import com.r.library.common.util.ThreadUtils;
import com.r.library.common.util.ToastUtils;

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
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.VideoView;

public class PlayerView extends RelativeLayout implements WeakHandlerListener {
    private final static String TAG = "PlayerView";
    private Context mContext;
    private WeakHandler mWeakHandler;
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

    private final int TIME_FORWORD_OR_REWIND = 10000; // 快进快退的时间间隔
    private final int TIME_DOUBLE_CLICK_DELAY = 1000; // 双击事件的间隔时间
    private final int TIME_UPDATE_PROGRESS = 50; // 更新播放时间的间隔时间

    private int mSavedTime = 0;

    private int mDuration, mOldDuration;
    private Runnable mLoadingRunnable = new Runnable() {
        @Override
        public void run() {
            if (null == mVideoView) {
                return;
            }
            mDuration = mVideoView.getCurrentPosition();
            if (mOldDuration == mDuration && mVideoView.isPlaying()) {
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
            mOldDuration = mDuration;
            mWeakHandler.postDelayed(mLoadingRunnable, 1000);
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

    /**
     * Start: 需要调用方设置的参数
     */

    private PlayerListener mPlayerListener; // 播放状态监听
    private boolean mDoubleClickPause = false; // 双击暂停模式
    private boolean mAutoFinish = false; // 播放完成关闭当前界面
    private int mAutoFinishDelay = 0; // 播放完成或者出错时，延时自动关闭当前界面
    private boolean mIsAdVideo = false; // 是否是广告
    private boolean mShowLoading = false; // 显示加载图
    private boolean mShowCenterClickToast = true; // 双击模式下，单击是否提示
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

        mParent = findViewById(R.id.rl_parent);
        mVideoView = findViewById(R.id.videoView);
        mFlFloatView = findViewById(R.id.fl_float_view);
        mFlFloatView2 = findViewById(R.id.fl_float_view_2);
        mPlayerController = findViewById(R.id.playerController);

        initVideoViewListener();
    }

    public void onRestart() {
        LogUtils.i(TAG, "onRestart()");
    }

    public void onStart() {
        LogUtils.i(TAG, "onStart()");
    }

    public void onResume() {
        LogUtils.i(TAG, "onResume() saved time: " + mSavedTime);
        playVideo();
    }

    public void onPause() {
        LogUtils.i(TAG, "onPause()");
        try {
            if (0 < mVideoView.getDuration()) {
                mSavedTime = mVideoView.getCurrentPosition();
            }
        } catch (Exception e) {
            LogUtils.i(TAG, "Save current position error: " + e.getMessage());
        }
    }

    public void onStop() {
        LogUtils.i(TAG, "onStop() saved time: " + mSavedTime);
        pauseVideo();
    }

    public void onDestroy() {
        LogUtils.i(TAG, "onDestroy()");
        stopVideo();
        mSavedTime = 0;
        mVideoView = null;
    }

    public boolean onKeyClick(int keyCode, KeyEvent event) {
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
        return false;
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
                mPlayerController.rebackCenterImg();
                if (null != mPlayerListener) {
                    mPlayerListener.onSeekCommpleted();
                }
                mSavedTime = 0;
                playVideo();
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
            public void onCenterClicked() {
                centerClicked();
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
            mVideoUri = params.getVideoUri();
            mVideoName = params.getVideoTitle();
            mPlayerListener = params.getPlayerListener();
            mDoubleClickPause = params.getDoubleClickPause();
            mAutoFinish = params.getAutoFinish();
            mAutoFinishDelay = params.getAutoFinishDelay();
            mIsAdVideo = params.isAdVideo();
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
                mPlayerController.setTitle(mContext.getString(R.string.player_text_left_top_tip,
                        mVideoName));
            }
            mPlayerController.setVideoType(mIsAdVideo);
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
        LogUtils.i(TAG, "updateVideoViewInfo()");
        ThreadManager.getInstance().excuteCached(new Runnable() {
            @Override
            public void run() {
                while (null != mVideoView) {
                    try {
                        int duration = mVideoView.getDuration();
                        int current = mVideoView.getCurrentPosition();
                        Message msg = new Message();
                        msg.what = MSG_WHAT_ON_UPDATE;
                        msg.arg1 = duration;
                        msg.arg2 = current;
                        mWeakHandler.sendMessage(msg);
                    } catch (Exception e) {
                        LogUtils.d(TAG, "update exception: " + e.getMessage());
                    }
                    ThreadUtils.sleep(TIME_UPDATE_PROGRESS);
                }
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
            playOrPauseVideo();
        }
        // 播放
        else if (KeyEvent.KEYCODE_MEDIA_PLAY == msg.arg1) { // 126
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
        if (!mVideoView.isPlaying()) {
            if (0 < mSavedTime) {
                mVideoView.seekTo(mSavedTime);
            } else {
                mVideoView.start();
            }
            mPlayerController.onPlaying();
            if (null != mPlayerListener) {
                mPlayerListener.onPlaying();
            }
        }
    }

    private boolean pauseVideo() {
        LogUtils.i(TAG, "pauseVideo()");
        if (mVideoView.isPlaying()) {
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
        LogUtils.i(TAG, "fastForWardVideo()");
        if (!mPlayerController.isShowed()) {
            mPlayerController.showAndAutoHide();
        } else {
            int duration = mVideoView.getDuration();
            if (-1 != duration && mVideoView.canSeekForward()) {
                int current = mVideoView.getCurrentPosition();
                current += TIME_FORWORD_OR_REWIND;
                if (current > duration) {
                    current = duration;
                }
                mVideoView.seekTo(current);
                mPlayerController.onFast();
                if (null != mPlayerListener) {
                    mPlayerListener.onFast();
                }
            } else {
                mPlayerController.showAndAutoHide();
                ToastUtils.showToast(mContext, R.string.player_toast_not_support_forward);
            }
        }
    }

    private void rewindVideo() {
        LogUtils.i(TAG, "fastForWardVideo()");
        if (!mPlayerController.isShowed()) {
            mPlayerController.showAndAutoHide();
        } else {
            int duration = mVideoView.getDuration();
            if (-1 != duration && mVideoView.canSeekBackward()) {
                int current = mVideoView.getCurrentPosition();
                current -= TIME_FORWORD_OR_REWIND;
                if (current < 0) {
                    current = 0;
                }
                mVideoView.seekTo(current);
                mPlayerController.onRewind();
                if (null != mPlayerListener) {
                    mPlayerListener.onRewind();
                }
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
        int duration = mVideoView.getDuration();
        if (-1 != duration && mVideoView.canSeekForward()) {
            if (progress > duration) {
                progress = duration;
            }
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
}
