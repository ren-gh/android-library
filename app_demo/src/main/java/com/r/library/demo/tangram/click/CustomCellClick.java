
package com.r.library.demo.tangram.click;

import com.r.library.common.util.LogUtils;
import com.r.library.common.util.ToastUtils;
import com.tmall.wireless.tangram.structure.BaseCell;
import com.tmall.wireless.tangram.support.SimpleClickSupport;

import android.view.View;

public class CustomCellClick extends SimpleClickSupport {
    private final String TAG = "CustomCellClick";

    public CustomCellClick() {
        setOptimizedMode(true);
    }

    @Override
    public void defaultClick(View targetView, BaseCell cell, int eventType) {
        LogUtils.d(TAG, "defaultClick() pos=" + cell.pos);
        ToastUtils.showToast(targetView.getContext(),
                "您点击了组件，type=" + cell.stringType + ", pos=" + cell.pos);
    }
}
