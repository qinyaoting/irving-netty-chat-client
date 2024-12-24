package com.irving.netty.chat;

import android.os.SystemClock;
import android.util.Log;

import com.irving.netty.chat.handler.ChatClientHandler;
import com.irving.netty.chat.handler.ChatListener;
import com.irving.netty.chat.handler.HeartBeatHandler;
import com.irving.netty.chat.protocol.IMMessage;
import com.irving.netty.chat.util.CoderUtil;
import com.irving.netty.chat.util.IpPortInfo;

import java.util.concurrent.TimeUnit;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;

/**
 * @author luojun
 * @version 1.0.0
 * @ClassName ChatClient
 * @Description TODO
 * @createTime 2021/11/14 4:51
 */
public class ChatClient {

    private static final String TAG = "ChatClient";

    private EventLoopGroup group; // 客户端Bootstrap参数

    private ChatListener listener; //服务监听对象

    private Channel channel; // 通过对象发送数据到服务端

    private boolean isConnect = false; //判断是否连接了

    private static int reconnectNum = 5;// 重连的次数，重连次数超过5次，停止重连

    private static boolean isNeedReconnect = true; // 是否需要重连

    private boolean isConnecting = false; // 是否正在连接

    private long reconnectIntervalTime = 15000;//重连的时间


    public void connect(IpPortInfo ipPortInfo) {
        if (isConnecting) {
            return;
        }

        // 起个线程
        Thread clientThread = new Thread("Netty-Client") {
            @Override
            public void run() {
                super.run();
                isNeedReconnect = true;
                reconnectNum = 5;
                connectServer(ipPortInfo);
            }
        };

        clientThread.start();
    }

    // 连接时的具体参数设置
    private void connectServer(IpPortInfo ipPortInfo) {
        synchronized (ChatClient.this) {
            ChannelFuture channelFuture = null;// 连接管理对象
            if (!isConnect) {
                isConnecting = true;
                group = new NioEventLoopGroup();//设置的连接group
                Bootstrap bootstrap = new Bootstrap();
                bootstrap.group(group)// 设置的一系列连接参数操作等
                        .channel(NioSocketChannel.class)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel ch) throws Exception {
                                ch.pipeline().addLast(new StringEncoder(CharsetUtil.UTF_8));
                                ch.pipeline().addLast(new StringDecoder(CharsetUtil.UTF_8));
//                                ch.pipeline().addLast(new IdleStateHandler(0, 5, 0, TimeUnit.SECONDS));//5s未发送数据，回调userEventTriggered
//                                ch.pipeline().addLast(new HeartBeatHandler());
                                ch.pipeline().addLast(new ChatClientHandler(listener));
                            }
                        });
                try {
                    //连接监听
                    channelFuture = bootstrap.connect(ipPortInfo.getIpAddr(), ipPortInfo.getPort()).addListener(new ChannelFutureListener() {
                        @Override
                        public void operationComplete(ChannelFuture channelFuture) throws Exception {
                            if (channelFuture.isSuccess()) {
                                isConnect = true;
                                channel = channelFuture.channel();
                            } else {
                                Log.e(TAG, "连接失败");
                                isConnect = false;
                            }
                            isConnecting = false;
                        }
                    }).sync();
                    // 等待连接关闭
                    channelFuture.channel().closeFuture().sync();
                    Log.e(TAG, " 断开连接");

                } catch (Exception e) {
                    Log.e(TAG, "e:" + e.toString());
                    e.printStackTrace();
                } finally {
                    isConnect = false;
                    listener.onServiceStatusConnectChanged(ChatListener.STATUS_CONNECT_CLOSED);//STATUS_CONNECT_CLOSED 这我自己定义的 接口标识
                    if (null != channelFuture) {
                        if (channelFuture.channel() != null && channelFuture.channel().isOpen()) {
                            channelFuture.channel().close();
                        }
                    }
                    group.shutdownGracefully();
                    reconnect(ipPortInfo);//重新连接
                }
            }
        }
    }

    //断开连接
    public void disconnect() {
        Log.e(TAG, "disconnect");
        isNeedReconnect = false;
        group.shutdownGracefully();
    }

    //重新连接
    public void reconnect(IpPortInfo ipPortInfo) {
        Log.e(TAG, "reconnect");
        if (isNeedReconnect && reconnectNum > 0 && !isConnect) {
            reconnectNum--;
            SystemClock.sleep(reconnectIntervalTime);
            if (isNeedReconnect && reconnectNum > 0 && !isConnect) {
                Log.e(TAG, "重新连接");
                connectServer(ipPortInfo);
            }else {
                disconnect();
            }
        }
    }

    //发送消息到服务端。 Bootstrap设置的时候我没有设置解码，这边才转的
    public boolean sendMsgToServer(IMMessage imMessage, ChannelFutureListener listener) {
        boolean flag = channel != null && isConnect;
        if (flag) {
            String content = CoderUtil.encode(imMessage);
            System.out.println(content);
            channel.writeAndFlush(content).addListener(listener);
        }
        return flag;
    }

    /**
     * 根据需要可以自己定义修改重连次数
     * @param reconnectNum
     */
    public void setReconnectNum(int reconnectNum) {
        this.reconnectNum = reconnectNum;
    }

    /**
     * 根据需要可以自己定义修改重连时间
     * @param reconnectIntervalTime
     */
    public void setReconnectIntervalTime(long reconnectIntervalTime) {
        this.reconnectIntervalTime = reconnectIntervalTime;
    }

    //现在连接的状态
    public boolean getConnectStatus() {
        return isConnect;
    }

    public boolean isConnecting() {
        return isConnecting;
    }

    public void setConnectStatus(boolean status) {
        this.isConnect = status;
    }

    public void setListener(ChatListener listener) {
        this.listener = listener;
    }
}
