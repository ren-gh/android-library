
package com.r.library.common.util;

import java.text.DecimalFormat;

public class TimeUtils {
    private static final String TAG = "TimeUtils";

    public static String formatDateTime(long milliseconds) {
        StringBuilder sb = new StringBuilder();
        long mss = milliseconds / 1000;
        long days = mss / (60 * 60 * 24);
        long hours = (mss % (60 * 60 * 24)) / (60 * 60);
        long minutes = (mss % (60 * 60)) / 60;
        long seconds = mss % 60;
        DecimalFormat format = new DecimalFormat("00");
        if (days > 0 || hours > 0) {
            sb.append(format.format(hours)).append(":").append(format.format(minutes)).append(":").append(format.format(seconds));
        } else {
            sb.append(format.format(minutes)).append(":").append(format.format(seconds));
        }
        return sb.toString();
    }
}
