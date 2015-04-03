package com.gztoucher.sensortest.bean;

/**
 * 类描述
 *
 * @author 李玉江[QQ:1032694760]
 * @version 2015/4/3
 *          Created by IntelliJ IDEA 14.1
 */
public class SensorBean {
    private int type;
    private float x;
    private float y;
    private float z;
    private long timeline;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public long getTimeline() {
        return timeline;
    }

    public void setTimeline(long timeline) {
        this.timeline = timeline;
    }

}
