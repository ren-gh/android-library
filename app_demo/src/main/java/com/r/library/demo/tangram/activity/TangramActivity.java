
package com.r.library.demo.tangram.activity;

import org.json.JSONArray;
import org.json.JSONException;

import com.owen.tvrecyclerview.widget.TvRecyclerView;
import com.r.library.common.util.AnimUtils;
import com.r.library.common.util.FileUtils;
import com.r.library.common.util.LogUtils;
import com.r.library.demo.R;
import com.r.library.demo.tangram.click.CustomCellClick;
import com.r.library.demo.tangram.exposure.CustomCellExposure;
import com.r.library.demo.tangram.model.CustomCell;
import com.r.library.demo.tangram.view.CustomCellView;
import com.r.library.demo.tangram.view.CustomInterfaceView;
import com.tmall.wireless.tangram.TangramBuilder;
import com.tmall.wireless.tangram.TangramEngine;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;

public class TangramActivity extends AppCompatActivity {
    private final String TAG = "TangramActivity";

    private TvRecyclerView mRecyclerView;
    private TangramBuilder.InnerBuilder mBuilder;
    private TangramEngine mEngine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tangram);
        LogUtils.d(TAG, "onCreate()");

        initRecyclerView();
        initTangramObject();
        LogUtils.d(TAG, "Support Rx? " + mEngine.supportRx());
        loadData();
    }

    private void initRecyclerView() {
        mRecyclerView = findViewById(R.id.rv_tangram_demo);
        // item选中、点击监听
        mRecyclerView.setOnItemListener(new TvRecyclerView.OnItemListener() {
            @Override
            public void onItemPreSelected(TvRecyclerView parent, View itemView, int position) {
                // 上次选中
                LogUtils.d(TAG, "onItemPreSelected() pos=" + position);
            }

            @Override
            public void onItemSelected(TvRecyclerView parent, View itemView, int position) {
                // 当前选中
                LogUtils.d(TAG, "onItemSelected() pos=" + position);
            }

            @Override
            public void onItemClick(TvRecyclerView parent, View itemView, int position) {
                // 点击
                LogUtils.d(TAG, "onItemClick() pos=" + position);
            }
        });
        // 焦点移动边界监听
        mRecyclerView.setOnInBorderKeyEventListener(new TvRecyclerView.OnInBorderKeyEventListener() {
            @Override
            public boolean onInBorderKeyEvent(int direction, View focusedView) {
                LogUtils.d(TAG, "onInBorderKeyEvent() direction=" + direction);
                switch (direction) {
                    case View.FOCUS_DOWN:
                        AnimUtils.startShakeYByProperty(focusedView, 0f, 0);
                        break;
                    case View.FOCUS_UP:
                        AnimUtils.startShakeYByProperty(focusedView, 0f, 0);
                        break;
                    case View.FOCUS_LEFT:
                        AnimUtils.startShakeXByProperty(focusedView, 0f, 0);
                        break;
                    case View.FOCUS_RIGHT:
                        AnimUtils.startShakeXByProperty(focusedView, 0f, 0);
                        break;
                }
                // 返回true时,事件将会被拦截由你来控制焦点
                return false;
            }
        });
        // 加载更多监听
        mRecyclerView.setOnLoadMoreListener(new TvRecyclerView.OnLoadMoreListener() {
            @Override
            public boolean onLoadMore() {
                LogUtils.d(TAG, "onLoadMore()");
                mRecyclerView.setLoadingMore(true); // 正在加载数据
                // mLayoutAdapter.appendDatas(); // 加载数据
                mRecyclerView.setLoadingMore(false); // 加载数据完毕
                return true; // 是否还有更多数据
            }
        });
        mRecyclerView.setHasMoreData(true);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        return super.dispatchKeyEvent(event);
    }

    @Override
    protected void onDestroy() {
        mEngine.destroy();
        super.onDestroy();
    }

    private void initTangramObject() {
        mBuilder = TangramBuilder.newInnerBuilder(this);
        mBuilder.registerCell("CustomInterface", CustomInterfaceView.class);
        mBuilder.registerCell("CustomCell", CustomCell.class, CustomCellView.class);
        mEngine = mBuilder.build();
        mEngine.addSimpleClickSupport(new CustomCellClick());
        mEngine.addExposureSupport(new CustomCellExposure());
        mEngine.bindView(mRecyclerView);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mEngine.onScrolled();
            }
        });

        mEngine.getLayoutManager().setFixOffset(0, 0, 0, 0);
        mEngine.setPreLoadNumber(3);
    }

    private void loadData() {
        String json = FileUtils
                .getContent(FileUtils.getAssetsFileInputStream(this, "data.json"));
        JSONArray data = null;
        try {
            data = new JSONArray(json);
            mEngine.setData(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
