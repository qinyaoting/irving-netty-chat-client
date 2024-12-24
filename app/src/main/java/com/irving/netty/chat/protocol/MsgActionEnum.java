package com.irving.netty.chat.protocol;

/**
 * @author luojun
 * @version 1.0.0
 * @ClassName MsgActionEnum
 * @Description 发送消息的动作类型 枚举
 * @createTime 2021/11/14 2:47
 */
public enum MsgActionEnum {
    SYSTEM("SYSTEM","系统消息"),

    LOGIN("LOGIN","登录指令"),
    LOGOUT("LOGOUT","登出指令"),
    CHAT("CHAT","聊天消息"),
    FLOWER("FLOWER","送鲜花"),
    KEEPALIVE("KEEPALIVE","心跳检测");

    private String name ;
    private String msg;

    MsgActionEnum(String name, String msg) {
        this.name = name;
        this.msg = msg;
    }

    /**
     * 判断是否是我们规定的协议指令
     * @param
     * @return
     */
//    public static boolean isIMP(String content){
//        return content.matches("^\\[(SYSTEM|LOGIN|LOGOUT|CHAT|FLOWER)\\]");
//    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}