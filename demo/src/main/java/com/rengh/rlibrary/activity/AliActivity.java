
package com.rengh.rlibrary.activity;

import com.rengh.rlibrary.R;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class AliActivity extends AppCompatActivity implements View.OnFocusChangeListener {
    private final String TAG = "AliActivity";
    private Context mContext;
    private TextView mTvTitle, mTvTitleRedPkg, mTvTitleVideos;
    private ImageButton mImgBtnRedPkg, mImgBtnVideo1, mImgBtnVideo2, mImgBtnVideo3;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ali);

        mContext = this;

        mTvTitle = findViewById(R.id.tv_title);
        mTvTitleRedPkg = findViewById(R.id.tv_title_red_pkg);
        mTvTitleVideos = findViewById(R.id.tv_title_videos);
        mImgBtnRedPkg = findViewById(R.id.img_btn_red_pkg);
        mImgBtnVideo1 = findViewById(R.id.img_btn_video_1);
        mImgBtnVideo2 = findViewById(R.id.img_btn_video_2);
        mImgBtnVideo3 = findViewById(R.id.img_btn_video_3);
        mRecyclerView = findViewById(R.id.rv_items);

        mImgBtnRedPkg.setOnFocusChangeListener(this);
        mImgBtnVideo1.setOnFocusChangeListener(this);
        mImgBtnVideo2.setOnFocusChangeListener(this);
        mImgBtnVideo3.setOnFocusChangeListener(this);

        mRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.img_btn_red_pkg: {
                setImgBtnFocus(mImgBtnRedPkg, hasFocus);
            }
                break;
            case R.id.img_btn_video_1: {
                setImgBtnFocus(mImgBtnVideo1, hasFocus);
            }
                break;
            case R.id.img_btn_video_2: {
                setImgBtnFocus(mImgBtnVideo2, hasFocus);
            }
                break;
            case R.id.img_btn_video_3: {
                setImgBtnFocus(mImgBtnVideo3, hasFocus);
            }
                break;
        }
    }

    private void setImgBtnFocus(ImageButton imgBtn, boolean hasFocus) {
        if (hasFocus) {
            imgBtn.setImageResource(R.color.colorPrimary);
        } else {
            imgBtn.setImageResource(R.color.colorAccent);
        }
    }
}
