package com.haocang.bjxjc.utils.tools;

/**
 * 接口 Url
 */

public class UrlConst {

    public static String Api_HelpDocUrl = ApiConstant.host + "/Content/help/help.html";//幫助文檔

    public static String Api_Login = "/Mobile/Login/Login";//登录接口
    public static String Api_UserUploadLocation = "/Mobile/EmergencyDispatch/UpdateAppLoginState?";
    public static String fileUpload = "/Mobile/FileUpload/ParamFileUpload";//文件表单信息上传接口
    public static String Api_GetFileListByFolderID = "/Mobile/FileUpload/GetFileListByFolderID?folderId=";//文件表单信息上传接口
    public static String Api_FloodPoint = "/Mobile/FloodPoint/GetList";//关注点易淹点列表
    public static String Api_PostFloodPoint = "/Mobile/FloodPoint/SaveMobileEntity?info=";//关注点易淹点提交和修改
    public static String Api_Event = "/Mobile/Event/GetList";//事件列表
    public static String url_equipment_list = "/Mobile/Equipment/GetList";//设备档案列表
    public static String url_equipmentType = "/Mobile/Equipment/GetTypeList";//设备档案类型
    public static String url_equipmentArea = "/Mobile/Equipment/GetAreaList";//设备档案区域
    public static String Api_GetEquParameter = "/Mobile/Equipment/GetEquParameter?ID=";//获取设备参数
    public static String url_get_epair_list = "/Mobile/Equipment/GetAllRepairList";//获取维修清单
    public static String Api_GetTaskList = "/Mobile/Equipment/GetTaskList";//获取保养清单
    public static String Api_GetMobileFaultRepairFlow = "/Mobile/Equipment/GetMobileFaultRepairFlow?id=";//获取维修转单流程
    public static String Api_PostSaveFaultRepair = "/Mobile/Equipment/SaveMobileFaultRepair?info=";//维修工单保存
    public static String Api_PostSaveFaultRepairOrder = "/Mobile/Equipment/SaveMobileFaultRepairOrder?info=";//维修工单完成信息保存


    public static String Api_WaterPointDetail = "/Mobile/WaterPoint/GetDetail?ID=";//积水点列表
    public static String Api_GetWaterPointByUserID = "/Mobile/WaterPoint/GetWaterPointByUserID";//根据用户id,找出其对应队伍的积水点
    public static String url_his_water_point = "/Mobile/WaterPoint/GetHistoryList";//积水点列表
    public static String Api_GetWaterMsgList = "/Mobile/WaterPoint/GetWaterMsgList?ID=";//积水点消息
    public static String Api_WaterPoint = "/Mobile/WaterPoint/GetList";//积水点列表
    public static String Api_Msg = "/Mobile/Msg/GetList";//消息
    public static String url_msg_num = "/Mobile/Msg/GetUserMsgNumber?userid=";//消息数量
    public static String Api_SaveMsgStatus = "/Mobile/Msg/SaveMsgStatus?info=";//更新消息状态

    public static String url_getEmergencySolutionDeatil =
            "/Mobile/EmergencyDispatch/GetEmergencySolutionDeatil?keyValue=";//获取预案明细
    public static String Api_GetReasonDeatil = "/Mobile/EmergencyDispatch/GetReasonDeatil?keyValue=";//获取防汛行动明细

    //综合监测
    public static String url_get_monitor_data = "/Mobile/Monitor/GetMonitorData";//综合监测首页数据源

    //数据曲线
    public static String Api_GetDataAnalysisTree = "/Mobile/DataAnalysis/GetTree?" +
            "queryJson={%22Fcat_ID%22:%22%22,%22keyword%22:%22%22}";
    public static String Api_GetDataAnalysisHisData = "/Mobile/DataAnalysis/GetHisData?";

    //Tools
    public static String url_get_is_run_reason = "/Mobile/Tools/GetIsRunReason";//获取当前预警行动
    public static String Api_GetIsLeader = "/Mobile/Tools/GetIsLeader";//修改打卡状态
    public static String Api_GetClockInValue = "/Mobile/Tools/GetClockInValue";//判断是否打卡
    public static String Api_PostUpdateClockIn = "/Mobile/Tools/UpdateClockIn?info=";//修改打卡状态
    public static String Api_GetEditArrangementBaseExit = "/Mobile/Tools/EditArrangementBaseExit";//根据队长ID找到队长对应的防汛队伍，将当前队伍变为未启用
    public static String Api_ActivationArrangementBase = "/Mobile/Tools/ActivationArrangementBase";//根据队长编号，找到队伍库数据，激活队伍,
    public static String Api_JudgeArrangementBase = "/Mobile/Tools/JudgeArrangementBase";//判断防汛队长的队伍是否存在
    public static String Api_GetIsAppUpdate = "/Mobile/Tools/GetIsAppUpdate?code=";//检查版本更新
    public static String Api_SaveArangementMobileEntity = "/Mobile/Tools/SaveArangementMobileEntity?info=";//修改防汛队伍

    //下拉列表数据
    public static String Api_List_GetDeptList = "/Mobile/ListData/GetDeptList";//部门下拉列表
    public static String Api_List_GetDeptTreeList = "/Mobile/ListData/GetDeptTreeList";//部门树形菜单下拉列表
    public static String url_warm_list = "/Mobile/ListData/getWarmList";//预警行动下拉列表
    public static String Api_List_GetArrangementBaseInfoList = "/Mobile/ListData/GetArrangementBaseInfoList";//防汛队伍下拉列表
    public static String Api_List_GetUsersByArrangementIDList = "/Mobile/ListData/GetUsersByArrangementIDList";//获取防汛队伍ID,获取队员下拉数据
    public static String Api_List_GetUserList = "/Mobile/ListData/GetUserList";//用户下拉列表
    public static String Api_List_GetTeamNameListByUserID = "/Mobile/WaterPoint/GetTeamNameListByUserID";//根据用户获取所属防汛队伍下拉列表
    public static String Api_List_GetSeeperReasonList = "/Mobile/ListData/GetSeeperReasonList";//积水原因下拉列表
    public static String Api_List_GetWaterResultList = "/Mobile/ListData/GetWaterResultList";//处置措施下拉列表
    public static String Api_List_GetGroupUserList = "/Mobile/ListData/GetGroupUserList?id=";//群组人员下拉
    public static String Api_List_GetDeptCarList = "/Mobile/ListData/GetDeptCarList?departmentId=";//防汛车辆下拉
    public static String Api_List_GetItemDataList = "/Mobile/ListData/GetItemDataList?itemcodes=";//数据字典数据获取

    public static final String url_change_pwd = "/Mobile/User/SubmitResetPassword";
    public static final String url_get_keep_order_list = "/Mobile/EquTask/GetPageList";
}
