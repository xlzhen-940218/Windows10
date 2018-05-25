package com.baidu.translate.entity;

/*
*英译中 返回单个字的拼音
*/
public class phonetic {
    private String src_str;
    private String trg_str;

    public String getSrc_str() {
        return src_str;
    }

    public void setSrc_str(String src_str) {
        this.src_str = src_str;
    }

    public String getTrg_str() {
        return trg_str;
    }

    public void setTrg_str(String trg_str) {
        this.trg_str = trg_str;
    }
}
