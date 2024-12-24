package com.irving.netty.chat.util;

import android.text.TextUtils;

import com.irving.netty.chat.protocol.IMMessage;
import com.irving.netty.chat.protocol.MsgActionEnum;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author luojun
 * @version 1.0.0
 * @ClassName CorderUtil
 * @Description 自定义编解码工具类
 * @createTime 2021/11/14 2:50
 */
public class CoderUtil {

    // 消息头 -
    private static Pattern pattern = Pattern.compile("^\\[(.*)\\](\\s\\-\\s(.*))?");


    public static IMMessage decode(String msg){
        if(TextUtils.isEmpty(msg)){
            return null;
        }
        try{
            Matcher m = pattern.matcher(msg);
            // [命令][命令发送时间][命令发送人][终端类型] - 内容
            String header = "";
            String content = "";
            if(m.matches()){
                header = m.group(1);
                content = m.group(3);
            }
            String[] headers = header.split("\\]\\[");
            long time = 0;
            try{
                time =  Long.parseLong(headers[1]);
            }catch (Exception e){
                System.err.println("时间转化出现异常：" + e);
            }
            String cmd = headers[0];


            if(msg.startsWith("[" + MsgActionEnum.SYSTEM.getName() + "]") ||
                    msg.startsWith("[" + MsgActionEnum.SYSTEM.getName() + "]")){
                return new IMMessage(cmd,Long.parseLong(headers[1]),Integer.parseInt(headers[2]),content);
            }else if(msg.startsWith("[" + MsgActionEnum.CHAT.getName() + "]")){
                String sender = headers[2];
                return new IMMessage(cmd,time,sender,content);
            }else if(msg.startsWith("[" + MsgActionEnum.FLOWER.getName() + "]")){
                return new IMMessage(cmd,headers[3],time,content);
            }else if(msg.startsWith("[" + MsgActionEnum.KEEPALIVE.getName() + "]") ||
                    msg.startsWith("[" + MsgActionEnum.KEEPALIVE.getName() + "]")){
                return new IMMessage(cmd,headers[3],time,content);
            }else{
                return null;
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    /***
     * 将IMMessage对象编码成指定的自定义协议字符串
     * @param msg
     * @return
     */
    public static String encode(IMMessage msg){
        if(null == msg){
            return "";
        }
        String cmd = msg.getCmd();
        String sender = msg.getSender();
        String prex = "[" +  cmd +"]" + "[" + msg.getTime() +"]";
        if(MsgActionEnum.LOGIN.getName().equals(cmd) || MsgActionEnum.LOGOUT.getName().equals(cmd) || MsgActionEnum.FLOWER.getName().equals(cmd)){
            prex += ("[" + sender + "][" + msg.getTerminal() + "]");
        }else if(MsgActionEnum.CHAT.getName().equals(cmd) ){
            prex += ("[" + sender + "]");
        }else if(MsgActionEnum.SYSTEM.getName().equals(cmd)){
            prex += ("[" + msg.getOnline() + "]");
        }
        if (!TextUtils.isEmpty(msg.getContent())) {
            prex += (" - " + msg.getContent());
        }
        return prex;
    }
}