package com.example.vonlion.kupao;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Created by fmq-pc on 2016/2/10.
 */
public class Edit_data extends Activity {
    TextView nickname;
    TextView height;
    TextView weight;
    TextView goal;
    TextView adress;
    TextView age;
    TextView signature;
    String USER_NAME;
    @Override


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_data);
       // Toast.makeText(Edit_data.this.getApplicationContext(), "111", Toast.LENGTH_SHORT).show();
        SharedPreferences share = getSharedPreferences("User_date",Login.MODE_PRIVATE);
        USER_NAME =share.getString("username", "");

        nickname = (TextView)findViewById(R.id.nickname);
        height = (TextView)findViewById(R.id.height);
        weight = (TextView)findViewById(R.id.weight);
        goal = (TextView)findViewById(R.id.goal);
        adress = (TextView)findViewById(R.id.adress);
        age = (TextView)findViewById(R.id.age);
        signature = (TextView)findViewById(R.id. signature);

        DatabaseHelper database = new DatabaseHelper(this);
        SQLiteDatabase db = database.getReadableDatabase();
        Cursor cursor = db.query("userdata", null, "username like?", new String[]{USER_NAME}, null, null, "username");
        if(cursor.getCount()!=0){
            //Toast.makeText(Edit_data.this.getApplicationContext(), "222", Toast.LENGTH_SHORT).show();


                  while(cursor.moveToNext()) {
                      nickname.setText(cursor.getString(cursor.getColumnIndex("nickname")));
                      height.setText(cursor.getString(cursor.getColumnIndex("height"))+"cm");
                      weight.setText(cursor.getString(cursor.getColumnIndex("weight")) + "kg");
                      goal.setText(cursor.getString(cursor.getColumnIndex("goal")) + "步");
                      adress.setText(cursor.getString(cursor.getColumnIndex("adress")));
                      age.setText(cursor.getString(cursor.getColumnIndex("age")));
                      signature.setText(cursor.getString(cursor.getColumnIndex("signature")));
                      //Toast.makeText(Edit_data.this.getApplicationContext(), "555", Toast.LENGTH_SHORT).show();
                  }

        }else{
            //Toast.makeText(Edit_data.this.getApplicationContext(), "111", Toast.LENGTH_SHORT).show();
        }
        db.close();
        cursor.close();
    }

    public void change_alpha(View v){

        
        Intent intent = new Intent(this,main_interface.class);

        startActivity(intent);

        overridePendingTransition(R.anim.out_alpha, R.anim.enter_alpha);
    }

    //    修改头像
    public void change_alpha1(View v){
        Intent intent = new Intent(this,Edit_photo.class);

        startActivity(intent);

        overridePendingTransition(R.anim.out_alpha, R.anim.enter_alpha);
    }

    //    修改姓名
    public void change_alpha2(View v){
        Intent intent = new Intent(this,Edit_name.class);

        startActivity(intent);

        overridePendingTransition(R.anim.out_alpha, R.anim.enter_alpha);
    }

//    修改身高
    public void change_alpha3(View v){
        Intent intent = new Intent(this,Edit_height.class);

        startActivity(intent);

        overridePendingTransition(R.anim.out_alpha, R.anim.enter_alpha);
    }

    //    修改体重
    public void change_alpha4(View v){
        Intent intent = new Intent(this,Edit_weight.class);

        startActivity(intent);

        overridePendingTransition(R.anim.out_alpha, R.anim.enter_alpha);
    }

    //    修改目标
    public void change_alpha5(View v){
        Intent intent = new Intent(this,Edit_target.class);

        startActivity(intent);

        overridePendingTransition(R.anim.out_alpha, R.anim.enter_alpha);
    }

    //    修改地区
    public void change_alpha6(View v){
        Intent intent = new Intent(this,Edit_location.class);

        startActivity(intent);

        overridePendingTransition(R.anim.out_alpha, R.anim.enter_alpha);
    }

    //    修改地址
    public void change_alpha7(View v){
        Intent intent = new Intent(this,Edit_address.class);

        startActivity(intent);

        overridePendingTransition(R.anim.out_alpha, R.anim.enter_alpha);
    }

    //    修改个性签名
    public void change_alpha8(View v){
        Intent intent = new Intent(this,Edit_mark.class);

        startActivity(intent);

        overridePendingTransition(R.anim.out_alpha, R.anim.enter_alpha);
    }
    @Override
    protected void onStop(){
        super.onStop();
        this.finish();
    }
}
