package com.escapevirus.entity;

import android.graphics.Paint;

/**
 * Created by xlzhen on 4/13 0013.
 * 分数 实体类
 */

public class ScoreEntity {
    private float x;
    private float y;
    private String text;
    private Paint paint;

    public ScoreEntity(float x, float y, String text, Paint paint) {
        this.x = x;
        this.y = y;
        this.text = text;
        this.paint = paint;
    }

    public ScoreEntity() {
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }
}
