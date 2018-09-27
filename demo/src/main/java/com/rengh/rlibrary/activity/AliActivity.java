
package com.rengh.rlibrary.activity;

import com.rengh.library.common.handler.WeakHandler;
import com.rengh.library.common.handler.WeakHandlerListener;
import com.rengh.library.common.util.LogUtils;
import com.rengh.library.common.util.UIUtils;
import com.rengh.rlibrary.R;
import com.rengh.rlibrary.bean.ItemBean;
import com.rengh.rlibrary.custom.AliAdapter;
import com.rengh.rlibrary.custom.OnRecyclerItemClickListener;
import com.rengh.rlibrary.custom.OnRecyclerItemFocusChangeListener;
import com.rengh.rlibrary.custom.onRecyclerItemLongClickListener;
import com.rengh.rlibrary.view.AliButtonCommodity;

import android.content.Context;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class AliActivity extends AppCompatActivity implements WeakHandlerListener {
    private final String TAG = "AliActivity";
    private Context mContext;
    private boolean mEatKeyEvent;
    private RecyclerView mRecyclerView;
    private List<ItemBean> mDatas;
    private AliAdapter mAdapter;
    private final int MAX_SPAN_COUNT = 1;
    private final int COUNT_OF_COMMODITY = 4;
    private final float SCROLL_SPEED = 60.0f;
    private final long SCROLL_DELAY = (long) (SCROLL_SPEED * 5f);
    private int mFocusPosition = 0;

    private final int MSG_WHAT_EAT_KEYEVENT = 1;

    private WeakHandler mWeakHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ali);

        mContext = this;
        mWeakHandler = new WeakHandler(this);

        mRecyclerView = findViewById(R.id.rv_items);

        if (initData()) {
            initRecyclerView();
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK || event.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER) {
            return super.dispatchKeyEvent(event);
        }
        boolean denied = false;
        if (SCROLL_DELAY > 0) {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (mEatKeyEvent) {
                    denied = true;
                } else {
                    denied = false;
                    mEatKeyEvent = true;
                }
                if (!mWeakHandler.hasMessages(MSG_WHAT_EAT_KEYEVENT)) {
                    mWeakHandler.sendEmptyMessageDelayed(MSG_WHAT_EAT_KEYEVENT, SCROLL_DELAY);
                }
            } else if (event.getAction() == KeyEvent.ACTION_UP) {
                if (mWeakHandler.hasMessages(MSG_WHAT_EAT_KEYEVENT)) {
                    mEatKeyEvent = false;
                    mWeakHandler.removeMessages(MSG_WHAT_EAT_KEYEVENT);
                }
            }
        }
        return denied ? true : super.dispatchKeyEvent(event);
    }

    @Override
    public void process(Message msg) {
        switch (msg.what) {
            case MSG_WHAT_EAT_KEYEVENT: {
                mEatKeyEvent = false;
            }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            // if (mFocusPosition > 2) {
            // return true;
            // }
        }
        return super.onKeyDown(keyCode, event);
    }

    // 初始化数据
    protected boolean initData() {
        if (mDatas == null) {
            mDatas = new ArrayList<>();
        }
        mDatas.clear();

        ItemBean titleBean = new ItemBean();
        titleBean.title = new ItemBean.Title();
        titleBean.title.title = null;
        mDatas.add(titleBean);

        ItemBean redPkgBean = new ItemBean();
        redPkgBean.redPackage = new ItemBean.RedPackage();
        redPkgBean.redPackage.activityCode = "22222";
        redPkgBean.redPackage.pid = "12345";
        redPkgBean.redPackage.picUrl = "https://img.alicdn.com/imgextra/i2/1943402959/O1CN011XjGQ5BihuPlDD3_!!1943402959.gif";
        mDatas.add(redPkgBean);

        ItemBean videoBean = new ItemBean();
        videoBean.video = new ItemBean.Video();
        videoBean.video.video1 = new ItemBean.Video.VideoItem();
        videoBean.video.video1.name = "Video1";
        videoBean.video.video1.url = "https://img.alicdn.com/tps/i4/TB17e0FXXzqK1RjSZFCSuvbxVXa.jpg_970x970q100.jpg_.webp";
        videoBean.video.video2 = new ItemBean.Video.VideoItem();
        videoBean.video.video2.name = "Video2";
        videoBean.video.video2.url = "https://img.alicdn.com/tfs/TB1ZkoLukCWBuNjy0FaXXXUlXXa-966-644.jpg_970x970q100.jpg_.webp";
        videoBean.video.video3 = new ItemBean.Video.VideoItem();
        videoBean.video.video3.name = "Video3";
        videoBean.video.video3.url = "https://img.alicdn.com/imgextra/i2/1114511827/O1CN011PMo4HVB0dQnaDM_!!1114511827.jpg";
        mDatas.add(videoBean);

        int max = 30;
        int line = max / COUNT_OF_COMMODITY;
        int last = max - COUNT_OF_COMMODITY * line;
        LogUtils.i(TAG, "max: " + max + ", line: " + line + ", last: " + last);

        int realLine = line;
        if (last != 0) {
            realLine = line + 1;
        }
        for (int i = 0; i < realLine; i++) {
            ItemBean commodityBean = new ItemBean();
            commodityBean.commodity = new ItemBean.Commodity();
            commodityBean.commodity.commodity1 = new ItemBean.Commodity.CommodityItem();
            commodityBean.commodity.commodity1.brand = "Huawei";
            commodityBean.commodity.commodity1.itemId = "itemid_" + i;
            commodityBean.commodity.commodity1.showPrice = "169";
            commodityBean.commodity.commodity1.title = "华为P9全网通";
            commodityBean.commodity.commodity1.whiteBgImage = "https://img.alicdn.com/imgextra/i2/1114511827/O1CN011PMo4HVB0dQnaDM_!!1114511827.jpg";
            if (i < line) {
                commodityBean.commodity.commodity2 = new ItemBean.Commodity.CommodityItem();
                commodityBean.commodity.commodity2.brand = "Huawei";
                commodityBean.commodity.commodity2.itemId = "itemid_" + i;
                commodityBean.commodity.commodity2.showPrice = "1699.00";
                commodityBean.commodity.commodity2.title = "华为P9全网通全网通全网通全网通全网通";
                commodityBean.commodity.commodity2.whiteBgImage = "https://img.alicdn.com/imgextra/i2/1114511827/O1CN011PMo4HVB0dQnaDM_!!1114511827.jpg";
                commodityBean.commodity.commodity3 = new ItemBean.Commodity.CommodityItem();
                commodityBean.commodity.commodity3.brand = "Huawei";
                commodityBean.commodity.commodity3.itemId = "itemid_" + i;
                commodityBean.commodity.commodity3.showPrice = "161239.9";
                commodityBean.commodity.commodity3.title = "华为P9全网通全网通全网通全网通全网通";
                commodityBean.commodity.commodity3.whiteBgImage = "https://img.alicdn.com/imgextra/i2/1114511827/O1CN011PMo4HVB0dQnaDM_!!1114511827.jpg";
                commodityBean.commodity.commodity4 = new ItemBean.Commodity.CommodityItem();
                commodityBean.commodity.commodity4.brand = "Huawei";
                commodityBean.commodity.commodity4.itemId = "itemid_" + i;
                commodityBean.commodity.commodity4.showPrice = "1608899.00";
                commodityBean.commodity.commodity4.title = "华为P9全网通全网通全网通全网通全网通全网通";
                commodityBean.commodity.commodity4.whiteBgImage = "https://img.alicdn.com/imgextra/i2/1114511827/O1CN011PMo4HVB0dQnaDM_!!1114511827.jpg";
            } else if (last == 2) {
                commodityBean.commodity.commodity2 = new ItemBean.Commodity.CommodityItem();
                commodityBean.commodity.commodity2.brand = "Huawei";
                commodityBean.commodity.commodity2.itemId = "itemid_" + i;
                commodityBean.commodity.commodity2.showPrice = "169999.00";
                commodityBean.commodity.commodity2.title = "华为P9全网全网通全网通全网通通";
                commodityBean.commodity.commodity2.whiteBgImage = "https://img.alicdn.com/imgextra/i2/1114511827/O1CN011PMo4HVB0dQnaDM_!!1114511827.jpg";
            } else if (last == 3) {
                commodityBean.commodity.commodity2 = new ItemBean.Commodity.CommodityItem();
                commodityBean.commodity.commodity2.brand = "Huawei";
                commodityBean.commodity.commodity2.itemId = "itemid_" + i;
                commodityBean.commodity.commodity2.showPrice = "169009.00";
                commodityBean.commodity.commodity2.title = "华为P9全网全网通全网通全网通通";
                commodityBean.commodity.commodity2.whiteBgImage = "https://img.alicdn.com/imgextra/i2/1114511827/O1CN011PMo4HVB0dQnaDM_!!1114511827.jpg";
                commodityBean.commodity.commodity3 = new ItemBean.Commodity.CommodityItem();
                commodityBean.commodity.commodity3.brand = "Huawei";
                commodityBean.commodity.commodity3.itemId = "itemid_" + i;
                commodityBean.commodity.commodity3.showPrice = "1699.00";
                commodityBean.commodity.commodity3.title = "华为P9全网通全网通全网通全网通全网通全网通";
                commodityBean.commodity.commodity3.whiteBgImage = "https://img.alicdn.com/imgextra/i2/1114511827/O1CN011PMo4HVB0dQnaDM_!!1114511827.jpg";
            } else {
                LogUtils.i(TAG, "last is 1");
            }
            mDatas.add(commodityBean);
        }

        LogUtils.i(TAG, "datas: " + mDatas);
        return mDatas.size() > 0;
    }

    private void initRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(mContext) {
            @Override
            public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
                LinearSmoothScroller smoothScroller = new LinearSmoothScroller(recyclerView.getContext()) {
                    @Override
                    protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                        return SCROLL_SPEED / displayMetrics.densityDpi;
                    }
                };
                smoothScroller.setTargetPosition(position);
                startSmoothScroll(smoothScroller);
            }
        };
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        manager.setInitialPrefetchItemCount(MAX_SPAN_COUNT);
        mRecyclerView.setVisibility(View.VISIBLE);
        mRecyclerView.setLayoutManager(manager);
        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
        mRecyclerView.setItemAnimator(defaultItemAnimator);
        mRecyclerView.setItemViewCacheSize(2);

        mRecyclerView.setAdapter(getAliAdapter());
    }

    private AliAdapter getAliAdapter() {
        if (null == mAdapter) {
            mAdapter = new AliAdapter(mDatas);
            mAdapter.setOnItemFocusChangeListener(new OnRecyclerItemFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {
                    onFocusChange(view, hasFocus, -1, -1);
                }

                @Override
                public void onFocusChange(View view, boolean hasFocus, int position, int index) {
                    if (position > 2) {
                        ((AliButtonCommodity) view).setScroll(hasFocus);
                    }
                    if (hasFocus) {
                        UIUtils.scaleView(view, 1.1f);
                    } else {
                        UIUtils.scaleView(view, 1.0f);
                    }
                    int offsetPosition = position;
                    if (3 <= position) {
                        if (mFocusPosition > position) {
                            offsetPosition = position - 1;
                        }
                        if (mFocusPosition < position) {
                            offsetPosition = position + 1;
                        }
                    }
                    mFocusPosition = position;
                    mRecyclerView.smoothScrollToPosition(offsetPosition);
                }
            });
            mAdapter.setOnItemClickListener(new OnRecyclerItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    onItemClick(view, position, -1);
                }

                @Override
                public void onItemClick(View view, int position, int index) {
                    LogUtils.i(TAG, "onItemClick: " + position + ", " + index);
                }
            });
            mAdapter.setOnItemLongClickListener(new onRecyclerItemLongClickListener() {
                @Override
                public void onItemLongClick(View view, int position) {
                    onItemLongClick(view, position, -1);
                }

                @Override
                public void onItemLongClick(View view, int position, int index) {
                    LogUtils.i(TAG, "onItemClick: " + position + ", " + index);
                }
            });
        }
        return mAdapter;
    }

}
