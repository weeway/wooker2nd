package com.example.vonlion.kupao;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by Vonlion on 2016/2/11.
 */
public class Edit_height extends Activity {
    String USER_NAME;
    TextView edheight;
    String str;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_height);

        SharedPreferences share = getSharedPreferences("User_date",Login.MODE_PRIVATE);
        USER_NAME =share.getString("username", "");

        view = (TextView) findViewById(R.id.spinnerText);
        spinner = (Spinner) findViewById(R.id.Spinner01);

        //将可选内容与ArrayAdapter连接起来
        adapter = new ArrayAdapter<String>(this,R.layout.simple_spinner_item,m);

        //设置下拉列表的风格
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);

        //将adapter 添加到spinner中
        spinner.setAdapter(adapter);

        //添加事件Spinner事件监听
        spinner.setOnItemSelectedListener(new SpinnerSelectedListener());

        //设置默认值
        spinner.setVisibility(View.VISIBLE);
    }

    public void yesheight(View v){
        DatabaseHelper database = new DatabaseHelper(this);
        SQLiteDatabase db = database.getReadableDatabase();
        ContentValues cv = new ContentValues();
        //edheight = (TextView)findViewById(R.id.Spinner01);

        Cursor cursor = db.query("userdata", null, "username like?", new String[]{USER_NAME}, null, null, "username");

        cv.put("username",USER_NAME);
        cv.put("height",str);
        if(cursor.getCount()!=0) {
            db.update("userdata", cv, "username=?", new String[]{USER_NAME});
        }else {
            db.insert("userdata", null, cv);
        }
        cv.clear();
        cursor.close();
        Intent intent = new Intent(this,Edit_data.class);

        startActivity(intent);

        overridePendingTransition(R.anim.out_alpha, R.anim.enter_alpha);
    }


    public void change_alpha(View v){
        Intent intent = new Intent(this,Edit_data.class);

        startActivity(intent);

        overridePendingTransition(R.anim.out_alpha, R.anim.enter_alpha);
    }

    private static final String[] m={"150","151","152","153","154","155","156","157","158","159",
            "160","161","162","163","164","165","166","167","168","169",
            "170","171","172","173","174","175","176","177","178","179",
            "180","181","182","183","184","185","186","187","188","189",
            "190","191","192","193","194","195","196","197","198","199","200"};

    private TextView view ;
    private Spinner spinner;
    private ArrayAdapter<String> adapter;

    //使用数组形式操作
    class SpinnerSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
//            view.setText("身高  "+m[arg2]+"cm");
            view.setText("身高  ");
            str = (String) spinner.getSelectedItem();
        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }
    @Override
    protected void onStop(){
        super.onStop();
        this.finish();
    }
}
