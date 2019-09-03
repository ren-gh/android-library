
package com.r.library.common.net;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;

import com.r.library.common.util.LogUtils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import androidx.annotation.RequiresPermission;
import androidx.core.app.ActivityCompat;

public class LocalNetHelper {
    private static final String TAG = "LocalNetHelper";
    public static final String TYPE_ETH_0 = "eth0";
    public static final String TYPE_WLAN_0 = "wlan0";
    public static final String TYPE_P2P_0 = "p2p0";
    public static final String TYPE_LO = "lo";

    public static List<String> getIMEI(Context context) {
        ArrayList<String> imeis = new ArrayList<>();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            try {
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                String imei = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    for (int slot = 0; slot < telephonyManager.getPhoneCount(); slot++) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            imei = telephonyManager.getImei(slot);
                        } else {
                            imei = telephonyManager.getDeviceId(slot);
                        }
                    }
                } else {
                    imei = telephonyManager.getDeviceId();
                }
                if (!imeis.contains(imei) && !TextUtils.isEmpty(imei) && !"null".equalsIgnoreCase(imei)) {
                    imeis.add(imei);
                }
            } catch (NoSuchMethodError e) {
            }
        }
        return imeis;
    }

    @RequiresPermission("android.permission.READ_PHONE_STATE")
    public static List<String> getMEID(Context context) {
        ArrayList<String> meids = new ArrayList<>();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            try {
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                String meid = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    for (int slot = 0; slot < telephonyManager.getPhoneCount(); slot++) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            meid = telephonyManager.getMeid(slot);
                        } else {
                            meid = telephonyManager.getDeviceId(TelephonyManager.PHONE_TYPE_CDMA);
                        }
                    }
                } else {
                    meid = telephonyManager.getDeviceId();
                }
                if (!meids.contains(meid) && !TextUtils.isEmpty(meid) && !"null".equalsIgnoreCase(meid)) {
                    meids.add(meid);
                }
            } catch (NoSuchMethodError e) {
            }
        }
        return meids;
    }

    public static String getMac(String type) {
        String mac = "00:00:00:00:00:00";
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface ntwInterface : interfaces)
                if (ntwInterface.getName().equalsIgnoreCase(type)) {
                    byte[] byteMac = ntwInterface.getHardwareAddress();
                    if (byteMac == null) {
                        return null;
                    }
                    StringBuilder strBuilder = new StringBuilder();
                    for (int i = 0; i < byteMac.length; i++) {
                        strBuilder.append(String.format("%02X:", byteMac[i]));
                    }
                    if (strBuilder.length() > 0) {
                        strBuilder.deleteCharAt(strBuilder.length() - 1);
                    }
                    mac = strBuilder.toString().toLowerCase(Locale.getDefault());
                    break;
                }
        } catch (Exception e) {
            LogUtils.e(TAG, "Exception: " + e.getMessage());
        }
        return mac.toLowerCase(Locale.getDefault());
    }

    public static String getIp() {
        String localIp = "0.0.0.0";
        try {
            Enumeration nis = NetworkInterface.getNetworkInterfaces();
            InetAddress ia = null;
            while (nis.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) nis.nextElement();
                Enumeration<InetAddress> ias = ni.getInetAddresses();
                while (ias.hasMoreElements()) {
                    ia = ias.nextElement();
                    if (ia instanceof Inet6Address) {
                        continue;// skip ipv6
                    }
                    String ip = ia.getHostAddress();
                    if (!"127.0.0.1".equals(ip)) {
                        localIp = ia.getHostAddress();
                        break;
                    }
                }
            }
        } catch (SocketException e) {
            LogUtils.e(TAG, "SocketException: " + e.getMessage());
        }
        return localIp;
    }

    public static String getIpName() {
        String name = null;
        try {
            Enumeration nis = NetworkInterface.getNetworkInterfaces();
            InetAddress ia = null;
            while (nis.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) nis.nextElement();
                Enumeration<InetAddress> ias = ni.getInetAddresses();
                while (ias.hasMoreElements()) {
                    ia = ias.nextElement();
                    if (ia instanceof Inet6Address) {
                        continue;// skip ipv6
                    }
                    String ip = ia.getHostAddress();
                    if (!"127.0.0.1".equals(ip)) {
                        name = ni.getName();
                        break;
                    }
                }
            }
        } catch (SocketException e) {
            LogUtils.e(TAG, "SocketException: " + e.getMessage());
        }
        return name;
    }

}
