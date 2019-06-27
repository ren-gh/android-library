
package com.rengh.app.star.fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.r.library.common.util.LogUtils;
import com.rengh.app.star.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;

public class PictureFragment extends BaseFragment {
    private final String TAG = "PictureFragment";
    private Listener mListener;
    private ImageView mImgPic;

    public interface Listener {
        void onLoadFailed();

        void onResourceReady();
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        LogUtils.i(TAG, "onCreateView()");
        // View view = super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_picture, container, false);
        mImgPic = view.findViewById(R.id.img_pic);
        setImageUrl();
        return view;
    }

    private void setImageUrl() {
        final String url = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1562071646&di=2875445d32a211fb0a87d4a78ed12ca5&imgtype=jpg&er=1&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201802%2F14%2F20180214181826_tcNih.jpeg";
        Glide.with(getActivity())
                .load(url)
                .placeholder(R.drawable.pic_default)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model,
                            Target<Drawable> target,
                            boolean isFirstResource) {
                        LogUtils.e(TAG, "onLoadFailed()");
                        if (null != mListener) {
                            mListener.onLoadFailed();
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model,
                            Target<Drawable> target,
                            DataSource dataSource,
                            boolean isFirstResource) {
                        LogUtils.i(TAG, "onResourceReady()");
                        if (null != mListener) {
                            mListener.onResourceReady();
                        }
                        return false;
                    }
                })
                .into(mImgPic);
    }
}
