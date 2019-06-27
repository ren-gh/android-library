
package com.rengh.app.star.util;

import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.FilenameFilter;

public class LocalFileUtils {
    /**
     * 随机取一个本地视频的地址
     * 
     * @return path
     */
    public static String getVideoPath(final String dirPath) {
        String path = null;
        File dir = new File(!TextUtils.isEmpty(dirPath) ? dirPath
                : Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera");
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    String[] arr = name.split("\\.");
                    if (arr == null || arr.length == 0) {
                        return false;
                    }
                    String end = arr[arr.length - 1];
                    return "mp4".equalsIgnoreCase(end)
                            || "ts".equalsIgnoreCase(end)
                            || "rm".equalsIgnoreCase(end)
                            || "rmvb".equalsIgnoreCase(end)
                            || "mov".equalsIgnoreCase(end)
                            || "mkv".equalsIgnoreCase(end)
                            || "avi".equalsIgnoreCase(end)
                            || "3gp".equalsIgnoreCase(end)
                            || "flv".equalsIgnoreCase(end)
                            || "mpg".equalsIgnoreCase(end)
                            || "wmv".equalsIgnoreCase(end)
                            || "f4v".equalsIgnoreCase(end)
                            || "vob".equalsIgnoreCase(end);
                }
            });
            if (null != files && files.length > 0) {
                int min = 0;
                int max = files.length - 1;
                int index = (int) (min + Math.random() * (max - 1 + 1));
                path = files[index].getPath();
            }
        }
        return path;
    }

}
