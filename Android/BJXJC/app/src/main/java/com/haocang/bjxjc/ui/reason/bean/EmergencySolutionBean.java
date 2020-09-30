package com.haocang.bjxjc.ui.reason.bean;

import com.haocang.bjxjc.utils.tools.MyTextUtils;
import com.haocang.bjxjc.utils.tools.OptionsItemsUtils;

/**
 * 创建时间：2019/7/16
 * 编 写 人：ShenC
 * 功能描述：
 */

public class EmergencySolutionBean {

    private String ID;
    private String Name;
    private String EmergencyTypeID;
    private String Precipitation;
    private String PlanLevel;
    private String UserCount;
    private String CarCount;
    private String Creator;
    private String CreateDT;
    private String Cars;
    private String Users;
    private String Forces;
    private String ArriveTime;
    private String IsActive;
    private String Content;
    private String Rainfall;

    public String getID() {
        return ID;
    }

    public String getName() {
        return Name;
    }

    public String getEmergencyTypeID() {
        return MyTextUtils.GetOpitemValue(OptionsItemsUtils.GetDataItemList("EmergencySolutionType"),1,EmergencyTypeID);
    }

    public String getPrecipitation() {
        return MyTextUtils.GetOpitemValue(OptionsItemsUtils.GetDataItemList("Rainfall"),1,Precipitation);
    }

    public String getPlanLevel() {
        return MyTextUtils.GetOpitemValue(OptionsItemsUtils.GetDataItemList("WarningLevel"),1,PlanLevel);
    }
    public String getUserCount() {
        return UserCount;
    }

    public String getCarCount() {
        return CarCount;
    }

    public String getCreator() {
        return Creator;
    }

    public String getCreateDT() {
        return CreateDT;
    }

    public String getCars() {
        return Cars;
    }

    public String getUsers() {
        return Users;
    }

    public String getForces() {
        return Forces;
    }

    public String getArriveTime() {
        return ArriveTime;
    }

    public String getIsActive() {
        return MyTextUtils.GetOpitemValue(OptionsItemsUtils.GetDataItemList("YesOrNo"),1,IsActive);
    }

    public String getContent() {
        return Content;
    }

    public String getRainfall() {
        return MyTextUtils.GetOpitemValue(OptionsItemsUtils.GetDataItemList("Rainfall_1"),1,Rainfall);
    }
}
