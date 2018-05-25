package com.baidu.translate.entity;


import java.util.List;

/*
 *百度翻译 返回实体类数据
 */
public class trans {
    private String dst;
    private String prefixWrap;
    private String src;
    //private Object relation;
    private List<String[]> result;

    public String getDst() {
        return dst;
    }

    public void setDst(String dst) {
        this.dst = dst;
    }

    public String getPrefixWrap() {
        return prefixWrap;
    }

    public void setPrefixWrap(String prefixWrap) {
        this.prefixWrap = prefixWrap;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public List<String[]> getResult() {
        return result;
    }

    public void setResult(List<String[]> result) {
        this.result = result;
    }
}
