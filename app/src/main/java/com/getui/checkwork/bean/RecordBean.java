package com.getui.checkwork.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by wang on 16/7/8.
 */
public class RecordBean extends BmobObject {

    private String date;
    private String week;
    private String time;
    private String imei;

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
