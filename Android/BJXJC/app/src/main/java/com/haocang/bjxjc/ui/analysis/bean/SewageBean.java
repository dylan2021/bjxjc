package com.haocang.bjxjc.ui.analysis.bean;

/**
 * 创建时间：2019/6/17
 * 编 写 人：ShenC
 * 功能描述：
 */

public class SewageBean {
    private String ID;
    private String Name;
    private String Parent;
    private Boolean IsCheck = false;
    private Boolean IsShow = true;


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


    public String getParent() {
        return Parent;
    }

    public void setParent(String parent) {
        Parent = parent;
    }

    public Boolean getCheck() {
        return IsCheck;
    }

    public void setCheck(Boolean check) {
        IsCheck = check;
    }

    public Boolean getShow() {
        return IsShow;
    }

    public void setShow(Boolean show) {
        IsShow = show;
    }

}
