package com.schedule.shao.memday.Utils;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;


public class SysFunction {
    public static void SendMsg(String phone, String message, Context context){
        PendingIntent pi = PendingIntent.getActivity(context, 0, new Intent("send"), 00);
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phone, null, message, pi, null);

    }
}
