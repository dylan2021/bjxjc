package com.haocang.bjxjc.ui.monitor.bean;

/**
 * 创建时间：2019/6/14
 * 编 写 人：ShenC
 * 功能描述：
 */

public class PumpBean {

    private String PID;
    private String BridgeAreaID;
    private String Fcat_ID;
    private String FullName;
    private String ShortName;
    private String Power;
    private String Status;
    private String MpointID;
    private String SortCode;
    private String DataDT;


    public String getPID() {
        return PID;
    }

    public void setPID(String PID) {
        this.PID = PID;
    }

    public String getBridgeAreaID() {
        return BridgeAreaID;
    }

    public void setBridgeAreaID(String bridgeAreaID) {
        BridgeAreaID = bridgeAreaID;
    }

    public String getFcat_ID() {
        return Fcat_ID;
    }

    public void setFcat_ID(String fcat_ID) {
        Fcat_ID = fcat_ID;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getShortName() {
        return ShortName;
    }

    public void setShortName(String shortName) {
        ShortName = shortName;
    }

    public String getPower() {
        return Power;
    }

    public void setPower(String power) {
        Power = power;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getMpointID() {
        return MpointID;
    }

    public void setMpointID(String mpointID) {
        MpointID = mpointID;
    }

    public String getSortCode() {
        return SortCode;
    }

    public void setSortCode(String sortCode) {
        SortCode = sortCode;
    }

    public String getDataDT() {
        return DataDT;
    }

    public void setDataDT(String dataDT) {
        DataDT = dataDT;
    }
}
