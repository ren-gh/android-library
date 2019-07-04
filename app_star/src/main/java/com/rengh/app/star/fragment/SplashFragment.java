
package com.rengh.app.star.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
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
import com.r.library.common.player2.VideoView;
import com.r.library.common.util.LogUtils;
import com.rengh.app.star.R;
import com.rengh.app.star.bean.AdBean;
import com.rengh.app.star.mode.SplashAdRequest;
import com.rengh.app.star.util.LocalFileUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SplashFragment extends BaseFragment implements View.OnClickListener {
    private final String TAG = "SplashFragment";
    private RelativeLayout mParent;
    private VideoView mPvVideo;
    private ImageView mIvSplash;
    private TextView mTvSkip;
    private Listener mListener;
    private AdBean mBean;

    public interface Listener {
        void onAdReady(AdBean bean);

        void onAdFinish();
    }

    public void setListener(Listener listener) {
        mListener = listener;
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
        mTvSkip = view.findViewById(R.id.tv_skip);

        mIvSplash.setVisibility(View.VISIBLE);
        mIvSplash.setOnClickListener(this);
        mIvSplash.setClickable(false);

        mPvVideo.setVisibility(View.GONE);
        mPvVideo.setOnClickListener(this);

        mTvSkip.setOnClickListener(this);
        mTvSkip.setVisibility(View.GONE);

        requestAdInfo();

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_splash: {
                Snackbar.make(mParent, getString(R.string.app_name) + "：图片详情", Snackbar.LENGTH_LONG).show();
                onFinish();
            }
                break;
            case R.id.pv_video: {
                Snackbar.make(mParent, getString(R.string.app_name) + "：视频详情", Snackbar.LENGTH_LONG).show();
                onFinish();
            }
                break;
            case R.id.tv_skip: {
                Snackbar.make(mParent, getString(R.string.app_name) + "：跳过", Snackbar.LENGTH_LONG).show();
                onFinish();
            }
                break;
        }
    }

    @Override
    public void onDetach() {
        LogUtils.i(TAG, "onDetach()");
        cancelTimer();
        releaseVideo();
        super.onDetach();
    }

    /*****************************
     * Start 开屏资源准备、结束事件 *
     *****************************/

    /**
     * 开屏资源准备就绪
     */
    private void onReady() {
        switch (mBean.getType()) {
            case AdBean.AdType.TYPE_PIC: {
                mTvSkip.setVisibility(View.VISIBLE);
                mIvSplash.setClickable(true);
            }
                break;
            case AdBean.AdType.TYPE_VIDEO: {
                mTvSkip.setVisibility(View.VISIBLE);
                mIvSplash.setImageDrawable(null);
                mIvSplash.setVisibility(View.GONE);
            }
                break;
            case AdBean.AdType.TYPE_ANIM: {
            }
                break;
        }
        if (null != mListener) {
            mListener.onAdReady(mBean);
        }
    }

    /**
     * 开屏资源展示结束
     */
    private void onFinish() {
        mTvSkip.setVisibility(View.GONE);
        mIvSplash.setImageDrawable(null);
        if (null != mListener) {
            mListener.onAdFinish();
            mListener = null;
            LogUtils.i(TAG, "onFinish()");
        }
    }

    /***************************
     * End 开屏资源准备、结束事件 *
     ***************************/

    /************************
     * Start 开屏资源请求事件 *
     ************************/

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
                            findVideoPathAndReqeust();
                        } else {
                            LogUtils.i(TAG, "No permisstion.");
                            SplashFragment.this.onFinish();
                        }
                    }
                });
    }

    /**
     * 查找本地相册的视频，随机选取一个后再进行开屏资源请求
     */
    @SuppressLint("CheckResult")
    private void findVideoPathAndReqeust() {
        Observable
                .create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                        String path = LocalFileUtils.getVideoPath(null);
                        int i = 30;
                        while (null == path && i > 0) {
                            path = LocalFileUtils.getVideoPath(null);
                            i--;
                        }
                        if (null == path) {
                            emitter.onError(new Throwable("path is null."));
                        } else {
                            emitter.onNext(path);
                            emitter.onComplete();
                        }
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        LogUtils.i(TAG, "onSubscribe() 开始...");
                    }

                    @Override
                    public void onNext(String path) {
                        LogUtils.i(TAG, "onNext() 完成：" + path);
                        new SplashAdRequest().setPath(path).request(getContext(), requestListener);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.i(TAG, "onError() 错误：" + e.getMessage());
                        new SplashAdRequest().request(getContext(), requestListener);
                    }

                    @Override
                    public void onComplete() {
                        LogUtils.i(TAG, "onComplete() 结束！");
                    }
                });
    }

    /**
     * 开屏资源请求监听器
     */
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

    /**********************
     * End 开屏资源请求事件 *
     **********************/

    /***************************
     * Start 开屏资源图片类型相关 *
     ***************************/

    /**
     * 图片类型开屏资源展示
     */
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

    /**
     * 图片类型开屏资源展示监听器
     */
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

    /**
     * 图片类型开屏资源展示定时器
     */
    private Disposable mDisposable;

    /**
     * 图片类型开屏资源展示定时器启动
     */
    private void startTimer() {
        if (null == mBean) {
            onFinish();
            return;
        }
        mDisposable = Observable.timer(mBean.getLength(), TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        onFinish();
                    }
                });
        LogUtils.i(TAG, "startTimer() Start timer: " + mBean.getLength() + "ms");
    }

    /**
     * 图片类型开屏资源展示时长定时器取消
     */
    private void cancelTimer() {
        if (null != mDisposable && !mDisposable.isDisposed()) {
            mDisposable.dispose();
            LogUtils.i(TAG, "cancelTimer() Cancel timer.");
        }
    }

    /*************************
     * End 开屏资源图片类型相关 *
     *************************/

    /***************************
     * Start 开屏资源视频类型相关 *
     ***************************/

    /**
     * 视频类型开屏资源展示
     */
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
                    SplashFragment.this.onReady();
                });
    }

    /**
     * 视频类型开屏资源释放
     */
    private void releaseVideo() {
        LogUtils.i(TAG, "releaseVideo()");
        if (mPvVideo.isPlaying()) {
            mPvVideo.release();
        }
    }

    /**
     * 视屏类型开屏资源展示后移除遮屏推按的定时器
     */
    private Disposable mPlayerTimer = null;

    /**
     * 视屏类型开屏资源播放监听器
     */
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

    /**
     * 视屏类型开屏资源播放监听器
     */
    private MediaPlayer.OnErrorListener mPlayerErrorListener = new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            LogUtils.i(TAG, "onError()");
            SplashFragment.this.onFinish();
            return false;
        }
    };

    /**
     * 视屏类型开屏资源播放监听器
     */
    private MediaPlayer.OnCompletionListener mPlayerCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            LogUtils.i(TAG, "onCompleted()");
            SplashFragment.this.onFinish();
        }
    };

    /*************************
     * End 开屏资源视频类型相关 *
     *************************/

}
