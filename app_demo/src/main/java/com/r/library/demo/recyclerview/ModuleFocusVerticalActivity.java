
package com.r.library.demo.recyclerview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.r.library.common.util.LogUtils;
import com.r.library.common.view.tvrecyclerview.ModuleLayoutManager;
import com.r.library.common.view.tvrecyclerview.SpaceItemDecoration;
import com.r.library.common.view.tvrecyclerview.TvRecyclerView;
import com.r.library.demo.R;

public class ModuleFocusVerticalActivity extends AppCompatActivity {

    private TvRecyclerView mTvRecyclerView;
    public int[] mStartIndex = {
            0, 2, 3, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 20
    };
    public int[] mItemRowSizes = {
            2, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 3
    };
    public int[] mItemColumnSizes = {
            2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 4
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_vertical);
        mTvRecyclerView = findViewById(R.id.tv_recycler_view);
        init();
    }

    private void init() {

        mTvRecyclerView.setSelectedScale(1.0f);

        int colume = 4;
        int width = getWindow().getWindowManager().getDefaultDisplay().getWidth();
        int height = getWindow().getWindowManager().getDefaultDisplay().getHeight();
        int itemSpace = getResources().getDimensionPixelSize(R.dimen.dp_3);
        int sideW = 2 * getResources().getDimensionPixelSize(R.dimen.dp_20);
        int spaceW = (colume - 1) * itemSpace;
        int itemW = (width - sideW - spaceW) / colume;
        int itemH = itemW * 9 / 16;
        LogUtils.d("ModuleFocusVerticalActivity",
                "item w: " + itemW + ", item h: " + itemH);
        ModuleLayoutManager manager = new MyModuleLayoutManager(colume,
                LinearLayoutManager.VERTICAL,
                itemW,
                itemH);
        mTvRecyclerView.setLayoutManager(manager);

        mTvRecyclerView.addItemDecoration(new SpaceItemDecoration(itemSpace));
        ModuleAdapter mAdapter = new ModuleAdapter(ModuleFocusVerticalActivity.this,
                mStartIndex.length);
        mTvRecyclerView.setAdapter(mAdapter);

        mTvRecyclerView.setOnItemStateListener(new TvRecyclerView.OnItemStateListener() {
            @Override
            public void onItemViewClick(View view, int position) {
                Toast.makeText(ModuleFocusVerticalActivity.this,
                        String.valueOf(mStartIndex[position]), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemViewFocusChanged(boolean gainFocus, View view, int position) {
            }
        });
        int itemPadding = getResources().getDimensionPixelSize(R.dimen.dp_1_5);
        mTvRecyclerView.setSelectPadding(itemPadding, itemPadding, itemPadding, itemPadding);
    }

    private class MyModuleLayoutManager extends ModuleLayoutManager {
        MyModuleLayoutManager(int rowCount,
                int orientation,
                int baseItemWidth,
                int baseItemHeight) {
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
