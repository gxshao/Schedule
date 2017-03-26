package com.schedule.shao.memday.Utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class ScheduleSQLiteOpenHelper extends SQLiteOpenHelper {


    public ScheduleSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table Schedule (ID INTEGER PRIMARY KEY AUTOINCREMENT,SCode varchar(20),STitle varchar(50),Content varchar(200),AlertTime Datetime,isAlert INTEGER,ModTime Datetime,State INTEGER)");
        db.execSQL("create table user (ID INTEGER PRIMARY KEY AUTOINCREMENT ,uid varchar(20),pwd varchar(20),name varchar(50))");
        db.execSQL("insert into user (uid,pwd) values ('admin','admin')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
