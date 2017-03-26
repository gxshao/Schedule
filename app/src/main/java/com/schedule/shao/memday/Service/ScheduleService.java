package com.schedule.shao.memday.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.schedule.shao.memday.MainActivity;
import com.schedule.shao.memday.Obj.Schedule;
import com.schedule.shao.memday.R;
import com.schedule.shao.memday.Utils.DateUtils;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class ScheduleService extends Service {
    private List<Schedule> mData = new ArrayList<>();
    private List<Long> mTimes = new ArrayList<>();
    static Context context;
    android.os.Handler handler = new android.os.Handler();
    private Thread mThread;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle bundle = intent.getBundleExtra("obj");
        mData = (List<Schedule>) bundle.getSerializable("list");
        if(mData.size()>0)
        {
            //stopSelf();
        Collections.sort(mData, new Comparator<Schedule>() {
            @Override
            public int compare(Schedule lhs, Schedule rhs) {
                Date date1 = DateUtils.stringToDate(lhs.getAlertTime().toString());
                Date date2 = DateUtils.stringToDate(rhs.getAlertTime().toString());
                if (date1.after(date2)) {
                    return 1;
                }
                return -1;
            }
        });
        for (Schedule temp : mData) {
            Date target = DateUtils.stringToDate(temp.getAlertTime().toString());
            Long keyTime = target.getTime() - new Date(System.currentTimeMillis()).getTime();
            mTimes.add(keyTime);
        }
        mThread = new Thread(ClockSystem);
        mThread.start();

        }
        return super.onStartCommand(intent, flags, startId);
    }

    Runnable ClockSystem = new Runnable() {
        @Override
        public void run() {
            for (int i = 0; i < mData.size(); i++) {
                if (mTimes.get(i) <= 0) {
                    continue;
                }
                if (mData.get(i).getIsAlert()== 0) {
                    continue;
                }
                final int x = i;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        NotifyEvent(mData.get(x));
                    }
                }, mTimes.get(i));
            }
        }
    };

    private synchronized void NotifyEvent(Schedule schedule) {
        NotificationManager manager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(context);
        PendingIntent contentIntent = PendingIntent.getActivities(context, 0,
        new Intent[]{new Intent(context, MainActivity.class)}, PendingIntent.FLAG_CANCEL_CURRENT);
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setContentIntent(contentIntent)
                .setSmallIcon(R.mipmap.logo)         //设置状态栏里面的图标（小图标）
                .setTicker("提醒")//设置状态栏的显示的信息
                .setWhen(System.currentTimeMillis())        //设置时间发生时间
                .setAutoCancel(true)                        //设置可以清除
                .setContentTitle("纪念日提醒")    //设置下拉列表里的标题
                .setContentText(schedule.getContent())
                .setVibrate(new long[]{1000,1000,1000,1000,1000,1000})
                .setLights(Color.RED,3000,3000)
                .setSound(uri);     //设置上下文内容

        Notification notification = builder.getNotification();
        manager.notify(1, notification);
    }


    @Override
    public void onCreate() {
        context = this;
        Toast.makeText(this, "纪念日提醒服务创建成功", Toast.LENGTH_SHORT).show();

        super.onCreate();
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Toast.makeText(context,"提醒服务已关闭，若需要使用请重启APP",Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }
}
