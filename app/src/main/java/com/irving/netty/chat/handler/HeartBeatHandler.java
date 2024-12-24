package com.irving.netty.chat.handler;

import android.util.Log;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @author luojun
 * @version 1.0.0
 * @ClassName HeartBeatHandler
 * @Description 用于检测channel的心跳handler, 继承ChannelInboundHandlerAdapter，
 * 从而不需要实现channelRead0方法(SimpleChannelInboundHandler的方法)
 * @createTime 2021/11/15 10:52
 */
public class HeartBeatHandler extends ChannelInboundHandlerAdapter {
    private String TAG = HeartBeatHandler.class.getSimpleName();

    int writeIdleTimes = 0;

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        // 判断evt是否是IdleStateEvent（用于触发用户事件，包含 读空闲/写空闲/读写空闲 ）
        if (!(evt instanceof IdleStateEvent)) {
            return;
        }
        IdleStateEvent event = (IdleStateEvent) evt;// 强制类型转换
        String eventType = null;
        switch (event.state()) {
            case READER_IDLE:
                eventType = "读空闲";
                break;
            case WRITER_IDLE:
                eventType = "写空闲";
                writeIdleTimes++;
                break;
            case ALL_IDLE:
                eventType = "读写空闲";
                break;
        }

        Log.e(TAG, ctx.channel().remoteAddress() + "超时事件：" + eventType);
        if (writeIdleTimes > 3) {
            if (event.state() == IdleState.WRITER_IDLE) {
                // 服务端断开
            }
        }
    }
}
