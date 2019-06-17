
package com.r.library.demo.recyclerview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.r.library.demo.R;

import java.util.List;

public class OWenAdapter extends RecyclerView.Adapter {
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
        return new OWenAdapter.RecyclerViewHolder(View.inflate(mContext, R.layout.item_owen_recycler, null));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        RecyclerViewHolder recyclerViewHolder = (RecyclerViewHolder) viewHolder;
        Glide.with(mContext).load(mDataList.get(i).getPicUrl()).into(recyclerViewHolder.mImgPic);
        recyclerViewHolder.mTitle.setText(mDataList.get(i).getTitle());
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    private class RecyclerViewHolder extends RecyclerView.ViewHolder {
        ImageView mImgPic;
        TextView mTitle;

        RecyclerViewHolder(View itemView) {
            super(itemView);
            mImgPic = itemView.findViewById(R.id.img_pic);
            mTitle = itemView.findViewById(R.id.tv_title);
        }
    }
}
