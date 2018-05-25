package com.baidu.translate.entity;

import java.util.List;

/*
*百度翻译 返回实体类数据
*/
public class dict {
    private List<symbols> symbols;
    private String word_name;
    private String from;
    private String[] word_means;
    private String is_CRI;
    private exchange exchange;
    private tags tags;


    public List<com.baidu.translate.entity.symbols> getSymbols() {
        return symbols;
    }

    public void setSymbols(List<com.baidu.translate.entity.symbols> symbols) {
        this.symbols = symbols;
    }

    public String getWord_name() {
        return word_name;
    }

    public void setWord_name(String word_name) {
        this.word_name = word_name;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String[] getWord_means() {
        return word_means;
    }

    public void setWord_means(String[] word_means) {
        this.word_means = word_means;
    }
}
