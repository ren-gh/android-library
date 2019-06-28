
package com.r.library.common.player;

import com.r.library.common.R;
import com.r.library.common.handler.WeakHandler;
import com.r.library.common.util.LogUtils;
import com.r.library.common.util.UIUtils;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

public class PlayerActivity extends AppCompatActivity {
    private final static String TAG = "PlayerActivity";
    private Context mContext;
    private WeakHandler mWeakHandler;
    private Window mWindow;
    private PlayerView mPlayerView;
    private PlayerListener mVideoListenerFromHelper = null;
    private PlayerListener mVideoListener = new PlayerListener() {
        @Override
        public void onStart(FrameLayout frameLayout, FrameLayout frameLayout2) {
            if (null != mVideoListenerFromHelper) {
                mVideoListenerFromHelper.onStart(frameLayout, frameLayout2);
            }
        }

        @Override
        public void onPlaying() {
            if (null != mVideoListenerFromHelper) {
                mVideoListenerFromHelper.onPlaying();
            }
        }

        @Override
        public void onUpdate(int current) {
            if (null != mVideoListenerFromHelper) {
                mVideoListenerFromHelper.onUpdate(current);
            }
        }

        @Override
        public void onLoadingShow() {
            if (null != mVideoListenerFromHelper) {
                mVideoListenerFromHelper.onLoadingShow();
            }
        }

        @Override
        public void onLoadingClose() {
            if (null != mVideoListenerFromHelper) {
                mVideoListenerFromHelper.onLoadingClose();
            }
        }

        @Override
        public void onPause() {
            if (null != mVideoListenerFromHelper) {
                mVideoListenerFromHelper.onPause();
            }
        }

        @Override
        public void onFast() {
            if (null != mVideoListenerFromHelper) {
                mVideoListenerFromHelper.onFast();
            }
        }

        @Override
        public void onRewind() {
            if (null != mVideoListenerFromHelper) {
                mVideoListenerFromHelper.onRewind();
            }
        }

        @Override
        public void onSeekCommpleted() {
            if (null != mVideoListenerFromHelper) {
                mVideoListenerFromHelper.onSeekCommpleted();
            }
        }

        @Override
        public void onError() {
            if (null != mVideoListenerFromHelper) {
                mVideoListenerFromHelper.onError();
            }
        }

        @Override
        public void onCompleted() {
            if (null != mVideoListenerFromHelper) {
                mVideoListenerFromHelper.onCompleted();
            }
        }

        @Override
        public void onStop() {
            if (null != mVideoListenerFromHelper) {
                mVideoListenerFromHelper.onStop();
            }
        }

        @Override
        public void onClick() {
            if (null != mVideoListenerFromHelper) {
                mVideoListenerFromHelper.onClick();
            }
        }

        @Override
        public void onFinish() {
            if (null != mVideoListenerFromHelper) {
                mVideoListenerFromHelper.onFinish();
            }
            finish();
        }

        @Override
        public void onSeekChanged(int progress) {
            if (null != mVideoListenerFromHelper) {
                mVideoListenerFromHelper.onSeekChanged(progress);
            }
        }

        @Override
        public void onBackClicked() {
            if (null != mVideoListenerFromHelper) {
                mVideoListenerFromHelper.onBackClicked();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_player);

        LogUtils.i(TAG, "onCreate()");

        UIUtils.setTranslationStatusBar(this);

        mContext = this;
        mWeakHandler = new WeakHandler();
        mWindow = getWindow();

        mPlayerView = findViewById(R.id.pv_video);

        PlayerParams params = PlayerHelper.getPlayerParams();
        if (null != params) {
            mVideoListenerFromHelper = params.getPlayerListener();
            params.setPlayerListener(mVideoListener);
        }
        mPlayerView.initValues();
        mPlayerView.setWindow(mWindow);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        UIUtils.setFullStateBar(this, hasFocus);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mPlayerView.onKeyDown(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mPlayerView.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onRestart() {
        super.onRestart();
        mPlayerView.onRestart();
    }

    @Override
    public void onStart() {
        super.onStart();
        mPlayerView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPlayerView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPlayerView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPlayerView.onStop(isFinishing());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPlayerView.onDestroy();
    }
}
