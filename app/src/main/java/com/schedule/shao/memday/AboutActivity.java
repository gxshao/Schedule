package com.schedule.shao.memday;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.schedule.shao.memday.Utils.ScheduleSQLOperation;

public class AboutActivity extends AppCompatActivity {
    ActionBar mActionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        mActionBar = getSupportActionBar();
        mActionBar.setCustomView(R.layout.detialbar);
        mActionBar.setDisplayOptions(getSupportActionBar().DISPLAY_SHOW_CUSTOM);
        TextView tv_title= (TextView) findViewById(R.id.bar_title);
        tv_title.setText("关于软件");
        ImageView iv_back= (ImageView) findViewById(R.id.btn_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AboutActivity.this.finish();
            }
        });
    }
    public void CleanGes(View v){
        SharedPreferences sp=this.getSharedPreferences("config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("key1","");
        editor.putString("key2","");
        editor.putBoolean("isPass",false);
        editor.commit();
        Toast.makeText(this,"密码清除成功",Toast.LENGTH_SHORT).show();
    }
    public void CleanData(View v){
        ScheduleSQLOperation.delete("delete from Schedule");
        Toast.makeText(this,"数据库清除成功",Toast.LENGTH_SHORT).show();
    }
}
