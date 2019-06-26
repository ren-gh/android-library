
package com.rengh.app.star.mode;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;

import com.r.library.common.util.LogUtils;
import com.r.library.common.util.ThreadUtils;
import com.rengh.app.star.bean.AdBean;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SplashAdRequest {
    private final String TAG = "SplashAdRequest";
    private String mPath;

    public interface Listener {
        void onSuccess(AdBean adBean);

        void onFailed(Throwable throwable);
    }

    public SplashAdRequest setPath(String path) {
        mPath = path;
        return this;
    }

    @SuppressLint("CheckResult")
    public void request(Context context, Listener listener) {
        Observable
                .create(new ObservableOnSubscribe<AdBean>() {
                    @Override
                    public void subscribe(ObservableEmitter<AdBean> emitter) throws Exception {
                        LogUtils.i(TAG, "create() Observable thread: "
                                + Thread.currentThread().getName());
                        AdBean bean = new AdBean();

                        bean.setId(1);
                        bean.setType(AdBean.AdType.TYPE_VIDEO);
                        bean.setUrl(
                                "http://g3com.cp21.ott.cibntv.net/vod/v1/Mjc0LzQ1LzIwL2xldHYtZ3VnLzE3LzExMjMzMzA0NTgtYXZjLTc3Ny1hYWMtNzctMTUwMDAtNjEwNjA1Mi04YWU0ZjQ4MGQ2NGI3MTc4N2NlNDMzZGUxMjNhZTg1MS0xNTU3Nzk5MTcwODc0LnRz?platid=100&splatid=10004&gugtype=1&mmsid=66981094&type=tv_1080p");
                        if (!TextUtils.isEmpty(mPath)) {
                            bean.setPath(mPath);
                        }
                        emitter.onNext(bean);
                        emitter.onComplete();
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<AdBean>() {
                    @Override
                    public void accept(AdBean adBean) throws Exception {
                        LogUtils.i(TAG, "doOnNext() Observable thread: " + Thread.currentThread().getName());
                        if (null != listener) {
                            listener.onSuccess(adBean);
                        }
                    }
                })
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtils.i(TAG, "doOnError() Observable thread: " + Thread.currentThread().getName());
                        if (null != listener) {
                            listener.onFailed(throwable);
                        }
                    }
                })
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        LogUtils.i(TAG, "doOnComplete() Observable thread: " + Thread.currentThread().getName());
                    }
                })
                .subscribe();
    }
}
