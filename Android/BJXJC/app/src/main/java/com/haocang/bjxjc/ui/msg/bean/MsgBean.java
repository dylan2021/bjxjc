package com.haocang.bjxjc.ui.msg.bean;

/**
 * 创建时间：2019/3/27
 * 编 写 人：ShenC
 * 功能描述：
 */

public class MsgBean {

    private String ID;
    private String Creator;
    private String CreateDT;
    private String Title;
    private String Context;
    private String Type;
    private String KeyValue;
    private String ReadDT;
    private String BillStatus;

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

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
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

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getKeyValue() {
        return KeyValue;
    }

    public void setKeyValue(String keyValue) {
        KeyValue = keyValue;
    }


    public String getReadDT() {
        return ReadDT;
    }

    public void setReadDT(String readDT) {
        ReadDT = readDT;
    }

    public String getBillStatus() {
        return BillStatus;
    }

    public void setBillStatus(String billStatus) {
        BillStatus = billStatus;
    }

    @Override
    public String toString() {
        return "MsgBean{" +
                "ID='" + ID + '\'' +
                ", Creator='" + Creator + '\'' +
                ", CreateDT='" + CreateDT + '\'' +
                ", Title='" + Title + '\'' +
                ", Context='" + Context + '\'' +
                ", Type='" + Type + '\'' +
                ", KeyValue='" + KeyValue + '\'' +
                ", ReadDT='" + ReadDT + '\'' +
                ", BillStatus='" + BillStatus + '\'' +
                '}';
    }
}
