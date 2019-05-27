
package com.r.library.common.player;

import android.view.MotionEvent;

public interface PlayerViewListener {
    boolean onTouchEvent(MotionEvent event);

    void onCenterClicked();

    void onClick();

    void onSeekBarChanged(int progress, boolean fromUser);
}
