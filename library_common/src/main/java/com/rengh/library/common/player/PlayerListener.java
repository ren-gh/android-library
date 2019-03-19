
package com.rengh.library.common.player;

public interface PlayerListener {
    void onStart();

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
}
