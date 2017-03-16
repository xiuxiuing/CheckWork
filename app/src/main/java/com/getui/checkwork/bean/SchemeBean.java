package com.getui.checkwork.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by wang on 16/10/25.
 */
public class SchemeBean extends BmobObject {
    private String imei;
    private String uri;
    private String brand;

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
