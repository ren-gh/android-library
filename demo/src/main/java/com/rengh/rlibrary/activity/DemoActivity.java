
package com.rengh.rlibrary.activity;

import android.Manifest;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.rengh.library.common.dialog.RDialog;
import com.rengh.library.common.net.LocalNetHelper;
import com.rengh.library.common.util.LogUtils;
import com.rengh.rlibrary.R;
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

        LogUtils.setAutoSave(false);
        LogUtils.setRootTag("RLibraryDemo");

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
                            }
                        });
            }
        });
        dialog.setContent("你好，欢迎使用R开发库！");
        dialog.show();
    }
}
