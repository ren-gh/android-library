
package com.r.library.common.player;

public class PlayerItem {
    private boolean isAd = false;
    private int number = 0;
    private String name = null;
    private String path = null;

    public boolean isAd() {
        return isAd;
    }

    public void setAd(boolean ad) {
        isAd = ad;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "PlayerItem{" +
                "isAd=" + isAd +
                ", number=" + number +
                ", name='" + name + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
