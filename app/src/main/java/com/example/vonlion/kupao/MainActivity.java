package com.example.vonlion.kupao;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


public class MainActivity extends Activity{
    private final int SPLASH_DISPLAY_LENGHT = 2500; //延迟2.5秒

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.index);
        new Handler().postDelayed(new Runnable(){

            @Override
            public void run() {
                Intent mainIntent = new Intent(MainActivity.this,Login.class);
                MainActivity.this.startActivity(mainIntent);
//                MainActivity.this.finish();
                overridePendingTransition(R.anim.out_alpha, R.anim.enter_alpha);
            }

        }, SPLASH_DISPLAY_LENGHT);
    }

    @Override
    protected void onStop(){
        super.onStop();
        this.finish();
    }
}