package com.xpping.windows10.entity;

import com.xpping.windows10.fragment.base.BaseFragment;

public class TaskBarEntity {
    private BaseFragment baseFragment;
    private String icon;
    private String name;
    private boolean isShow;
    private boolean isOpen;

    public TaskBarEntity() {
    }

    public TaskBarEntity(BaseFragment baseFragment) {
        this.baseFragment = baseFragment;
        this.icon =""+ baseFragment.getIcon();
        this.name = baseFragment.getName();
    }

    public TaskBarEntity(String icon, String name,boolean isOpen) {
        this.icon = icon;
        this.name = name;
        this.isOpen=isOpen;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    public BaseFragment getBaseFragment() {
        return baseFragment;
    }

    public void setBaseFragment(BaseFragment baseFragment) {
        this.baseFragment = baseFragment;
    }
}
