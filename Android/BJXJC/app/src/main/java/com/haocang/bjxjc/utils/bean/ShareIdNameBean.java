package com.haocang.bjxjc.utils.bean;

/**
 * 创建时间：2019/3/5
 * 编 写 人：ShenC
 * 功能描述：
 */

public class ShareIdNameBean {

    private  String ID;
    private  String Name;
    private  String Parent;
    private  String	StationTypeID;
    private  String ClockIn;
    public ShareIdNameBean(){
    }

    public ShareIdNameBean(String id,String name){
        this.ID = id;
        this.Name = name;
    }

    public String getParent() {
        return Parent;
    }

    public void setParent(String parent) {
        Parent = parent;
    }

    public String getStationTypeID() {
        return StationTypeID;
    }

    public void setStationTypeID(String stationTypeID) {
        StationTypeID = stationTypeID;
    }

    public String getID() {
        return ID;
    }
    public void setID(String iD) {
        ID = iD;
    }
    public String getName() {
        return Name;
    }
    public void setName(String name) {
        Name = name;
    }

    public String getClockIn() {
        return ClockIn;
    }
}
