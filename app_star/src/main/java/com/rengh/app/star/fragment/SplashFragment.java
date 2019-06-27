
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
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.r.library.common.util.LogUtils;
import com.rengh.app.star.R;
import com.rengh.app.star.bean.AdBean;
import com.rengh.app.star.mode.SplashAdRequest;
import com.rengh.app.star.player.VideoView;
import com.rengh.app.star.util.LocalFileUtils;
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

        requestAdInfo();

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_tip: {
                Snackbar.make(mParent, getString(R.string.app_name) + "：已点击", Snackbar.LENGTH_LONG).show();
                onFinish();
            }
                break;
        }
    }

    @Override
    public void onStart() {
        LogUtils.i(TAG, "onStart()");
        super.onStart();
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
        releaseVideo();
        super.onDetach();
    }

    /**
     * 开屏资源准备就绪
     */
    private void onReady() {
        mTvTip.setVisibility(View.VISIBLE);
        if (null != mListener) {
            mListener.onAdReady(mBean);
        }
    }

    /**
     * 开屏资源展示结束
     */
    private void onFinish() {
        mIvSplash.setImageDrawable(null);
        if (null != mListener) {
            mListener.onAdFinish();
            mListener = null;
            LogUtils.i(TAG, "onFinish()");
        }
    }

    /**
     * 请求开屏资源
     */
    @SuppressLint("CheckResult")
    private void requestAdInfo() {
        // new SplashAdRequest().request(getContext(), requestListener);
        new RxPermissions(this)
                .request(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean granted) throws Exception {
                        if (granted) {
                            String path = LocalFileUtils.getVideoPath(null);
                            while (null == path) {
                                path = LocalFileUtils.getVideoPath(null);
                            }
                            LogUtils.i(TAG, "Video path: " + path);
                            new SplashAdRequest().setPath(path).request(getContext(), requestListener);
                        } else {
                            LogUtils.i(TAG, "No permisstion.");
                            SplashFragment.this.onFinish();
                        }
                    }
                });
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
        LogUtils.i(TAG, "setImageUrl()");
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
        LogUtils.i(TAG, "playVideo() uri=" + uri);
        if (null == uri) {
            onFinish();
            return;
        }

        if (mPvVideo.isPlaying()) {
            mPvVideo.stop();
        }

        mPvVideo.setOnPreparedListener(mPlayerPreparedListener);
        mPvVideo.setOnCompletionListener(mPlayerCompletionListener);
        mPvVideo.setOnErrorListener(mPlayerErrorListener);
        mPvVideo.setVisibility(View.VISIBLE);
        mPvVideo.setVideoPath(uri);
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

    private void releaseVideo() {
        LogUtils.i(TAG, "releaseVideo()");
        if (mPvVideo.isPlaying()) {
            mPvVideo.release();
        }
    }

    private Disposable mPlayerTimer = null;

    private MediaPlayer.OnPreparedListener mPlayerPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                @Override
                public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                    LogUtils.i(TAG, "onVideoSizeChanged()");
                    mPvVideo.changeVideoSize();
                }
            });
        }
    };

    private MediaPlayer.OnErrorListener mPlayerErrorListener = new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            LogUtils.i(TAG, "onError()");
            SplashFragment.this.onFinish();
            return false;
        }
    };

    private MediaPlayer.OnCompletionListener mPlayerCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            LogUtils.i(TAG, "onCompleted()");
            SplashFragment.this.onFinish();
        }
    };

}
