
package com.r.library.common.player;

public class PlayerHelper {
    private volatile static PlayerParams playerParams = null;

    public static PlayerParams getPlayerParams() {
        return playerParams;
    }

    public static void setPlayerParams(PlayerParams params) {
        playerParams = params;
    }

    public static void clearAll() {
        playerParams = null;
    }
}
