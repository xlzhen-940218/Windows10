package com.baidu.translate.entity;

import java.util.List;

/*
 *百度翻译 返回实体类数据
 */
public class parts {
    private String part_name;
    private String part;
    //private List<means> means;

    public String getPart_name() {
        return part_name;
    }

    public void setPart_name(String part_name) {
        this.part_name = part_name;
    }

    public String getPart() {
        return part;
    }

    public void setPart(String part) {
        this.part = part;
    }

    /*public List<com.baidu.translate.entity.means> getMeans() {
        return means;
    }

    public void setMeans(List<com.baidu.translate.entity.means> means) {
        this.means = means;
    }*/
}
