package com.haocang.bjxjc.ui.login.bean;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Property;

/**
 * 创建时间：2019/7/18
 * 编 写 人：ShenC
 * 功能描述：
 */

public class DataItemBean {
    @Id(column="ID")
    private String ID;
    @Property(column="F_ItemCode")
    private String F_ItemCode;
    @Property(column="F_ItemName")
    private String F_ItemName;
    @Property(column="F_ItemValue")
    private String F_ItemValue;

    public String getID() {
        return ID;
    }
    public void setID(String ID) {
        this.ID = ID;
    }

    public String getF_ItemCode() {
        return F_ItemCode;
    }

    public void setF_ItemCode(String f_ItemCode) {
        F_ItemCode = f_ItemCode;
    }

    public String getF_ItemName() {
        return F_ItemName;
    }

    public void setF_ItemName(String f_ItemName) {
        F_ItemName = f_ItemName;
    }

    public String getF_ItemValue() {
        return F_ItemValue;
    }

    public void setF_ItemValue(String f_ItemValue) {
        F_ItemValue = f_ItemValue;
    }
}
