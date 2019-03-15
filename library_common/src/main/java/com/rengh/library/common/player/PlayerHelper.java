
package com.rengh.library.common.player;

public class PlayerHelper {
    public static final String KEY_TITLE = "title";
    public static final String KEY_DOUBLE_CLICK_TO_PAUSE = "doubleClickToPause";

    private static PlayerListener mPlayerListener;

    public static void setListener(PlayerListener listener) {
        mPlayerListener = listener;
    }

    public static PlayerListener getListener() {
        return mPlayerListener;
    }
}
