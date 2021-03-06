
package com.r.library.common.player;

import android.graphics.drawable.Drawable;
import android.net.Uri;

public class PlayerParams {
    private int number = 0;
    private String videoTitle = null;
    private Uri videoUri = null;
    private Drawable coverDrawable = null;
    private PlayerListener playerListener;
    private boolean isAdVideo = false;
    private boolean disableCountDown = false;
    private boolean doubleClickPause = false;
    private boolean autoFinish = false;
    private int autoFinishDelay = 0;
    private boolean showLoading = false;
    private boolean showCenterClickToast = true;
    private boolean ignoreBackKey = false;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String sVideoTile) {
        this.videoTitle = sVideoTile;
    }

    public Uri getVideoUri() {
        return videoUri;
    }

    public void setVideoUri(Uri sVideoUri) {
        this.videoUri = sVideoUri;
    }

    public Drawable getCoverDrawable() {
        return coverDrawable;
    }

    public void setCoverDrawable(Drawable sCoverDrawable) {
        this.coverDrawable = sCoverDrawable;
    }

    public PlayerListener getPlayerListener() {
        return playerListener;
    }

    public void setPlayerListener(PlayerListener sPlayerListener) {
        this.playerListener = sPlayerListener;
    }

    public Boolean isAdVideo() {
        return isAdVideo;
    }

    public void setAdVideo(Boolean sIsAdVideo) {
        this.isAdVideo = sIsAdVideo;
    }

    public boolean isDisableCountDown() {
        return disableCountDown;
    }

    public void setDisableCountDown(boolean disableCountDown) {
        this.disableCountDown = disableCountDown;
    }

    public Boolean getDoubleClickPause() {
        return doubleClickPause;
    }

    public void setDoubleClickPause(Boolean sDoubleClick) {
        this.doubleClickPause = sDoubleClick;
    }

    public Boolean getAutoFinish() {
        return autoFinish;
    }

    public void setAutoFinish(Boolean sAutoFinish) {
        this.autoFinish = sAutoFinish;
    }

    public Integer getAutoFinishDelay() {
        return autoFinishDelay;
    }

    public void setAutoFinishDelay(Integer sAutoFinishDelay) {
        this.autoFinishDelay = sAutoFinishDelay;
    }

    public Boolean getShowLoading() {
        return showLoading;
    }

    public void setShowLoading(Boolean sShowLoading) {
        this.showLoading = sShowLoading;
    }

    public Boolean getShowCenterClickToast() {
        return showCenterClickToast;
    }

    public void setShowCenterClickToast(Boolean sShowCenterClickToast) {
        this.showCenterClickToast = sShowCenterClickToast;
    }

    public Boolean getIgnoreBackKey() {
        return ignoreBackKey;
    }

    public void setIgnoreBackKey(Boolean ignoreBackKey) {
        this.ignoreBackKey = ignoreBackKey;
    }
}
