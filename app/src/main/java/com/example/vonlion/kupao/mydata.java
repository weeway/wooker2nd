package com.example.vonlion.kupao;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Created by fmq-pc on 2016/1/30.
 */
public class mydata extends Activity {
    private static final String FILE_NAME = "MyData";
    private SharedPreferences mydata;
    private TextView tvWeight;
    private TextView tvHigh;
    private TextView tvBMI;
    private TextView tvTarget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mydata);
        mydata = getSharedPreferences(FILE_NAME,0);
        tvBMI = (TextView) findViewById(R.id.tvBMI);
        tvHigh = (TextView) findViewById(R.id.tvHigh);
        tvTarget = (TextView) findViewById(R.id.tvTarget);
        tvWeight = (TextView) findViewById(R.id.tvWeight);
    }

    public void change_alpha(View v){
        Intent intent = new Intent(this,main_interface.class);

        startActivity(intent);

        overridePendingTransition(R.anim.out_alpha, R.anim.enter_alpha);
    }

    public void change_alpha2(View v){
        Intent intent = new Intent(this,Edit_data.class);

        startActivity(intent);

        overridePendingTransition(R.anim.out_alpha, R.anim.enter_alpha);
    }

    public void storeData(){
        SharedPreferences.Editor mydataEditor = mydata.edit();
        mydataEditor.putString("weight",tvWeight.getText().toString()
                    .split("k")[0]);
        mydataEditor.putString("high",tvHigh.getText().toString()
                    .split("c")[0]);
        mydataEditor.putString("target",tvTarget.getText().toString()
                    .split("步")[0]);
        mydataEditor.putString("BMI",tvBMI.getText().toString()
                     );
        mydataEditor.apply();
    }

}


