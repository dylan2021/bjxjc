package com.haocang.bjxjc.utils.tools;

import com.esri.core.geometry.Geometry;

import net.tsz.afinal.FinalDb;

/**
 * 创建时间：2019/3/4
 * 编 写 人：ShenC
 * 功能描述：参数设置类
 */

public class ApiConstant {

    public static String APP_Code = "BJXJC";
    public static String LogFlag = "BJXJC_Log";

    public static FinalDb db;
    public static int db_version = 1;//数据库版本
    public static String Map_ClientId = "uK0DxqYT0om1UXa9";

    public static final int PAGE_SHOW_LIMIT = 15;//列表固定显示数量
    public static int PAGE_LIMIT = 0;

    // 测试服务器
/*    public static String ip = "http://192.168.2.139";//服务ip地址
    public static String host = ip + ":8080";//服务ip地址
    public static String host_ht = ip + ":8080/Content/ht/";//ht服务ip地址
    public static String HUB_URL = ip + ":8081/signalr/hubs";//地址
    public static String MAPBASEURL = "http://52.80.233.216:6080/arcgis/rest/services/BNAH/DCJ_BASE_ZDY/MapServer";
    public static String MAPPIPEURL = "http://192.168.2.118:6080/arcgis/rest/services/SZSW_TYFW/pipe/MapServer";
    public static String WeatherURL = "http://192.168.2.118:8020/Web/SZWeatherInfo/GetNext10d";
    public static String MAPCoordTransformUrl = "http://52.80.233.216:6080/arcgis/rest/services/" +
            "Utilities/Geometry/GeometryServer/project";*/

    //sc 本地
   /* public static String ip = "http://192.168.30.127";//服务ip地址
    public static String host = ip + ":8020";//服务ip地址
    public static String host_ht = "http://kaifa.hc-yun.com:31044/Content/ht/";//ht服务ip地址
    public static String HUB_URL = ip + ":8081/signalr/hubs";//地址
    public static String MAPBASEURL = "http://52.80.233.216:6080/arcgis/rest/services/BNAH/DCJ_BASE_ZDY/MapServer";
    public static String MAPPIPEURL = "http://192.168.2.118:6080/arcgis/rest/services/SZSW_TYFW/pipe/MapServer";
    public static String WeatherURL = host+"/mobile/homedata/GetHomeData";
    public static String MAPCoordTransformUrl = "http://52.80.233.216:6080/arcgis/rest/services" +
            "/Utilities/Geometry/GeometryServer/project";*/


    //---------------------------------  外网   --------------------------------------
   public static String ip = "http://kaifa.hc-yun.com";//服务ip地址
    public static String host = ip + ":31044";//服务ip地址
    public static String HUB_URL = ip + ":31049/signalr/hubs";//地址
    public static String MAPBASEURL = "http://52.80.233.216:6080/arcgis/rest/services/BNAH/DCJ_BASE_ZDY/MapServer";
    public static String MAPPIPEURL = "http://192.168.2.118:6080/arcgis/rest/services/" +
            "SZSW_TYFW/pipe/MapServer";

    //测试环境 外网
/*   public static String ip = "http://kaifa.hc-yun.com";//服务ip地址
    public static String host = ip + ":33027";//服务ip地址
    public static String HUB_URL = ip + ":33025/signalr/hubs";//地址
    public static String MAPBASEURL = ip + ":33026/arcgis/rest/services/SZSW_TYFW/Basemap/MapServer";
    public static String MAPPIPEURL = ip + ":33026/arcgis/rest/services/SZSW_TYFW/pipe/MapServer";*/

    public static String host_ht = ip + ":31044/Content/ht/";//ht服务ip地址
    public static String WeatherURL = ip + ":31044/mobile/homedata/GetHomeData";
    public static String MAPCoordTransformUrl = "http://52.80.233.216:6080/arcgis/rest/services/" +
            "Utilities/Geometry/GeometryServer/project";
    //---------------------------------  外网   --------------------------------------


    public static int INITIAL_TIMEOUT_MS_10 = 10 * 10000;
    public static int INITIAL_TIMEOUT_MS_5 = 5 * 10000;
    public static String application_json ="application/json";
    public static  String application_form = "application/x-www-form-urlencoded";
    public static String PassWord ="PassWord";
}
