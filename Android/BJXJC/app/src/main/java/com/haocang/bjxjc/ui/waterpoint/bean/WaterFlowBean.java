package com.haocang.bjxjc.ui.waterpoint.bean;

/**
 * 创建时间：2019/5/23
 * 编 写 人：ShenC
 * 功能描述：
 */

public class WaterFlowBean {


    private String ID;
    private String Creator;
    private String CreateDT;
    private String Context;
    private String KeyValue;
    private String F_FileSize;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getCreator() {
        return Creator;
    }

    public void setCreator(String creator) {
        Creator = creator;
    }

    public String getCreateDT() {
        return CreateDT;
    }

    public void setCreateDT(String createDT) {
        CreateDT = createDT;
    }

    public String getContext() {
        return Context;
    }

    public void setContext(String context) {
        Context = context;
    }

    public String getKeyValue() {
        return KeyValue;
    }

    public void setKeyValue(String keyValue) {
        KeyValue = keyValue;
    }

    public String getF_FileSize() {
        if(F_FileSize == null) {return "0";}

        return F_FileSize;
    }

    public void setF_FileSize(String f_FileSize) {
        F_FileSize = f_FileSize;
    }
}
