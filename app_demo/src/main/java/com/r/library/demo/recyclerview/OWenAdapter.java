
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
import com.r.library.demo.R;

import java.util.List;

public class OWenAdapter extends RecyclerView.Adapter {
    private String TAG = "OWenAdapter";
    private Context mContext;
    private List<OWenItem> mDataList;
    private OnItemListener mOnItemListener;

    public OWenAdapter(Context context) {
        mContext = context;
    }

    public void setData(List list) {
        mDataList = list;
    }

    public void setOnItemListener(OnItemListener listener) {
        mOnItemListener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int pos) {
        LogUtils.i(TAG, "onCreateViewHolder() pos=" + pos);
        View view = View.inflate(mContext, R.layout.item_owen_recycler, null);
        return new OWenAdapter.RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int pos) {
        LogUtils.i(TAG, "onBindViewHolder() pos=" + pos);
        RecyclerViewHolder holder = (RecyclerViewHolder) viewHolder;

        holder.onBind(pos);
        holder.setImageUrl(mDataList.get(pos).getPicUrl());
        holder.setTitle(mDataList.get(pos).getTitle());
        holder.setOnItemListener();
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

        private void setFocusable(boolean focusable) {
            mItemView.setFocusable(focusable);
        }

        private void setClickable(boolean clickable) {
            mItemView.setClickable(clickable);
        }

        public void setOnItemListener() {
            if (null != mOnItemListener) {
                setFocusable(true);
                mItemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        mOnItemListener.onFocusChanged(v, hasFocus, mPosition);
                    }
                });
                setClickable(true);
                mItemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemListener.onClick(v, mPosition);
                    }
                });
            } else {
                mItemView.setOnFocusChangeListener(null);
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

    public interface OnItemListener {
        void onFocusChanged(View view, boolean hasFocus, int position);

        void onClick(View view, int position);
    }
}
