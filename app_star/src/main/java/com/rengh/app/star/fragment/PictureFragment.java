
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

public class PictureFragment extends Fragment {
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

    @Override
    public void onAttach(Context context) {
        LogUtils.i(TAG, "onAttach()");
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        LogUtils.i(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
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

    @Override
    public void onStart() {
        LogUtils.i(TAG, "onStart()");
        super.onStart();
    }

    @Override
    public void onResume() {
        LogUtils.i(TAG, "onResume()");
        super.onResume();
    }

    @Override
    public void onPause() {
        LogUtils.i(TAG, "onPause()");
        super.onPause();
    }

    @Override
    public void onStop() {
        LogUtils.i(TAG, "onStop()");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        LogUtils.i(TAG, "onDestroyView()");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        LogUtils.i(TAG, "onDestroy()");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        LogUtils.i(TAG, "onDetach()");
        super.onDetach();
    }

    private void setImageUrl() {
        final String url = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1561451242506&di=ea34c472cdcbce97251f0cdb06bc9776&imgtype=0&src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F01cffe5b485d25a8012036be383706.gif";
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
