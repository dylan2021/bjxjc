package com.haocang.bjxjc.ui.home.bean;

import java.util.List;

/**
 * 创建时间：2019/5/21
 * 编 写 人：ShenC
 * 功能描述：
 */

public class WeatherBean {

    private String date;
    private String high;
    private String low;
    private WeatherDayBean day;
    private WeatherDayBean night;


    public String getDate() {
        return date;
    }


    public void setDate(String date) {
        this.date = date;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public WeatherDayBean getDay() {
        return day;
    }

    public void setDay(WeatherDayBean day) {
        this.day = day;
    }

    public WeatherDayBean getNight() {
        return night;
    }

    public void setNight(WeatherDayBean night) {
        this.night = night;
    }
}
