package com.xpping.windows10.entity;

import android.graphics.drawable.Drawable;

/**
 * Created by xlzhen on 9/11 0011.
 * app实体类
 */

public class AppEntity {

    private String appTitle;
    private String appPackage;
    private String headName;
    private Drawable appDrawable;

    public String getAppTitle() {
        return appTitle;
    }

    public void setAppTitle(String appTitle) {
        this.appTitle = appTitle;
    }

    public String getAppPackage() {
        return appPackage;
    }

    public void setAppPackage(String appPackage) {
        this.appPackage = appPackage;
    }

    public String getHeadName() {
        return headName;
    }

    public void setHeadName(String headName) {
        this.headName = headName;
    }

    public Drawable getAppDrawable() {
        return appDrawable;
    }

    public void setAppDrawable(Drawable appDrawable) {
        this.appDrawable = appDrawable;
    }
}
