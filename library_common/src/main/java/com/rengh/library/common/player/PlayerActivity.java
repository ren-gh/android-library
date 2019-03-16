
package com.rengh.library.common.player;

import com.rengh.library.common.R;
import com.rengh.library.common.handler.WeakHandler;
import com.rengh.library.common.handler.WeakHandlerListener;
import com.rengh.library.common.util.LogUtils;
import com.rengh.library.common.util.ThreadManager;
import com.rengh.library.common.util.ThreadUtils;
import com.rengh.library.common.util.ToastUtils;
import com.rengh.library.common.view.FullScreenVideoView;
import com.tbruyelle.rxpermissions2.RxPermissions;

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

import io.reactivex.functions.Consumer;

public class PlayerActivity extends AppCompatActivity implements WeakHandlerListener {
    private final static String TAG = "PlayerActivity";
    private Context mContext;
    private WeakHandler mWeakHandler;
    private RxPermissions mRxPermissions;
    private PlayerListener mPlayerListener;

    private FullScreenVideoView mVideoView;
    private PlayerController mPlayerController;
    private MediaPlayer.OnPreparedListener mOnPreparedListener;
    private MediaPlayer.OnErrorListener mOnErrorListener;
    private MediaPlayer.OnCompletionListener mOnCompletionListener;
    private MediaPlayer.OnSeekCompleteListener mOnSeekCompletionListener;

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

        mContext = this;
        mWeakHandler = new WeakHandler(this);
        mRxPermissions = new RxPermissions(this);

        mVideoView = findViewById(R.id.videoView);
        mPlayerController = findViewById(R.id.playerController);

        initVideoViewListener();

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
            PlayerHelper.clearAll();
            LogUtils.i(TAG, "title: " + mVideoName + ", uri: " + mVideoUri);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.i(TAG, "onResume()");
        final String readSDCard = "android.permission.WRITE_EXTERNAL_STORAGE";
        if (mRxPermissions.isGranted(readSDCard)) {
            setVideoViewBase();
        } else {
            mRxPermissions.request()
                    .subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean granted) throws Exception {
                            LogUtils.i(TAG, "request permission: " + granted);
                            if (granted) {
                                setVideoViewBase();
                            } else {
                                ToastUtils.showToast(mContext, "将无法播放SD卡内视频文件");
                            }
                        }
                    });
        }
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
        Message msg = new Message();
        msg.what = MSG_WHAT_ON_CLICKED;
        msg.arg1 = keyCode;
        msg.obj = event;
        mWeakHandler.sendMessage(msg);
        return true;
    }

    @Override
    public void process(Message msg) {
        switch (msg.what) {
            case MSG_WHAT_ON_PREPARED: {
                if (!mIsAdVideo) {
                    ToastUtils.showToast(mContext, "开始播放……");
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
                ToastUtils.showToast(mContext, "错误，无法播放该视频！" +
                        " 错误类型: " + msg.arg1 + "。附加错误码: " + msg.arg2);
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
                    ToastUtils.showToast(mContext, "播放完成");
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
    }

    private void setVideoViewBase() {
        if (null == mVideoUri) {
            ToastUtils.showToast(mContext, "视频路径错误");
            mWeakHandler.sendEmptyMessage(MSG_WHAT_ON_FINISH);
        } else {
            mVideoView.setOnPreparedListener(mOnPreparedListener);
            mVideoView.setOnErrorListener(mOnErrorListener);
            mVideoView.setOnCompletionListener(mOnCompletionListener);
            if (TextUtils.isEmpty(mVideoName)) {
                mPlayerController.setTitle("无标题");
            } else {
                mPlayerController.setTitle(mVideoName);
            }
            mPlayerController.setVideoType(mIsAdVideo);
            if (Build.VERSION.SDK_INT >= 16) {
                mPlayerController.setBackground(mCoverDrawable);
            } else {
                mPlayerController.setBackgroundDrawable(mCoverDrawable);
            }
            mVideoView.setVideoURI(mVideoUri);
            if (mDoubleClick && !mIsAdVideo) {
                ToastUtils.showToast(this, "双击暂停或播放");
            }
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
                        if (-1 != duration) {
                            Message msg = new Message();
                            msg.what = MSG_WHAT_ON_UPDATE;
                            msg.arg1 = duration;
                            msg.arg2 = current;
                            mWeakHandler.sendMessage(msg);
                        }
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
            mPlayerController.showOrHide();
        }
        // 播放/暂停
        else if (KeyEvent.KEYCODE_DPAD_CENTER == msg.arg1) { // 23
            if (mDoubleClick) {
                doubleClickCenterToPause();
            } else {
                msg = new Message();
                msg.what = MSG_WHAT_ON_CLICKED;
                msg.arg1 = KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE;
                processMediaOnClick(msg);
            }
        }
        // 播放/暂停
        else if (KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE == msg.arg1) { // 85
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
                ToastUtils.showToast(mContext, "该视频不支持暂停");
            }
        }
        // 播放
        else if (KeyEvent.KEYCODE_MEDIA_PLAY == msg.arg1) { // 126
            if (!mVideoView.isPlaying()) {
                mVideoView.start();
                mPlayerController.onPlaying();
                if (null != mPlayerListener) {
                    mPlayerListener.onPlaying();
                }
            } else {
                ToastUtils.showToast(mContext, "正在播放"
                        + (TextUtils.isEmpty(mVideoName) ? "" : "：" + mVideoName));
            }
        }
        // 暂停
        else if (KeyEvent.KEYCODE_MEDIA_PAUSE == msg.arg1) { // 127
            if (mVideoView.isPlaying() && mVideoView.canPause()) {
                mVideoView.pause();
                mPlayerController.onPause();
                if (null != mPlayerListener) {
                    mPlayerListener.onPause();
                }
            } else {
                ToastUtils.showToast(mContext, "该视频不支持暂停");
            }
        }
        // 快进
        else if (KeyEvent.KEYCODE_DPAD_RIGHT == msg.arg1 // 22
                || KeyEvent.KEYCODE_MEDIA_FAST_FORWARD == msg.arg1) { // 90
            if (!mPlayerController.isShowed()) {
                mPlayerController.showAndAutoHide();
            } else {
                int duration = mVideoView.getDuration();
                if (-1 != duration || mVideoView.canSeekForward()) {
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
                    ToastUtils.showToast(mContext, "该视频不支持快进");
                }
            }
        }
        // 快退
        else if (KeyEvent.KEYCODE_DPAD_LEFT == msg.arg1 // 21
                || KeyEvent.KEYCODE_MEDIA_REWIND == msg.arg1) { // 89
            if (!mPlayerController.isShowed()) {
                mPlayerController.showAndAutoHide();
            } else {
                int duration = mVideoView.getDuration();
                if (-1 != duration || mVideoView.canSeekBackward()) {
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
                    ToastUtils.showToast(mContext, "该视频不支持快退");
                }
            }
        }
        // 停止
        else if (KeyEvent.KEYCODE_MEDIA_STOP == msg.arg1) { // 86
            mVideoView.stopPlayback();
            mPlayerController.onStop();
            if (null != mPlayerListener) {
                mPlayerListener.onStop();
            }
        }
    }

    private void doubleClickBackToFinish() {
        if (!mBackKeyPressed) {
            ToastUtils.showToast(this, "再按一次退出");
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
