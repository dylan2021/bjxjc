package com.haocang.bjxjc.utils.bean;

import com.haocang.bjxjc.utils.tools.MyTextUtils;

/**
 * 创建时间：2019/3/30
 * 编 写 人：ShenC
 * 功能描述：
 */

public class EarlyWarningActionBean {

    private String ID;
    private String YearMon;
    private String StartTime;
    private String Precipitation;
    private String rainfall;
    private String PlanLevel;
    private String EmergencySolutionID;
    private String EmergencySolutionName;
    private String UserCount;
    private String CarCount;
    private String Name;
    private String TaskWaterPoint;
    private String AllWaterPoint;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getYearMon() {
        return YearMon;
    }

    public String getEmergencySolutionName() {
        return EmergencySolutionName;
    }

    public void setEmergencySolutionName(String emergencySolutionName) {
        EmergencySolutionName = emergencySolutionName;
    }

    public void setYearMon(String yearMon) {
        YearMon = yearMon;
    }

    public String getStartTime() {


        return StartTime.substring(0, StartTime.length() - 3);
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getEmergencySolutionID() {
        return EmergencySolutionID;
    }

    public void setEmergencySolutionID(String emergencySolutionID) {
        EmergencySolutionID = emergencySolutionID;
    }

    public String getUserCount() {
        return UserCount;
    }

    public void setUserCount(String userCount) {
        UserCount = userCount;
    }

    public String getCarCount() {
        return CarCount;
    }

    public void setCarCount(String carCount) {
        CarCount = carCount;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getTaskWaterPoint() {
        return TaskWaterPoint;
    }

    public void setTaskWaterPoint(String taskWaterPoint) {
        TaskWaterPoint = taskWaterPoint;
    }

    public String getAllWaterPoint() {
        return AllWaterPoint;
    }

    public void setAllWaterPoint(String allWaterPoint) {
        AllWaterPoint = allWaterPoint;
    }

    public String getRainfall() {
        if (MyTextUtils.IsNull(rainfall)) {
            return "";
        }
        switch (rainfall) {
            case "1":
                return "2小时雨量50mm以上";
            case "2":
                return "3小时雨量50mm以上";
            case "3":
                return "3小时雨量100mm以上";
            case "4":
                return "6小时雨量50mm以上";
            case "5":
                return "12小时雨量50mm以上";
            case "6":
                return "12小时雨量50mm以下";
            default:
                return "";
        }
    }

    public String getPrecipitation() {
        if (MyTextUtils.IsNull(Precipitation)) {
            return "";
        }
        switch (Precipitation) {
            case "5":
                return "关注级";
            case "4":
                return "Ⅳ级";
            case "3":
                return "Ⅲ级";
            case "2":
                return "Ⅱ级";
            case "1":
                return "Ⅰ级";
            default:
                return "";
        }
    }

    public String getPlanLevel() {
        if (MyTextUtils.IsNull(PlanLevel)) {
            return "";
        }
        switch (PlanLevel) {
            case "4":
                return "蓝色预警";
            case "3":
                return "黄色预警";
            case "2":
                return "橙色预警";
            case "1":
                return "红色预警";
            case "0":
                return "白色预警";
            default:
                return "";
        }
    }

    public void setPlanLevel(String planLevel) {
        PlanLevel = planLevel;
    }

    public void setPrecipitation(String precipitation) {
        Precipitation = precipitation;
    }
}
