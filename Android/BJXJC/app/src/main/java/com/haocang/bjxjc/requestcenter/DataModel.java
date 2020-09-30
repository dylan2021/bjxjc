package com.haocang.bjxjc.requestcenter;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.haocang.bjxjc.ui.analysis.bean.MpointCurveBean;
import com.haocang.bjxjc.ui.equipment.bean.EquParameterBean;
import com.haocang.bjxjc.ui.faultrepair.bean.EquRepairBean;
import com.haocang.bjxjc.ui.equipment.bean.EquTaskBean;
import com.haocang.bjxjc.ui.equipment.bean.EquipmentBean;
import com.haocang.bjxjc.ui.faultrepair.bean.FaultFlowBean;
import com.haocang.bjxjc.ui.ht.bean.ScreenTreeBean;
import com.haocang.bjxjc.ui.login.bean.DataItemBean;
import com.haocang.bjxjc.ui.monitor.bean.MapDataBean;
import com.haocang.bjxjc.ui.reason.bean.EmergencySolutionBean;
import com.haocang.bjxjc.ui.reason.bean.WarningDetailBean;
import com.haocang.bjxjc.utils.tools.PhoneUtils;
import com.haocang.bjxjc.utils.tools.TextUtil;
import com.haocang.commonlib.network.interfaces.InitDataCallBack;
import com.haocang.commonlib.network.request.RequestTool;
import com.haocang.bjxjc.App;
import com.haocang.commonlib.network.interfaces.InitDataBeanBack;
import com.haocang.commonlib.network.interfaces.InitDataOneBeanBack;
import com.haocang.bjxjc.ui.event.EventBean;
import com.haocang.bjxjc.ui.floodpoint.bean.FloodPointBean;
import com.haocang.bjxjc.ui.home.bean.MsgNumberBean;
import com.haocang.bjxjc.ui.msg.bean.MsgBean;
import com.haocang.bjxjc.ui.user.bean.ArrangementBaseBean;
import com.haocang.bjxjc.ui.user.bean.GroupUserBean;
import com.haocang.bjxjc.ui.waterpoint.bean.WaterFlowBean;
import com.haocang.bjxjc.ui.waterpoint.bean.WaterPointBean;
import com.haocang.bjxjc.utils.bean.ArrangementBaseInfoBean;
import com.haocang.bjxjc.utils.bean.ArrangementBean;
import com.haocang.bjxjc.utils.bean.DeptBean;
import com.haocang.bjxjc.utils.bean.EarlyWarningActionBean;
import com.haocang.bjxjc.utils.bean.FileBean;
import com.haocang.bjxjc.utils.bean.FloodCarBean;
import com.haocang.bjxjc.utils.bean.ProvinceBean;
import com.haocang.bjxjc.utils.bean.ReasonBean;
import com.haocang.bjxjc.utils.bean.ShareIdNameBean;
import com.haocang.bjxjc.utils.bean.UserBean;
import com.haocang.bjxjc.utils.bean.XyBean;
import com.haocang.bjxjc.utils.tools.ApiConstant;

/**
 * 网络请求类
 */

public class DataModel {
    //GET请求
    public static void requestGETMode(final String ModelType, String url, final InitDataBeanBack callback) {
        if (PhoneUtils.isNoNetwork(App.getAppContext())) {
            callback.callbak(false, null, "网络异常,请检查网络设置");
            return;
        }
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.d(TAG, "获取配置数据:" + obj.toString());
                            if (obj.getInt("Code") == 0) {
                                Gson gson = new Gson();
                                if (ModelType.equals("FloodPointBean")) {
                                    List<FloodPointBean> datalist = gson.fromJson(obj.getString("Data"), new TypeToken<List<FloodPointBean>>() {
                                    }.getType());
                                    callback.callbak(true, datalist, "请求成功");
                                } else if (ModelType.equals("WaterPointBean")) {
                                    List<WaterPointBean> datalist = gson.fromJson(obj.getString("Data"), new TypeToken<List<WaterPointBean>>() {
                                    }.getType());
                                    callback.callbak(true, datalist, "请求成功");
                                } else if (ModelType.equals("DeptBean")) {
                                    List<DeptBean> datalist = gson.fromJson(obj.getString("Data"), new TypeToken<List<DeptBean>>() {
                                    }.getType());
                                    callback.callbak(true, datalist, "请求成功");
                                } else if (ModelType.equals("UserBean")) {
                                    List<UserBean> datalist = gson.fromJson(obj.getString("Data"), new TypeToken<List<UserBean>>() {
                                    }.getType());
                                    callback.callbak(true, datalist, "请求成功");
                                } else if (ModelType.equals("ReasonBean")) {
                                    List<ReasonBean> datalist = gson.fromJson(obj.getString("Data"), new TypeToken<List<ReasonBean>>() {
                                    }.getType());
                                    callback.callbak(true, datalist, "请求成功");
                                } else if (ModelType.equals("EventBean")) {
                                    List<EventBean> datalist = gson.fromJson(obj.getString("Data"), new TypeToken<List<EventBean>>() {
                                    }.getType());
                                    callback.callbak(true, datalist, "请求成功");
                                } else if (ModelType.equals("FileBean")) {
                                    List<FileBean> datalist = gson.fromJson(obj.getString("Data"), new TypeToken<List<FileBean>>() {
                                    }.getType());
                                    callback.callbak(true, datalist, "请求成功");
                                } else if (ModelType.equals("MsgBean")) {
                                    List<MsgBean> datalist = gson.fromJson(obj.getString("Data"), new TypeToken<List<MsgBean>>() {
                                    }.getType());
                                    callback.callbak(true, datalist, "请求成功");
                                } else if (ModelType.equals("EarlyWarningActionBean")) {
                                    Log.d(TAG, "接口请求返回:" + obj.toString());
                                    List<EarlyWarningActionBean> datalist = gson.fromJson(obj.getString("Data"), new TypeToken<List<EarlyWarningActionBean>>() {
                                    }.getType());
                                    callback.callbak(true, datalist, "请求成功");
                                } else if (ModelType.equals("ArrangementBean")) {
                                    List<ArrangementBean> datalist = gson.fromJson(obj.getString("Data"), new TypeToken<List<ArrangementBean>>() {
                                    }.getType());
                                    callback.callbak(true, datalist, "请求成功");
                                } else if (ModelType.equals("ArrangementBaseInfoBean")) {
                                    List<ArrangementBaseInfoBean> datalist = gson.fromJson(obj.getString("Data"), new TypeToken<List<ArrangementBaseInfoBean>>() {
                                    }.getType());
                                    callback.callbak(true, datalist, "请求成功");
                                } else if (ModelType.equals("ArrangementBaseBean")) {
                                    List<ArrangementBaseBean> datalist = gson.fromJson(obj.getString("Data"), new TypeToken<List<ArrangementBaseBean>>() {
                                    }.getType());
                                    callback.callbak(true, datalist, "请求成功");
                                } else if (ModelType.equals("ShareIdNameBean")) {
                                    List<ShareIdNameBean> datalist = gson.fromJson(obj.getString("Data"), new TypeToken<List<ShareIdNameBean>>() {
                                    }.getType());
                                    callback.callbak(true, datalist, "请求成功");
                                } else if (ModelType.equals("GroupUserBean")) {
                                    List<GroupUserBean> datalist = gson.fromJson(obj.getString("Data"), new TypeToken<List<GroupUserBean>>() {
                                    }.getType());
                                    callback.callbak(true, datalist, "请求成功");
                                } else if (ModelType.equals("WaterFlowBean")) {
                                    List<WaterFlowBean> datalist = gson.fromJson(obj.getString("Data"), new TypeToken<List<WaterFlowBean>>() {
                                    }.getType());
                                    callback.callbak(true, datalist, "请求成功");
                                } else if (ModelType.equals("FloodCarBean")) {
                                    List<FloodCarBean> datalist = gson.fromJson(obj.getString("Data"), new TypeToken<List<FloodCarBean>>() {
                                    }.getType());
                                    callback.callbak(true, datalist, "请求成功");
                                } else if (ModelType.equals("ScreenTreeBean")) {
                                    List<ScreenTreeBean> datalist = gson.fromJson(obj.getString("Data"), new TypeToken<List<ScreenTreeBean>>() {
                                    }.getType());
                                    callback.callbak(true, datalist, "请求成功");

                                } else if (ModelType.equals("MapDataBean")) {
                                    List<MapDataBean> datalist = gson.fromJson(obj.getString("Data"), new TypeToken<List<MapDataBean>>() {
                                    }.getType());
                                    callback.callbak(true, datalist, "请求成功");
                                } else if (ModelType.equals("MpointCurveBean")) {
                                    List<MpointCurveBean> datalist = gson.fromJson(obj.getString("Data"), new TypeToken<List<MpointCurveBean>>() {
                                    }.getType());
                                    callback.callbak(true, datalist, "请求成功");
                                } else if (ModelType.equals("DataItemBean")) {
                                    List<DataItemBean> datalist = gson.fromJson(obj.getString("Data"), new TypeToken<List<DataItemBean>>() {
                                    }.getType());
                                    callback.callbak(true, datalist, "请求成功");
                                } else if (ModelType.equals("EquipmentBean")) {
                                    List<EquipmentBean> datalist = gson.fromJson(obj.getString("Data"), new TypeToken<List<EquipmentBean>>() {
                                    }.getType());
                                    callback.callbak(true, datalist, "请求成功");
                                } else if (ModelType.equals("EquRepairBean")) {
                                    List<EquRepairBean> datalist = gson.fromJson(obj.getString("Data"), new TypeToken<List<EquRepairBean>>() {
                                    }.getType());
                                    callback.callbak(true, datalist, "请求成功");
                                } else if (ModelType.equals("EquTaskBean")) {
                                    List<EquTaskBean> datalist = gson.fromJson(obj.getString("Data"), new TypeToken<List<EquTaskBean>>() {
                                    }.getType());
                                    callback.callbak(true, datalist, "请求成功");
                                } else if (ModelType.equals("FaultFlowBean")) {
                                    List<FaultFlowBean> datalist = gson.fromJson(obj.getString("Data"), new TypeToken<List<FaultFlowBean>>() {
                                    }.getType());
                                    callback.callbak(true, datalist, "请求成功");
                                } else {
                                    callback.callbak(false, null, "实体未定义");
                                }
                            } else {
                                callback.callbak(false, null, "错误码：" + obj.getInt("Code") + "," + obj.getString("Message"));
                            }
                        } catch (JSONException e) {
                            callback.callbak(false, null, "解析错误" + e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.callbak(false, null, ModelType + "请求失败");
            }
        });
        App.requestQueue.add(request);
    }


    /**
     * GET 请求
     */
    public static void requestGETModeinfo(final String ModelType, String ApiUrl, final InitDataBeanBack callback) {
        StringRequest request = new StringRequest(Request.Method.GET, ApiUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            if (jsonobject.getInt("code") == 200) {
                                Gson gson = new Gson();

                                //======实体类定义==================================================================
                                if (ModelType.equals("ScreenTreeBean")) {
                                    List<ScreenTreeBean> datalist = gson.fromJson(jsonobject.getString("data"), new TypeToken<List<ScreenTreeBean>>() {
                                    }.getType());
                                    callback.callbak(true, datalist, "请求成功");
                                } else {
                                    callback.callbak(false, null, "实体未定义");
                                }

                                //====================================================================================

                            } else {
                                callback.callbak(false, null, "错误码：" + jsonobject.getInt("code") + "," + jsonobject.getString("info"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            callback.callbak(false, null, "解析错误" + e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.callbak(false, null, "网络或者服务端错误：" + ModelType);
            }
        });
        App.requestQueue.add(request);

       /* jsonObjectRequeststauts.setRetryPolicy(new DefaultRetryPolicy(Cons.INITIAL_TIMEOUT_MS_10, 1, 1.0f));
        RequestTool.getInstance(App.getAppContext()).addToRequestQueue(jsonObjectRequeststauts);*/
    }

    /*
     * 单次Get请求
     */
    public static void requestGETOneMode(final String ModelType, String url, final InitDataOneBeanBack callback) {
        Log.d(TAG, "预案数据请求:" + url);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "预案数据请求返回:" + response);
                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            if (jsonobject.getInt("Code") == 0) {
                                Gson gson = new Gson();

                                //======s实体类定义==================================================================
                                if (ModelType.equals("ProvinceBean")) {
                                    ProvinceBean data = gson.fromJson(jsonobject.getString("Data"), new TypeToken<ProvinceBean>() {
                                    }.getType());
                                    callback.callbak(true, data, "请求成功");
                                } else if (ModelType.equals("WaterPointBean")) {
                                    WaterPointBean data = gson.fromJson(jsonobject.getString("Data"), new TypeToken<WaterPointBean>() {
                                    }.getType());
                                    callback.callbak(true, data, "请求成功");
                                } else if (ModelType.equals("MsgNumberBean")) {
                                    MsgNumberBean data = gson.fromJson(jsonobject.getString("Data"), new TypeToken<MsgNumberBean>() {
                                    }.getType());
                                    callback.callbak(true, data, "请求成功");
                                } else if (ModelType.equals("XyBean")) {
                                    XyBean data = gson.fromJson(jsonobject.getString("Data"), new TypeToken<XyBean>() {
                                    }.getType());
                                    callback.callbak(true, data, "请求成功");
                                } else if (ModelType.equals("EmergencySolutionBean")) {
                                    EmergencySolutionBean data = gson.fromJson(jsonobject.getString("Data"), new TypeToken<EmergencySolutionBean>() {
                                    }.getType());
                                    callback.callbak(true, data, "请求成功");

                                } else if (ModelType.equals("WarningDetailBean")) {
                                    WarningDetailBean data = gson.fromJson(jsonobject.getString("Data"), new TypeToken<WarningDetailBean>() {
                                    }.getType());
                                    callback.callbak(true, data, "请求成功");

                                } else if (ModelType.equals("EquParameterBean")) {
                                    EquParameterBean data = gson.fromJson(jsonobject.getString("Data"), new TypeToken<EquParameterBean>() {
                                    }.getType());
                                    callback.callbak(true, data, "请求成功");
                                } else {
                                    callback.callbak(false, null, "实体未定义");
                                }
                                //====================================================================================

                            } else {
                                callback.callbak(false, null, jsonobject.getString("Message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            callback.callbak(false, null, "解析错误" + e.toString());
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.callbak(false, null, "网络或者服务端错误：" + ModelType);
            }
        });
        App.requestQueue.add(request);
  /*      jsonObjectRequeststauts.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
        RequestTool.getInstance(App.getAppContext()).addToRequestQueue(jsonObjectRequeststauts);*/
    }


    /**
     * GET 请求
     */
    public static void requestGETObject(final String ModelType, String ApiUrl, final InitDataBeanBack callback) {
        StringRequest jsonObjectRequeststauts = new StringRequest(Request.Method.GET, ApiUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        if (ModelType.equals("ProvinceBean")) {
                            List<ProvinceBean> datalist = gson.fromJson(response, new TypeToken<List<ProvinceBean>>() {
                            }.getType());
                            callback.callbak(true, datalist, "请求成功");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.callbak(false, null, "网络或者服务端错误");
            }
        });

        jsonObjectRequeststauts.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
        RequestTool.getInstance(App.getAppContext()).addToRequestQueue(jsonObjectRequeststauts);
    }


    public static void getBillNo(final String MenuID, final InitDataOneBeanBack callback) {
        String ApiUrl = ApiConstant.host + "/WebWZ/Mobile/share/tool/GetBillNo?MenuID=" + MenuID;
        StringRequest jsonObjectRequeststauts = new StringRequest(Request.Method.GET, ApiUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            if (jsonobject.getBoolean("Result")) {

                                callback.callbak(true, jsonobject.getString("Data"), "请求成功");

                            } else {
                                callback.callbak(false, null, jsonobject.getString("Msg"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            callback.callbak(false, null, "解析错误" + e.toString());
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.callbak(false, null, "网络或者服务端错误");
            }
        });
        RequestTool.getInstance(App.getAppContext()).addToRequestQueue(jsonObjectRequeststauts);
    }

    /**
     * GET 请求
     */
    public static void requestGET(Context context, String ApiUrl, final InitDataCallBack callback) {
        StringRequest request = new StringRequest(Request.Method.GET, ApiUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        callback.callbak(true, response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.callbak(false, "坐标转换失败:" + TextUtil.getErrorMsg(error));
            }
        });
        App.requestQueue.add(request);
    }

    //POST 请求
    public static void requestPOST(Context context, String ApiUrl,
                                   HashMap<String, Object> maps, final InitDataCallBack callback) {
        JSONObject params = new JSONObject(maps);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, ApiUrl, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(ApiConstant.LogFlag, "DataModel > requestPOST  数据获取成功");
                        Log.d("修改密码", "返回," + response.toString());
                        try {
                            if (response.getInt("Code") == 0) {
                                callback.callbak(true, response.getString("Data"));
                            } else {
                                callback.callbak(false, "" + response.getString("Message"));
                            }
                        } catch (JSONException e) {
                            Log.d("修改密码", "解析失败," + e.toString());
                            callback.callbak(false, "请求失败,稍后重试");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.callbak(false, "请求失败,稍后重试");
            }
        });
        App.requestQueue.add(request);
    }

    // PUT请求
    public static void requestPUT(Context context, String ApiUrl, HashMap<String, Object> maps, final InitDataCallBack callback) {
        JSONObject params = new JSONObject(maps);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, ApiUrl, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(ApiConstant.LogFlag, "DataModel > requestPOST  数据获取成功");

                        try {
                            if (response.getBoolean("Result")) {
                                callback.callbak(true, response.getString("Data"));
                            } else {
                                callback.callbak(false, "数据错误" + response.getString("Msg"));
                            }
                        } catch (JSONException e) {
                            callback.callbak(false, "解析错误");
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.callbak(false, "网络错误,数据加载失败。");
            }
        });
        App.requestQueue.add(request);
    }


    /**
     * DELETE请求
     */
    public static void requestDELETE(Context context, String ApiUrl, HashMap<String, Object> maps, final InitDataCallBack callback) {
        JSONObject params = new JSONObject(maps);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, ApiUrl, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(ApiConstant.LogFlag, "DataModel > requestPOST  数据获取成功");
                        try {
                            if (response.getBoolean("Result")) {
                                callback.callbak(true, response.getString("Data"));
                            } else {
                                callback.callbak(false, "数据错误" + response.getString("Msg"));
                            }
                        } catch (JSONException e) {
                            callback.callbak(false, "解析错误");
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.callbak(false, "网络错误获取数据失败。");
            }
        });
        App.requestQueue.add(request);
    }

    private static String TAG = DataModel.class.getSimpleName();
}
