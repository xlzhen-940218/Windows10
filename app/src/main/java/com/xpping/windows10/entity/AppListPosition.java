package com.xpping.windows10.entity;

/**
 * Created by xlzhen on 9/12 0012.
 * app列表 快速定位
 */

public class AppListPosition {
    private String text;
    private int position;

    public AppListPosition(String text, int position) {
        this.text = text;
        this.position = position;
    }

    public AppListPosition() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
