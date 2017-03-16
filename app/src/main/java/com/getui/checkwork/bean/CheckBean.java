package com.getui.checkwork.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by wang on 16/7/11.
 */
public class CheckBean extends BmobObject {

    private String date;
    private String week;
    private String early;
    private String last;
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

    public String getEarly() {
        return early;
    }

    public void setEarly(String early) {
        this.early = early;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }
}
