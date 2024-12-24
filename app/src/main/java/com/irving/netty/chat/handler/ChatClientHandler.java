package com.irving.netty.chat.handler;

import android.util.Log;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author luojun
 * @version 1.0.0
 * @ClassName ChatClientHandler
 * @Description 客户端nettyChandler
 * @createTime 2021/11/14 5:00
 */
public class ChatClientHandler extends SimpleChannelInboundHandler<String> {

    private static final String TAG = ChatClientHandler.class.getSimpleName();
    private ChatListener listener;

    public ChatClientHandler(ChatListener listener) {
        this.listener = listener;
    }

    /**
     * 连接成功
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        listener.onServiceStatusConnectChanged(ChatListener.STATUS_CONNECT_SUCCESS);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        Log.e(TAG, "channelInactive");
    }

    // 接收到服务端消息
    @Override
    public void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        listener.onMessageResponse(ctx, msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // 当引发异常时关闭连接。
        Log.e(TAG, "引发异常,关闭连接:" + cause.toString());
        listener.onServiceStatusConnectChanged(ChatListener.STATUS_CONNECT_ERROR);
        cause.printStackTrace();
        ctx.close();
    }
}
