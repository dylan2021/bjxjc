package com.haocang.bjxjc.ui.equipment.bean;

/**
 *保养信息
 */

public class EquTaskBean {
    private String TasksID;
    private String FullName;
    private String RepairID;
    private String DID;
    private String PlanID;
    private String EquID;
    private String EquName;
    private String Cycle;
    private String TasksDT;
    private String BeginDT;
    private String CompleteDT;
    private String CompleteUser;
    private String Content;
    private String Status;
    private String IsOverdue;
    private String Executor;
    private String realName;
    private String DefectNum;
    private String F_SortCode;
    private String Description;
    private String OverdueDescription;


    public String getFullName() {
        return FullName;
    }

    public String getEquName() {
        return EquName;
    }

    public String getTasksID() {
        return TasksID;
    }

    public String getRepairID() {
        return RepairID;
    }


    public String getRealName() {
        return realName;
    }

    public String getDID() {
        return DID;
    }

    public String getPlanID() {
        return PlanID;
    }

    public String getEquID() {
        return EquID;
    }

    public String getCycle() {
        return Cycle;
    }

    public String getTasksDT() {
        return TasksDT;
    }

    public String getBeginDT() {
        return BeginDT;
    }

    public String getCompleteDT() {
        return CompleteDT;
    }

    public String getCompleteUser() {
        return CompleteUser;
    }

    public String getContent() {
        return Content;
    }

    public String getStatus() {
        return Status;
    }

    public String getIsOverdue() {
        return IsOverdue;
    }

    public String getExecutor() {
        return Executor;
    }

    public String getDefectNum() {
        return DefectNum;
    }

    public String getF_SortCode() {
        return F_SortCode;
    }

    public String getDescription() {
        return Description;
    }

    public String getOverdueDescription() {
        return OverdueDescription;
    }
}
