package com.example.vonlion.kupao;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.slidingmenu.view.SlidingMenu;

/**
 * Created by Vonlion on 2015/11/26.
 */
public class main_interface extends Activity{
    private SlidingMenu mLeftMenu;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    TextView tvName;
    TextView tvHight;
    TextView tvWeight;
    TextView tvAge;
    TextView tvTarget;
    String USER_NAME;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLeftMenu = (SlidingMenu) findViewById(R.id.id_menu);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        SharedPreferences share = getSharedPreferences("User_date",Login.MODE_PRIVATE);
        USER_NAME =share.getString("username", "");

        tvHight = (TextView)findViewById(R.id.tvHigh);
        tvWeight = (TextView)findViewById(R.id.tvWeight);
        tvAge = (TextView)findViewById(R.id.tvAge);
        tvTarget = (TextView)findViewById(R.id.tvTarget);
        tvName = (TextView)findViewById(R.id.name);

        DatabaseHelper database = new DatabaseHelper(this);
        SQLiteDatabase db = database.getReadableDatabase();
        Cursor cursor = db.query("userdata", null, "username like?", new String[]{USER_NAME}, null, null, "username");
        if(cursor.getCount()!=0){
            //Toast.makeText(Edit_data.this.getApplicationContext(), "222", Toast.LENGTH_SHORT).show();

            while(cursor.moveToNext()) {
                tvName.setText(cursor.getString(cursor.getColumnIndex("nickname")));
                tvHight.setText(cursor.getString(cursor.getColumnIndex("height"))+"cm");
                tvWeight.setText(cursor.getString(cursor.getColumnIndex("weight"))+"kg");
                tvAge.setText(cursor.getString(cursor.getColumnIndex("age")));
                tvTarget.setText(cursor.getString(cursor.getColumnIndex("signature"))+"步");

                //Toast.makeText(Edit_data.this.getApplicationContext(), "555", Toast.LENGTH_SHORT).show();
            }

        }else{
            //Toast.makeText(Edit_data.this.getApplicationContext(), "111", Toast.LENGTH_SHORT).show();
        }
        db.close();
        cursor.close();
    }

    public void toggleMenu(View view) {
        mLeftMenu.toggle();
    }

    public void change_alpha0(View v) {//主界面进入资料界面
        Intent intent = new Intent(this, mydata.class);
        startActivity(intent);
        overridePendingTransition(R.anim.out_alpha, R.anim.enter_alpha);
    }

    public void change_alpha(View v) {//注销
        SharedPreferences share = getSharedPreferences("user-password", map.MODE_PRIVATE);
        SharedPreferences.Editor editor = share.edit();
        editor.putBoolean("isCheck",false);
        editor.commit();
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        overridePendingTransition(R.anim.out_alpha, R.anim.enter_alpha);
    }

    public void change_alpha2(View v) {//主界面进入跑步界面
        Intent intent = new Intent(this, map.class);
        startActivity(intent);
        overridePendingTransition(R.anim.out_alpha, R.anim.enter_alpha);
    }


    public void change1(View v) {//左界面跳转到修改资料
        Intent intent = new Intent(this, Edit_data.class);
        startActivity(intent);
        overridePendingTransition(R.anim.out_alpha, R.anim.enter_alpha);
    }

    public void change2(View v) {//左界面跳转到历史界面
        Intent intent = new Intent(this,Histroy.class);
        startActivity(intent);
        overridePendingTransition(R.anim.out_alpha, R.anim.enter_alpha);
    }

    public void change4(View v) {//左界面跳转到我的好友
        Intent intent = new Intent(this,Friends.class);
        startActivity(intent);
        overridePendingTransition(R.anim.out_alpha, R.anim.enter_alpha);
    }

    public void change6(View v) {//左界面跳转到修改资料界面
        Intent intent = new Intent(this, Edit_data.class);
        startActivity(intent);
        overridePendingTransition(R.anim.out_alpha, R.anim.enter_alpha);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "main_interface Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.vonlion.kupao/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "main_interface Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.vonlion.kupao/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

}
