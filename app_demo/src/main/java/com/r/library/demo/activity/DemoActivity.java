
package com.r.library.demo.activity;

import java.util.ArrayList;
import java.util.List;

import com.r.library.common.apk.ApkInstaller;
import com.r.library.common.dialog.RDialog;
import com.r.library.common.handler.WeakHandler;
import com.r.library.common.handler.WeakHandlerListener;
import com.r.library.common.net.LocalNetHelper;
import com.r.library.common.notification.NotificationHelper;
import com.r.library.common.player.PlayerActivity;
import com.r.library.common.player.PlayerHelper;
import com.r.library.common.player.PlayerParams;
import com.r.library.common.util.AppInfo;
import com.r.library.common.util.AppInfoGetter;
import com.r.library.common.util.FileUtils;
import com.r.library.common.util.LogUtils;
import com.r.library.common.util.ThreadManager;
import com.r.library.common.util.ToastUtils;
import com.r.library.common.util.UrlUtils;
import com.r.library.demo.R;
import com.r.library.demo.broadcast.NotificationClickReceiver;
import com.r.library.demo.runnable.MyFileRunnable;
import com.r.library.demo.util.BgUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DemoActivity extends AppCompatActivity implements View.OnClickListener, WeakHandlerListener, View.OnFocusChangeListener {
    private final String TAG = "DemoActivity";
    private RxPermissions mRxPermissions;
    private Context mContext;
    private WeakHandler mHandler;
    private View mRootView;
    private TextView mTvInfo;
    private Button mBtnFile;
    private Button mBtnChangeBg;
    private Button mBtnUpdateDemo;
    private Button mBtnInstallApk;
    private Button mBtnLiveActvity;
    private Button mBtnPlayNormal;
    private Button mBtnPlayCover;
    private Button mBtnPlayDouble;
    private Button mBtnPlayFinish;
    private Button mBtnPlayAd;

    private final int RESULT_CODE_PACKAGE_INSTALL_DEMO = 1000;
    private final int RESULT_CODE_PACKAGE_INSTALL_TEST = 1001;

    private String VIDEO_URL_1 = "http://g3com.cp21.ott.cibntv.net/vod/v1/Mjc0LzQ1LzIwL2xldHYtZ3VnLzE3LzExMjMzMzA0NTgtYXZjLTc3Ny1hYWMtNzctMTUwMDAtNjEwNjA1Mi04YWU0ZjQ4MGQ2NGI3MTc4N2NlNDMzZGUxMjNhZTg1MS0xNTU3Nzk5MTcwODc0LnRz?platid=100&splatid=10004&gugtype=1&mmsid=66981094&type=tv_1080p";
    private String VIDEO_URL_2 = "http://g3com.cp21.ott.cibntv.net/vod/v1/Mjc5LzQ1LzEwOC9sZXR2LWd1Zy8xNy8xMTIyODU4NzMyLWF2Yy03NzctYWFjLTc3LTYwMDAwLTI1ODM3MDI4LTYxMDdiY2RhMzBhY2Y5YmMxOTE4OWNjOGE4ZjI1Mzc1LTE1NTI2NDA1MzU1MDgudHM=?platid=100&splatid=10002&gugtype=6&mmsid=66929100&type=tv_1080p";

    private final int MSG_WHAT_UPDATE_BASE_INFO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        LogUtils.i(TAG, "onCreate()");

        mContext = this;
        mHandler = new WeakHandler(this);
        mRxPermissions = new RxPermissions(this);

        mRootView = findViewById(R.id.root);
        BgUtils.autoUpdateBackground(mContext, mRootView);

        mBtnFile = findViewById(R.id.btn_file);
        mBtnChangeBg = findViewById(R.id.btn_change_bg);
        mBtnUpdateDemo = findViewById(R.id.btn_update_own);
        mBtnInstallApk = findViewById(R.id.btn_install_apk);

        mBtnLiveActvity = findViewById(R.id.btn_play_live);
        mBtnPlayNormal = findViewById(R.id.btn_play_normal);
        mBtnPlayCover = findViewById(R.id.btn_play_cover_view);
        mBtnPlayDouble = findViewById(R.id.btn_play_double_click);
        mBtnPlayFinish = findViewById(R.id.btn_play_auto_finish);
        mBtnPlayAd = findViewById(R.id.btn_play_ad_mode);
        mTvInfo = findViewById(R.id.tv_info);

        mBtnFile.setOnClickListener(this);
        mBtnChangeBg.setOnClickListener(this);
        mBtnUpdateDemo.setOnClickListener(this);
        mBtnInstallApk.setOnClickListener(this);

        mBtnLiveActvity.setOnClickListener(this);
        mBtnPlayNormal.setOnClickListener(this);
        mBtnPlayCover.setOnClickListener(this);
        mBtnPlayDouble.setOnClickListener(this);
        mBtnPlayFinish.setOnClickListener(this);
        mBtnPlayAd.setOnClickListener(this);

        mBtnUpdateDemo.setOnFocusChangeListener(this);
        mBtnInstallApk.setOnFocusChangeListener(this);

        Intent intent = getIntent();
        if (null != intent) {
            String action = intent.getAction();
            Uri uri = intent.getData();
            LogUtils.i(TAG, "action: " + action + ", uri: " + uri);
        }

        showDialog();

        rxJavaTest();

        initHideView(mRootView, R.anim.slide_out_fade);
        initHideView(mBtnUpdateDemo, R.anim.slide_out_top);
        initHideView(mBtnInstallApk, R.anim.slide_out_top);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startAnimation(mRootView, R.anim.slide_in_fade);
            }
        }, 1000);

        UrlUtils.UrlEntity entity = UrlUtils.parse(VIDEO_URL_2);
        if (null != entity.params) {
            LogUtils.i(TAG, "mmsid: " + entity.params.get("mmsid"));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.btn_update_own: {
                if (hasFocus) {
                    startAnimation(mBtnUpdateDemo, R.anim.slide_in_top);
                } else {
                    startAnimation(mBtnUpdateDemo, R.anim.slide_out_top);
                }
            }
                break;
            case R.id.btn_install_apk: {
                if (hasFocus) {
                    startAnimation(mBtnInstallApk, R.anim.slide_in_top);
                } else {
                    startAnimation(mBtnInstallApk, R.anim.slide_out_top);
                }
            }
                break;
        }
    }

    @SuppressLint("CheckResult")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_file: {
                ThreadManager.getInstance().excuteCached(new MyFileRunnable(mContext));
            }
                break;
            case R.id.btn_change_bg: {
                BgUtils.nextBackground(mContext, mRootView);
            }
                break;
            case R.id.btn_update_own: {
                // noinspection ResultOfMethodCallIgnored
                checkAndInstallDemo();
            }
                break;
            case R.id.btn_install_apk: {
                checkAndInstallTest();
            }
                break;
            case R.id.btn_play_live: {
                Intent intent = new Intent();
                intent.setClass(mContext, LiveActivity.class);
                startActivity(intent);
            }
                break;
            case R.id.btn_play_normal: {
                mRxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .subscribe(granted -> {
                            PlayerParams params = new PlayerParams();
                            params.setShowLoading(true);
                            params.setVideoTile("乐融LeTV");
                            params.setVideoUri(Uri.parse(VIDEO_URL_1));
                            PlayerHelper.setPlayerParams(params);
                            Intent intent = new Intent();
                            intent.setClass(mContext, PlayerActivity.class);
                            startActivity(intent);
                        });
            }
                break;
            case R.id.btn_play_cover_view: {
                PlayerParams params = new PlayerParams();
                params.setCoverDrawable(BgUtils.getCoverDrawable(mContext));
                params.setVideoTile("轩尼诗");
                params.setVideoUri(Uri.parse(VIDEO_URL_2));
                PlayerHelper.setPlayerParams(params);
                Intent intent = new Intent();
                intent.setClass(mContext, PlayerActivity.class);
                startActivity(intent);
            }
                break;
            case R.id.btn_play_double_click: {
                PlayerParams params = new PlayerParams();
                params.setDoubleClickPause(true);
                params.setShowLoading(true);
                params.setAutoFinishDelay(0);
                params.setVideoTile("轩尼诗");
                params.setVideoUri(Uri.parse(VIDEO_URL_2));
                PlayerHelper.setPlayerParams(params);
                Intent intent = new Intent();
                intent.setClass(mContext, PlayerActivity.class);
                startActivity(intent);
            }
                break;
            case R.id.btn_play_auto_finish: {
                mRxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .subscribe(granted -> {
                            PlayerParams params = new PlayerParams();
                            params.setCoverDrawable(BgUtils.getCoverDrawable(mContext));
                            params.setAdVideo(true);
                            params.setIgnoreBackKey(true);
                            params.setAutoFinish(true);
                            params.setAutoFinishDelay(1000);
                            params.setVideoTile("乐融LeTV");
                            params.setVideoUri(Uri.parse(VIDEO_URL_1));
                            PlayerHelper.setPlayerParams(params);
                            Intent intent = new Intent();
                            intent.setClass(mContext, PlayerActivity.class);
                            startActivity(intent);
                        });
            }
                break;
            case R.id.btn_play_ad_mode: {
                mRxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .subscribe(granted -> {
                            PlayerParams params = new PlayerParams();
                            params.setCoverDrawable(BgUtils.getCoverDrawable(mContext));
                            params.setAdVideo(true);
                            params.setIgnoreBackKey(true);
                            params.setAutoFinish(true);
                            params.setVideoTile("乐融LeTV");
                            params.setVideoUri(Uri.parse(VIDEO_URL_1));
                            PlayerHelper.setPlayerParams(params);
                            Intent intent = new Intent();
                            intent.setClass(mContext, PlayerActivity.class);
                            startActivity(intent);
                        });
            }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RESULT_CODE_PACKAGE_INSTALL_DEMO:
                if (resultCode == RESULT_OK) {
                    checkAndInstallDemo();
                }
                break;
            case RESULT_CODE_PACKAGE_INSTALL_TEST:
                if (resultCode == RESULT_OK) {
                    checkAndInstallTest();
                }
                break;
            default:
                break;
        }
    }

    @SuppressLint("CheckResult")
    private void checkAndInstallDemo() {
        mRxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(granted -> {
                    if (granted) {
                        String path = Environment.getExternalStorageDirectory().getPath() + "/demo.apk";
                        installOrUninstall(path, RESULT_CODE_PACKAGE_INSTALL_DEMO);
                    } else {
                        ToastUtils.showToast(mContext, "没有权限读取SD卡");
                    }
                });
    }

    @SuppressLint("CheckResult")
    private void checkAndInstallTest() {
        // noinspection ResultOfMethodCallIgnored
        mRxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(granted -> {
                    if (granted) {
                        String path = Environment.getExternalStorageDirectory().getPath() + "/test.apk";
                        installOrUninstall(path, RESULT_CODE_PACKAGE_INSTALL_TEST);
                    } else {
                        ToastUtils.showToast(mContext, "没有权限读取SD卡");
                    }
                });
    }

    private void installOrUninstall(String path, int resultCode) {
        LogUtils.d(TAG, "path: " + path);
        if (FileUtils.isExists(path)) {
            if (ApkInstaller.canRequestPackageInstalls(DemoActivity.this, resultCode)) {
                ApkInstaller.installApk(DemoActivity.this, path);
            }
        } else {
            ToastUtils.showToast(mContext, "找不到文件：" + path);
        }
    }

    private void showDialog() {
        final RDialog dialog = new RDialog(this);
        dialog.setButtonNoClick(v -> {
            dialog.dismiss();
        });
        dialog.setButtonYesClick(v -> {
            dialog.dismiss();
            ThreadManager.getInstance().excuteCached(() -> {
                List<AppInfo> appInfos = AppInfoGetter.getAllAppInfo(mContext);
                for (AppInfo appInfo : appInfos) {
                    LogUtils.i(TAG, "name: " + appInfo.getAppName()
                            + ", pkg: " + appInfo.getPackageName()
                            + ", version: " + appInfo.getVersionCode());
                }
            });
            setDeviceInfo();
        });
        dialog.setContent("你好，欢迎使用R开发库！");
        dialog.show();
    }

    @SuppressLint("CheckResult")
    private void setDeviceInfo() {
        // noinspection ResultOfMethodCallIgnored
        mRxPermissions.request(Manifest.permission.READ_PHONE_STATE)
                .subscribe(granted -> {
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
                });
    }

    @Override
    public void process(Message message) {
        switch (message.what) {
            case MSG_WHAT_UPDATE_BASE_INFO: {
            }
                break;
            default: {
            }
                break;
        }
    }

    class DeviceInfo {
        List<String> imeis;
        List<String> meids;
    }

    private void updateInfoView(DeviceInfo deviceInfo) {
        StringBuilder builder;
        builder = new StringBuilder();
        builder.append("Common:").append("\n");
        builder.append("有线：").append(LocalNetHelper.getMac("eth0")).append("\n");
        builder.append("无线：").append(LocalNetHelper.getMac("wlan0")).append("\n");
        builder.append("IP：").append(LocalNetHelper.getIp()).append("\n");
        builder.append("联网网卡：").append(LocalNetHelper.getIpName()).append("\n");
        builder.append("联网网卡MAC：").append(LocalNetHelper.getMac(LocalNetHelper.getIpName())).append("\n");
        builder.append("IMEI：").append((null == deviceInfo.imeis ? "没有权限" : deviceInfo.imeis)).append("\n");
        builder.append("MEID：").append((null == deviceInfo.meids ? "没有权限" : deviceInfo.meids)).append("\n");

        mTvInfo.setText(builder.toString());
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
                .create((ObservableOnSubscribe<String>) emitter -> {
                    LogUtils.i(TAG, "=========== subscribe ============");
                    LogUtils.i(TAG, "Thread name: " + Thread.currentThread().getName());
                    emitter.onNext("Hello world!");
                    emitter.onNext("This is a test applition!");
                    emitter.onComplete();
                })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .doOnSubscribe(disposable -> {
                    LogUtils.i(TAG, "=========== doOnSubscribe ============");
                    LogUtils.i(TAG, "Thread name: " + Thread.currentThread().getName());
                })
                .observeOn(Schedulers.newThread())
                .doOnNext(s -> {
                    LogUtils.i(TAG, "=========== doOnNext ============");
                    LogUtils.i(TAG, "Thread name: " + Thread.currentThread().getName());
                    LogUtils.i(TAG, s);
                })
                .observeOn(Schedulers.newThread())
                .doOnComplete(() -> {
                    LogUtils.i(TAG, "=========== doOnComplete ============");
                    LogUtils.i(TAG, "Thread name: " + Thread.currentThread().getName());
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

    private void initHideView(View view, int animOutId) {
        Animation animation = AnimationUtils.loadAnimation(this, animOutId);
        if (animation != null) {
            animation.setFillAfter(true);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    view.setAlpha(1.0f);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            view.setAlpha(0.0f);
            view.startAnimation(animation);
        }
    }

    private void startAnimation(View view, int animOutId) {
        view.startAnimation(getAnimation(this, animOutId));
    }

    private Animation getAnimation(Context context, int resId) {
        Animation animation = AnimationUtils.loadAnimation(context, resId);
        if (animation != null) {
            animation.setFillAfter(true);
        }
        return animation;
    }

}
