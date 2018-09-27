
package com.rengh.rlibrary.custom;

import android.view.View;

public interface OnRecyclerItemClickListener {
    void onItemClick(View view, int position);

    void onItemClick(View view, int position, int index);
}
