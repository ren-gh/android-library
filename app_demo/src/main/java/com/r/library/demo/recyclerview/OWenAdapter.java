
package com.r.library.demo.recyclerview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.r.library.common.util.LogUtils;
import com.r.library.common.util.ToastUtils;
import com.r.library.common.util.UIUtils;
import com.r.library.demo.R;

import java.util.List;

public class OWenAdapter extends RecyclerView.Adapter {
    private String TAG = "OWenAdapter";
    private Context mContext;
    private List<OWenItem> mDataList;

    public OWenAdapter(Context context) {
        mContext = context;
    }

    public void setData(List list) {
        mDataList = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LogUtils.i(TAG, "onCreateViewHolder() pos=" + i);
        View view = View.inflate(mContext, R.layout.item_owen_recycler, null);
        return new OWenAdapter.RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        LogUtils.i(TAG, "onBindViewHolder() pos=" + i);
        RecyclerViewHolder holder = (RecyclerViewHolder) viewHolder;

        // holder.onBind(i);
        // holder.setFocusable(true);
        // holder.setClickable(true);
        holder.setImageUrl(mDataList.get(i).getPicUrl());
        holder.setTitle(mDataList.get(i).getTitle());
        // holder.enableOnFocusChangeListener(true);
        // holder.enableOnClickListener(true);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    private class RecyclerViewHolder extends RecyclerView.ViewHolder {
        View mItemView;
        ImageView mImgPic;
        TextView mTitle;
        int mPosition;

        protected RecyclerViewHolder(View itemView) {
            super(itemView);
            mItemView = itemView;
            mImgPic = mItemView.findViewById(R.id.img_pic);
            mTitle = mItemView.findViewById(R.id.tv_title);
        }

        public void onBind(int position) {
            mPosition = position;
        }

        public void setFocusable(boolean focusable) {
            mItemView.setFocusable(focusable);
        }

        public void setClickable(boolean clickable) {
            mItemView.setClickable(clickable);
        }

        public void enableOnFocusChangeListener(boolean enable) {
            if (enable) {
                setFocusable(true);
                mItemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus) {
                            UIUtils.scaleView(v, 1.1f);
                        } else {
                            UIUtils.scaleView(v, 1.0f);
                        }
                    }
                });
            } else {
                mItemView.setOnFocusChangeListener(null);
            }
        }

        public void enableOnClickListener(boolean enable) {
            if (enable) {
                setClickable(true);
                mItemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtils.showToast(mContext, "Clicked pos " + mPosition);
                    }
                });
            } else {
                mItemView.setOnClickListener(null);
            }
        }

        public void setImageUrl(String url) {
            Glide.with(mContext)
                    .load(url)
                    .placeholder(R.drawable.pic_default)
                    .into(mImgPic);
        }

        public void setTitle(String text) {
            mTitle.setText(text);
        }
    }
}
