
package com.rengh.rlibrary.custom;

import android.view.View;

public interface onRecyclerItemLongClickListener {
    void onItemLongClick(View view, int position);

    void onItemLongClick(View view, int position, int index);
}
