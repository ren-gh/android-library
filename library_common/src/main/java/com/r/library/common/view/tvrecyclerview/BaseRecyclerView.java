
package com.r.library.common.view.tvrecyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.r.library.common.util.LogUtils;

public class BaseRecyclerView extends RecyclerView {
    private static final String TAG = BaseRecyclerView.class.getSimpleName();

    public BaseRecyclerView(Context context) {
        super(context);
    }

    public BaseRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        boolean bRes;
        try {
            bRes = super.dispatchTouchEvent(event);
        } catch (Exception e) {
            LogUtils.e(TAG, "dispatchTouchEvent Exception", e);
            bRes = true;
        }
        return bRes;
    }

    @Override
    protected boolean dispatchGenericPointerEvent(MotionEvent event) {
        boolean bRes;
        try {
            bRes = super.dispatchGenericPointerEvent(event);
        } catch (Exception e) {
            LogUtils.e(TAG, "dispatchGenericPointerEvent Exception", e);
            bRes = true;
        }
        return bRes;
    }

    @Override
    protected boolean dispatchHoverEvent(MotionEvent event) {
        boolean bRes;
        try {
            bRes = super.dispatchHoverEvent(event);
        } catch (Exception e) {
            LogUtils.e(TAG, "dispatchHoverEvent Exception", e);
            bRes = true;
        }
        return bRes;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        try {
            super.onLayout(changed, l, t, r, b);
        } catch (Exception e) {
            LogUtils.e(TAG, "onLayout Exception", e);
        }
    }
}
