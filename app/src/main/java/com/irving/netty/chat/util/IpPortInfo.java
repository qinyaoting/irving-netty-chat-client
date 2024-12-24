package com.irving.netty.chat.util;

/**
 * @author luojun
 * @version 1.0.0
 * @ClassName IpPortInfo
 * @Description ip端口信息
 * @createTime 2021/11/16 17:02
 */
public class IpPortInfo {

    public IpPortInfo(String ipAddr, int port) {
        this.ipAddr = ipAddr;
        this.port = port;
    }

    private String ipAddr;

    private int port;

    public String getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
