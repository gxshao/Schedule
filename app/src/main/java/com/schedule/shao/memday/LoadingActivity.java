package com.schedule.shao.memday;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class LoadingActivity extends Activity {
    SharedPreferences sp;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        sp=this.getSharedPreferences("config", Context.MODE_PRIVATE);

        hand.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                     intent=new Intent(LoadingActivity.this,LoginActivity.class);
                startActivity(intent);
                LoadingActivity.this.finish();
            }
        },2000);

    }
    Handler hand=new Handler();
}
