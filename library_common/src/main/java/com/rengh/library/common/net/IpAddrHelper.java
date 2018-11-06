
package com.rengh.library.common.net;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.rengh.library.common.util.ThreadManager;

/**
 * Created by rengh on 17-10-12.
 */

public class IpAddrHelper {
    public static boolean isIpAddr(String addr) {
        if (TextUtils.isEmpty(addr)) {
            return false;
        }
        if (addr.length() < 7 || addr.length() > 15 || TextUtils.isEmpty(addr)) {
            return false;
        }

        /**
         * 判断IP格式和范围
         */
        String rexp = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\."
                + "(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";

        Pattern pat = Pattern.compile(rexp);
        Matcher mat = pat.matcher(addr);
        return mat.find();
    }

    public static void getIpInfo(final Handler handler, final String ip, final int successCode,
            final int failedCode, final int failedRequestCode) {
        if (null == handler || !IpAddrHelper.isIpAddr(ip)) {
            return;
        }
        ThreadManager.getInstance().excuteCached(new Runnable() {
            @Override
            public void run() {
                String url = "http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=json&ip=" + ip;
                NetHelper.HttpResponse httpResponse = new NetHelper().request(url);
                if (httpResponse != null && httpResponse.isSuccess()) {
                    String response = httpResponse.getResponse();
                    IpInfo ipInfoObj = new IpInfo(response);
                    if (ipInfoObj.isSuccess()) {
                        Message msg = new Message();
                        msg.what = successCode;
                        msg.obj = ipInfoObj;
                        handler.sendMessage(msg);
                    } else {
                        Message msg = new Message();
                        msg.what = failedCode;
                        handler.sendMessage(msg);
                    }
                } else {
                    Message msg = new Message();
                    msg.what = failedRequestCode;
                    handler.sendMessage(msg);
                }
            }
        });
    }

    public static class IpInfo {
        private boolean success = false;
        private String contry = null;
        private String province = null;
        private String city = null;

        public IpInfo(String response) {
            try {
                JSONObject json = new JSONObject(response);
                contry = json.optString("country");
                province = json.optString("province");
                city = json.optString("city");
                success = true;
            } catch (JSONException e) {
                success = false;
            }
        }

        public boolean isSuccess() {
            return success;
        }

        public String getContry() {
            return contry;
        }

        public String getProvince() {
            return province;
        }

        public String getCity() {
            return city;
        }

        @Override
        public String toString() {
            return "(" + contry +
                    "," + province +
                    "," + city +
                    ')';
        }
    }

    public static final long a1 = getIpNum("10.0.0.0");
    public static final long a2 = getIpNum("10.255.255.255");
    public static final long b1 = getIpNum("172.16.0.0");
    public static final long b2 = getIpNum("172.31.255.255");
    public static final long c1 = getIpNum("192.168.0.0");
    public static final long c2 = getIpNum("192.168.255.255");
    public static final long d1 = getIpNum("10.44.0.0");
    public static final long d2 = getIpNum("10.69.0.255");

    public static boolean isInnerIP(String ip) {
        long n = getIpNum(ip);
        return (n >= a1 && n <= a2) || (n >= b1 && n <= b2) || (n >= c1 && n <= c2) || (n >= d1
                && n <= d2);
    }

    private static long getIpNum(String ipAddress) {
        String[] ip = ipAddress.split("\\.");
        long a = Integer.parseInt(ip[0]);
        long b = Integer.parseInt(ip[1]);
        long c = Integer.parseInt(ip[2]);
        long d = Integer.parseInt(ip[3]);
        return a * 256 * 256 * 256 + b * 256 * 256 + c * 256 + d;
    }
}
