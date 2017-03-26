package com.schedule.shao.memday;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.schedule.shao.memday.Obj.MyInfo;
import com.schedule.shao.memday.Utils.ScheduleSQLOperation;

public class LoginActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ScheduleSQLOperation.CreateDatebase(this);
    }
    public void Login(View V){
        String uid=((EditText)this.findViewById(R.id.uid)).getText().toString().trim();
        String pwd=((EditText)this.findViewById(R.id.pwd)).getText().toString().trim();
        if(ScheduleSQLOperation.Login(uid,pwd))
        {
            MyInfo.password=pwd;
            Toast.makeText(LoginActivity.this,"登录成功正在跳转!",Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
            this.finish();
        }else
        {
            Toast.makeText(LoginActivity.this,"登录失败!",Toast.LENGTH_SHORT).show();
        }
    }
}
