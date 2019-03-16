
package com.rengh.rlibrary.activity;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.rengh.library.common.dialog.RDialog;
import com.rengh.library.common.net.LocalNetHelper;
import com.rengh.library.common.notification.NotificationHelper;
import com.rengh.library.common.player.PlayerActivity;
import com.rengh.library.common.player.PlayerListener;
import com.rengh.library.common.player.PlayerHelper;
import com.rengh.library.common.util.LogUtils;
import com.rengh.rlibrary.R;
import com.rengh.rlibrary.broadcast.NotificationClickReceiver;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class DemoActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {
    private final String TAG = "DemoActivity";
    private RxPermissions mRxPermissions;
    private Context mContext;
    private TextView mTvInfo;
    private Button mBtnPlayNormal, mBtnPlayCover, mBtnPlayDouble, mBtnPlayFinish, mBtnPlayAd;

    private String VIDEO_NAME = "测试视频";
    private String VIDEO_URL = "http://ivi.bupt.edu.cn/hls/cctv1hd.m3u8";
    private String VIDEO_URL_2 = "/sdcard/ad.ts";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        mContext = this;
        mRxPermissions = new RxPermissions(this);

        mTvInfo = findViewById(R.id.tv_info);
        mBtnPlayNormal = findViewById(R.id.btn_play_normal);
        mBtnPlayCover = findViewById(R.id.btn_play_cover_view);
        mBtnPlayDouble = findViewById(R.id.btn_play_double_click);
        mBtnPlayFinish = findViewById(R.id.btn_play_auto_finish);
        mBtnPlayAd = findViewById(R.id.btn_play_ad_mode);

        mBtnPlayNormal.setOnClickListener(this);
        mBtnPlayCover.setOnClickListener(this);
        mBtnPlayDouble.setOnClickListener(this);
        mBtnPlayFinish.setOnClickListener(this);
        mBtnPlayAd.setOnClickListener(this);

        mBtnPlayNormal.setOnFocusChangeListener(this);
        mBtnPlayCover.setOnFocusChangeListener(this);
        mBtnPlayDouble.setOnFocusChangeListener(this);
        mBtnPlayFinish.setOnFocusChangeListener(this);
        mBtnPlayAd.setOnFocusChangeListener(this);

        Intent intent = getIntent();
        if (null != intent) {
            String action = intent.getAction();
            Uri uri = intent.getData();
            LogUtils.i(TAG, "action: " + action + ", uri: " + uri);
        }

        showDialog();

        rxJavaTest();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_play_normal: {
                PlayerHelper.setPlayerListener(mPlayerListener);
                PlayerHelper.setCoverDrawable(null);
                PlayerHelper.setDoubleClick(false);
                PlayerHelper.setAdVideo(false);
                PlayerHelper.setAutoFinish(false);
                PlayerHelper.setAutoFinishDelay(0);
                PlayerHelper.setVideoTile(VIDEO_NAME);
                PlayerHelper.setVideoUri(Uri.parse(VIDEO_URL));
                Intent intent = new Intent();
                intent.setClass(mContext, PlayerActivity.class);
                startActivity(intent);
            }
                break;
            case R.id.btn_play_cover_view: {
                PlayerHelper.setPlayerListener(mPlayerListener);
                PlayerHelper.setCoverDrawable(getResources().getDrawable(R.drawable.ic_desk_app_bg));
                PlayerHelper.setDoubleClick(false);
                PlayerHelper.setAdVideo(false);
                PlayerHelper.setAutoFinish(false);
                PlayerHelper.setAutoFinishDelay(0);
                PlayerHelper.setVideoTile(VIDEO_NAME);
                PlayerHelper.setVideoUri(Uri.parse(VIDEO_URL));
                Intent intent = new Intent();
                intent.setClass(mContext, PlayerActivity.class);
                startActivity(intent);
            }
                break;
            case R.id.btn_play_double_click: {
                PlayerHelper.setPlayerListener(mPlayerListener);
                PlayerHelper.setCoverDrawable(null);
                PlayerHelper.setDoubleClick(true);
                PlayerHelper.setAdVideo(false);
                PlayerHelper.setAutoFinish(false);
                PlayerHelper.setAutoFinishDelay(0);
                PlayerHelper.setVideoTile(VIDEO_NAME);
                PlayerHelper.setVideoUri(Uri.parse(VIDEO_URL));
                Intent intent = new Intent();
                intent.setClass(mContext, PlayerActivity.class);
                startActivity(intent);
            }
                break;
            case R.id.btn_play_auto_finish: {
                PlayerHelper.setPlayerListener(mPlayerListener);
                PlayerHelper.setCoverDrawable(null);
                PlayerHelper.setDoubleClick(false);
                PlayerHelper.setAdVideo(false);
                PlayerHelper.setAutoFinish(true);
                PlayerHelper.setAutoFinishDelay(3000);
                PlayerHelper.setVideoTile(VIDEO_NAME);
                PlayerHelper.setVideoUri(Uri.parse(VIDEO_URL));
                Intent intent = new Intent();
                intent.setClass(mContext, PlayerActivity.class);
                startActivity(intent);
            }
                break;
            case R.id.btn_play_ad_mode: {
                PlayerHelper.setPlayerListener(mPlayerListener);
                PlayerHelper.setCoverDrawable(null);
                PlayerHelper.setDoubleClick(false);
                PlayerHelper.setAdVideo(true);
                PlayerHelper.setAutoFinish(true);
                PlayerHelper.setAutoFinishDelay(0);
                PlayerHelper.setVideoTile(VIDEO_NAME);
                PlayerHelper.setVideoUri(Uri.parse(VIDEO_URL));
                Intent intent = new Intent();
                intent.setClass(mContext, PlayerActivity.class);
                startActivity(intent);
            }
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
switch (v.getId()){
    case R.id.btn_play_normal:
    case R.id.btn_play_cover_view:
    case R.id.btn_play_double_click:
    case R.id.btn_play_auto_finish:
    case R.id.btn_play_ad_mode:{
        Button btn = (Button)v;
        if(hasFocus){
            btn.setTextColor(getResources().getColor(R.color.colorRed));
        }else{
            btn.setTextColor(getResources().getColor(R.color.black));
        }
    }break;
}
    }

    private PlayerListener mPlayerListener = new PlayerListener() {
        @Override
        public void onStart() {
            LogUtils.i(TAG, "onStart()");
        }

        @Override
        public void onPlaying() {
            LogUtils.i(TAG, "onPlaying()");
        }

        @Override
        public void onPause() {
            LogUtils.i(TAG, "onPause()");
        }

        @Override
        public void onFast() {
            LogUtils.i(TAG, "onFast()");
        }

        @Override
        public void onRewind() {
            LogUtils.i(TAG, "onRewind()");
        }

        @Override
        public void onSeekCommpleted() {
            LogUtils.i(TAG, "onSeekCommpleted()");
        }

        @Override
        public void onError() {
            LogUtils.i(TAG, "onError()");
        }

        @Override
        public void onCompleted() {
            LogUtils.i(TAG, "onCompleted()");
        }

        @Override
        public void onStop() {
            LogUtils.i(TAG, "onStop()");
        }

        @Override
        public void onClick() {
            LogUtils.i(TAG, "onClick()");
        }

        @Override
        public void onFinish() {
            LogUtils.i(TAG, "onFinish()");
        }
    };

    private void showDialog() {
        final RDialog dialog = new RDialog(this);
        dialog.setButtonNoClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setButtonYesClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                setDeviceInfo();
            }
        });
        dialog.setContent("你好，欢迎使用R开发库！");
        dialog.show();
    }

    private void setDeviceInfo() {
        mRxPermissions.request(Manifest.permission.READ_PHONE_STATE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean granted) throws Exception {
                        DeviceInfo deviceInfo = new DeviceInfo();
                        if (granted) {
                            deviceInfo.imeis = LocalNetHelper.getIMEI(mContext);
                            deviceInfo.meids = LocalNetHelper.getMEID(mContext);
                        } else {
                            deviceInfo.imeis = null;
                            deviceInfo.meids = null;
                        }
                        updateInfoView(deviceInfo);
                        updateNotification(deviceInfo);
                    }
                });
    }

    class DeviceInfo {
        List<String> imeis;
        List<String> meids;
    }

    private void updateInfoView(DeviceInfo deviceInfo) {
        mTvInfo.setText("有线：" + LocalNetHelper.getMac("eth0") + "\n"
                + "无线：" + LocalNetHelper.getMac("wlan0") + "\n"
                + "IP: " + LocalNetHelper.getIp() + "\n"
                + "联网网卡: " + LocalNetHelper.getIpName() + "\n"
                + "联网网卡MAC: " + LocalNetHelper.getMac(LocalNetHelper.getIpName()) + "\n"
                + "IMEI: " + (null == deviceInfo.imeis ? "没有权限" : deviceInfo.imeis) + "\n"
                + "MEID: " + (null == deviceInfo.meids ? "没有权限" : deviceInfo.meids) + "\n");
    }

    private void updateNotification(DeviceInfo deviceInfo) {
        Intent intent = new Intent(mContext, NotificationClickReceiver.class);
        intent.putExtra("notificationId", 1);
        intent.putExtra("eth0Mac", LocalNetHelper.getMac("eth0"));
        intent.putExtra("wlan0Mac", LocalNetHelper.getMac("wlan0"));
        intent.putExtra("netDev", LocalNetHelper.getIpName());
        intent.putExtra("netDevMac", LocalNetHelper.getMac(LocalNetHelper.getIpName()));
        intent.putExtra("netDevIp", LocalNetHelper.getIp());
        intent.putStringArrayListExtra("imei", (ArrayList<String>) deviceInfo.imeis);
        intent.putStringArrayListExtra("meid", (ArrayList<String>) deviceInfo.meids);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext,
                1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = NotificationHelper.getInstance()
                .getDefBuilder(getString(R.string.app_name),
                        "本机信息",
                        "恭喜您，本机信息已获取成功！",
                        pendingIntent,
                        R.mipmap.ic_launcher,
                        R.mipmap.ic_launcher,
                        mContext.getResources().getColor(android.R.color.holo_red_dark));
        NotificationHelper.getInstance().showNotification(1, builder);
    }

    private void rxJavaTest() {
        Observable
                .create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                        LogUtils.i(TAG, "=========== subscribe ============");
                        LogUtils.i(TAG, "Thread name: " + Thread.currentThread().getName());
                        emitter.onNext("Hello world!");
                        emitter.onNext("This is a test applition!");
                        emitter.onComplete();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        LogUtils.i(TAG, "=========== doOnSubscribe ============");
                        LogUtils.i(TAG, "Thread name: " + Thread.currentThread().getName());
                    }
                })
                .observeOn(Schedulers.newThread())
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        LogUtils.i(TAG, "=========== doOnNext ============");
                        LogUtils.i(TAG, "Thread name: " + Thread.currentThread().getName());
                        LogUtils.i(TAG, s);
                    }
                })
                .observeOn(Schedulers.newThread())
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        LogUtils.i(TAG, "=========== doOnComplete ============");
                        LogUtils.i(TAG, "Thread name: " + Thread.currentThread().getName());
                    }
                })
                .observeOn(Schedulers.single())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        LogUtils.i(TAG, "=========== onSubscribe ============");
                        LogUtils.i(TAG, "Thread name: " + Thread.currentThread().getName());
                    }

                    @Override
                    public void onNext(String s) {
                        LogUtils.i(TAG, "=========== onNext ============");
                        LogUtils.i(TAG, "Thread name: " + Thread.currentThread().getName());
                        LogUtils.i(TAG, s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.i(TAG, "=========== onError ============");
                        LogUtils.i(TAG, "Thread name: " + Thread.currentThread().getName());
                    }

                    @Override
                    public void onComplete() {
                        LogUtils.i(TAG, "=========== onComplete ============");
                        LogUtils.i(TAG, "Thread name: " + Thread.currentThread().getName());
                    }
                });
    }
}
