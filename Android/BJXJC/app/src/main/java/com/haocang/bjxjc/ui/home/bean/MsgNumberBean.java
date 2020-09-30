package com.haocang.bjxjc.ui.home.bean;

/**
 * 创建时间：2019/3/28
 * 编 写 人：ShenC
 * 功能描述：
 */

public class MsgNumberBean {
    private String MsgNumber;
    private String TaskNumber;
    private String WaterPoint_S0;
    private String WaterPoint_S1;
    private String WaterPoint_S2;
    private String WaterPoint_S3;
    private String WaterPoint_S4;
    private String WaterPoint_SUM;
    private String Event_S0;
    private String Event_S1;
    private String Event_S2;
    private String Event_SUM;

    private String IsFloodUser;



    public String getMsgNumber() {
        return MsgNumber;
    }


    public String getTaskNumber() {
        return TaskNumber;
    }


    public String getWaterPoint_S0() {
        return WaterPoint_S0;
    }


    public String getWaterPoint_S1() {
        return WaterPoint_S1;
    }


    public String getWaterPoint_S2() {
        return WaterPoint_S2;
    }


    public String getWaterPoint_SUM() {
        return WaterPoint_SUM;
    }


    public String getEvent_S0() {
        return Event_S0;
    }


    public String getEvent_S1() {
        return Event_S1;
    }


    public String getEvent_S2() {
        return Event_S2;
    }


    public String getEvent_SUM() {
        return Event_SUM;
    }

    public String getWaterPoint_S3() {
        return WaterPoint_S3;
    }

    public String getWaterPoint_S4() {
        return WaterPoint_S4;
    }

    public String getIsFloodUser() {
        if(IsFloodUser == null){
            return "0";
        }
        return IsFloodUser;
    }
}
