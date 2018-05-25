package com.baidu.translate.entity;

/*
 *百度翻译 返回实体类数据
 */
public class means {
    private String has_mean;
    private String[] means;
    private String text;
    private String part;
    private String split;
    private String word_mean;

    public String getHas_mean() {
        return has_mean;
    }

    public void setHas_mean(String has_mean) {
        this.has_mean = has_mean;
    }

    public String[] getMeans() {
        return means;
    }

    public void setMeans(String[] means) {
        this.means = means;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPart() {
        return part;
    }

    public void setPart(String part) {
        this.part = part;
    }

    public String getSplit() {
        return split;
    }

    public void setSplit(String split) {
        this.split = split;
    }

    public String getWord_mean() {
        return word_mean;
    }

    public void setWord_mean(String word_mean) {
        this.word_mean = word_mean;
    }
}
