package com.baidu.translate.entity;

/*
 *百度翻译 返回实体类数据
 */
public class tags {
    private String[] core;
    private String[] other;

    public String[] getCore() {
        return core;
    }

    public void setCore(String[] core) {
        this.core = core;
    }

    public String[] getOther() {
        return other;
    }

    public void setOther(String[] other) {
        this.other = other;
    }
}
