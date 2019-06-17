
package com.r.library.demo.recyclerview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.r.library.common.view.tvrecyclerview.ModuleLayoutManager;
import com.r.library.common.view.tvrecyclerview.SpaceItemDecoration;
import com.r.library.common.view.tvrecyclerview.TvRecyclerView;
import com.r.library.demo.R;

public class ModuleFocusVerticalActivity extends AppCompatActivity {

    private TvRecyclerView mTvRecyclerView;
    public int[] mStartIndex = {
            0, 2, 3, 6, 7, 8, 9, 10, 11, 12, 13, 14, 16, 17, 20, 22, 23
    };
    public int[] mItemRowSizes = {
            2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1
    };
    public int[] mItemColumnSizes = {
            2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 2, 1, 1
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_vertical);
        mTvRecyclerView = findViewById(R.id.tv_recycler_view);
        init();
    }

    private void init() {
        int max = 40;
        mStartIndex = new int[max];
        mItemRowSizes = new int[max];
        mItemColumnSizes = new int[max];
        for (int i = 0; i < max; i++) {
            mStartIndex[i] = i;
            mItemRowSizes[i] = 1;
            mItemColumnSizes[i] = 1;
        }

        mTvRecyclerView.setSelectedScale(1.1f);

        ModuleLayoutManager manager = new MyModuleLayoutManager(4, LinearLayoutManager.VERTICAL,
                400, 260);
        mTvRecyclerView.setLayoutManager(manager);

        int itemSpace = getResources().getDimensionPixelSize(R.dimen.dp_3);
        mTvRecyclerView.addItemDecoration(new SpaceItemDecoration(itemSpace));
        ModuleAdapter mAdapter = new ModuleAdapter(ModuleFocusVerticalActivity.this, mStartIndex.length);
        mTvRecyclerView.setAdapter(mAdapter);

        mTvRecyclerView.setOnItemStateListener(new TvRecyclerView.OnItemStateListener() {
            @Override
            public void onItemViewClick(View view, int position) {
                Toast.makeText(ModuleFocusVerticalActivity.this,
                        ContantUtil.TEST_DATAS[position], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemViewFocusChanged(boolean gainFocus, View view, int position) {
            }
        });
        int width = getResources().getDimensionPixelSize(R.dimen.dp_1_5);
        mTvRecyclerView.setSelectPadding(width, width, width, width);
        // mTvRecyclerView.setSelectPadding(35, 34, 35, 38);
    }

    private class MyModuleLayoutManager extends ModuleLayoutManager {

        MyModuleLayoutManager(int rowCount, int orientation, int baseItemWidth, int baseItemHeight) {
            super(rowCount, orientation, baseItemWidth, baseItemHeight);
        }

        @Override
        protected int getItemStartIndex(int position) {
            if (position < mStartIndex.length) {
                return mStartIndex[position];
            } else {
                return 0;
            }
        }

        @Override
        protected int getItemRowSize(int position) {
            if (position < mItemRowSizes.length) {
                return mItemRowSizes[position];
            } else {
                return 1;
            }
        }

        @Override
        protected int getItemColumnSize(int position) {
            if (position < mItemColumnSizes.length) {
                return mItemColumnSizes[position];
            } else {
                return 1;
            }
        }

        @Override
        protected int getColumnSpacing() {
            return getResources().getDimensionPixelSize(R.dimen.dp_10);
        }

        @Override
        protected int getRowSpacing() {
            return getResources().getDimensionPixelSize(R.dimen.dp_10);
        }
    }
}
