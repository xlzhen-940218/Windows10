package com.baidu.translate.entity;

import java.util.List;

/*
 *百度翻译 返回实体类数据
 */
public class symbols {
    private String word_symbol;
    private String symbol_mp3;

    private String ph_am_mp3;
    private String ph_am;
    private String ph_en_mp3;
    private String ph_en;
    private String ph_other;
    private String ph_tts_mp3;

    private List<parts> parts;

    public String getWord_symbol() {
        return word_symbol;
    }

    public void setWord_symbol(String word_symbol) {
        this.word_symbol = word_symbol;
    }

    public String getSymbol_mp3() {
        return symbol_mp3;
    }

    public void setSymbol_mp3(String symbol_mp3) {
        this.symbol_mp3 = symbol_mp3;
    }

    public List<com.baidu.translate.entity.parts> getParts() {
        return parts;
    }

    public void setParts(List<com.baidu.translate.entity.parts> parts) {
        this.parts = parts;
    }

    public String getPh_am_mp3() {
        return ph_am_mp3;
    }

    public void setPh_am_mp3(String ph_am_mp3) {
        this.ph_am_mp3 = ph_am_mp3;
    }

    public String getPh_am() {
        return ph_am;
    }

    public void setPh_am(String ph_am) {
        this.ph_am = ph_am;
    }

    public String getPh_en_mp3() {
        return ph_en_mp3;
    }

    public void setPh_en_mp3(String ph_en_mp3) {
        this.ph_en_mp3 = ph_en_mp3;
    }

    public String getPh_en() {
        return ph_en;
    }

    public void setPh_en(String ph_en) {
        this.ph_en = ph_en;
    }

    public String getPh_other() {
        return ph_other;
    }

    public void setPh_other(String ph_other) {
        this.ph_other = ph_other;
    }

    public String getPh_tts_mp3() {
        return ph_tts_mp3;
    }

    public void setPh_tts_mp3(String ph_tts_mp3) {
        this.ph_tts_mp3 = ph_tts_mp3;
    }
}
