package com.baidu.translate.entity;

import java.util.List;

/*
 *百度翻译 返回实体类数据
 */
public class TransEntity {
    private int errno;
    private en_ph en_ph;
    private String from;
    private String to;
    private List<trans> trans;
    private dict dict;
    private List<phonetic> phonetic;

    public List<com.baidu.translate.entity.phonetic> getPhonetic() {
        return phonetic;
    }

    public void setPhonetic(List<com.baidu.translate.entity.phonetic> phonetic) {
        this.phonetic = phonetic;
    }

    public int getErrno() {
        return errno;
    }

    public void setErrno(int errno) {
        this.errno = errno;
    }

    public com.baidu.translate.entity.en_ph getEn_ph() {
        return en_ph;
    }

    public void setEn_ph(com.baidu.translate.entity.en_ph en_ph) {
        this.en_ph = en_ph;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public List<com.baidu.translate.entity.trans> getTrans() {
        return trans;
    }

    public void setTrans(List<com.baidu.translate.entity.trans> trans) {
        this.trans = trans;
    }

    public com.baidu.translate.entity.dict getDict() {
        return dict;
    }

    public void setDict(com.baidu.translate.entity.dict dict) {
        this.dict = dict;
    }
}
