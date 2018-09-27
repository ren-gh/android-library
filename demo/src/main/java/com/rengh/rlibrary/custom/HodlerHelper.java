
package com.rengh.rlibrary.custom;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.rengh.rlibrary.R;
import com.rengh.rlibrary.view.AliButtonCommodity;
import com.rengh.rlibrary.view.AliButtonRedPkg;
import com.rengh.rlibrary.view.AliButtonVideo;

public class HodlerHelper {

    public static class TitleViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        TextView tvTitle;

        public TitleViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.tvTitle = itemView.findViewById(R.id.tv_title);
        }
    }

    public static class RedPkgiewHolder extends RecyclerView.ViewHolder {
        View itemView;
        TextView tvTitle;
        AliButtonRedPkg imgBtn;

        public RedPkgiewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.tvTitle = itemView.findViewById(R.id.tv_title_red_pkg);
            this.imgBtn = itemView.findViewById(R.id.img_btn_red_pkg);
        }
    }

    public static class VideoViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        TextView tvTitle;
        AliButtonVideo imgBtn1, imgBtn2, imgBtn3;

        public VideoViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.tvTitle = itemView.findViewById(R.id.tv_title_videos);
            this.imgBtn1 = itemView.findViewById(R.id.img_btn_video_1);
            this.imgBtn2 = itemView.findViewById(R.id.img_btn_video_2);
            this.imgBtn3 = itemView.findViewById(R.id.img_btn_video_3);
        }
    }

    public static class CommodityViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        TextView tvTitle;
        AliButtonCommodity imgBtn1, imgBtn2, imgBtn3, imgBtn4;

        public CommodityViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.tvTitle = itemView.findViewById(R.id.tv_title_commodity);
            this.imgBtn1 = itemView.findViewById(R.id.img_btn_commodity_1);
            this.imgBtn2 = itemView.findViewById(R.id.img_btn_commodity_2);
            this.imgBtn3 = itemView.findViewById(R.id.img_btn_commodity_3);
            this.imgBtn4 = itemView.findViewById(R.id.img_btn_commodity_4);
        }
    }

}
