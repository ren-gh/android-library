
package com.rengh.rlibrary.activity;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.rengh.library.common.dialog.RDialog;
import com.rengh.library.common.net.LocalNetHelper;
import com.rengh.library.common.notification.NotificationHelper;
import com.rengh.library.common.player.PlayerActivity;
import com.rengh.library.common.player.PlayerActivity2;
import com.rengh.library.common.player.PlayerListener;
import com.rengh.library.common.player.PlayerHelper;
import com.rengh.library.common.player.PlayerParams;
import com.rengh.library.common.util.LogUtils;
import com.rengh.library.common.util.UIUtils;
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

public class DemoActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = "DemoActivity";
    private RxPermissions mRxPermissions;
    private Context mContext;
    private TextView mTvInfo;
    private Button mBtnLiveActvity, mBtnPlayNormal, mBtnPlayCover, mBtnPlayDouble, mBtnPlayFinish, mBtnPlayAd;

    private String VIDEO_URL_1 = "/sdcard/douyin.mp4";
    private String VIDEO_URL_2 = "/sdcard/ad.ts";
    private String VIDEO_URL_3 = "http://g3com.cp21.ott.cibntv.net/vod/v1/Mjc5LzQ1LzEwOC9sZXR2LWd1Zy8xNy8xMTIyODU4NzMyLWF2Yy03NzctYWFjLTc3LTYwMDAwLTI1ODM3MDI4LTYxMDdiY2RhMzBhY2Y5YmMxOTE4OWNjOGE4ZjI1Mzc1LTE1NTI2NDA1MzU1MDgudHM=?platid=100&splatid=10002&gugtype=6&mmsid=66929100&type=tv_1080p";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        mContext = this;
        mRxPermissions = new RxPermissions(this);

        mBtnLiveActvity = findViewById(R.id.btn_play_live);
        mBtnPlayNormal = findViewById(R.id.btn_play_normal);
        mBtnPlayCover = findViewById(R.id.btn_play_cover_view);
        mBtnPlayDouble = findViewById(R.id.btn_play_double_click);
        mBtnPlayFinish = findViewById(R.id.btn_play_auto_finish);
        mBtnPlayAd = findViewById(R.id.btn_play_ad_mode);
        mTvInfo = findViewById(R.id.tv_info);

        mBtnLiveActvity.setOnClickListener(this);
        mBtnPlayNormal.setOnClickListener(this);
        mBtnPlayCover.setOnClickListener(this);
        mBtnPlayDouble.setOnClickListener(this);
        mBtnPlayFinish.setOnClickListener(this);
        mBtnPlayAd.setOnClickListener(this);

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
            case R.id.btn_play_live: {
                Intent intent = new Intent();
                intent.setClass(mContext, LiveActivity.class);
                startActivity(intent);
            }
                break;
            case R.id.btn_play_normal: {
                PlayerParams params = new PlayerParams();
                params.setPlayerListener(null);
                params.setCoverDrawable(null);
                params.setDoubleClick(false);
                params.setAdVideo(false);
                params.setAutoFinish(false);
                params.setShowLoading(true);
                params.setAutoFinishDelay(0);
                params.setVideoTile("测试视频");
                params.setVideoUri(Uri.parse(VIDEO_URL_1));
                PlayerHelper.setPlayerParams(params);
                Intent intent = new Intent();
                intent.setClass(mContext, PlayerActivity2.class);
                startActivity(intent);
            }
                break;
            case R.id.btn_play_cover_view: {
                PlayerParams params = new PlayerParams();
                params.setPlayerListener(null);
                params.setCoverDrawable(getResources().getDrawable(R.drawable.ic_letv_bg));
                params.setDoubleClick(false);
                params.setAdVideo(false);
                params.setAutoFinish(false);
                params.setShowLoading(false);
                params.setAutoFinishDelay(0);
                params.setVideoTile("在线");
                params.setVideoUri(Uri.parse(VIDEO_URL_3));
                PlayerHelper.setPlayerParams(params);
                Intent intent = new Intent();
                intent.setClass(mContext, PlayerActivity2.class);
                startActivity(intent);
            }
                break;
            case R.id.btn_play_double_click: {
                PlayerParams params = new PlayerParams();
                params.setPlayerListener(null);
                params.setCoverDrawable(null);
                params.setDoubleClick(true);
                params.setAdVideo(false);
                params.setAutoFinish(false);
                params.setShowLoading(true);
                params.setAutoFinishDelay(0);
                params.setVideoTile("在线");
                params.setVideoUri(Uri.parse(VIDEO_URL_3));
                PlayerHelper.setPlayerParams(params);
                Intent intent = new Intent();
                intent.setClass(mContext, PlayerActivity2.class);
                startActivity(intent);
            }
                break;
            case R.id.btn_play_auto_finish: {
                PlayerParams params = new PlayerParams();
                params.setPlayerListener(null);
                params.setCoverDrawable(getResources().getDrawable(R.drawable.ic_letv_bg));
                params.setDoubleClick(false);
                params.setAdVideo(true);
                params.setAutoFinish(true);
                params.setShowLoading(false);
                params.setAutoFinishDelay(0);
                params.setVideoTile("本地广告-竖屏");
                params.setVideoUri(Uri.parse(VIDEO_URL_1));
                PlayerHelper.setPlayerParams(params);
                Intent intent = new Intent();
                intent.setClass(mContext, PlayerActivity2.class);
                startActivity(intent);
            }
                break;
            case R.id.btn_play_ad_mode: {
                PlayerParams params = new PlayerParams();
                params.setPlayerListener(null);
                params.setCoverDrawable(getResources().getDrawable(R.drawable.ic_letv_bg));
                params.setDoubleClick(false);
                params.setAdVideo(true);
                params.setAutoFinish(true);
                params.setShowLoading(false);
                params.setAutoFinishDelay(0);
                params.setVideoTile("本地广告-横屏");
                params.setVideoUri(Uri.parse(VIDEO_URL_2));
                PlayerHelper.setPlayerParams(params);
                Intent intent = new Intent();
                intent.setClass(mContext, PlayerActivity2.class);
                startActivity(intent);
            }
                break;
        }
    }

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
                .getDefBuilder("测试",
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
