package com.haocang.bjxjc.utils.bean;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Property;

/**
 * 创建时间：2019/3/13
 * 编 写 人：ShenC
 * 功能描述：
 */

public class ReasonBean {
    @Id(column="ID")
    private String ID;
    @Property(column="Name")
    private String Name;

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
}
