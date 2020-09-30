package com.haocang.bjxjc.activity.bean;

/**
 * 创建时间：2019/6/13
 * 编 写 人：ShenC
 * 功能描述：
 */

public class PointIconBean {

    private double X;
    private double Y;
    private int Icon;


    public PointIconBean(double x,double y,int icon){
        this.X = x;
        this.Y = y;
        this.Icon = icon;
    }


    public double getX() {
        return X;
    }

    public void setX(double x) {
        X = x;
    }

    public double getY() {
        return Y;
    }

    public void setY(double y) {
        Y = y;
    }

    public int getIcon() {
        return Icon;
    }

    public void setIcon(int icon) {
        Icon = icon;
    }
}
