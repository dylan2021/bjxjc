package com.haocang.bjxjc.ui.monitor.bean;

/**
 * 创建时间：2019/6/14
 * 编 写 人：ShenC
 * 功能描述：
 */

public class RealDataBean {

    private String P_Category;
    private String MpointID;
    private String ShortName;
    private String AlarmName;
    private String DataDT;
    private String Priority;
    private String Status;
    private String Unit;
    private String Value;
    private String Description;
    private String SortCode;
    private String IsHomeStatus;

    public String getP_Category() {
        return P_Category;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public void setP_Category(String p_Category) {
        P_Category = p_Category;
    }

    public String getMpointID() {
        return MpointID;
    }

    public void setMpointID(String mpointID) {
        MpointID = mpointID;
    }

    public String getShortName() {
        return ShortName;
    }

    public void setShortName(String shortName) {
        ShortName = shortName;
    }

    public String getAlarmName() {
        return AlarmName;
    }

    public void setAlarmName(String alarmName) {
        AlarmName = alarmName;
    }

    public String getDataDT() {
        return DataDT;
    }

    public void setDataDT(String dataDT) {
        DataDT = dataDT;
    }

    public String getPriority() {
        return Priority;
    }

    public void setPriority(String priority) {
        Priority = priority;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getSortCode() {
        return SortCode;
    }

    public void setSortCode(String sortCode) {
        SortCode = sortCode;
    }

    public String getIsHomeStatus() {
        return IsHomeStatus;
    }

    public void setIsHomeStatus(String isHomeStatus) {
        IsHomeStatus = isHomeStatus;
    }
}
