package com.rengh.rlibrary.custom;

import android.view.View;

public interface OnRecyclerItemFocusChangeListener {
    void onFocusChange(View view, boolean hasFocus);

    void onFocusChange(View view, boolean hasFocus, int position, int index);
}
