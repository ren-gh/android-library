
package com.rengh.app.star.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.r.library.common.util.LogUtils;
import com.rengh.app.star.R;

public class LoginFragment extends BaseFragment {
    private final String TAG = "LoginFragment";
    private LoginListener mListener;
    private Button mBtnLogin;

    public interface LoginListener {
        void onLogin();

        void onLoginFailed();
    }

    public void setListener(LoginListener listener) {
        mListener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        LogUtils.i(TAG, "onCreateView()");
        // View view = super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        mBtnLogin = view.findViewById(R.id.btn_login);
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onLogin();
                }
            }
        });
        return view;
    }
}
