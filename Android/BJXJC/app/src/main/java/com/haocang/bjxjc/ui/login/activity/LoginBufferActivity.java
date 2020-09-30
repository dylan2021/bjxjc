package com.haocang.bjxjc.ui.login.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.haocang.bjxjc.ui.login.bean.DataItemBean;
import com.haocang.bjxjc.utils.tools.CacheData;
import com.haocang.commonlib.network.request.MD5Utils;
import com.haocang.commonlib.network.request.RequestTool;
import com.haocang.bjxjc.R;
import com.haocang.bjxjc.activity.comm.CommFullActivity;
import com.haocang.bjxjc.App;
import com.haocang.bjxjc.requestcenter.DataModel;
import com.haocang.commonlib.network.interfaces.InitDataBeanBack;
import com.haocang.commonlib.network.interfaces.InitDataCallBack;
import com.haocang.bjxjc.ui.home.activity.MainActivity;
import com.haocang.bjxjc.ui.login.bean.AppLogBean;
import com.haocang.bjxjc.ui.login.bean.UserInfoBean;
import com.haocang.bjxjc.utils.bean.ArrangementBaseInfoBean;
import com.haocang.bjxjc.utils.bean.ArrangementBean;
import com.haocang.bjxjc.utils.bean.DeptBean;
import com.haocang.bjxjc.utils.bean.FloodCarBean;
import com.haocang.bjxjc.utils.bean.ReasonBean;
import com.haocang.bjxjc.utils.bean.UserBean;
import com.haocang.bjxjc.utils.tools.ApiConstant;
import com.haocang.bjxjc.utils.tools.UrlConst;
import com.haocang.bjxjc.utils.tools.FileUtils;
import com.haocang.bjxjc.utils.tools.SharedPreferencesUtil;

import net.tsz.afinal.annotation.view.ViewInject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * 数据缓冲加载 -登录
 */
public class LoginBufferActivity extends CommFullActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    @ViewInject(id = R.id.login_info)
    TextView login_info;
    @ViewInject(id = R.id.tv_app_code)
    TextView tv_app_code;

    static final int VOICE_REQUEST_CODE = 66;


    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        requestPermissions();

    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_logobuffer;
    }

    private void initActivity() {
        packageCode();
        App.initConfig();//创建本地数据库，crashlog初始化
        FileUtils.initFile();//建立工作目录
        initListData();//初始化登陆数据
        //checkVersion();
        doLogin();
    }

    private void packageCode() {
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            tv_app_code.setText("V" + info.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //一次加载所有需要的权限
    private void requestPermissions() {

        if (Build.VERSION.SDK_INT >= 23) {
            Boolean check1 = (ContextCompat
                    .checkSelfPermission(this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
            Boolean check2 = (ContextCompat
                    .checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);
            Boolean check3 = (ContextCompat
                    .checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED);
            Boolean check4 = (ContextCompat
                    .checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED);
            Boolean check5 = (ContextCompat
                    .checkSelfPermission(this,
                            Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED);
            Boolean check6 = (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED);


            if (check1 && check2 && check3 && check4 && check5 && check6) {

                initActivity();
            } else {
                ActivityCompat.requestPermissions(
                        LoginBufferActivity.this, new String[]{
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.CAMERA,
                                Manifest.permission.RECORD_AUDIO,
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.READ_PHONE_STATE},
                        VOICE_REQUEST_CODE);
            }

        } else {

            initActivity();

        }

    }


    //检查更新
    private void checkVersion() {
        checkversion(this, new InitDataCallBack() {
            @Override
            public void callbak(boolean isSucess, final String str) {
                if (isSucess) {
                    try {
                        Gson gson = new Gson();
                        List<AppLogBean> datalist = gson.fromJson(str, new TypeToken<List<AppLogBean>>() {
                        }.getType());

                        Intent intent = new Intent(LoginBufferActivity.this, UpdataLogActivity.class);
                        intent.putExtra("AppUrl", ApiConstant.host + "/AppFile/" + ApiConstant.APP_Code + ".apk");
                        intent.putExtra("AppLog", datalist.get(0).getContent());
                        startActivity(intent);
                        finish();
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        return;
                    } catch (Exception e) {
                    }
                }
                checkUpdateVersion();
            }
        });
    }

    //判断是否自动登录
    private void checkUpdateVersion() {
        checkversion(this, new InitDataCallBack() {
            @Override
            public void callbak(boolean b, final String str) {
                if (b) {
                    Intent intent = new Intent(LoginBufferActivity.this, UpdataLogActivity.class);
                    intent.putExtra("str", str);
                    startActivity(intent);
                    finish();
                }
            }
        });

        //登录
        doLogin();

    }

    private void doLogin() {
        if (SharedPreferencesUtil.GetIsSubmitPass(LoginBufferActivity.this)) {
            UserInfoBean userinfo = SharedPreferencesUtil.GetUserInfo(LoginBufferActivity.this);
            HashMap<String, Object> maps = new HashMap<>();
            final String ps = userinfo.getF_Password();
            maps.put("LoginName", userinfo.getF_Account());
            maps.put("Password", MD5Utils.MD5(ps));
            DataModel.requestPOST(mContext, ApiConstant.host + UrlConst.Api_Login, maps, new InitDataCallBack() {

                @Override
                public void callbak(boolean b, String string) {
                    if (b) {
                        Gson gson = new Gson();
                        UserInfoBean data = gson.fromJson(string, new TypeToken<UserInfoBean>() {
                        }.getType());
                        SharedPreferencesUtil.SaveUserInfo(LoginBufferActivity.this, data.getF_UserId(), data.getF_Account(),
                                data.getF_RealName(), ps, data.getF_DepartmentId(), data.getF_DepartmentName(), data.getF_HeadIcon(), "Y");
                        Intent intent = new Intent(LoginBufferActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        //登录成功
                    } else {
                        Intent intent = new Intent(LoginBufferActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        } else {
            Intent intent = new Intent(LoginBufferActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }


    //加载基础数据
    private void initListData() {
        initDeptList();//部门
        initWarmList();//预警行动
        initUserList();//用户
        initArrangementList();//应急队伍
        initArrangementBaseInfoList();//防汛队伍
        initFloodCarList();//车辆

        initItemDataList();//数据字典同步
    }

    //获取部门数据
    private void initDeptList() {
        String ApiUrl = ApiConstant.host + UrlConst.Api_List_GetDeptList;
        DataModel.requestGETMode("DeptBean", ApiUrl, new InitDataBeanBack<DeptBean>() {
            @Override
            public void callbak(boolean b, List<DeptBean> list, String msg) {
                if (b) {

                    ApiConstant.db.deleteByWhere(DeptBean.class, "name like '%%'");
                    for (DeptBean model : list) {
//                        //判断数据是否存在
//                        List<DeptBean> resultList = Config.db.findAllByWhere(DeptBean.class, "ID = '" + model.getID() + "'");
//                        if (resultList.size() > 0) {
//                            DeptBean mm = resultList.get(0);
//                            Config.db.update(mm);
//                        } else {
                        DeptBean mm = new DeptBean();
                        mm.setID(model.getID());
                        mm.setName(model.getName());
                        ApiConstant.db.save(mm);
//                        }
                    }
                    login_info.setText("部门数据加载成功");
                } else {
                    login_info.setText("部门数据" + msg);
                }
            }
        });
    }

    //获取用户数据
    private void initUserList() {
        String ApiUrl = ApiConstant.host + UrlConst.Api_List_GetUserList;
        DataModel.requestGETMode("UserBean", ApiUrl, new InitDataBeanBack<UserBean>() {
            @Override
            public void callbak(boolean b, List<UserBean> list, String msg) {
                if (b) {
                    ApiConstant.db.deleteByWhere(UserBean.class, "name like '%%'");
                    for (UserBean model : list) {
                        UserBean mm = new UserBean();
                        mm.setID(model.getID());
                        mm.setName(model.getName());
                        ApiConstant.db.save(mm);
                    }
                    login_info.setText("用户数据加载成功");
                } else {
                    login_info.setText("用户数据" + msg);
                }
            }
        });
    }

    //获取预警行动数据
    private void initWarmList() {
        String ApiUrl = ApiConstant.host + UrlConst.url_warm_list;
        DataModel.requestGETMode("ReasonBean", ApiUrl, new InitDataBeanBack<ReasonBean>() {
            @Override
            public void callbak(boolean b, List<ReasonBean> list, String msg) {
                if (b) {
                    ApiConstant.db.deleteByWhere(ReasonBean.class, "name like '%%'");
                    for (ReasonBean model : list) {
                        ReasonBean mm = new ReasonBean();
                        mm.setID(model.getID());
                        mm.setName(model.getName());
                        ApiConstant.db.save(mm);
                    }
                    login_info.setText("预警数据加载成功");
                } else {
                    login_info.setText("预警数据" + msg);
                }
            }
        });
    }

    //获取应急队伍数据
    private void initArrangementList() {
        String ApiUrl = ApiConstant.host + UrlConst.url_warm_list;
        DataModel.requestGETMode("ArrangementBean", ApiUrl, new InitDataBeanBack<ArrangementBean>() {
            @Override
            public void callbak(boolean b, List<ArrangementBean> list, String msg) {
                if (b) {
                    ApiConstant.db.deleteByWhere(ArrangementBean.class, "name like '%%'");
                    for (ArrangementBean model : list) {
                        ArrangementBean mm = new ArrangementBean();
                        mm.setID(model.getID());
                        mm.setName(model.getName());
                        ApiConstant.db.save(mm);
                    }
                    login_info.setText("应急队伍数据加载成功");
                } else {
                    login_info.setText("应急队伍数据，" + msg);
                }
            }
        });
    }


    //获取防汛队伍下拉数据
    private void initArrangementBaseInfoList() {
        String ApiUrl = ApiConstant.host + UrlConst.Api_List_GetArrangementBaseInfoList;
        DataModel.requestGETMode("ArrangementBaseInfoBean", ApiUrl, new InitDataBeanBack<ArrangementBaseInfoBean>() {
            @Override
            public void callbak(boolean b, List<ArrangementBaseInfoBean> list, String msg) {
                if (b) {
                    ApiConstant.db.deleteByWhere(ArrangementBaseInfoBean.class, "name like '%%'");
                    for (ArrangementBaseInfoBean model : list) {
                        ArrangementBaseInfoBean mm = new ArrangementBaseInfoBean();
                        mm.setID(model.getID());
                        mm.setName(model.getName());
                        ApiConstant.db.save(mm);
                    }
                    login_info.setText("防汛队伍数据加载成功");
                } else {
                    login_info.setText("防汛队伍数据，" + msg);
                }
            }
        });
    }


    //防汛车辆数据
    private void initFloodCarList() {
        String ApiUrl = ApiConstant.host + UrlConst.Api_List_GetDeptCarList;
        DataModel.requestGETMode("FloodCarBean", ApiUrl, new InitDataBeanBack<FloodCarBean>() {
            @Override
            public void callbak(boolean b, List<FloodCarBean> list, String msg) {
                if (b) {
                    ApiConstant.db.deleteByWhere(FloodCarBean.class, "name like '%%'");
                    for (FloodCarBean model : list) {
                        FloodCarBean mm = new FloodCarBean();
                        mm.setID(model.getID());
                        mm.setName(model.getName());
                        ApiConstant.db.save(mm);
                    }
                    login_info.setText("防汛车辆数据加载成功");
                } else {
                    login_info.setText("防汛车辆数据，" + msg);
                }
            }
        });
    }

    //数据字典数据同步
    private void initItemDataList() {
        String code = CacheData.GetItemDataConfig();//获取配置数据字典
        String ApiUrl = ApiConstant.host + UrlConst.Api_List_GetItemDataList + code;
        DataModel.requestGETMode("DataItemBean", ApiUrl, new InitDataBeanBack<DataItemBean>() {
            @Override
            public void callbak(boolean b, List<DataItemBean> list, String msg) {
                if (b) {
                    ApiConstant.db.deleteByWhere(DataItemBean.class, "F_ItemCode like '%%'");
                    for (DataItemBean model : list) {
                        DataItemBean mm = new DataItemBean();
                        mm.setID(model.getF_ItemCode() + model.getF_ItemValue());
                        mm.setF_ItemCode(model.getF_ItemCode());
                        mm.setF_ItemName(model.getF_ItemName());
                        mm.setF_ItemValue(model.getF_ItemValue());
                        ApiConstant.db.save(mm);
                    }
                    login_info.setText("数据字典数据同步成功");
                } else {
                    login_info.setText("数据字典同步数据，" + msg);
                }
            }
        });
    }

    @SuppressLint("Override")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == VOICE_REQUEST_CODE) {
            if ((grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    && (grantResults[1] == PackageManager.PERMISSION_GRANTED)
                    && (grantResults[2] == PackageManager.PERMISSION_GRANTED)
                    && (grantResults[3] == PackageManager.PERMISSION_GRANTED)
                    && (grantResults[4] == PackageManager.PERMISSION_GRANTED)
                    && (grantResults[5] == PackageManager.PERMISSION_GRANTED)
            ) {

                initActivity();

            } else {
                Toast.makeText(LoginBufferActivity.this, "已拒绝权限！",
                        Toast.LENGTH_SHORT).show();
            }

        }
    }


    //检查版本
    private void checkversion(Context context, final InitDataCallBack callback) {
        PackageManager manager = context.getPackageManager();
        PackageInfo info;
        try {
            info = manager.getPackageInfo(context.getPackageName(), 0);
            int version = info.versionCode;
            StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, ApiConstant.host + UrlConst.Api_GetIsAppUpdate + version,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i(ApiConstant.LogFlag, "版本升级:" + response);
                            try {
                                JSONObject json = new JSONObject(response);
                                if (json.getBoolean("Success")) {
                                    callback.callbak(true, json.getString("Data"));
                                } else {
                                    callback.callbak(false, "没有有新版本");
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                callback.callbak(false, "数据解析失败");
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    callback.callbak(false, "数据请求失败");
                }
            });
            RequestTool.getInstance(context).addToRequestQueue(jsonObjectRequest);
        } catch (PackageManager.NameNotFoundException e1) {
            e1.printStackTrace();
            callback.callbak(false, "获取版本号失败");
        }

    }


}
