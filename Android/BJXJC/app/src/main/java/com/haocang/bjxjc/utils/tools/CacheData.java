package com.haocang.bjxjc.utils.tools;

import com.amap.api.location.AMapLocation;
import com.haocang.bjxjc.App;
import com.haocang.bjxjc.ui.equipment.bean.EquTaskBean;
import com.haocang.bjxjc.ui.equipment.bean.EquipmentBean;
import com.haocang.bjxjc.ui.event.EventBean;
import com.haocang.bjxjc.ui.faultrepair.bean.EquRepairBean;
import com.haocang.bjxjc.ui.floodpoint.bean.FloodPointBean;
import com.haocang.bjxjc.utils.bean.XyBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述：临时缓存数据
 */

public class CacheData {
    public static String IsFloodUser = null;
    public static XyBean Cache_MyLocation = null;
    public static AMapLocation MyLocation = null;
    public static String LocationCity = null;
    public static String LocationDistrict = null;

    public static String Cache_WaringID = "";  //预警行动编号

    public static String UserID() {
        return SharedPreferencesUtil.GetUserInfo(App.getAppContext()).getF_UserId();
    }

    public static String UserName() {
        return SharedPreferencesUtil.GetUserInfo(App.getAppContext()).getF_RealName();
    }

    public static String getSP_PWD() {
        return SharedPreferencesUtil.GetUserInfo(App.getAppContext()).getF_Password();
    }

    public static String DeptName() {
        return SharedPreferencesUtil.GetUserInfo(App.getAppContext()).getF_DepartmentName();
    }

    public static String GetItemDataConfig() {
        List<String> itemcodes = new ArrayList<>();//定义获取的字典类型
        itemcodes.add("SeeperLevel");  //积水等级
        itemcodes.add("EventType");    //事件类型
        itemcodes.add("EventLevel");    //事件等级
        itemcodes.add("State");        //处理状态
        itemcodes.add("Rainfall");     //预案类别
        itemcodes.add("Warning");      //预警等级
        itemcodes.add("WarningLevel"); //预案等级
        itemcodes.add("FloodPointType");//易淹点类型
        itemcodes.add("DutyPerson");//	值班岗位
        itemcodes.add("WarningLeve");//	预案等级
        itemcodes.add("EmergencySolutionType");//应急预案分类
        itemcodes.add("MsgType");       //	消息类别
        itemcodes.add("SeeperSource");//积水来源
        itemcodes.add("Rainfall_1");//	降雨量
        itemcodes.add("YesOrNo");//	是否(Y/N)
        itemcodes.add("userType");//防汛库人员职责
        itemcodes.add("EquAssetState");//资产状态
        itemcodes.add("EquABC");//ABC
        itemcodes.add("RepairState");//维修状态
        itemcodes.add("RepairResult");//处理结果
        itemcodes.add("EquCycle");//保养周期
        itemcodes.add("EquState");//保养状态
        itemcodes.add("ErrorLevel");//故障级别
        itemcodes.add("ErrorType");// 故障类别

        String code = "";
        for (int i = 0; i < itemcodes.size(); i++) {
            if (i == itemcodes.size() - 1) {
                code += "'" + itemcodes.get(i) + "'";
            } else {
                code += "'" + itemcodes.get(i) + "',";
            }
        }

        return code;
    }


    //临时对象缓存
    public static FloodPointBean mFloodPointBean = null;
    public static String mWaterPointID = null;
    public static EventBean mEventBean = null;
    public static EquipmentBean mEquipmentBean = null;
    public static EquRepairBean mEquRepairBean = null;
    public static EquTaskBean mEquTaskBean = null;

    //阅读模式
    public static Boolean FloodPoint_UPDATE = false;
    public static Boolean WaterPoint_UPDATE = false;
    public static Boolean Event_UPDATE = false;

    //push
    public static String PUSH_MSG = "";
    public static String PUSH_DT = "";

}
