package com.haocang.bjxjc.utils.tools;

import com.haocang.bjxjc.ui.login.bean.DataItemBean;
import com.haocang.bjxjc.utils.bean.ArrangementBaseInfoBean;
import com.haocang.bjxjc.utils.bean.DeptBean;
import com.haocang.bjxjc.utils.bean.ProvinceBean;
import com.haocang.bjxjc.utils.bean.ReasonBean;
import com.haocang.bjxjc.utils.bean.UserBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建时间：2019/3/13
 * 编 写 人：ShenC
 * 功能描述：
 */

public class OptionsItemsUtils {

    public static ArrayList<ProvinceBean> GetWaterPointStatus() {
        ArrayList<ProvinceBean> OptionsItems = new ArrayList<>();
        OptionsItems.add(new ProvinceBean(0, "未处理", "0", ""));
        OptionsItems.add(new ProvinceBean(1, "处理中", "1", ""));
        OptionsItems.add(new ProvinceBean(2, "已处理", "2", ""));
        return OptionsItems;
    }

    public static ArrayList<ProvinceBean> GetAllWaterPointStatus() {
        ArrayList<ProvinceBean> OptionsItems = new ArrayList<>();
        OptionsItems.add(new ProvinceBean(0, "全部", "", ""));
        OptionsItems.add(new ProvinceBean(1, "未处置", "0", ""));
        OptionsItems.add(new ProvinceBean(2, "处置中", "1", ""));
        OptionsItems.add(new ProvinceBean(3, "已处置", "2", ""));
        OptionsItems.add(new ProvinceBean(4, "未巡查", "N", ""));
        OptionsItems.add(new ProvinceBean(5, "未积水", "Y", ""));
        return OptionsItems;
    }

    public static ArrayList<ProvinceBean> GetAllOldWaterPointStatus() {
        ArrayList<ProvinceBean> OptionsItems = new ArrayList<>();
        OptionsItems.add(new ProvinceBean(0, "全部", "", ""));
        OptionsItems.add(new ProvinceBean(1, "未处置", "0", ""));
        OptionsItems.add(new ProvinceBean(2, "处置中", "1", ""));
        OptionsItems.add(new ProvinceBean(3, "已处置", "2", ""));
        return OptionsItems;
    }

    public static ArrayList<ProvinceBean> getTroubleItems() {
        ArrayList<ProvinceBean> OptionsItems = new ArrayList<>();
        OptionsItems.add(new ProvinceBean(0, "全部", "", ""));
        OptionsItems.add(new ProvinceBean(1, "否", "N", ""));
        OptionsItems.add(new ProvinceBean(2, "是", "Y", ""));
        return OptionsItems;
    }
    public static ArrayList<ProvinceBean> getOverTimeItems() {
        ArrayList<ProvinceBean> OptionsItems = new ArrayList<>();
        OptionsItems.add(new ProvinceBean(0, "全部", "", ""));
        OptionsItems.add(new ProvinceBean(1, "否", "0", ""));
        OptionsItems.add(new ProvinceBean(2, "是", "1", ""));
        return OptionsItems;
    }
    public static ArrayList<ProvinceBean> getKeepStatusItems() {
        ArrayList<ProvinceBean> OptionsItems = new ArrayList<>();
        OptionsItems.add(new ProvinceBean(0, "全部", "", ""));
        OptionsItems.add(new ProvinceBean(1, "初始", "0", ""));
        OptionsItems.add(new ProvinceBean(2, "已分配", "1", ""));
        OptionsItems.add(new ProvinceBean(3, "执行中", "2", ""));
        OptionsItems.add(new ProvinceBean(4, "执行中(转派)", "3", ""));
        OptionsItems.add(new ProvinceBean(5, "挂起", "4", ""));
        OptionsItems.add(new ProvinceBean(6, "关闭", "5", ""));
        OptionsItems.add(new ProvinceBean(7, "完成", "6", ""));
        return OptionsItems;
    }
    public static ArrayList<ProvinceBean> GetWaterPointIsCarPass() {
        ArrayList<ProvinceBean> OptionsItems = new ArrayList<>();
        OptionsItems.add(new ProvinceBean(0, "否", "0", ""));
        OptionsItems.add(new ProvinceBean(1, "是", "1", ""));
        return OptionsItems;
    }

    public static ArrayList<ProvinceBean> GetWaterPointSeeperLevel() {
        ArrayList<ProvinceBean> OptionsItems = new ArrayList<>();
        OptionsItems.add(new ProvinceBean(0, "轻度积水", "1", ""));
        OptionsItems.add(new ProvinceBean(1, "中度积水", "2", ""));
        OptionsItems.add(new ProvinceBean(2, "严重积水", "3", ""));
        return OptionsItems;
    }

    public static ArrayList<ProvinceBean> GetEventStatus() {
        ArrayList<ProvinceBean> OptionsItems = new ArrayList<>();
        OptionsItems.add(new ProvinceBean(0, "全部", "", ""));
        OptionsItems.add(new ProvinceBean(1, "未处理", "0", ""));
        OptionsItems.add(new ProvinceBean(2, "处理中", "1", ""));
        OptionsItems.add(new ProvinceBean(3, "已处理", "2", ""));
        return OptionsItems;
    }

    public static ArrayList<ProvinceBean> GetEventOnlyStatus() {
        ArrayList<ProvinceBean> OptionsItems = new ArrayList<>();
        OptionsItems.add(new ProvinceBean(1, "未处理", "0", ""));
        OptionsItems.add(new ProvinceBean(2, "处理中", "1", ""));
        OptionsItems.add(new ProvinceBean(3, "已处理", "2", ""));
        return OptionsItems;
    }


    public static ArrayList<ProvinceBean> GetALLFloodPointType() {
        ArrayList<ProvinceBean> OptionsItems = new ArrayList<>();
        OptionsItems.add(new ProvinceBean(0, "全部", "", ""));
        OptionsItems.add(new ProvinceBean(1, "易淹点", "1", ""));
        OptionsItems.add(new ProvinceBean(2, "积水点", "2", ""));
        return OptionsItems;
    }

    public static ArrayList<ProvinceBean> GetFloodPointType() {
        ArrayList<ProvinceBean> OptionsItems = new ArrayList<>();
        OptionsItems.add(new ProvinceBean(1, "易淹点", "1", ""));
        OptionsItems.add(new ProvinceBean(2, "积水点", "2", ""));
        return OptionsItems;
    }

    public static ArrayList<ProvinceBean> GetDeptList() {
        ArrayList<ProvinceBean> OptionsItems = new ArrayList<>();
        List<DeptBean> list = ApiConstant.db.findAllByWhere(DeptBean.class, "name like '%%'");
        for (int i = 0; i < list.size(); i++) {
            OptionsItems.add(new ProvinceBean(i, list.get(i).getName(), list.get(i).getID(), ""));
        }
        return OptionsItems;
    }

    public static ArrayList<ProvinceBean> GetUserList() {
        ArrayList<ProvinceBean> OptionsItems = new ArrayList<>();
        List<UserBean> list = ApiConstant.db.findAllByWhere(UserBean.class, "name like '%%'");
        for (int i = 0; i < list.size(); i++) {
            OptionsItems.add(new ProvinceBean(i, list.get(i).getName(),
                    list.get(i).getID(), ""));
        }
        return OptionsItems;
    }

    public static ArrayList<ProvinceBean> getWarmList() {
        ArrayList<ProvinceBean> OptionsItems = new ArrayList<>();
        List<ReasonBean> list = ApiConstant.db.findAllByWhere(ReasonBean.class, "name like '%%'");
        for (int i = 0; i < list.size(); i++) {
            OptionsItems.add(new ProvinceBean(i, list.get(i).getName(), list.get(i).getID(), ""));
        }
        return OptionsItems;
    }


    public static ArrayList<ProvinceBean> GetArrangementBaseInfoList() {
        ArrayList<ProvinceBean> OptionsItems = new ArrayList<>();
        List<ArrangementBaseInfoBean> list = ApiConstant.db.findAllByWhere(
                ArrangementBaseInfoBean.class, "name like '%%'");
        for (int i = 0; i < list.size(); i++) {
            OptionsItems.add(new ProvinceBean(i, list.get(i).getName(), list.get(i).getID(), ""));
        }
        return OptionsItems;
    }

    public static ArrayList<ProvinceBean> GetAllReasonList() {
        ArrayList<ProvinceBean> OptionsItems = new ArrayList<>();
        List<ReasonBean> list = ApiConstant.db.findAllByWhere(ReasonBean.class, "name like '%%'");
        OptionsItems.add(new ProvinceBean(0, "全部", "", ""));
        for (int i = 0; i < list.size(); i++) {
            OptionsItems.add(new ProvinceBean(i, list.get(i).getName(), list.get(i).getID(), ""));
        }
        return OptionsItems;
    }

    public static ArrayList<ProvinceBean> GetTaskReasonList() {
        ArrayList<ProvinceBean> OptionsItems = new ArrayList<>();
        List<ReasonBean> list = ApiConstant.db.findAllByWhere(ReasonBean.class, "name like '%%'");
        OptionsItems.add(new ProvinceBean(0, "当前任务", "", ""));
        if (CacheData.Cache_WaringID.equals("")) {
            for (int i = 0; i < list.size(); i++) {
                OptionsItems.add(new ProvinceBean(i, list.get(i).getName(), list.get(i).getID(), ""));
            }
        } else {
            for (int i = 0; i < list.size(); i++) {
                if (!list.get(i).getID().equals(CacheData.Cache_WaringID))
                    OptionsItems.add(new ProvinceBean(i, list.get(i).getName(), list.get(i).getID(), ""));
            }
        }
        return OptionsItems;
    }

    //获取历史预警行动列表
    public static ArrayList<ProvinceBean> GetOldAllReasonList() {
        ArrayList<ProvinceBean> OptionsItems = new ArrayList<>();
        List<ReasonBean> list = ApiConstant.db.findAllByWhere(ReasonBean.class, "name like '%%'");
        OptionsItems.add(new ProvinceBean(0, "全部", "", ""));
        if (CacheData.Cache_WaringID.equals("")) {
            for (int i = 0; i < list.size(); i++) {
                OptionsItems.add(new ProvinceBean(i, list.get(i).getName(), list.get(i).getID(), ""));
            }
        } else {
            for (int i = 0; i < list.size(); i++) {
                if (!list.get(i).getID().equals(CacheData.Cache_WaringID))
                    OptionsItems.add(new ProvinceBean(i, list.get(i).getName(), list.get(i).getID(), ""));
            }
        }
        return OptionsItems;
    }


    public static ArrayList<ProvinceBean> GetFileStatusList() {
        ArrayList<ProvinceBean> OptionsItems = new ArrayList<>();
        OptionsItems.add(new ProvinceBean(3, "预处理", "3", ""));
        OptionsItems.add(new ProvinceBean(0, "未处理", "0", ""));
        OptionsItems.add(new ProvinceBean(1, "处理中", "1", ""));
        OptionsItems.add(new ProvinceBean(2, "已处理", "2", ""));
        return OptionsItems;
    }


    public static ArrayList<ProvinceBean> GetIP() {
        ArrayList<ProvinceBean> OptionsItems = new ArrayList<>();
        OptionsItems.add(new ProvinceBean(0, "测试内网", "0", ""));
        OptionsItems.add(new ProvinceBean(1, "测试外网", "1", ""));
        OptionsItems.add(new ProvinceBean(2, "现场内网", "2", ""));
        return OptionsItems;
    }


    public static ArrayList<ProvinceBean> GetDataItemList(String code) {
        ArrayList<ProvinceBean> OptionsItems = new ArrayList<>();
        List<DataItemBean> list = ApiConstant.db.findAllByWhere(DataItemBean.class, "F_ItemCode = '" + code + "'");
        for (int i = 0; i < list.size(); i++) {
            OptionsItems.add(new ProvinceBean(i, list.get(i).getF_ItemName(), list.get(i).getF_ItemValue(), ""));
        }
        return OptionsItems;
    }

    public static ArrayList<ProvinceBean> GetDataItemAllList(String code) {
        ArrayList<ProvinceBean> OptionsItems = new ArrayList<>();
        List<DataItemBean> list = ApiConstant.db.findAllByWhere(DataItemBean.class, "F_ItemCode = '" + code + "'");

        OptionsItems.add(new ProvinceBean(0, "全部", "", ""));
        for (int i = 0; i < list.size(); i++) {
            OptionsItems.add(new ProvinceBean(i + 1, list.get(i).getF_ItemName(), list.get(i).getF_ItemValue(), ""));
        }
        return OptionsItems;
    }

}
