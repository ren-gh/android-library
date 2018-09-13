
package com.rengh.rlibrary.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.rengh.library.common.dialog.RDialog;
import com.rengh.library.common.lan.LanHelper;
import com.rengh.library.common.util.LogUtils;
import com.rengh.rlibrary.R;

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
                String mac = LanHelper.getMac(mContext);
                mTvInfo.setText(mac);
            }
        });
        dialog.setButtonYesClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setContent("你好，欢迎使用R开发库！");
        dialog.show();
    }
}
