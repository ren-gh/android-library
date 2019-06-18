
package com.r.library.demo.recyclerview;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.view.View;

import com.owen.tvrecyclerview.widget.TvRecyclerView;
import com.owen.tvrecyclerview.widget.V7GridLayoutManager;
import com.r.library.common.util.LogUtils;
import com.r.library.common.util.ToastUtils;
import com.r.library.common.util.UIUtils;
import com.r.library.common.view.tvrecyclerview.SpaceItemDecoration;
import com.r.library.demo.R;

import java.util.ArrayList;
import java.util.List;

public class OWenRecyclerActivity extends AppCompatActivity {
    private final String TAG = "OWenRecyclerActivity";
    private Context mContext;
    private TvRecyclerView mTvRecyclerView;
    private V7GridLayoutManager mLayoutManager;
    private OWenAdapter mOWenAdapter;
    private List<OWenItem> mDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owen_recycler);

        mContext = this;

        mTvRecyclerView = findViewById(R.id.rv_owen_demo);

        mLayoutManager = new V7GridLayoutManager(mContext, 4,
                V7GridLayoutManager.VERTICAL, false);
        mTvRecyclerView.setLayoutManager(mLayoutManager);

        int itemSpace = getResources().getDimensionPixelSize(R.dimen.dp_2);
        mTvRecyclerView.addItemDecoration(new SpaceItemDecoration(itemSpace));
        mTvRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mOWenAdapter = new OWenAdapter(this);
        setListener();
        getMoreData();
        LogUtils.i(TAG, "data size: " + mDataList.size());
        mOWenAdapter.setData(mDataList);
        mTvRecyclerView.setAdapter(mOWenAdapter);
    }

    private void setListener() {
        mTvRecyclerView.setOnLoadMoreListener(new TvRecyclerView.OnLoadMoreListener() {
            @Override
            public boolean onLoadMore() {
                LogUtils.i(TAG, "onLoadMore() Load more...");
                // getMoreData();
                // mOWenAdapter.setData(mDataList);
                // mOWenAdapter.notifyDataSetChanged();
                return true;
            }
        });
        mTvRecyclerView.setOnInBorderKeyEventListener(new TvRecyclerView.OnInBorderKeyEventListener() {
            @Override
            public boolean onInBorderKeyEvent(int direction, View focused) {
                LogUtils.i(TAG, "onInBorderKeyEvent() direction=" + direction + " focused=" + focused.hasFocus());
                return false;
            }
        });
        /**
         * 与 Adapter 的 setOnItemListener 互斥，Adapter 优先
         */
        mTvRecyclerView.setOnItemListener(new TvRecyclerView.OnItemListener() {
            @Override
            public void onItemPreSelected(TvRecyclerView parent, View itemView, int position) {
                LogUtils.i(TAG, "onItemPreSelected() position=" + position);
                UIUtils.scaleView(itemView, 1.0f);
            }

            @Override
            public void onItemSelected(TvRecyclerView parent, View itemView, int position) {
                LogUtils.i(TAG, "onItemSelected() position=" + position);
                UIUtils.scaleView(itemView, 1.1f);
            }

            @Override
            public void onItemClick(TvRecyclerView parent, View itemView, int position) {
                LogUtils.i(TAG, "onItemClick() position=" + position);
                ToastUtils.showToast(mContext, "Clicked pos " + position);
            }
        });
        /**
         * 与 TvRecyclerView 的 setOnItemListener 互斥，Adapter 优先
         */
        mOWenAdapter.setOnItemListener(new OWenAdapter.OnItemListener() {
            @Override
            public void onFocusChanged(View view, boolean hasFocus, int position) {
                LogUtils.i(TAG, "onFocusChanged() hasFocus: " + hasFocus + ", position: " + position);
                if (hasFocus) {
                    UIUtils.scaleView(view, 1.1f);
                } else {
                    UIUtils.scaleView(view, 1.0f);
                }
            }

            @Override
            public void onClick(View view, int position) {
                LogUtils.i(TAG, "onClick() position: " + position);
                ToastUtils.showToast(mContext, "Clicked pos " + position);
            }
        });
    }

    private List getMoreData() {
        if (null == mDataList) {
            mDataList = new ArrayList<>();
        }
        for (int i = 0; i < 60; i++) {
            String url;
            int result = i % 5;
            switch (result) {
                case 0:
                    url = "https://img11.360buyimg.com/babel/s700x360_jfs/t1/61713/22/696/102562/5cee7259E2306aa02/1226f2d6ec447f44.jpg!q90!cc_350x180";
                    break;
                case 1:
                    url = "https://img11.360buyimg.com/babel/s700x360_jfs/t1/63760/18/1902/23514/5d03374eE7f4a7f7b/a5b68f6525d2a2c0.jpg!q90!cc_350x180";
                    break;
                case 2:
                    url = "https://img20.360buyimg.com/babel/s700x360_jfs/t1/45352/36/1604/135803/5cf4ccd7E425556bf/e1cdadbf85fa5cf1.jpg!q90!cc_350x180";
                    break;
                case 3:
                    url = "https://img20.360buyimg.com/babel/s700x360_jfs/t1/36600/13/8797/130882/5ceded14E050346fb/e0e688084a9d1854.jpg!q90!cc_350x180";
                    break;
                case 4:
                    url = "https://img10.360buyimg.com/babel/s700x360_jfs/t1/50968/14/2281/131841/5d030a2cEcf46a671/5421f01555f1b4d3.jpg!q90!cc_350x180";
                    break;
                default:
                    url = "https://img10.360buyimg.com/img/jfs/t1/65477/24/1867/249268/5d03380eE9c52b872/a7a2864e42dde553.gif";
                    break;
            }
            OWenItem item = new OWenItem();
            item.setType("type");
            item.setTitle("测试 " + mDataList.size());
            item.setPicUrl(url);
            item.setParams("其他参数");
            mDataList.add(item);
        }
        return mDataList;
    }

}
