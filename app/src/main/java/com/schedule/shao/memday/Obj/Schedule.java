package com.schedule.shao.memday.Obj;

import java.io.Serializable;



public class Schedule implements Serializable{
    public String getsCode() {
        return sCode;
    }

    public void setsCode(String sCode) {
        this.sCode = sCode;
    }

    public String getsTitle() {
        return sTitle;
    }

    public void setsTitle(String sTitle) {
        this.sTitle = sTitle;
    }
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAlertTime() {
        return alertTime;
    }

    public void setAlertTime(String alertTime) {
        this.alertTime = alertTime;
    }

    public int getIsAlert() {
        return isAlert;
    }

    public void setIsAlert(int isAlert) {
        this.isAlert = isAlert;
    }

    public String getModTime() {
        return modTime;
    }

    public void setModTime(String modTime) {
        this.modTime = modTime;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    private String sCode;
    private String sTitle;
    private String content;
    private String alertTime;
    private int isAlert;
    private String modTime;
    private int state;

    public Schedule(String sCode, String sTitle,  String content, String alertTime, int isAlert, String modTime, int state) {
        this.sCode = sCode;
        this.sTitle = sTitle;
        this.content = content;
        this.alertTime = alertTime;
        this.isAlert = isAlert;
        this.modTime = modTime;
        this.state = state;
    }
}
