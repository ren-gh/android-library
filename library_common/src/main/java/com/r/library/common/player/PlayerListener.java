
package com.r.library.common.player;

import android.widget.FrameLayout;

public interface PlayerListener {
    void onStart(FrameLayout frameLayout, FrameLayout frameLayout2);

    void onPlaying();

    void onPause();

    void onFast();

    void onRewind();

    void onSeekCommpleted();

    void onError();

    void onCompleted();

    void onStop();

    void onClick();

    void onFinish();

    void onSeekChanged(int progress);

    void onBackClicked();
}
