package com.schedule.shao.memday.Utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.schedule.shao.memday.Obj.Schedule;

import java.util.ArrayList;
import java.util.List;

public class ScheduleSQLOperation {
    private static ScheduleSQLiteOpenHelper helper;
    private static SQLiteDatabase db;

    public static void CreateDatebase(Context context) {
        helper = new ScheduleSQLiteOpenHelper(context, "Schedule.db", null, 1);
    }
    public static int getCode(){
        db=helper.getReadableDatabase();
        int result=0;
        try{
            Cursor cursor=db.rawQuery("select count(scode) from schedule",new String[]{});
            while (cursor.moveToNext())
            {
                result=cursor.getInt(0);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
    /**
     * 添加日程
     */
    public static boolean AddSchedule(Schedule obj) {
        db = helper.getWritableDatabase();
        boolean flag=false;
        String sql = "insert into Schedule (SCode,STitle,Content,AlertTime,isAlert,ModTime,State) values(?,?,?,?,?,?,?)";
        try {
            db.execSQL(sql, new Object[]{obj.getsCode(), obj.getsTitle(), obj.getContent(), obj.getAlertTime(), obj.getIsAlert(), obj.getModTime(), obj.getState()});
            flag=true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return flag;
    }

    public static boolean ModSchedule(Schedule obj) {
        db = helper.getWritableDatabase();
        boolean flag=false;
        String sql = "update Schedule set STitle='" + obj.getsTitle() + "',Content='" +
                obj.getContent() + "',AlertTime='" + obj.getAlertTime() + "',isAlert='" + obj.getIsAlert() + "',ModTime='" + obj.getModTime() + "',State='" + obj.getState() + "' where Scode='" + obj.getsCode() + "' ";
        try {
            db.execSQL(sql);
            flag=true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return flag;
    }
    public static boolean DelScheduleByScode(String SCode) {
        return delete(" Delete from Schedule where SCode='" + SCode + "'");
    }
    public static boolean delete(String sql) {
        boolean flag=false;
        db = helper.getWritableDatabase();
        try {
            db.execSQL(sql);
            flag=true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return flag;
    }

    public static List<Schedule> QueryToday(String date, int state) {
        String sql = "select * from Schedule where AlertTime like '%" + date + "%' and state=" + state;
        return QueryData(sql);
    }
    public static List<Schedule> QueryMonth(String date) {
        String sql = "select * from Schedule where AlertTime like '%" + date+ "%'";
        return QueryData(sql);
    }
    public static List<Schedule> QueryData(String sql) {
        db = helper.getReadableDatabase();
        List<Schedule> list = new ArrayList<>();
        Cursor cursor=null;
        try {

             cursor = db.rawQuery(sql, new String[]{});
            while (cursor.moveToNext()) {
                Schedule temp = new Schedule(cursor.getString(cursor.getColumnIndex("SCode")),
                        cursor.getString(cursor.getColumnIndex("STitle")),
                        cursor.getString(cursor.getColumnIndex("Content")),
                        cursor.getString(cursor.getColumnIndex("AlertTime")),
                        cursor.getInt(cursor.getColumnIndex("isAlert")),
                        cursor.getString(cursor.getColumnIndex("ModTime")),
                        cursor.getInt(cursor.getColumnIndex("State")));
                list.add(temp);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            cursor.close();
        }
        return list;
    }


    public static boolean Login(String uid,String pwd){
        db = helper.getReadableDatabase();
        boolean flag=false;

        try {
           Cursor cur= db.rawQuery("select * from user where uid='"+uid+"' and pwd='"+pwd+"'",new String[]{});
            if(cur.getCount()>0)
                flag=true;
            cur.close();
        }catch (Exception e)
        {
         e.printStackTrace();
        }

        return flag;
    }

    public static void ChangePassword(String pwd){
        db = helper.getReadableDatabase();
       db.execSQL("update user set pwd='"+pwd+"' where uid='admin'");
    }

}
