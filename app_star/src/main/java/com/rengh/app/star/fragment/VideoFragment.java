
package com.rengh.app.star.fragment;

import com.r.library.common.player.PlayerHelper;
import com.r.library.common.player.PlayerListener;
import com.r.library.common.player.PlayerParams;
import com.r.library.common.player.PlayerView;
import com.r.library.common.util.LogUtils;
import com.rengh.app.star.R;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class VideoFragment extends BaseFragment {
    private final String TAG = "VideoFragment";
    private PlayerView mPlayerView;
    private PlayerParams mPlayerParams;
    private PlayerListener mVideoListenerFromHelper = null;

    public void setPlayerParams(PlayerParams params) {
        mPlayerParams = params;
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
    public void onDestroy() {
        LogUtils.i(TAG, "onDestroy()");
        super.onDestroy();
        mPlayerView.onDestroy();
    }

}
