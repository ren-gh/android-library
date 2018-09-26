
package com.rengh.rlibrary.activity;

import com.rengh.library.common.handler.WeakHandler;
import com.rengh.library.common.handler.WeakHandlerListener;
import com.rengh.library.common.util.LogUtils;
import com.rengh.rlibrary.R;
import com.rengh.rlibrary.bean.ItemBean;
import com.rengh.rlibrary.glide.GlideHelper;

import android.content.Context;
import android.graphics.Rect;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AliActivity extends AppCompatActivity implements WeakHandlerListener {
    private final String TAG = "AliActivity";
    private Context mContext;
    private boolean mEatKeyEvent;
    private RecyclerView mRecyclerView;
    private List<ItemBean> mDatas;
    private MyAdapter mAdapter;
    private final int MAX_SPAN_COUNT = 1;

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
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (mEatKeyEvent) {
                LogUtils.i(TAG, "Denied key event...");
                denied = true;
            } else {
                LogUtils.i(TAG, "Allow key event...");
                denied = false;
                mEatKeyEvent = true;
            }
            if (!mWeakHandler.hasMessages(MSG_WHAT_EAT_KEYEVENT)) {
                mWeakHandler.sendEmptyMessageDelayed(MSG_WHAT_EAT_KEYEVENT, 80);
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

    private void onImgBtnFocus(ImageButton imgBtn, boolean hasFocus) {
        if (hasFocus) {
            imgBtn.setBackgroundResource(R.color.colorPrimary);
        } else {
            imgBtn.setBackgroundResource(R.color.colorTrans);
        }
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
        videoBean.video.video2.name = "Video1";
        videoBean.video.video2.url = "https://img.alicdn.com/tfs/TB1ZkoLukCWBuNjy0FaXXXUlXXa-966-644.jpg_970x970q100.jpg_.webp";
        videoBean.video.video3 = new ItemBean.Video.VideoItem();
        videoBean.video.video3.name = "Video1";
        videoBean.video.video3.url = "https://img.alicdn.com/imgextra/i2/1114511827/O1CN011PMo4HVB0dQnaDM_!!1114511827.jpg";
        mDatas.add(videoBean);

        int max = 50;
        int line = max / 3;
        int last = max - 3 * line;
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
            commodityBean.commodity.commodity1.showPrice = "1699.00";
            commodityBean.commodity.commodity1.title = "华为P9全网通";
            commodityBean.commodity.commodity1.whiteBgImage = "https://img.alicdn.com/imgextra/i2/1114511827/O1CN011PMo4HVB0dQnaDM_!!1114511827.jpg";
            if (i < line) {
                commodityBean.commodity.commodity2 = new ItemBean.Commodity.CommodityItem();
                commodityBean.commodity.commodity2.brand = "Huawei";
                commodityBean.commodity.commodity2.itemId = "itemid_" + i;
                commodityBean.commodity.commodity2.showPrice = "1699.00";
                commodityBean.commodity.commodity2.title = "华为P9全网通";
                commodityBean.commodity.commodity2.whiteBgImage = "https://img.alicdn.com/imgextra/i2/1114511827/O1CN011PMo4HVB0dQnaDM_!!1114511827.jpg";
                commodityBean.commodity.commodity3 = new ItemBean.Commodity.CommodityItem();
                commodityBean.commodity.commodity3.brand = "Huawei";
                commodityBean.commodity.commodity3.itemId = "itemid_" + i;
                commodityBean.commodity.commodity3.showPrice = "1699.00";
                commodityBean.commodity.commodity3.title = "华为P9全网通";
                commodityBean.commodity.commodity3.whiteBgImage = "https://img.alicdn.com/imgextra/i2/1114511827/O1CN011PMo4HVB0dQnaDM_!!1114511827.jpg";
            } else if (last == 2) {
                commodityBean.commodity.commodity2 = new ItemBean.Commodity.CommodityItem();
                commodityBean.commodity.commodity2.brand = "Huawei";
                commodityBean.commodity.commodity2.itemId = "itemid_" + i;
                commodityBean.commodity.commodity2.showPrice = "1699.00";
                commodityBean.commodity.commodity2.title = "华为P9全网通";
                commodityBean.commodity.commodity2.whiteBgImage = "https://img.alicdn.com/imgextra/i2/1114511827/O1CN011PMo4HVB0dQnaDM_!!1114511827.jpg";
            } else {
                LogUtils.i(TAG, "last is 1");
            }
            mDatas.add(commodityBean);
        }

        LogUtils.i(TAG, "datas: " + mDatas);
        return mDatas.size() > 0;
    }

    private int mFocusPosition = 0;

    private void initRecyclerView() {
        GridLayoutManager manager = new GridLayoutManager(mContext, MAX_SPAN_COUNT,
                StaggeredGridLayoutManager.VERTICAL, false);
        mRecyclerView.setVisibility(View.VISIBLE);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new MyAdapter(mDatas);
        mAdapter.setOnItemFocusChangeListener(new OnRecyclerItemFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                onFocusChange(view, hasFocus, -1, -1);
            }

            @Override
            public void onFocusChange(View view, boolean hasFocus, int position, int index) {
                LogUtils.i(TAG, "has focus: " + hasFocus + ", " + position + ", " + index);
                onImgBtnFocus((ImageButton) view, hasFocus);
                if (1 == position) {
                    mRecyclerView.smoothScrollToPosition(0);
                } else if (1 < position) {
                    int itemPostion = position;
                    if (mFocusPosition < itemPostion) {
                        mRecyclerView.smoothScrollToPosition(position + 1);
                    } else if (mFocusPosition > itemPostion) {
                        mRecyclerView.smoothScrollToPosition(position - 1);
                    }
                    mFocusPosition = position;
                } else if (0 == position) {
                } else {
                    mFocusPosition = -1;
                }
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
        mRecyclerView.setAdapter(mAdapter);
    }

    interface OnRecyclerItemFocusChangeListener {
        void onFocusChange(View view, boolean hasFocus);

        void onFocusChange(View view, boolean hasFocus, int position, int index);
    }

    interface OnRecyclerItemClickListener {
        void onItemClick(View view, int position);

        void onItemClick(View view, int position, int index);
    }

    interface onRecyclerItemLongClickListener {
        void onItemLongClick(View view, int position);

        void onItemLongClick(View view, int position, int index);
    }

    public class TitleViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        TextView tvTitle;

        public TitleViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.tvTitle = itemView.findViewById(R.id.tv_title);
        }
    }

    public class RedPkgiewHolder extends RecyclerView.ViewHolder {
        View itemView;
        TextView tvTitle;
        ImageButton imgBtn;

        public RedPkgiewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.tvTitle = itemView.findViewById(R.id.tv_title_red_pkg);
            this.imgBtn = itemView.findViewById(R.id.img_btn_red_pkg);
        }
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        TextView tvTitle;
        ImageButton imgBtn1, imgBtn2, imgBtn3;

        public VideoViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.tvTitle = itemView.findViewById(R.id.tv_title_videos);
            this.imgBtn1 = itemView.findViewById(R.id.img_btn_video_1);
            this.imgBtn2 = itemView.findViewById(R.id.img_btn_video_2);
            this.imgBtn3 = itemView.findViewById(R.id.img_btn_video_3);
        }
    }

    public class CommodityViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        TextView tvTitle;
        ImageButton imgBtn1, imgBtn2, imgBtn3;

        public CommodityViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.tvTitle = itemView.findViewById(R.id.tv_title_commodity);
            this.imgBtn1 = itemView.findViewById(R.id.img_btn_commodity_1);
            this.imgBtn2 = itemView.findViewById(R.id.img_btn_commodity_2);
            this.imgBtn3 = itemView.findViewById(R.id.img_btn_commodity_3);
        }
    }

    public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<ItemBean> beans;
        private OnRecyclerItemFocusChangeListener mOnItemFocusChangeListener;// 焦点事件
        private OnRecyclerItemClickListener mOnItemClickListener;// 单击事件
        private onRecyclerItemLongClickListener mOnItemLongClickListener;// 长按事件

        private final int VIEW_TYPE_TITLE = 1;
        private final int VIEW_TYPE_RED_PKG = 2;
        private final int VIEW_TYPE_VIDEO = 3;
        private final int VIEW_TYPE_COMMODITY = 4;

        private final int COUNT_OF_TITLE = 1;
        private final int COUNT_OF_RED_PKG = 1;
        private final int COUNT_OF_VIDEO = 1;

        public final int INDEX_OF_ITEM_1 = 1;
        public final int INDEX_OF_ITEM_2 = 2;
        public final int INDEX_OF_ITEM_3 = 3;

        public MyAdapter(List<ItemBean> beans) {
            this.beans = beans;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder viewHolder = null;
            switch (viewType) {
                case VIEW_TYPE_TITLE: {
                    View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item_title, parent, false);
                    viewHolder = new TitleViewHolder(item);
                }
                    break;
                case VIEW_TYPE_RED_PKG: {
                    View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item_red_pkg, parent, false);
                    viewHolder = new RedPkgiewHolder(item);
                }
                    break;
                case VIEW_TYPE_VIDEO: {
                    View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item_video, parent, false);
                    viewHolder = new VideoViewHolder(item);
                }
                    break;
                case VIEW_TYPE_COMMODITY: {
                    View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item_commodity, parent, false);
                    viewHolder = new CommodityViewHolder(item);
                }
                    break;
            }
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            View.OnFocusChangeListener onFocusChangeListener1 = new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (mOnItemFocusChangeListener != null) {
                        mOnItemFocusChangeListener.onFocusChange(v, hasFocus, position, INDEX_OF_ITEM_1);
                    }
                }
            };
            View.OnFocusChangeListener onFocusChangeListener2 = new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (mOnItemFocusChangeListener != null) {
                        mOnItemFocusChangeListener.onFocusChange(v, hasFocus, position, INDEX_OF_ITEM_2);
                    }
                }
            };
            View.OnFocusChangeListener onFocusChangeListener3 = new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (mOnItemFocusChangeListener != null) {
                        mOnItemFocusChangeListener.onFocusChange(v, hasFocus, position, INDEX_OF_ITEM_3);
                    }
                }
            };
            View.OnClickListener onClickListener1 = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(v, position, INDEX_OF_ITEM_1);
                    }
                }
            };
            View.OnClickListener onClickListener2 = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(v, position, INDEX_OF_ITEM_2);
                    }
                }
            };
            View.OnClickListener onClickListener3 = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(v, position, INDEX_OF_ITEM_3);
                    }
                }
            };
            if (holder instanceof TitleViewHolder) {
                LogUtils.i(TAG, "bind title..." + position + ", bean: " + beans.get(position));
                TextView tvTitle = ((TitleViewHolder) holder).tvTitle;
                String title = beans.get(position).title.title;
                if (TextUtils.isEmpty(title)) {
                    tvTitle.setVisibility(View.GONE);
                } else {
                    tvTitle.setText(title);
                }
            } else if (holder instanceof RedPkgiewHolder) {
                LogUtils.i(TAG, "bind pkg..." + position + ", bean: " + beans.get(position));
                TextView tvTitle = ((RedPkgiewHolder) holder).tvTitle;
                tvTitle.setVisibility(View.INVISIBLE);
                ImageButton imageButton = ((RedPkgiewHolder) holder).imgBtn;
                String picUrl = beans.get(position).redPackage.picUrl;
                if (TextUtils.isEmpty(picUrl)) {
                    imageButton.setImageResource(R.color.colorAccent);
                    ((RedPkgiewHolder) holder).itemView.setVisibility(View.INVISIBLE);
                } else {
                    GlideHelper.load2View(mContext, imageButton, picUrl);
                }
                imageButton.setOnFocusChangeListener(onFocusChangeListener1);
                imageButton.setOnClickListener(onClickListener1);

                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) imageButton.getLayoutParams();
                if (mDatas.size() > 3) {
                    // 有商品
                    params.setMargins(0, 200, 0, 0);
                } else {
                    params.setMargins(0, 324, 0, 0);
                }
                imageButton.setLayoutParams(params);
            } else if (holder instanceof VideoViewHolder) {
                LogUtils.i(TAG, "bind video..." + position + ", bean: " + beans.get(position));
                TextView tvTitle = ((VideoViewHolder) holder).tvTitle;
                tvTitle.setText("精彩视频");
                ImageButton imageButton1 = ((VideoViewHolder) holder).imgBtn1;
                if (null != beans.get(position).video.video1) {
                    String url1 = beans.get(position).video.video1.url;
                    if (TextUtils.isEmpty(url1)) {
                        imageButton1.setImageResource(R.color.colorAccent);
                    } else {
                        GlideHelper.load2View(mContext, imageButton1, url1);
                    }
                    imageButton1.setVisibility(View.VISIBLE);
                    imageButton1.setOnFocusChangeListener(onFocusChangeListener1);
                    imageButton1.setOnClickListener(onClickListener1);
                } else {
                    imageButton1.setVisibility(View.INVISIBLE);
                }
                ImageButton imageButton2 = ((VideoViewHolder) holder).imgBtn2;
                if (null != beans.get(position).video.video2) {
                    String url2 = beans.get(position).video.video2.url;
                    if (TextUtils.isEmpty(url2)) {
                        imageButton2.setImageResource(R.color.colorAccent);
                    } else {
                        GlideHelper.load2View(mContext, imageButton2, url2);
                    }
                    imageButton2.setVisibility(View.VISIBLE);
                    imageButton2.setOnFocusChangeListener(onFocusChangeListener2);
                    imageButton2.setOnClickListener(onClickListener2);
                } else {
                    imageButton2.setVisibility(View.INVISIBLE);
                }
                ImageButton imageButton3 = ((VideoViewHolder) holder).imgBtn3;
                if (null != beans.get(position).video.video3) {
                    String url3 = beans.get(position).video.video3.url;
                    if (TextUtils.isEmpty(url3)) {
                        imageButton3.setImageResource(R.color.colorAccent);
                    } else {
                        GlideHelper.load2View(mContext, imageButton3, url3);
                    }
                    imageButton3.setVisibility(View.VISIBLE);
                    imageButton3.setOnFocusChangeListener(onFocusChangeListener3);
                    imageButton3.setOnClickListener(onClickListener3);
                } else {
                    imageButton3.setVisibility(View.INVISIBLE);
                }
            } else if (holder instanceof CommodityViewHolder) {
                LogUtils.i(TAG, "bind commodity..." + position + ", bean: " + beans.get(position));
                TextView tvTitle = ((CommodityViewHolder) holder).tvTitle;
                if (position == (COUNT_OF_TITLE + COUNT_OF_RED_PKG + COUNT_OF_VIDEO)) {
                    tvTitle.setText("爆款热卖");
                    tvTitle.setVisibility(View.VISIBLE);
                } else {
                    tvTitle.setVisibility(View.GONE);
                }
                ImageButton imageButton1 = ((CommodityViewHolder) holder).imgBtn1;
                if (null != beans.get(position).commodity.commodity1) {
                    String url1 = beans.get(position).commodity.commodity1.whiteBgImage;
                    if (TextUtils.isEmpty(url1)) {
                        imageButton1.setImageResource(R.color.colorAccent);
                    } else {
                        GlideHelper.load2View(mContext, imageButton1, url1);
                    }
                    imageButton1.setVisibility(View.VISIBLE);
                    imageButton1.setOnFocusChangeListener(onFocusChangeListener1);
                    imageButton1.setOnClickListener(onClickListener1);
                } else {
                    imageButton1.setVisibility(View.INVISIBLE);
                }
                ImageButton imageButton2 = ((CommodityViewHolder) holder).imgBtn2;
                if (null != beans.get(position).commodity.commodity2) {
                    String url2 = beans.get(position).commodity.commodity2.whiteBgImage;
                    if (TextUtils.isEmpty(url2)) {
                        imageButton2.setImageResource(R.color.colorAccent);
                    } else {
                        GlideHelper.load2View(mContext, imageButton2, url2);
                    }
                    imageButton2.setVisibility(View.VISIBLE);
                    imageButton2.setOnFocusChangeListener(onFocusChangeListener2);
                    imageButton2.setOnClickListener(onClickListener2);
                } else {
                    imageButton2.setVisibility(View.INVISIBLE);
                }
                ImageButton imageButton3 = ((CommodityViewHolder) holder).imgBtn3;
                if (null != beans.get(position).commodity.commodity3) {
                    String url3 = beans.get(position).commodity.commodity3.whiteBgImage;
                    if (TextUtils.isEmpty(url3)) {
                        imageButton3.setImageResource(R.color.colorAccent);
                    } else {
                        GlideHelper.load2View(mContext, imageButton3, url3);
                    }
                    imageButton3.setVisibility(View.VISIBLE);
                    imageButton3.setOnFocusChangeListener(onFocusChangeListener3);
                    imageButton3.setOnClickListener(onClickListener3);
                } else {
                    imageButton3.setVisibility(View.INVISIBLE);
                }
            }
        }

        @Override
        public int getItemCount() {
            return beans.size();
        }

        @Override
        public int getItemViewType(int position) {
            if (0 == position) {
                return VIEW_TYPE_TITLE;
            } else if (1 == position) {
                return VIEW_TYPE_RED_PKG;
            } else if (2 == position) {
                return VIEW_TYPE_VIDEO;
            } else {
                return VIEW_TYPE_COMMODITY;
            }
        }

        public void setOnItemFocusChangeListener(OnRecyclerItemFocusChangeListener onItemFocusChangeListener) {
            mOnItemFocusChangeListener = onItemFocusChangeListener;
        }

        public void setOnItemClickListener(OnRecyclerItemClickListener onItemClickListener) {
            mOnItemClickListener = onItemClickListener;
        }

        public void setOnItemLongClickListener(onRecyclerItemLongClickListener onItemLongClickListener) {
            mOnItemLongClickListener = onItemLongClickListener;
        }

    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f / spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }
}
