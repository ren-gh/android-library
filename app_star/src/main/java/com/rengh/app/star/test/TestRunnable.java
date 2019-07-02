
package com.rengh.app.star.test;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.googlecode.tesseract.android.TessBaseAPI;
import com.r.library.common.util.BitmapUtils;
import com.r.library.common.util.FileUtils;
import com.r.library.common.util.LogUtils;
import com.rengh.app.star.app.StarApplication;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class TestRunnable implements Runnable {
    private final String TAG = "TestRunnable";

    @Override
    public void run() {
        getTextFromBitmap();
    }

    @SuppressLint("CheckResult")
    private void getTextFromBitmap() {
        Observable
                .create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                        TessBaseAPI tessBaseAPI = null;
                        try {
                            Bitmap bitmap = BitmapUtils.readBitMap(StarApplication.getInstance(),
                                    "/sdcard/1.png");
                            final String path = "/sdcard/tessbase/";
                            FileUtils.checkDirExists(path + "tessdata");

                            tessBaseAPI = new TessBaseAPI();
                            tessBaseAPI.init(path, "chi_sim");
                            // tessBaseAPI.setVariable(TessBaseAPI.VAR_CHAR_WHITELIST,
                            // "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789");
                            // 识别白名单
                            tessBaseAPI.setVariable(TessBaseAPI.VAR_CHAR_BLACKLIST,
                                    "!@#$%^&*()_+=-[]}{;:'\"\\|~`,./<>?"); // 识别黑名单
                            tessBaseAPI.setPageSegMode(TessBaseAPI.PageSegMode.PSM_AUTO_OSD);
                            tessBaseAPI.setImage(bitmap);
                            String result = tessBaseAPI.getUTF8Text();
                            if (TextUtils.isEmpty(result)) {
                                emitter.onError(new Throwable("识别失败"));
                            } else {
                                emitter.onNext(result);
                                emitter.onComplete();
                            }
                        } catch (Exception e) {
                            LogUtils.e(TAG, "OCR识别异常", e);
                        } finally {
                            if (null != tessBaseAPI) {
                                tessBaseAPI.clear();
                                tessBaseAPI.end();
                            }
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        LogUtils.i(TAG, "onSubscribe() 开始识别...");
                    }

                    @Override
                    public void onNext(String s) {
                        LogUtils.i(TAG, "onNext() 识别结果：" + s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e(TAG, "onError() 识别失败：", e);
                    }

                    @Override
                    public void onComplete() {
                        LogUtils.i(TAG, "onComplete() 识别结束");
                    }
                });
    }
}
