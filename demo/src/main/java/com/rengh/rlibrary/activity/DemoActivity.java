
package com.rengh.rlibrary.activity;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.rengh.library.common.dialog.RDialog;
import com.rengh.library.common.net.LocalNetHelper;
import com.rengh.library.common.notification.NotificationHelper;
import com.rengh.library.common.util.LogUtils;
import com.rengh.rlibrary.R;
import com.rengh.rlibrary.broadcast.NotificationClickReceiver;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;

public class DemoActivity extends AppCompatActivity {
    private Context mContext;
    private TextView mTvInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        mContext = this;

        mTvInfo = findViewById(R.id.tv_info);

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
                RxPermissions.getInstance(mContext)
                        .request(Manifest.permission.READ_PHONE_STATE)
                        .subscribe(new Action1<Boolean>() {
                            @Override
                            public void call(Boolean granted) {
                                List<String> imeis;
                                List<String> meids;
                                if (granted) {
                                    imeis = LocalNetHelper.getIMEI(mContext);
                                    meids = LocalNetHelper.getMEID(mContext);
                                } else {
                                    imeis = null;
                                    meids = null;
                                }
                                mTvInfo.setText("有线：" + LocalNetHelper.getMac("eth0") + "\n"
                                        + "无线：" + LocalNetHelper.getMac("wlan0") + "\n"
                                        + "IP: " + LocalNetHelper.getIp() + "\n"
                                        + "联网网卡: " + LocalNetHelper.getIpName() + "\n"
                                        + "联网网卡MAC: " + LocalNetHelper.getMac(LocalNetHelper.getIpName()) + "\n"
                                        + "IMEI: " + (null == imeis ? "没有权限" : imeis) + "\n"
                                        + "MEID: " + (null == meids ? "没有权限" : meids) + "\n");

                                Intent intent = new Intent(mContext, NotificationClickReceiver.class);
                                intent.putExtra("notificationId", 1);
                                intent.putExtra("eth0Mac", LocalNetHelper.getMac("eth0"));
                                intent.putExtra("wlan0Mac", LocalNetHelper.getMac("wlan0"));
                                intent.putExtra("netDev", LocalNetHelper.getIpName());
                                intent.putExtra("netDevMac", LocalNetHelper.getMac(LocalNetHelper.getIpName()));
                                intent.putExtra("netDevIp", LocalNetHelper.getIpName());
                                intent.putStringArrayListExtra("imei", (ArrayList<String>) imeis);
                                intent.putStringArrayListExtra("meid", (ArrayList<String>) meids);
                                PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext,
                                        1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                                NotificationCompat.Builder builder = NotificationHelper.getInstance()
                                        .getDefBuilder(getString(R.string.app_name),
                                                "本机信息",
                                                "恭喜您，本机信息已获取成功！",
                                                pendingIntent,
                                                R.mipmap.ic_launcher,
                                                R.mipmap.ic_launcher,
                                                mContext.getResources().getColor(R.color.colorRed));
                                NotificationHelper.getInstance().showNotification(1, builder);
                            }
                        });
            }
        });
        dialog.setContent("你好，欢迎使用R开发库！");
        dialog.show();
    }
}
