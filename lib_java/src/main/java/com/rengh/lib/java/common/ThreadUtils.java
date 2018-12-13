
package com.rengh.lib.java.common;

/**
 * Created by rengh on 17-5-29.
 */
public class ThreadUtils {

    public static boolean sleep(long mills) {
        try {
            Thread.sleep(mills);
            return true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (NoSuchMethodError e) { // 大数据统计到的异常
            Thread.currentThread().interrupt();
        }
        return false;
    }
}
