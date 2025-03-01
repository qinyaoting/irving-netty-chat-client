package com.irving.netty.chat.protocol;

/**
 * @author luojun
 * @version 1.0.0
 * @ClassName IMMessage
 * @Description 消息体对象
 * @createTime 2021/11/14 2:47
 */
public class IMMessage {
    private static final long serialVersionUID = 8763561286199081881L;
    private String addr; // IP地址以及端口
    private String cmd; // 命令类型 SYSTEM|LOGIN|LOGOUT|CHAT|FLOWER|KEEPALIVE
    private long time; // 命令发送时间
    private int online; // 当前在线人数
    private String sender;// 发送人
    private String receiver;// 接收人
    private String content; // 消息内容
    private String terminal; // 终端
    public IMMessage(){}

    public IMMessage(String cmd){
        this.cmd = cmd;
    }

    public IMMessage(String cmd,String sender){
        this.cmd = cmd;
        this.time = time;
        this.online = online;
        this.content = content;
    }

    public IMMessage(String cmd,long time,int online,String content){
        this.cmd = cmd;
        this.time = time;
        this.online = online;
        this.content = content;
    }

    public IMMessage(String cmd,String terminal,long time,String sender){
        this.cmd = cmd;
        this.time = time;
        this.sender = sender;
        this.terminal = terminal;
    }


    public IMMessage(String cmd,long time,String sender,String content){
        this.cmd = cmd;
        this.time = time;
        this.sender = sender;
        this.content = content;
    }

    @Override
    public String toString() {
        return "IMMessage{" +
                "addr='" + addr + '\'' +
                ", cmd='" + cmd + '\'' +
                ", time=" + time +
                ", online=" + online +
                ", sender='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getOnline() {
        return online;
    }

    public void setOnline(int online) {
        this.online = online;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }
}