
package com.rengh.library.common.player;

public class PlayerHelper {
    public static final String KEY_VIDEO_TITLE_SHOWED_ON_TOP = "videoTitle";

    private static PlayerListener sPlayerListener;
    private static Boolean sDoubleClick = false;
    private static Boolean sAutoFinish = false;
    private static Integer sAutoFinishDelay = 0;

    public static PlayerListener getPlayerListener() {
        return sPlayerListener;
    }

    public static void setPlayerListener(PlayerListener sPlayerListener) {
        PlayerHelper.sPlayerListener = sPlayerListener;
    }

    public static Boolean getDoubleClick() {
        return sDoubleClick;
    }

    public static void setDoubleClick(Boolean sDoubleClick) {
        PlayerHelper.sDoubleClick = sDoubleClick;
    }

    public static Boolean getAutoFinish() {
        return sAutoFinish;
    }

    public static void setAutoFinish(Boolean sAutoFinish) {
        PlayerHelper.sAutoFinish = sAutoFinish;
    }

    public static Integer getAutoFinishDelay() {
        return sAutoFinishDelay;
    }

    public static void setAutoFinishDelay(Integer sAutoFinishDelay) {
        PlayerHelper.sAutoFinishDelay = sAutoFinishDelay;
    }

    public static void clearAll() {
        setPlayerListener(null);
        setAutoFinish(false);
        setDoubleClick(false);
        setAutoFinishDelay(0);
    }
}
