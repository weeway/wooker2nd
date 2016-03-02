package com.example.vonlion.kupao;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ParseException;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;

/**
 * Created by Vonlion on 2016/1/5.
 */
public class RegisterActivity extends Activity {

    private EditText username;
    private EditText password;
    private EditText password1;
    private String msg;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        password1 = (EditText)findViewById(R.id.password1);

        findViewById(R.id.traceroute_rootview2).setOnClickListener(new View.OnClickListener() {
            //点击屏幕外取消输入框
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.traceroute_rootview2:
                        InputMethodManager imm = (InputMethodManager)
                                getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        break;
                }

            }
        });
    }

    public void change_alpha(View v){//注册老用户返回登录界面
        Intent intent = new Intent(this, Login.class);

        startActivity(intent);

        overridePendingTransition(R.anim.out_alpha, R.anim.enter_alpha);
    }


   public void change_roll(View v) throws ParseException, IOException, JSONException {
       final String name = username.getText().toString();
       final String pwd = password.getText().toString();
       final String pwd1 = password1.getText().toString();
       final Intent intent = new Intent(this, MainActivity.class);
       if(!pwd.equals(pwd1)){
           Toast.makeText(getApplicationContext(), "两次输入密码不一致", Toast.LENGTH_SHORT).show();
           return;
       }
       else {
           new Thread(new Runnable(){
               @Override
               public void run() {
                   Get get =new Get();
                   try {
                       msg = get.loginb("http://115.159.120.123:8080/LoginServer/RegisterServer?username="+name+"&&password="+pwd);
                   } catch (IOException e) {
                       // TODO Auto-generated catch block
                       e.printStackTrace();
                   }
                   Looper.prepare();
                   Toast.makeText(getApplicationContext(), msg,
                           Toast.LENGTH_SHORT).show();

					 if(msg.equals("注册成功")){
                         startActivity(intent);
                         overridePendingTransition(R.anim.lefttoright, R.anim.righttoleft);
					 }

                   Looper.loop();
               }
           }).start();
       }
   }

}
