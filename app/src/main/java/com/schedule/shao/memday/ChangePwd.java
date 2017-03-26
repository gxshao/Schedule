package com.schedule.shao.memday;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.schedule.shao.memday.Obj.MyInfo;
import com.schedule.shao.memday.Utils.ScheduleSQLOperation;

public class ChangePwd extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);
        ScheduleSQLOperation.CreateDatebase(this);
        Button btn= (Button) findViewById(R.id.btn_chpwd);
        btn.setOnClickListener(CLICK);

    }

    OnClickListener CLICK=new OnClickListener(){
        @Override
        public void onClick(View v) {
            EditText old,npwd,cpwd;
            old= (EditText) findViewById(R.id.curpwd);
            npwd= (EditText) findViewById(R.id.newpwd);
            cpwd= (EditText) findViewById(R.id.confirm);
            String opwd=old.getText().toString(),newpwd=npwd.getText().toString(),conf=cpwd.getText().toString();
            if(newpwd.equals("")||opwd.equals("")||conf.equals(""))
            {
                Toast.makeText(ChangePwd.this,"不能为空！",Toast.LENGTH_SHORT).show();
                return;
            }
            if(!opwd.equals(MyInfo.password))
            {
                Toast.makeText(ChangePwd.this,"旧密码不正确！",Toast.LENGTH_SHORT).show();
                return;
            }
            if(!newpwd.equals(conf))
            {
                Toast.makeText(ChangePwd.this,"两次输入密码不一致！",Toast.LENGTH_SHORT).show();
                return;
            }
            ScheduleSQLOperation.ChangePassword(newpwd);
            Toast.makeText(ChangePwd.this,"修改成功,即将返回重新登录！",Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(ChangePwd.this,LoginActivity.class);
            startActivity(intent);
            ChangePwd.this.finish();
        }
    };
}
