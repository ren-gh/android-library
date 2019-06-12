
package com.r.library.demo.recyclerview;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.r.library.common.util.UIUtils;
import com.r.library.demo.R;

public class MaulCarouselAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private OnItemStateListener mListener;

    MaulCarouselAdapter(Context context) {
        mContext = context;
    }

    public void setOnItemStateListener(OnItemStateListener listener) {
        mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecyclerViewHolder(View.inflate(mContext, R.layout.item_recyclerview_maul, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final RecyclerViewHolder viewHolder = (RecyclerViewHolder) holder;
        viewHolder.mName.setText(ContantUtil.TEST_DATAS[position]);
        viewHolder.mImageView.setImageDrawable(ContextCompat.getDrawable(mContext,
                ContantUtil.getImgResourceId(position)));
    }

    @Override
    public int getItemCount() {
        return ContantUtil.TEST_DATAS.length / 2;
    }

    private class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnFocusChangeListener {

        RelativeLayout mRelativeLayout;
        TextView mName;
        ImageView mImageView;

        RecyclerViewHolder(View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.tv_item_tip);
            mRelativeLayout = itemView.findViewById(R.id.fl_main_layout);
            mImageView = itemView.findViewById(R.id.iv_item);
            mRelativeLayout.setOnClickListener(this);
            mRelativeLayout.setOnFocusChangeListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getAdapterPosition());
            }
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                UIUtils.scaleView(mRelativeLayout, 1.10f);
            } else {
                UIUtils.scaleView(mRelativeLayout, 1.00f);
            }
        }
    }

    public interface OnItemStateListener {
        void onItemClick(View view, int position);
    }
}
