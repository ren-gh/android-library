package com.r.library.common.player;

public interface PlayerViewListener {
    void onCenterClicked();
    void onSeekBarChanged(int progress, boolean fromUser);
}
