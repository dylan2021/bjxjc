package com.haocang.bjxjc.ui.user.bean;

/**
 * 创建时间：2019/5/23
 * 编 写 人：ShenC
 * 功能描述：
 */

public class ArrangementBaseBean {
    private String  ID;
    private String  TeamName;
    private String  TeamMember;
    private String  CarID;
    private String  CarEquipped;
    private String ManagementOffice;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTeamName() {
        return TeamName;
    }

    public void setTeamName(String teamName) {
        TeamName = teamName;
    }

    public String getTeamMember() {
        return TeamMember;
    }

    public void setTeamMember(String teamMember) {
        TeamMember = teamMember;
    }

    public String getCarID() {
        return CarID;
    }

    public void setCarID(String carID) {
        CarID = carID;
    }

    public String getCarEquipped() {
        return CarEquipped;
    }

    public void setCarEquipped(String carEquipped) {
        CarEquipped = carEquipped;
    }

    public String getManagementOffice() {
        return ManagementOffice;
    }
    public void setManagementOffice(String managementOffice) {
        ManagementOffice = managementOffice;
    }
}
