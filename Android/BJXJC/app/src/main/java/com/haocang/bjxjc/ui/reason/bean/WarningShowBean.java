package com.haocang.bjxjc.ui.reason.bean;

/**
 * 创建时间：2019/7/17
 * 编 写 人：ShenC
 * 功能描述：
 */

public class WarningShowBean {

    private String ID;
    private String Location;
    private String Status;
    private String IsDelete;

    public WarningShowBean(String ID, String location, String status, String isDelete) {
        this.ID = ID;
        this.Location = location;
        this.Status = status;
        this.IsDelete = isDelete;
    }

    public String getID() {
        return ID;
    }

    public String getLocation() {
        return Location;
    }

    public String getStatus() {
        return Status;
    }

    public String getIsDelete() {
        return IsDelete;
    }
}
