
package com.rengh.rlibrary.custom;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rengh.library.common.handler.WeakHandler;
import com.rengh.library.common.util.LogUtils;
import com.rengh.rlibrary.R;
import com.rengh.rlibrary.bean.ItemBean;
import com.rengh.rlibrary.view.AliButtonCommodity;
import com.rengh.rlibrary.view.AliButtonRedPkg;
import com.rengh.rlibrary.view.AliButtonVideo;

import java.util.List;

public class AliAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final String TAG = "AliAdapter";
    private WeakHandler mWeakHandler;

    private List<ItemBean> mBeans;
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
    public final int INDEX_OF_ITEM_4 = 4;

    public AliAdapter(List<ItemBean> beans, WeakHandler weakHandler) {
        this.mBeans = beans;
        this.mWeakHandler = weakHandler;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case VIEW_TYPE_TITLE: {
                View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item_title, parent, false);
                viewHolder = new HodlerHelper.TitleViewHolder(item);
            }
                break;
            case VIEW_TYPE_RED_PKG: {
                View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item_red_pkg, parent, false);
                viewHolder = new HodlerHelper.RedPkgiewHolder(item);
            }
                break;
            case VIEW_TYPE_VIDEO: {
                View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item_video, parent, false);
                viewHolder = new HodlerHelper.VideoViewHolder(item);
            }
                break;
            case VIEW_TYPE_COMMODITY: {
                View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item_commodity, parent, false);
                viewHolder = new HodlerHelper.CommodityViewHolder(item);
            }
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof HodlerHelper.TitleViewHolder) {
            LogUtils.i(TAG, "bind title..." + position + ", bean: " + mBeans.get(position));
            mWeakHandler.post(new Runnable() {
                @Override
                public void run() {
                    TextView tvTitle = ((HodlerHelper.TitleViewHolder) holder).tvTitle;
                    String title = mBeans.get(position).title.title;
                    if (TextUtils.isEmpty(title)) {
                        tvTitle.setVisibility(View.GONE);
                    } else {
                        tvTitle.setText(title);
                    }
                }
            });
        } else if (holder instanceof HodlerHelper.RedPkgiewHolder) {
            LogUtils.i(TAG, "bind pkg..." + position + ", bean: " + mBeans.get(position));
            mWeakHandler.post(new Runnable() {
                @Override
                public void run() {
                    TextView tvTitle = ((HodlerHelper.RedPkgiewHolder) holder).tvTitle;
                    tvTitle.setVisibility(View.INVISIBLE);
                    AliButtonRedPkg imageButton = ((HodlerHelper.RedPkgiewHolder) holder).imgBtn;
                    String picUrl = mBeans.get(position).redPackage.picUrl;
                    if (TextUtils.isEmpty(picUrl)) {
                        imageButton.setImageResource(R.color.colorTrans);
                        ((HodlerHelper.RedPkgiewHolder) holder).itemView.setVisibility(View.INVISIBLE);
                    } else {
                        imageButton.setPic(picUrl);
                    }
                    imageButton.setOnFocusChangeListener(getFocusChangeListener(position, INDEX_OF_ITEM_1));
                    imageButton.setOnClickListener(getOnClickListener(position, INDEX_OF_ITEM_1));
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) imageButton.getLayoutParams();
                    if (mBeans.size() > 3) {
                        params.setMargins(0, 200, 0, 0);
                    } else {
                        params.setMargins(0, 324, 0, 0);
                    }
                    imageButton.setLayoutParams(params);
                }
            });
        } else if (holder instanceof HodlerHelper.VideoViewHolder) {
            LogUtils.i(TAG, "bind video..." + position + ", bean: " + mBeans.get(position));
            mWeakHandler.post(new Runnable() {
                @Override
                public void run() {
                    setVideoView(((HodlerHelper.VideoViewHolder) holder).imgBtn1, mBeans.get(position).video.video1,
                            getFocusChangeListener(position, INDEX_OF_ITEM_1), getOnClickListener(position, INDEX_OF_ITEM_1));
                    setVideoView(((HodlerHelper.VideoViewHolder) holder).imgBtn2, mBeans.get(position).video.video2,
                            getFocusChangeListener(position, INDEX_OF_ITEM_2), getOnClickListener(position, INDEX_OF_ITEM_2));
                    setVideoView(((HodlerHelper.VideoViewHolder) holder).imgBtn3, mBeans.get(position).video.video3,
                            getFocusChangeListener(position, INDEX_OF_ITEM_3), getOnClickListener(position, INDEX_OF_ITEM_3));
                }
            });
        } else if (holder instanceof HodlerHelper.CommodityViewHolder) {
            LogUtils.i(TAG, "bind commodity..." + position + ", bean: " + mBeans.get(position));
            mWeakHandler.post(new Runnable() {
                @Override
                public void run() {
                    TextView tvTitle = ((HodlerHelper.CommodityViewHolder) holder).tvTitle;
                    if (position == (COUNT_OF_TITLE + COUNT_OF_RED_PKG + COUNT_OF_VIDEO)) {
                        tvTitle.setVisibility(View.VISIBLE);
                    } else {
                        tvTitle.setVisibility(View.GONE);
                    }
                    setCommodityView(((HodlerHelper.CommodityViewHolder) holder).imgBtn1, mBeans.get(position).commodity.commodity1,
                            getFocusChangeListener(position, INDEX_OF_ITEM_2), getOnClickListener(position, INDEX_OF_ITEM_1));
                    setCommodityView(((HodlerHelper.CommodityViewHolder) holder).imgBtn2, mBeans.get(position).commodity.commodity2,
                            getFocusChangeListener(position, INDEX_OF_ITEM_2), getOnClickListener(position, INDEX_OF_ITEM_2));
                    setCommodityView(((HodlerHelper.CommodityViewHolder) holder).imgBtn3, mBeans.get(position).commodity.commodity3,
                            getFocusChangeListener(position, INDEX_OF_ITEM_2), getOnClickListener(position, INDEX_OF_ITEM_3));
                    setCommodityView(((HodlerHelper.CommodityViewHolder) holder).imgBtn4, mBeans.get(position).commodity.commodity4,
                            getFocusChangeListener(position, INDEX_OF_ITEM_2), getOnClickListener(position, INDEX_OF_ITEM_4));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mBeans.size();
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

    private void setVideoView(AliButtonVideo btn,
            ItemBean.Video.VideoItem item,
            View.OnFocusChangeListener onFocusChangeListener,
            View.OnClickListener onClickListener) {
        if (null != item) {
            if (TextUtils.isEmpty(item.url)) {
                btn.setImageResource(R.color.colorTrans);
            } else {
                btn.setPic(item.url);
            }
            btn.setVisibility(View.VISIBLE);
            btn.setOnFocusChangeListener(onFocusChangeListener);
            btn.setOnClickListener(onClickListener);
            if (TextUtils.isEmpty(item.name)) {
                btn.setName("");
            } else {
                btn.setName(item.name);
            }
        } else {
            btn.setVisibility(View.INVISIBLE);
        }
    }

    private void setCommodityView(AliButtonCommodity btn,
            ItemBean.Commodity.CommodityItem item,
            View.OnFocusChangeListener onFocusChangeListener,
            View.OnClickListener onClickListener) {
        if (null != item) {
            if (TextUtils.isEmpty(item.whiteBgImage)) {
                btn.setImageResource(R.color.colorTrans);
            } else {
                btn.setPic(item.whiteBgImage);
            }
            btn.setVisibility(View.VISIBLE);
            btn.setOnFocusChangeListener(onFocusChangeListener);
            btn.setOnClickListener(onClickListener);
            btn.setBrand(item.brand);
            btn.setTitle(item.title);
            btn.setPric(item.showPrice);
        } else {
            btn.setVisibility(View.INVISIBLE);
        }
    }

    private View.OnFocusChangeListener getFocusChangeListener(final int position, final int index) {
        View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (mOnItemFocusChangeListener != null) {
                    mOnItemFocusChangeListener.onFocusChange(v, hasFocus, position, index);
                }
            }
        };
        return onFocusChangeListener;
    }

    private View.OnClickListener getOnClickListener(final int position, final int index) {
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(v, position, index);
                }
            }
        };
        return onClickListener;
    }

}
