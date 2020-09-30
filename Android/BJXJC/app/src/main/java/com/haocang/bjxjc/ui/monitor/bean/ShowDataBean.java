package com.haocang.bjxjc.ui.monitor.bean;

/**
 * 创建时间：2019/6/14
 * 编 写 人：ShenC
 * 功能描述：
 */

public class ShowDataBean {

    private int index;
    private String ID;
    private String Name;
    private String Status;
    private String Values;
    private String Unit;
    private String DataDT;
    private String AlarmName;
    private String Type;


    public ShowDataBean(int index,String Type,String ID, String name, String status, String values, String unit, String dataDT,String AlarmName) {
        this.index = index;
        this.ID = ID;
        this.Name = name;
        this. Status = status;
        this. Values = values;
        this. Unit = unit;
        this. DataDT = dataDT;
        this. AlarmName = AlarmName;
        this. Type = Type;

    }


    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getValues() {
        return Values;
    }

    public void setValues(String values) {
        Values = values;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public String getDataDT() {
        return DataDT;
    }

    public void setDataDT(String dataDT) {
        DataDT = dataDT;
    }

    public String getAlarmName() {
        return AlarmName;
    }


    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public void setAlarmName(String alarmName) {
        AlarmName = alarmName;
    }
}
