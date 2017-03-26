package com.schedule.shao.memday;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;

public class CalendarActivity extends AppCompatActivity {
    TextView tv_date;
    ActionBar mActionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_activiy);
        tv_date= (TextView) findViewById(R.id.tv_date);
        Calendar cal=Calendar.getInstance();
        tv_date.setText(cal.get(Calendar.YEAR)+"年"+(cal.get(Calendar.MONTH)+1)+"月"+cal.get(Calendar.DATE)+"日  "+cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE));
        mActionBar = getSupportActionBar();
        mActionBar.setCustomView(R.layout.detialbar);
        mActionBar.setDisplayOptions(getSupportActionBar().DISPLAY_SHOW_CUSTOM);
        TextView tv_title= (TextView) findViewById(R.id.bar_title);
        tv_title.setText("日历");
        ImageView iv_back= (ImageView) findViewById(R.id.btn_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarActivity.this.finish();
            }
        });
    }
}
