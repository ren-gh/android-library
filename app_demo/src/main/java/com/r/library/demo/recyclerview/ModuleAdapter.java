
package com.r.library.demo.recyclerview;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.r.library.demo.R;

public class ModuleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private int mItemCount;

    ModuleAdapter(Context context, int itemCount) {
        mContext = context;
        mItemCount = itemCount;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecyclerViewHolder(View.inflate(mContext, R.layout.module_item_recyclerview, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final RecyclerViewHolder viewHolder = (RecyclerViewHolder) holder;
        viewHolder.mName.setText("P: " + position);
        GradientDrawable drawable = (GradientDrawable) viewHolder.mFrameLayout.getBackground();
        drawable.setColor(ContextCompat.getColor(mContext, ContantUtil.getRandColor()));
    }

    @Override
    public int getItemCount() {
        return mItemCount;
    }

    private class RecyclerViewHolder extends RecyclerView.ViewHolder {

        FrameLayout mFrameLayout;
        TextView mName;

        RecyclerViewHolder(View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.tv_item_tip);
            mFrameLayout = itemView.findViewById(R.id.fl_main_layout);
        }
    }
}