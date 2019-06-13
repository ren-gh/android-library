
package com.r.library.demo.recyclerview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.r.library.common.util.ToastUtils;
import com.r.library.common.view.tvrecyclerview.SpaceItemDecoration;
import com.r.library.common.view.tvrecyclerview.TvRecyclerView;
import com.r.library.demo.R;

public class NormalActivity extends AppCompatActivity {
    private final String TAG = "NormalActivity";
    private TvRecyclerView mTvRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);
        mTvRecyclerView = findViewById(R.id.tv_recycler_view);
        TvRecyclerView.openDEBUG();
        init();
    }

    private void init() {
        GridLayoutManager manager = new GridLayoutManager(this, 4);
        manager.setOrientation(GridLayoutManager.HORIZONTAL);

        mTvRecyclerView.setLayoutManager(manager);

        int itemSpace = getResources().getDimensionPixelSize(R.dimen.dp_10);
        mTvRecyclerView.addItemDecoration(new SpaceItemDecoration(itemSpace));
        NormalAdapter mAdapter = new NormalAdapter(this);
        mTvRecyclerView.setAdapter(mAdapter);

        mTvRecyclerView.setOnItemStateListener(new TvRecyclerView.OnItemStateListener() {
            @Override
            public void onItemViewClick(View view, int position) {
                ToastUtils.showToast(NormalActivity.this, ContantUtil.TEST_DATAS[position]);
            }

            @Override
            public void onItemViewFocusChanged(boolean gainFocus, View view, int position) {
            }
        });

        mTvRecyclerView.setOnScrollStateListener(new TvRecyclerView.onScrollStateListener() {
            @Override
            public void onScrollEnd(View view) {
                ToastUtils.showToast(NormalActivity.this, "scroll at end");
            }

            @Override
            public void onScrollStart(View view) {
                ToastUtils.showToast(NormalActivity.this, "scroll at start");
            }
        });

        int width = getResources().getDimensionPixelSize(R.dimen.dp_1_5);
        mTvRecyclerView.setSelectPadding(width, width, width, width);
    }
}
