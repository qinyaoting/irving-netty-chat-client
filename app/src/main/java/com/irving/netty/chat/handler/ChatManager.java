package com.irving.netty.chat.handler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.irving.netty.chat.ChatClient;
import com.irving.netty.chat.protocol.IMMessage;
import com.irving.netty.chat.protocol.MsgActionEnum;
import com.irving.netty.chat.util.CoderUtil;
import com.irving.netty.chat.util.IpPortInfo;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author luojun
 * @version 1.0.0
 * @ClassName ChatManager
 * @Description chat管理器，接受处理消息，发送消息
 * @createTime 2021/11/14 12:44
 */
public class ChatManager implements ChatListener {

    private String TAG = ChatManager.class.getSimpleName();
    public static volatile ChatManager instance = null;
    private ChatClient chatClient = null;
    private Handler handler;



    public ChatManager() {
        chatClient = new ChatClient();
    }

    public static ChatManager getInstance(Handler handler) {
        if (instance == null) {
            synchronized (ChatManager.class) {
                if (instance == null) {
                    instance = new ChatManager();
                }
            }
        }
        instance.setHandler(handler);
        return instance;
    }

    public void setHandler(Handler handler){
        this.handler = handler;
    }

    public void connectNetty(IpPortInfo ipPortSetInfo) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e(TAG, "客户端启动自动连接...");
                if (!chatClient.getConnectStatus()) {
                    chatClient.setListener(ChatManager.this);
                    chatClient.connect(ipPortSetInfo);
                } else {
                    chatClient.disconnect();
                }
            }
        }).start();
    }

    public void sendData(IMMessage imMessage) {
        chatClient.sendMsgToServer(imMessage, new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    Log.e(TAG, "发送成功");
                } else {
                    Log.e(TAG, "发送失败");
                }
            }
        });
    }

    @Override
    public void onMessageResponse(ChannelHandlerContext ctx, String msg) {
        dealMsg(ctx,msg);
    }

    @Override
    public void onServiceStatusConnectChanged(int statusCode) {
        if (statusCode == ChatManager.STATUS_CONNECT_SUCCESS) {
            Log.e(TAG, "STATUS_CONNECT_SUCCESS:");
            if (chatClient.getConnectStatus()) {
                Log.e(TAG, "连接成功");
            }
        } else {
            Log.e(TAG, "onServiceStatusConnectChanged:" + statusCode);
            if (!chatClient.getConnectStatus()) {
                Log.e(TAG, "网路不好，正在重连");
            }
        }
        Bundle bundle = new Bundle();
        bundle.putInt("statusCode", statusCode);//往Bundle中存放数据
        Message message = new Message();
        message.setData(bundle);
        message.what = 1;
        handlerSendMessage(message);
    }

    private void handlerSendMessage(Message message) {
        handler.sendMessage(message);
    }

    public void close() {
        if (chatClient != null) {
            chatClient.disconnect();
        }
    }

    /**
     * 处理消息（netty）
     * @param ctx
     * @param msg
     */
    public void dealMsg(ChannelHandlerContext ctx , String msg){
        // 编解码
        IMMessage decode = CoderUtil.decode(msg);
        dealMsg(ctx,decode);
    }
    public void dealMsg(ChannelHandlerContext ctx, IMMessage msg){
        if(msg == null){
            return;
        }
        System.out.println(msg);
        String cmd = msg.getCmd();
        int onlineUsers = msg.getOnline();
        long sysTime = msg.getTime();
        String content = msg.getContent();
        String sender = msg.getSender();
        if(cmd.equals(MsgActionEnum.SYSTEM.getName())){

            Message message = new Message();
            message.what = 3;
            Bundle bundle = new Bundle();
            bundle.putString("onlineUsers", String.valueOf(onlineUsers));//往Bundle中存放数据
            bundle.putString("sysTime", String.valueOf(sysTime));//往Bundle中存放数据
            bundle.putString("content", content);//往Bundle中存放数据
            message.setData(bundle);
            handlerSendMessage(message);
        }else if(cmd.equals(MsgActionEnum.CHAT.getName())){
            Message message = new Message();
            message.what = 4;
            Bundle bundle = new Bundle();
            bundle.putString("sysTime", String.valueOf(sysTime));//往Bundle中存放数据
            bundle.putString("content", content);//往Bundle中存放数据
            bundle.putString("sender", sender);//往Bundle中存放数据
            message.setData(bundle);
            handlerSendMessage(message);

        }else if(cmd.equals(MsgActionEnum.FLOWER.getName())){
        }else if(cmd.equals(MsgActionEnum.LOGOUT.getName())){

        }else if(cmd.equals(MsgActionEnum.KEEPALIVE.getName())){

        }
    }

    /**
     * 获取系统时间
     * @return
     */
    private Long sysTime(){
        return System.currentTimeMillis();
    }
}
