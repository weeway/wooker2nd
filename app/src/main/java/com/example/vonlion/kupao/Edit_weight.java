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
public class Edit_weight extends Activity {
    String USER_NAME;
    TextView edweight;
    String str;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_weight);
        SharedPreferences share = getSharedPreferences("User_date",Login.MODE_PRIVATE);
        USER_NAME =share.getString("username", "");

        view2 = (TextView)findViewById(R.id.spinnerText2);
        spinner2 = (Spinner)findViewById(R.id.Spinner02);
        adapter2 = new ArrayAdapter<String>(this,R.layout.simple_spinner_item,m2);
        adapter2.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(new SpinnerSelectedListener());
        spinner2.setVisibility(View.VISIBLE);
    }

    public void yesweight(View v){
        DatabaseHelper database = new DatabaseHelper(this);
        SQLiteDatabase db = database.getReadableDatabase();
        ContentValues cv = new ContentValues();
       // edweight = (TextView)findViewById(R.id.Spinner02);

        Cursor cursor = db.query("userdata", null, "username like?", new String[]{USER_NAME}, null, null, "username");

        cv.put("username",USER_NAME);
        cv.put("weight",str);
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

    private static final String[] m2={"40","41","42","43","44","45","46","47","48","49",
            "50","51","52","53","54","55","56","57","58","59",
            "60","61","62","63","64","65","66","67","68","69",
            "70","71","72","73","74","75","76","77","78","79",
            "80","81","82","83","84","85","86","87","88","89",
            "90","91","92","93","94","95","96","97","98","99",
            "100","101","102","103","104","105","106","107","108","109","110"};

    private TextView view2 ;
    private Spinner spinner2;
    private ArrayAdapter<String> adapter2;

    //使用数组形式操作
    class SpinnerSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
//            view.setText("身高  "+m[arg2]+"cm");
            view2.setText("体重  ");
            str = (String) spinner2.getSelectedItem();

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
