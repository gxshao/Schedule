package com.schedule.shao.memday;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.schedule.shao.memday.Obj.Schedule;
import com.schedule.shao.memday.Utils.ScheduleSQLOperation;

import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.HashMap;

public class Detials extends AppCompatActivity {
    android.support.v7.app.ActionBar mActionBar;
    ImageView btn_back;
    HashMap<String, Object> map;
    TextView btn_DatePick;
    Button btn_save;
    EditText et_Content;
    Switch btn_isAlert;
    String mAlertTime,mPrior,Content,sCode;
    PopupWindow mPopupWindow;
    String mYear, mMonth, mDay, mHour, mMinutes;
    Calendar cal = Calendar.getInstance();
    int isAlert;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detials);
        Intent intent = this.getIntent();
        Bundle bundle = intent.getBundleExtra("obj");
        map = (HashMap<String, Object>) bundle.get("map");
        mActionBar = getSupportActionBar();
        mActionBar.setCustomView(R.layout.detialbar);
        mActionBar.setDisplayOptions(getSupportActionBar().DISPLAY_SHOW_CUSTOM);
        btn_back = (ImageView) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Detials.this.finish();
            }
        });
        btn_DatePick= (TextView) findViewById(R.id.DatePick);
        btn_DatePick.setOnClickListener(tvOnclickListener);
        btn_isAlert= (Switch) findViewById(R.id.btn_isAlert);
        btn_isAlert.setOnCheckedChangeListener(saveListener);
        btn_save= (Button) findViewById(R.id.Save);
        btn_save.setOnClickListener(SaveContent);
        et_Content = (EditText) findViewById(R.id.et_Content);
        loadData();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(menu!=null)
        {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try{
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                    Log.e(getClass().getSimpleName(), "onMenuOpened...unable to set icons for overflow menu", e);
                }
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void loadData() {
        mAlertTime= (String) map.get("AlertTime");
        isAlert= (int) map.get("isAlert");
        btn_isAlert.setChecked(isAlert==1?true:false);
        Content= (String) map.get("Content");
        sCode= (String) map.get("sCode");
        btn_DatePick.setText(mAlertTime);
        et_Content.setText(Content);
    }
    View.OnClickListener SaveContent=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ModData();
        }
    };
    Switch.OnCheckedChangeListener saveListener = new Switch.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                isAlert = 1;
            } else {
                isAlert = 0;
            }
        }


    };
    private void ModData(){
        if (et_Content.getText().length() <= 0) {
            Toast.makeText(this, "请填写内容", Toast.LENGTH_SHORT).show();
            return;
        }
        Schedule add = new Schedule(sCode + "",
                et_Content.getText().toString().trim(),
                et_Content.getText().toString().trim(),
                mAlertTime,
                isAlert,
                mAlertTime,
                0);
        boolean flag=ScheduleSQLOperation.ModSchedule(add);
        if(flag)
        {
            Toast.makeText(this,"修改成功",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,"修改失败",Toast.LENGTH_SHORT).show();
        }
    }
    View.OnClickListener tvOnclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ShowTimeDialog().show();
        }
    };
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.SaveModified:
                ModData();
                break;
            case R.id.Delete:
                if(ScheduleSQLOperation.DelScheduleByScode(sCode))
                {
                    Toast.makeText(this,"删除成功",Toast.LENGTH_SHORT).show();
                    this.finish();
                }else {
                    Toast.makeText(this,"删除失败",Toast.LENGTH_SHORT).show();
                }

            break;
            default:break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        setOverflowIconVisible(featureId, menu);
        return super.onMenuOpened(featureId, menu);
    }

    private void setOverflowIconVisible(int featureId, Menu menu) {
        if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                    Log.d("OverflowIconVisible", e.getMessage());
                }
            }
        }
    }

    public DatePickerDialog.OnDateSetListener mDateListenerDialog = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year + "";
            mMonth = (monthOfYear + 1) + "";
            mDay = dayOfMonth + "";
            btn_DatePick.setText(year + "年" + (monthOfYear + 1) + "月" + dayOfMonth + "日");
            new TimePickerDialog(Detials.this, mTimeListenerDialog, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show();
        }
    };
    public TimePickerDialog.OnTimeSetListener mTimeListenerDialog = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            mMinutes = minute + "";
            mHour = hourOfDay + "";
            mAlertTime = mYear + "-" + mMonth + "-" + mDay + " " + mHour + ":" + mMinutes;
            Toast.makeText(Detials.this, mAlertTime, Toast.LENGTH_SHORT).show();
        }
    };

    private Dialog ShowTimeDialog() {
        return new DatePickerDialog(this, mDateListenerDialog, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
    }
}
