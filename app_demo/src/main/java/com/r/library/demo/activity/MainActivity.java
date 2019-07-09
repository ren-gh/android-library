
package com.r.library.demo.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.r.library.common.player2.VideoView;
import com.r.library.common.util.BitmapUtils;
import com.r.library.common.util.LogUtils;
import com.r.library.demo.R;
import com.r.library.demo.util.HistUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {
    private final String TAG = "MainActivity";
    private Context context;
    private VideoView videoView;
    /**
     * CV相机
     */
    private CameraBridgeViewBase mCVCamera;
    /**
     * 加载OpenCV的回调
     */
    private BaseLoaderCallback mLoaderCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        videoView = findViewById(R.id.videoView);

        // 初始化CV相机
        mCVCamera = findViewById(R.id.cv);
        mCVCamera.setVisibility(CameraBridgeViewBase.VISIBLE);
        // 设置相机监听
        mCVCamera.setCvCameraViewListener(this);
        // 连接到OpenCV的回调
        mLoaderCallback = new BaseLoaderCallback(this) {
            @Override
            public void onManagerConnected(int status) {
                switch (status) {
                    case LoaderCallbackInterface.SUCCESS:
                        mCVCamera.enableView();
                        break;
                    default:
                        break;
                }
            }
        };

        requestPermission();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 销毁OpenCV相机
        if (mCVCamera != null)
            mCVCamera.disableView();
    }

    @Override
    protected void onDestroy() {
        videoView.release();
        super.onDestroy();
    }

    @SuppressLint("CheckResult")
    private void requestPermission() {
        new RxPermissions(this)
                .request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            // playVideo();
                            if (!OpenCVLoader.initDebug()) {
                                LogUtils.i(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
                                OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, getApplicationContext(), mLoaderCallback);
                            } else {
                                LogUtils.i(TAG, "OpenCV library found inside package. Using it!");
                                mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
                            }
                        } else {
                            LogUtils.e(TAG, "Permission ERROR!");
                        }
                    }
                });
    }

    private void playVideo() {
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                LogUtils.i(TAG, "Current: " + videoView.getCurrentPosition());
                finish();
            }
        });
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                int duration = videoView.getDuration();
                LogUtils.i(TAG, "Duration: " + duration);
            }
        });
        videoView.setVideoPath(Uri.parse("/sdcard/ad.ts"));
        videoView.start();
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        Observable
                .create(new ObservableOnSubscribe<Double>() {
                    @Override
                    public void subscribe(ObservableEmitter<Double> emitter) throws Exception {
                        try {
                            Mat src = Utils.loadResource(context, R.drawable.screen_3);
                            Mat dest = Utils.loadResource(context, R.drawable.screen_2);
                            double result = HistUtils.comPareHist(src, dest);
                            emitter.onNext(result);
                        } catch (Exception e) {
                        }
                        emitter.onComplete();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Double>() {
                    @Override
                    public void accept(Double result) throws Exception {
                        LogUtils.i(TAG, "onCameraViewStarted() " + result);
                    }
                });
    }

    @Override
    public void onCameraViewStopped() {

    }

    private Bitmap bmpCap, bmpLogo;

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        try {
            Mat src = Utils.loadResource(context, R.drawable.logo_cctv_3_t3);
            Mat dest = inputFrame.rgba();
            double result = HistUtils.comPareHist(src, dest);
            LogUtils.i(TAG, "onCameraFrame() " + result);
        } catch (Exception e) {
        }
        return inputFrame.rgba();
    }
}
