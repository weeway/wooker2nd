package com.example.vonlion.kupao;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ParseException;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.slidingmenu.view.SlidingMenu;

import org.json.JSONException;

import java.io.IOException;

/**
 * Created by fmq-pc on 2016/2/9.
 */
public class Login extends Activity {

    private EditText username;
    private EditText password;
    String msg="登录超时";
    private SlidingMenu mLeftMenu;
    private CheckBox cbStorePass;
    private Button bt;

    //public  static String  USER_NAME;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        cbStorePass = (CheckBox) findViewById(R.id.cbStorePass);
        mLeftMenu = (SlidingMenu)findViewById(R.id.id_menu);
        bt = (Button) findViewById(R.id.button_left);
        SharedPreferences sharedPreferences = getSharedPreferences("user-password",map.MODE_PRIVATE);
        findViewById(R.id.traceroute_rootview).setOnClickListener(new View.OnClickListener() {
            //点击屏幕外取消输入框
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.traceroute_rootview:
                        InputMethodManager imm = (InputMethodManager)
                                getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        break;
                }
            }
        });

        if(sharedPreferences.contains("username")) {
            username.setText(sharedPreferences.getString("username", ""));
            password.setText(sharedPreferences.getString("password", ""));
        }
        if(sharedPreferences.contains("username")&&sharedPreferences.contains("password")
                &&sharedPreferences.getBoolean("isCheck",false)){

            try {
                change_roll(bt);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public void toggleMenu(View view){
        mLeftMenu.toggle();
    }

    public void change_alpha(View v){//登录跳转注册
        Intent intent = new Intent(this, RegisterActivity.class);

        startActivity(intent);

        overridePendingTransition(R.anim.out_alpha, R.anim.enter_alpha);
    }

    //登录进入主界面
    public void change_roll(View v) throws ParseException, IOException, JSONException {
        final String name = this.username.getText().toString();
        final String pwd = this.password.getText().toString();

        SharedPreferences share = getSharedPreferences("user-password", map.MODE_PRIVATE);
        SharedPreferences.Editor editor = share.edit();
        editor.putString("username",name);
        editor.putString("password",pwd);
        editor.putBoolean("isCheck",cbStorePass.isChecked()?true:false);
        editor.commit();

        final Intent intent = new Intent(this, main_interface.class);
        (new Thread(new Runnable() {
            public void run() {
                Get get = new Get();
                try {
                    msg = get.loginb("http://115.159.120.123:8080/LoginServer/ReceiveServers?username=" + name + "&&password=" + pwd);
                } catch (IOException var3) {
                    var3.printStackTrace();
                }
                Looper.prepare();
                Toast.makeText(Login.this.getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                if(msg.equals("登陆成功")){
                   // USER_NAME = name;
                   //将用户名用SharedPreferences存储
                    SharedPreferences sharedPreferences = getSharedPreferences("User_date",Login.MODE_PRIVATE); //私有数据
                    SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
                    editor.putString("username",name);
                    editor.commit();//提交修改

                    startActivity(intent);

                    overridePendingTransition(R.anim.lefttoright, R.anim.righttoleft);
                }
                Looper.loop();
            }
        })).start();
    }

    @Override
    protected void onStop(){
        super.onStop();
        this.finish();
    }
}
