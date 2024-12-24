package com.irving.netty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.irving.netty.chat.ChatClient;
import com.irving.netty.chat.handler.ChatManager;
import com.irving.netty.chat.util.IpPortInfo;
import com.irving.netty.chat.util.JumpUtils;

public class LoginActivity extends AppCompatActivity {

    private TextView ipAddrTextView, portTextView, nickNameTextView;
    private AppCompatCheckBox item_pwd;
    private AppCompatTextView do_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                int statusCode = msg.getData().getInt("statusCode");
                if(statusCode == ChatManager.STATUS_CONNECT_SUCCESS){
                    // 连接成功
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_LONG).show();
                    String nickNameStr = nickNameTextView.getText().toString();
                    Bundle bundle = new Bundle();
                    bundle.putString("nickName", nickNameStr);//往Bundle中存放数据
                    JumpUtils.startActivity(com.irving.netty.LoginActivity.this, com.irving.netty.MainActivity.class, bundle, false);
                }else {
                    Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_LONG).show();
                    ChatManager.getInstance(handler).close();
                }
            }
        }
    };

    private void initView() {
        ipAddrTextView = findViewById(R.id.ip_addr);
        portTextView = findViewById(R.id.port);
        nickNameTextView = findViewById(R.id.nick_name);
        do_login = findViewById(R.id.do_login);
        do_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validParam();
                doLogin();


            }
        });

        // 记住密码
        item_pwd = findViewById(R.id.item_pwd);
        item_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUser();
            }
        });

        // 记住了密码补全用户信息
        fillUser();
    }

    private void fillUser() {
        SharedPreferences p = getSharedPreferences("user", MODE_PRIVATE);
        String ipAddr = p.getString("ipAddr", "192.168.1.137");
        String port = p.getString("port", "8060");
        String nickName = p.getString("nickName", "tom2");
        if (!TextUtils.isEmpty(ipAddr)) {
            ipAddrTextView.setText(ipAddr);
        }

        if (!TextUtils.isEmpty(port)) {
            portTextView.setText(port);
        }

        if (!TextUtils.isEmpty(nickName)) {
            nickNameTextView.setText(nickName);
            item_pwd.setChecked(true);
        }
    }

    /**
     * 保存用户信息
     */
    private void saveUser() {
        SharedPreferences s = getSharedPreferences("user", MODE_PRIVATE);
        SharedPreferences.Editor editor = s.edit();
        boolean b = item_pwd.isChecked();
        if (b) {
            editor.putString("ipAddr", ipAddrTextView.getText().toString());
            editor.putString("port", portTextView.getText().toString());
            editor.putString("nickName", nickNameTextView.getText().toString());
        } else {
            editor.clear();
        }
        editor.apply();
    }

    /**
     * 参数校验
     */
    private void validParam() {
        if (TextUtils.isEmpty(ipAddrTextView.getText())) {
            Toast.makeText(LoginActivity.this, "请输入ip！", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(portTextView.getText())) {
            Toast.makeText(LoginActivity.this, "请输入端口！", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(nickNameTextView.getText())) {
            Toast.makeText(LoginActivity.this, "请输入昵称！", Toast.LENGTH_LONG).show();
            return;
        }
    }

    /**
     * 执行login操作
     */
    private void doLogin() {
        saveUser();
        String ipAddr = ipAddrTextView.getText().toString();
        int port = Integer.parseInt(portTextView.getText().toString());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ChatManager.getInstance(handler).connectNetty(new IpPortInfo(ipAddr, port));
            }
        }, 1000);
    }
}