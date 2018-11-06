
package com.rengh.library.common.util;

import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by mby on 17-12-28.
 */

public class UrlUtils {
    private static final String TAG = "UrlUtils";

    /**
     * 生成url
     * 
     * @param host
     * @param param
     * @return
     */
    public static String getUrl(String host, Map<String, String> param) {
        try {
            StringBuilder stringBuilder = new StringBuilder("");
            if (param != null) {
                Iterator<String> iterator = param.keySet().iterator();
                if (iterator.hasNext()) {
                    if (host.contains("?") == false) {
                        stringBuilder.append("?");
                    }
                }
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    Object value = param.get(key);
                    stringBuilder.append(key);
                    stringBuilder.append("=");
                    stringBuilder.append(URLEncoder.encode(value.toString(), "utf-8"));
                    if (iterator.hasNext()) {
                        stringBuilder.append("&");
                    }
                }
            }
            return host + stringBuilder.toString();
        } catch (Exception e) {
            LogUtils.i(TAG, "questTask get Url e : " + e != null ? e.toString() : "e == null");
        }
        return null;
    }
}
