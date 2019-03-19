
package com.rengh.library.common.player;

import android.graphics.drawable.Drawable;
import android.net.Uri;

public class PlayerHelper {
    private static String sVideoTile = null;
    private static Uri sVideoUri = null;
    private static Drawable sCoverDrawable = null;
    private static PlayerListener sPlayerListener;
    private static Boolean sIsAdVideo = false;
    private static Boolean sDoubleClick = false;
    private static Boolean sAutoFinish = false;
    private static Integer sAutoFinishDelay = 0;
    private static Boolean sShowLoading = false;

    public static String getVideoTile() {
        return sVideoTile;
    }

    public static void setVideoTile(String videoTile) {
        sVideoTile = videoTile;
    }

    public static Uri getVideoUri() {
        return sVideoUri;
    }

    public static void setVideoUri(Uri videoUri) {
        sVideoUri = videoUri;
    }

    public static Drawable getCoverDrawable() {
        return sCoverDrawable;
    }

    public static void setCoverDrawable(Drawable coverDrawable) {
        sCoverDrawable = coverDrawable;
    }

    public static PlayerListener getPlayerListener() {
        return sPlayerListener;
    }

    public static void setPlayerListener(PlayerListener playerListener) {
        sPlayerListener = playerListener;
    }

    public static Boolean isAdVideo() {
        return sIsAdVideo;
    }

    public static void setAdVideo(Boolean isAdVideo) {
        sIsAdVideo = isAdVideo;
    }

    public static Boolean getDoubleClick() {
        return sDoubleClick;
    }

    public static void setDoubleClick(Boolean doubleClick) {
        sDoubleClick = doubleClick;
    }

    public static Boolean getAutoFinish() {
        return sAutoFinish;
    }

    public static void setAutoFinish(Boolean autoFinish) {
        sAutoFinish = autoFinish;
    }

    public static Integer getAutoFinishDelay() {
        return sAutoFinishDelay;
    }

    public static void setAutoFinishDelay(Integer autoFinishDelay) {
        sAutoFinishDelay = autoFinishDelay;
    }

    public static Boolean getShowLoading() {
        return sShowLoading;
    }

    public static void setShowLoading(Boolean sShowLoading) {
        PlayerHelper.sShowLoading = sShowLoading;
    }

    public static void clearAll() {
        setVideoTile(null);
        setVideoUri(null);
        setCoverDrawable(null);
        setPlayerListener(null);
        setAutoFinish(false);
        setDoubleClick(false);
        setAutoFinishDelay(0);
        setShowLoading(false);
    }
}
