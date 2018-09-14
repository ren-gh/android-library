
package com.rengh.library.common.net;

public class LanDevice {
    public String ip, type, flag, mac, mask, device;

    @Override
    public String toString() {
        return "LanDevice{" +
                "ip='" + ip + '\'' +
                ", type='" + type + '\'' +
                ", flag='" + flag + '\'' +
                ", mac='" + mac + '\'' +
                ", mask='" + mask + '\'' +
                ", device='" + device + '\'' +
                '}';
    }
}
