
package com.r.library.demo.tangram.exposure;

import com.r.library.common.util.LogUtils;
import com.tmall.wireless.tangram.dataparser.concrete.Card;
import com.tmall.wireless.tangram.structure.BaseCell;
import com.tmall.wireless.tangram.support.ExposureSupport;

import android.support.annotation.NonNull;
import android.view.View;

public class CustomCellExposure extends ExposureSupport {
    private final String TAG = "CustomCellExposure";

    public CustomCellExposure() {
        setOptimizedMode(true);
    }

    /**
     * 布局展示曝光
     * 
     * @param card
     * @param offset
     * @param position
     */
    @Override
    public void onExposure(@NonNull Card card, int offset, int position) {
        LogUtils.d(TAG, "onExposure() "
                + "card: " + card
                + ", offset: " + offset
                + ", position: " + position);
    }

    /**
     * 组件首次展示曝光
     * 
     * @param targetView
     * @param cell
     * @param type
     */
    @Override
    public void defaultExposureCell(@NonNull View targetView, @NonNull BaseCell cell, int type) {
        LogUtils.d(TAG, "defaultExposureCell() "
                + "targetView=" + targetView.getClass().getSimpleName()
                + ", pos=" + cell.pos
                + ", type=" + type);
    }

    /**
     * 组件每次展示曝光
     * 
     * @param targetView
     * @param cell
     * @param type
     */
    @Override
    public void defaultTrace(@NonNull View targetView, @NonNull BaseCell cell, int type) {
        LogUtils.d(TAG, "defaultTrace() "
                + "targetView=" + targetView.getClass().getSimpleName()
                + ", pos=" + cell.pos
                + ", type=" + type);
    }
}
