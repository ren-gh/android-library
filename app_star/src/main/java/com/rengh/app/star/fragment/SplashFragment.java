
package com.rengh.app.star.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.r.library.common.util.LogUtils;
import com.rengh.app.star.R;

public class SplashFragment extends Fragment {
    private final String TAG = "SplashFragment";

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
        // return super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_splash, container, false);
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
}
