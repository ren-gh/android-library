
package com.rengh.app.star.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.r.library.common.util.LogUtils;
import com.r.library.common.util.ToastUtils;
import com.rengh.app.star.R;

public class TabListFragment extends BaseFragment {
    private final String TAG = "TabListFragment";
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        LogUtils.i(TAG, "onCreateView()");
        recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_recyclerview, container, false);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(new RecyclerViewAdapter(getContext()));
        return recyclerView;
    }

    public static class RecyclerViewAdapter extends RecyclerView.Adapter<ListViewHolder> {
        private final String TAG = "RecyclerViewAdapter";
        private Context context;

        public RecyclerViewAdapter(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public ListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.layout_tag_list_fragment_item, viewGroup, false);
            return new ListViewHolder(view, i);
        }

        @Override
        public void onBindViewHolder(@NonNull ListViewHolder viewHolder, int i) {
            viewHolder.setTitle("标题：" + i);
            viewHolder.setImageUrl("https://puui.qpic.cn/vcover_hz_pic/0/faua0eu552golq21561012537/226");
            viewHolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LogUtils.i(TAG, "onClick() pos=" + i);
                    ToastUtils.showToast(context, "点击：" + i);
                }
            });
        }

        @Override
        public int getItemCount() {
            return 20;
        }
    }

    public static class ListViewHolder extends RecyclerView.ViewHolder {
        private final String TAG = "ListViewHolder";
        private final View view;
        private TextView tv;
        private ImageView iv;
        private int position;

        public ListViewHolder(View view, int position) {
            super(view);
            this.view = view;
            this.tv = this.view.findViewById(R.id.textView);
            this.iv = this.view.findViewById(R.id.imageView);
            this.position = position;
        }

        public View getView() {
            return this.view;
        }

        public void setTitle(CharSequence charSequence) {
            this.tv.setText(charSequence);
        }

        public void setImageUrl(String url) {
            Glide.with(this.view).load(url).placeholder(R.drawable.pic_default).into(this.iv);
        }

        public void setOnClickListener(View.OnClickListener listener) {
            view.setOnClickListener(listener);
        }
    }

}
