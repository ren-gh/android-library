
package com.rengh.app.star.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.r.library.common.player.BasePlayerListener;
import com.r.library.common.player.PlayerHelper;
import com.r.library.common.player.PlayerParams;
import com.r.library.common.util.LogUtils;
import com.rengh.app.star.R;
import com.rengh.app.star.bean.AdBean;
import com.rengh.app.star.mode.SplashAdRequest;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.FilenameFilter;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SplashFragment extends Fragment implements View.OnClickListener {
    private final String TAG = "SplashFragment";
    private RelativeLayout mParent;
    private VideoView mPvVideo;
    private ImageView mIvSplash;
    private TextView mTvTip;
    private Listener mListener;
    private AdBean mBean;
    private Disposable mDisposable;

    public interface Listener {
        void onAdReady(AdBean bean);

        void onAdFinish();
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    @Override
    public void onAttach(Context context) {
        LogUtils.i(TAG, "onAttach()");
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        LogUtils.i(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        LogUtils.i(TAG, "onCreateView()");
        View view = inflater.inflate(R.layout.fragment_splash, container, false);

        mParent = view.findViewById(R.id.rl_parent);
        mPvVideo = view.findViewById(R.id.pv_video);
        mIvSplash = view.findViewById(R.id.iv_splash);
        mTvTip = view.findViewById(R.id.tv_tip);

        mIvSplash.setVisibility(View.VISIBLE);
        mPvVideo.setVisibility(View.GONE);
        mTvTip.setVisibility(View.GONE);

        mTvTip.setClickable(true);
        mTvTip.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_tip: {
                requestAdInfo();
            }
                break;
        }
    }

    @Override
    public void onStart() {
        LogUtils.i(TAG, "onStart()");
        super.onStart();
        requestAdInfo();
    }

    @Override
    public void onResume() {
        LogUtils.i(TAG, "onResume()");
        super.onResume();
    }

    @Override
    public void onPause() {
        LogUtils.i(TAG, "onPause()");
        super.onPause();
    }

    @Override
    public void onStop() {
        LogUtils.i(TAG, "onStop()");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        LogUtils.i(TAG, "onDestroyView()");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        LogUtils.i(TAG, "onDestroy()");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        LogUtils.i(TAG, "onDetach()");
        cancelTimer();
        super.onDetach();
    }

    @SuppressLint("CheckResult")
    private void requestAdInfo() {
        // new SplashAdRequest().request(getContext(), requestListener);
        new RxPermissions(this)
                .request(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean granted) throws Exception {
                        if (granted) {
                            String path = getVideoPath();
                            while (null == path) {
                                path = getVideoPath();
                            }
                            LogUtils.i(TAG, "Video path: " + path);
                            new SplashAdRequest().setPath(path).request(getContext(),
                                    requestListener);
                        } else {
                            LogUtils.i(TAG, "No permisstion.");
                            SplashFragment.this.onFinish();
                        }
                    }
                });
    }

    private String getVideoPath() {
        String path = null;
        File dir = new File(Environment.getExternalStorageDirectory().getPath()
                + "/DCIM/Camera");
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(".mp4");
                }
            });
            if (null != files && files.length > 0) {
                int min = 0;
                int max = files.length - 1;
                int index = (int) (min + Math.random() * (max - 1 + 1));
                path = files[index].getPath();
            }
        }
        return path;
    }

    private SplashAdRequest.Listener requestListener = new SplashAdRequest.Listener() {
        @Override
        public void onSuccess(AdBean bean) {
            LogUtils.i(TAG, "onSuccess() bean=" + bean);
            mBean = bean;
            if (null != mBean) {
                switch (mBean.getType()) {
                    case AdBean.AdType.TYPE_PIC: {
                        setImageUrl();
                    }
                        break;
                    case AdBean.AdType.TYPE_VIDEO: {
                        playVideo();
                    }
                        break;
                    case AdBean.AdType.TYPE_ANIM: {
                        onFinish();
                    }
                        break;
                    default: {
                        onFinish();
                    }
                        break;
                }
            } else {
                onFinish();
            }
        }

        @Override
        public void onFailed(Throwable throwable) {
            LogUtils.i(TAG, "onFailed() throwable=" + throwable);
            onFinish();
        }
    };

    private void setImageUrl() {
        if (null == mBean) {
            onFinish();
            return;
        }
        Glide.with(getActivity())
                .load(mBean.getUrl())
                .placeholder(R.drawable.splash_bg_pic)
                .listener(mGlideListener)
                .into(mIvSplash);
    }

    private RequestListener mGlideListener = new RequestListener<Drawable>() {
        @Override
        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
            LogUtils.e(TAG, "onLoadFailed()");
            onFinish();
            return false;
        }

        @Override
        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
            LogUtils.i(TAG, "onResourceReady()");
            onReady();
            startTimer();
            return false;
        }
    };

    private void startTimer() {
        if (null == mBean) {
            onFinish();
            return;
        }
        Observable.timer(mBean.getLength(), TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(Long aLong) {
                        onFinish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        onFinish();
                    }

                    @Override
                    public void onComplete() {
                        onFinish();
                    }
                });
        LogUtils.i(TAG, "startTimer() Start timer: " + mBean.getLength() + "ms");
    }

    private void cancelTimer() {
        if (null != mDisposable && !mDisposable.isDisposed()) {
            mDisposable.dispose();
            LogUtils.i(TAG, "cancelTimer() Cancel timer.");
        }
    }

    @SuppressLint("CheckResult")
    private void playVideo() {
        Uri uri = Uri.parse(TextUtils.isEmpty(mBean.getPath()) ? mBean.getUrl() : mBean.getPath());
        if (null == uri) {
            onFinish();
            return;
        }
        PlayerParams params = new PlayerParams();
        params.setCoverDrawable(getResources().getDrawable(R.drawable.splash_bg_pic));
        params.setVideoUri(uri);
        params.setDisableCountDown(true);
        params.setAdVideo(true);
        params.setPlayerListener(playerListener);
        PlayerHelper.setPlayerParams(params);

        if (mPvVideo.isPlaying()) {
            mPvVideo.stopPlayback();
        }

        mPvVideo.setOnPreparedListener(mPlayerPreparedListener);
        mPvVideo.setOnCompletionListener(mPlayerCompletionListener);
        mPvVideo.setOnErrorListener(mPlayerErrorListener);
        mPvVideo.setVisibility(View.VISIBLE);
        mPvVideo.setVideoURI(uri);
        mPvVideo.start();

        mPlayerTimer = Observable.interval(0L, 100L, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    mPlayerTimer.dispose();
                    mIvSplash.setImageDrawable(null);
                    mIvSplash.setVisibility(View.GONE);
                    SplashFragment.this.onReady();
                });
    }

    private Disposable mPlayerTimer = null;

    private MediaPlayer.OnPreparedListener mPlayerPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                @Override
                public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                    // RelativeLayout.LayoutParams params = changeVideoSize(mp);
                    // params.addRule(RelativeLayout.CENTER_IN_PARENT, mParent.getId());
                    // mPvVideo.setLayoutParams(params);
                }
            });
        }
    };

    private MediaPlayer.OnErrorListener mPlayerErrorListener = new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            LogUtils.i(TAG, "onError()");
            // SplashFragment.this.onFinish();
            mTvTip.performClick();
            return false;
        }
    };

    private MediaPlayer.OnCompletionListener mPlayerCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            LogUtils.i(TAG, "onCompleted()");
            // SplashFragment.this.onFinish();
            mTvTip.performClick();
        }
    };

    private BasePlayerListener playerListener = new BasePlayerListener() {
        private boolean updated = false;

        @Override
        public void onUpdate(int current) {
            if (current > 0 && !updated) {
                LogUtils.i(TAG, "onPlaying()");
                SplashFragment.this.onReady();
                updated = true;
            }
        }

        @Override
        public void onError() {
            LogUtils.i(TAG, "onError()");
            SplashFragment.this.onFinish();
        }

        @Override
        public void onCompleted() {
            LogUtils.i(TAG, "onCompleted()");
            SplashFragment.this.onFinish();
        }
    };

    private RelativeLayout.LayoutParams changeVideoSize(MediaPlayer mp) {
        LogUtils.i(TAG, "changeVideoSize()");
        // 改变视频的尺寸自适应。
        int videoWidth = mp.getVideoWidth();
        int videoHeight = mp.getVideoHeight();

        int surfaceWidth = mPvVideo.getWidth();
        int surfaceHeight = mPvVideo.getHeight();

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
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(videoWidth, videoHeight);
        return params;
    }

    private void onReady() {
        mTvTip.setVisibility(View.VISIBLE);
        if (null != mListener) {
            mListener.onAdReady(mBean);
        }
    }

    private void onFinish() {
        mIvSplash.setImageDrawable(null);
        if (null != mListener) {
            mListener.onAdFinish();
            mListener = null;
            LogUtils.i(TAG, "onFinish()");
        }
    }

}
