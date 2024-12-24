package com.irving.netty.chat.modle;


/**
 * @author luojun
 * @version 1.0.0
 * @ClassName ChatMessage
 * @Description chat消息处理类，交给适配器管理
 * @createTime 2021/11/14 12:44
 */
public class ChatMessage {
    private String content;
    private String time;
    private int isMeSend;// 0是对方发送 1是自己发送 2系统消息
    private int isRead;// 是否已读（0未读 1已读）
    private String sender; // 发送者

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getIsMeSend() {
        return isMeSend;
    }

    public void setIsMeSend(int isMeSend) {
        this.isMeSend = isMeSend;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
