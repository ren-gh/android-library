
package com.r.library.common.view.tvrecyclerview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.r.library.common.util.LogUtils;

public class CustomTvRecyclerView extends TvRecyclerView {
    private static final String TAG = CustomTvRecyclerView.class.getSimpleName();
    private int mNextFocusIndex = 0;

    public CustomTvRecyclerView(Context context) {
        super(context);
    }

    public CustomTvRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomTvRecyclerView(Context context, AttributeSet attrs, int defStyle) {
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

    public void selectedViewRequestFocus() {
        View selectedView = getSelectedView();
        if (selectedView == null) {
            LinearLayoutManager layoutManager = (LinearLayoutManager) getLayoutManager();
            if (layoutManager != null){
                selectedView = layoutManager.findViewByPosition(getSelectedPosition());
                if(selectedView == null){
                    selectedView = getChildAt(0);
                }
            }
        }

        if (selectedView != null && selectedView.isFocusable()) {
            selectedView.requestFocus();
        }
    }

    public boolean isSlideToBottom() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) getLayoutManager();
        //屏幕中最后一个可见子项的position
        int lastVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition();
        //当前屏幕所看到的子项个数
        int visibleItemCount = layoutManager.getChildCount();
        //当前RecyclerView的所有子项个数
        int totalItemCount = layoutManager.getItemCount();
        int state = getScrollState();
        if (visibleItemCount > 0 && lastVisibleItemPosition == totalItemCount - 1 && state == SCROLL_STATE_IDLE) {
            return true;
        }
        return false;
    }

    public boolean isSlideToTop() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) getLayoutManager();
        //屏幕中第一个可见子项的position
        int firstVisibleItemPosition = layoutManager.findFirstCompletelyVisibleItemPosition();
        //当前屏幕所看到的子项个数
        int visibleItemCount = layoutManager.getChildCount();
        int state = getScrollState();
        if (visibleItemCount > 0 && firstVisibleItemPosition == 0 && state == SCROLL_STATE_IDLE) {
            return true;
        }

        return false;
    }

    public boolean isSlideNearbyTop() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) getLayoutManager();
        //屏幕中第一个可见子项的position
        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
        //当前屏幕所看到的子项个数
        int visibleItemCount = layoutManager.getChildCount();
        if (visibleItemCount > 0 && firstVisibleItemPosition == 0) {
            return true;
        }

        return false;
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        mNextFocusIndex = getFocusedIndex();
        super.dispatchDraw(canvas);
    }

    private int getFocusedIndex() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (getChildAt(i).hasFocus()) {
                return i;
            }
        }
        return 0;
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        int index = i;
        int end = childCount - 1;
        if (i >= mNextFocusIndex && i < end) {
            index++;
        } else if (i == end) {
            index = mNextFocusIndex;
        }
        return index;
    }

}
