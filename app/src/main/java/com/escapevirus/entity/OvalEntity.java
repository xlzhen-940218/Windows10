package com.escapevirus.entity;

import android.graphics.Paint;

import com.escapevirus.enumfolder.DirectionEnum;

/**
 * Created by xlzhen on 4/12 0012.
 * 小圆圈 实体类
 */

public class OvalEntity {
    private float x;
    private float y;
    private float radius;
    private DirectionEnum directionEnum;
    private Paint paint;

    public OvalEntity() {
    }

    public DirectionEnum getDirectionEnum() {
        return directionEnum;
    }

    public void setDirectionEnum(DirectionEnum directionEnum) {
        this.directionEnum = directionEnum;
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

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }
}
