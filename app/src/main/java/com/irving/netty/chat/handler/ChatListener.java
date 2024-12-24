package com.irving.netty.chat.handler;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author luojun
 * @version 1.0.0
 * @ClassName NettyListener
 * @Description 消息连接相关属性
 * @createTime 2021/11/14 5:03
 */
public interface ChatListener {
    byte STATUS_CONNECT_SUCCESS = 1;//连接成功

    byte STATUS_CONNECT_CLOSED = 0;//关闭连接

    byte STATUS_CONNECT_ERROR = 0;//连接失败

    /**
     * 当接收到系统消息
     */
    void onMessageResponse(ChannelHandlerContext ctx, String msg);

    /**
     * 当连接状态发生变化时调用
     */
    void onServiceStatusConnectChanged(int statusCode);
}
