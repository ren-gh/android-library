
package com.rengh.library.common.player;

import com.rengh.library.common.R;
import com.rengh.library.common.handler.WeakHandler;
import com.rengh.library.common.handler.WeakHandlerListener;
import com.rengh.library.common.util.LogUtils;
import com.rengh.library.common.util.ThreadManager;
import com.rengh.library.common.util.ThreadUtils;
import com.rengh.library.common.util.ToastUtils;
import com.rengh.library.common.util.UIUtils;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.VideoView;

public class PlayerActivity extends AppCompatActivity implements WeakHandlerListener {
    private final static String TAG = "PlayerActivity";
    private Context mContext;
    private WeakHandler mWeakHandler;
    private PlayerListener mPlayerListener;

    private VideoView mVideoView;
    private PlayerController mPlayerController;
    private MediaPlayer.OnPreparedListener mOnPreparedListener;
    private MediaPlayer.OnErrorListener mOnErrorListener;
    private MediaPlayer.OnCompletionListener mOnCompletionListener;
    private MediaPlayer.OnSeekCompleteListener mOnSeekCompletionListener;

    private PlayerViewListener mViewListener;

    private final int MSG_WHAT_ON_PREPARED = 1;
    private final int MSG_WHAT_ON_ERROR = 2;
    private final int MSG_WHAT_ON_COMPLETION = 3;
    private final int MSG_WHAT_ON_UPDATE = 4;
    private final int MSG_WHAT_ON_CLICKED = 5;
    private final int MSG_WHAT_ON_FINISH = 6;

    private final int TIME_FORWORD_OR_REWIND = 5000;
    private final int TIME_DOUBLE_CLICK_DELAY = 1000;
    private final int TIME_UPDATE_PROGRESS = 200;

    private boolean mDoubleClick = false; // 双击暂停模式
    private boolean mAutoFinish = false; // 播放完成关闭当前界面
    private int mAutoFinishDelay = 0; // 播放完成或者出错时，延时自动关闭当前界面
    private boolean mIsAdVideo = false; // 是否是广告
    private boolean mShowLoading = false; // 显示加载图

    private boolean mCenterKeyPressed = false; // 记录是否有首次按键
    private Runnable mCenterKeyRunnable = new Runnable() {
        @Override
        public void run() {
            mCenterKeyPressed = false;
            if (null != mPlayerListener) {
                mPlayerListener.onClick();
            }
        }
    };
    private boolean mBackKeyPressed = false; // 记录是否有首次按键
    private Runnable mBackKeyRunnable = new Runnable() {
        @Override
        public void run() {
            mBackKeyPressed = false;
        }
    };

    private String mVideoName = null;
    private Uri mVideoUri = null;
    private Drawable mCoverDrawable = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        LogUtils.i(TAG, "onCreate()");

        UIUtils.setTransStateBar(this);

        mContext = this;
        mWeakHandler = new WeakHandler(this);

        mVideoView = findViewById(R.id.videoView);
        mPlayerController = findViewById(R.id.playerController);

        initVideoViewListener();
        initValues();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        UIUtils.setFullStateBar(this, hasFocus);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.i(TAG, "onResume()");
        initVideoView();
    }

    @Override
    protected void onPause() {
        LogUtils.i(TAG, "onPause()");
        mVideoView.pause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        LogUtils.i(TAG, "onStop()");
        mVideoView.stopPlayback();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        LogUtils.i(TAG, "onDestroy()");
        mVideoView = null;
        super.onDestroy();
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
    public void process(Message msg) {
        switch (msg.what) {
            case MSG_WHAT_ON_PREPARED: {
                if (!mIsAdVideo) {
                    ToastUtils.showToast(mContext, R.string.player_toast_start);
                }
                mVideoView.start();
                updateVideoViewInfo();
                mPlayerController.onStarted();
                if (null != mPlayerListener) {
                    mPlayerListener.onStart();
                }
            }
                break;
            case MSG_WHAT_ON_ERROR: {
                mVideoView.stopPlayback();
                ToastUtils.showToast(mContext, getString(R.string.player_toast_error_with_code,
                        msg.arg1, msg.arg2));
                mPlayerController.onError();
                if (null != mPlayerListener) {
                    mPlayerListener.onError();
                }
                if (mAutoFinish) {
                    mWeakHandler.sendEmptyMessageDelayed(MSG_WHAT_ON_FINISH,
                            TIME_FORWORD_OR_REWIND);
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
                if (KeyEvent.KEYCODE_BACK == msg.arg1) { // 4
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
                finish();
            }
                break;
        }
    }

    private void initVideoViewListener() {
        LogUtils.i(TAG, "initVideoViewListener()");
        mOnSeekCompletionListener = new MediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MediaPlayer mp) {
                mPlayerController.rebackCenterImg();
                if (null != mPlayerListener) {
                    mPlayerListener.onSeekCommpleted();
                }
            }
        };
        mOnPreparedListener = new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                LogUtils.i(TAG, "onPrepared()");
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

    private void initValues() {
        Intent intent = getIntent();
        if (null != intent) {
            mVideoUri = PlayerHelper.getVideoUri();
            mVideoName = PlayerHelper.getVideoTile();
            mPlayerListener = PlayerHelper.getPlayerListener();
            mDoubleClick = PlayerHelper.getDoubleClick();
            mAutoFinish = PlayerHelper.getAutoFinish();
            mAutoFinishDelay = PlayerHelper.getAutoFinishDelay();
            mIsAdVideo = PlayerHelper.isAdVideo();
            mCoverDrawable = PlayerHelper.getCoverDrawable();
            mShowLoading = PlayerHelper.getShowLoading();
            PlayerHelper.clearAll();
            LogUtils.i(TAG, "title: " + mVideoName + ", uri: " + mVideoUri);
        }
    }

    private void initVideoView() {
        if (null == mVideoUri) {
            ToastUtils.showToast(mContext, R.string.player_toast_uri_be_null);
            mWeakHandler.sendEmptyMessage(MSG_WHAT_ON_FINISH);
        } else {
            mVideoView.setOnPreparedListener(mOnPreparedListener);
            mVideoView.setOnErrorListener(mOnErrorListener);
            mVideoView.setOnCompletionListener(mOnCompletionListener);
            if (TextUtils.isEmpty(mVideoName)) {
                mPlayerController.setTitle(getString(R.string.player_text_left_top_tip,
                        getString(R.string.player_text_video_title_unknow)));
            } else {
                mPlayerController.setTitle(getString(R.string.player_text_left_top_tip,
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

    private void updateVideoViewInfo() {
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
        if (mVideoView.isPlaying()) {
            mPlayerController.showOrHide(true);
        } else {
            mPlayerController.showOrHide(false);
        }
    }

    private void centerClicked() {
        Message msg;
        if (mDoubleClick) {
            doubleClickCenterToPause();
        } else {
            msg = new Message();
            msg.what = MSG_WHAT_ON_CLICKED;
            msg.arg1 = KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE;
            processMediaOnClick(msg);
        }
    }

    private void playVideo() {
        if (!mVideoView.isPlaying()) {
            mVideoView.start();
            mPlayerController.onPlaying();
            if (null != mPlayerListener) {
                mPlayerListener.onPlaying();
            }
        } else {
            if (TextUtils.isEmpty(mVideoName)) {
                ToastUtils.showToast(mContext, R.string.player_toast_playing);
            } else {
                ToastUtils.showToast(mContext, getString(R.string.player_toast_playing_with_name, mVideoName));
            }
        }
    }

    private void pauseVideo() {
        if (mVideoView.isPlaying() && mVideoView.canPause()) {
            mVideoView.pause();
            mPlayerController.onPause();
            if (null != mPlayerListener) {
                mPlayerListener.onPause();
            }
        } else {
            ToastUtils.showToast(mContext, R.string.player_toast_not_support_pause);
        }
    }

    private void fastForWardVideo() {
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
        mVideoView.stopPlayback();
        mPlayerController.onStop();
        if (null != mPlayerListener) {
            mPlayerListener.onStop();
        }
    }

    private void playOrPauseVideo() {
        if (mVideoView.canPause()) {
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
        } else {
            ToastUtils.showToast(mContext, R.string.player_toast_not_support_pause);
        }
    }

    private void onSeekBarChangedByUser(int progress) {
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
        if (!mBackKeyPressed) {
            ToastUtils.showToast(this, R.string.player_toast_double_click_to_exit);
            mBackKeyPressed = true;
            mWeakHandler.postDelayed(mBackKeyRunnable, TIME_DOUBLE_CLICK_DELAY);
        } else {
            mBackKeyPressed = false;
            mWeakHandler.removeCallbacks(mBackKeyRunnable);
            mWeakHandler.sendEmptyMessage(MSG_WHAT_ON_FINISH);
        }
    }

    private void doubleClickCenterToPause() {
        if (!mCenterKeyPressed) {
            if (mVideoView.isPlaying()) {
                ToastUtils.showToast(mContext, R.string.player_toast_double_click_to_pause);
            } else {
                ToastUtils.showToast(mContext, R.string.player_toast_double_click_to_play);
            }
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
