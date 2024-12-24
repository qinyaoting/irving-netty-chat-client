package com.irving.netty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.irving.netty.chat.ChatClient;
import com.irving.netty.chat.adapter.AdapterChatMessage;
import com.irving.netty.chat.handler.ChatManager;
import com.irving.netty.chat.modle.ChatMessage;
import com.irving.netty.chat.protocol.IMMessage;
import com.irving.netty.chat.protocol.MsgActionEnum;
import com.irving.netty.chat.util.JumpUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getSimpleName();

    private Context mContext;


    private Button btn_send; // 发送按钮
    private TextView et_content;

    private TextView mainOnLineUserNumTextView,mainNickNameTextView; // 在线人数、昵称名称

    private ListView listView;
    private List<ChatMessage> chatMessageList = new ArrayList<>();//消息列表
    private AdapterChatMessage adapter_chatMessage; // 聊天适配器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext=MainActivity.this;

        ChatManager.getInstance(handler);

        initView();

    }



    public void initView(){
        Intent intent = getIntent();
        String nickName = intent.getStringExtra("nickName");
        IMMessage imMessage = new IMMessage("LOGIN", new Date().getTime(), nickName, "WebSocket");

        // 登录成功后，跳转聊天界面，发送登录消息              flow-1
        ChatManager.getInstance(handler).sendData(imMessage);

        mainOnLineUserNumTextView = findViewById(R.id.online_user_num);
        mainNickNameTextView = findViewById(R.id.main_nick_name);

        mainNickNameTextView.setText(nickName);

        btn_send = findViewById(R.id.btn_send);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IMMessage imMessage = new IMMessage(MsgActionEnum.CHAT.getName(),new Date().getTime(),mainNickNameTextView.getText().toString(), et_content.getText().toString());
                ChatManager.getInstance(handler).sendData(imMessage);

                et_content.setText("");

            }
        });

        //监听输入框的变化
        et_content = findViewById(R.id.et_content);
        et_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (et_content.getText().toString().length() > 0) {
                    btn_send.setVisibility(View.VISIBLE);
                } else {
                    btn_send.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        findViewById(R.id.iv_return).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IMMessage imMessage = new IMMessage("LOGOUT", new Date().getTime(), mainNickNameTextView.getText().toString(), "WebSocket");

                // 返回，退出登录
                ChatManager.getInstance(handler).sendData(imMessage);
                ChatManager.getInstance(handler).close();
                Toast.makeText(MainActivity.this, "退出成功", Toast.LENGTH_LONG).show();

                JumpUtils.startActivity(com.irving.netty.MainActivity.this, com.irving.netty.LoginActivity.class,null,false);
            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 2) {
                // TODO消息发送成功

            }

            if(msg.what == 3){
                // 系统消息 TODO
                String onlineUsers = msg.getData().getString("onlineUsers"); // 在线用户
                String sysTime = msg.getData().getString("sysTime"); // 系统时间
                String content = msg.getData().getString("content"); // 消息内容

                mainOnLineUserNumTextView.setText(onlineUsers);

                ChatMessage chatMessage=new ChatMessage();
                chatMessage.setContent(content);
                chatMessage.setIsMeSend(2);
                chatMessage.setIsRead(1);
                chatMessage.setTime(sysTime);
                chatMessageList.add(chatMessage);

                initChatMsgListView();

            }if(msg.what == 4){
                // 聊天 TODO
                String sysTime = msg.getData().getString("sysTime"); // 系统时间
                String content = msg.getData().getString("content"); // 消息内容
                String sender = msg.getData().getString("sender"); // 消息内容

                ChatMessage chatMessage=new ChatMessage();
                chatMessage.setContent(content);
                if(sender.equals("you")){
                    chatMessage.setIsMeSend(1);
                    chatMessage.setIsRead(1);
                }else {
                    chatMessage.setIsMeSend(0);
                    chatMessage.setIsRead(0);
                }

                chatMessage.setTime(sysTime);
                chatMessage.setSender(sender);
                chatMessageList.add(chatMessage);

                initChatMsgListView();

            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initChatMsgListView(){
        adapter_chatMessage = new AdapterChatMessage(mContext, chatMessageList);
        listView = findViewById(R.id.chatmsg_listView);
        listView.setAdapter(adapter_chatMessage);
        listView.setSelection(chatMessageList.size());
    }

}