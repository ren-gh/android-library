
package com.r.library.common.lan;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.r.library.common.net.LocalNetHelper;
import com.r.library.common.util.LogUtils;

import android.text.TextUtils;

public class LanHelper {
    private static final String TAG = "LanHelper";

    public static List<LanDevice> getDevicesFromLan() {
        String ip = LocalNetHelper.getIp();
        if (TextUtils.isEmpty(ip)) {
            return null;
        }
        String host = ip.substring(0, ip.lastIndexOf(".") + 1);
        LogUtils.i(TAG, "ip: " + ip + ", host: " + host);
        sendMsg(host);
        return readArp();
    }

    private static void sendMsg(String net) {
        DatagramPacket dp = new DatagramPacket(new byte[0], 0, 0);
        DatagramSocket socket = null;
        int position = 1;
        while (position < 255) {
            try {
                if (null == socket) {
                    socket = new DatagramSocket();
                }
                dp.setAddress(InetAddress.getByName(net + String.valueOf(position)));
                socket.send(dp);
                position++;
                if (position == 125) {// 分两段掉包,一次性发的话,达到236左右,会耗时3秒左右再往下发
                    socket.close();
                    socket = new DatagramSocket();
                }
            } catch (IOException e) {
                position++;
            } catch (Exception e) {
                position++;
            }
        }
        if (null != socket) {
            socket.close();
        }
    }

    private static List<LanDevice> readArp() {
        List<LanDevice> beans = new ArrayList<LanDevice>();
        try {
            LanDevice bean = new LanDevice();
            BufferedReader br = new BufferedReader(new FileReader("/proc/net/arp"));
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    line = line.trim();
                    if (line.toUpperCase(Locale.getDefault()).contains("IP"))
                        continue;
                    while (line.contains("  ")) {
                        line = line.replaceAll("  ", " ");
                    }
                    String[] tmpArr = line.split(" ");
                    if (null != tmpArr && tmpArr.length >= 6) {
                        bean.ip = tmpArr[0];
                        bean.type = tmpArr[1];
                        bean.flag = tmpArr[2];
                        bean.mac = tmpArr[3];
                        bean.mask = tmpArr[4];
                        bean.device = tmpArr[5];
                        if (bean.mac.contains("00:00:00:00:00:00"))
                            continue;
                        LogUtils.d(TAG, "bean: " + bean);
                        beans.add(bean);
                    }
                } catch (Exception e) {
                }
            }
            br.close();
        } catch (Exception e) {
        }
        return beans;
    }
}
