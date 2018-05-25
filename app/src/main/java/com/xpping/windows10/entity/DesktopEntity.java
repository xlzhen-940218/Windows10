package com.xpping.windows10.entity;

/**
 * Created by xlzhen on 9/12 0012.
 * 桌面 实体类
 */

public class DesktopEntity extends AppEntity {
    private DesktopType desktopType;
    private String appIcon;


    public String getAppIcon() {
        return appIcon;
    }


    public void setAppIcon(String appIcon) {
        this.appIcon = appIcon;
    }

    public DesktopType getDesktopType() {
        return desktopType;
    }

    public void setDesktopType(DesktopType desktopType) {
        this.desktopType = desktopType;
    }

    public enum DesktopType{
        system,app
    }
}
