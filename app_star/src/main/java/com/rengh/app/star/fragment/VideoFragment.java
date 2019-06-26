
package com.rengh.app.star.fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.r.library.common.player.PlayerHelper;
import com.r.library.common.player.PlayerListener;
import com.r.library.common.player.PlayerParams;
import com.r.library.common.player.PlayerView;
import com.r.library.common.util.LogUtils;
import com.rengh.app.star.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class VideoFragment extends Fragment {
    private final String TAG = "VideoFragment";
    private PlayerView mPlayerView;
    private PlayerParams mPlayerParams;
    private PlayerListener mVideoListenerFromHelper = null;

    public void setPlayerParams(PlayerParams params) {
        mPlayerParams = params;
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
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        mPlayerView = view.findViewById(R.id.pv_video);
        if (null != mPlayerParams) {
            mVideoListenerFromHelper = mPlayerParams.getPlayerListener();
            mPlayerParams.setPlayerListener(mVideoListenerFromHelper);
        }
        PlayerHelper.setPlayerParams(mPlayerParams);
        mPlayerView.initValues();
        return view;
    }

    @Override
    public void onStart() {
        LogUtils.i(TAG, "onStart()");
        super.onStart();
        mPlayerView.onStart();
    }

    @Override
    public void onResume() {
        LogUtils.i(TAG, "onResume()");
        super.onResume();
        mPlayerView.onResume();
    }

    @Override
    public void onPause() {
        LogUtils.i(TAG, "onPause()");
        super.onPause();
        mPlayerView.onPause();
    }

    @Override
    public void onStop() {
        LogUtils.i(TAG, "onStop()");
        super.onStop();
        mPlayerView.onStop(isRemoving());
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
        mPlayerView.onDestroy();
    }

    @Override
    public void onDetach() {
        LogUtils.i(TAG, "onDetach()");
        super.onDetach();
    }

}
