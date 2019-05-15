
package com.r.library.common.lan;

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
