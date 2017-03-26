package com.schedule.shao.memday.Utils;

import java.sql.Date;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;

public class DateUtils {

    public static Date stringToDate(String dateString) {
        ParsePosition position = new ParsePosition(0);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        java.util.Date dateValue = null;
        dateValue =simpleDateFormat.parse(dateString,position);
        return new java.sql.Date(dateValue.getTime());
    }

}