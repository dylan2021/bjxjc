package com.haocang.bjxjc.ui.login.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import com.bigkoo.pickerview.OptionsPickerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.haocang.commonlib.network.request.MD5Utils;
import com.haocang.bjxjc.R;
import com.haocang.bjxjc.activity.comm.CommFullActivity;
import com.haocang.bjxjc.requestcenter.DataModel;
import com.haocang.commonlib.network.interfaces.InitDataCallBack;
import com.haocang.bjxjc.ui.home.activity.MainActivity;
import com.haocang.bjxjc.ui.login.bean.UserInfoBean;
import com.haocang.bjxjc.utils.bean.ProvinceBean;
import com.haocang.bjxjc.utils.tools.ApiConstant;
import com.haocang.bjxjc.utils.tools.UrlConst;
import com.haocang.commonlib.loadingdialog.LoadingDialog;
import com.haocang.bjxjc.utils.tools.MyUtils;
import com.haocang.bjxjc.utils.tools.MyTextUtils;
import com.haocang.bjxjc.utils.tools.OptionsItemsUtils;
import com.haocang.bjxjc.utils.tools.SharedPreferencesUtil;

import net.tsz.afinal.annotation.view.ViewInject;

import java.util.HashMap;
import java.util.List;

import static com.haocang.bjxjc.utils.tools.ApiConstant.ip;

/**
 * 用户登录
 */

public class LoginActivity extends CommFullActivity {
    @ViewInject(id = R.id.login_username)
    EditText et_username;//用户名
    @ViewInject(id = R.id.login_userpass)
    EditText et_password;//密码
    @ViewInject(id = R.id.login_button_login)
    Button login_button;//登录按钮
    @ViewInject(id = R.id.login_ck_pass)
    CheckBox login_ck_pass;//记住密码

    @ViewInject(id = R.id.login_setting)
    ImageView login_setting;//隐形的IP设置入口

    private LoadingDialog dialog;
    private LoginActivity context;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=LoginActivity.this;
        initEvent();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_login;
    }


    private void initEvent() {
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
            }
        });

        //登录设置
        login_setting.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                switchLoginType(OptionsItemsUtils.GetIP());
                return true;
            }
        });

    }

    private void Login() {
        final String username = et_username.getText().toString().trim();
        final String password = et_password.getText().toString().trim();

        if (MyTextUtils.IsNull(username)) {
            ToastShow("用户名不能为空");
        } else if (MyTextUtils.IsNull(password)) {
            ToastShow("密码不能为空");
        } else if (!MyUtils.isOPenGPS(context)) {
            ToastShow("请在通知栏允许GPS定位！");
        } else {
            HashMap<String, Object> maps = new HashMap<>();
            maps.put("LoginName", username);
            maps.put("Password", MD5Utils.MD5(password));

            dialog = new LoadingDialog(context, "登录中");
            dialog.show();
            DataModel.requestPOST(mContext, ApiConstant.host + UrlConst.Api_Login,
                    maps, new InitDataCallBack() {
                @Override
                public void callbak(boolean isLoginSucess, String msg) {
                    Log.d("登录结果:",msg);
                    if (isLoginSucess) {
                        Gson gson = new Gson();
                        UserInfoBean data = gson.fromJson(msg, new TypeToken<UserInfoBean>() {
                        }.getType());

                        String IsSubmitPass = "N";
                        if(login_ck_pass.isChecked()){
                            IsSubmitPass = "Y";
                        }
                        Log.d("修改密码", "用户id:"+data.getF_UserId());
                        SharedPreferencesUtil.SaveUserInfo(context,
                                data.getF_UserId(),
                                data.getF_Account(),
                                data.getF_RealName(),
                                password,
                                data.getF_DepartmentId(),
                                data.getF_DepartmentName(),
                                data.getF_HeadIcon(),IsSubmitPass);
                        Intent intent = new Intent(context, MainActivity.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    } else {
                        ToastShow("登录失败," + msg);
                        dialog.close();
                    }
                }
            });
        }
    }

    private void switchLoginType(final List<ProvinceBean> items) {
        OptionsPickerView.Builder builder = new OptionsPickerView.Builder(mContext, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String value = items.get(options1).getDescription();
                switch (value){
                    case "0"://测试内网
                        ApiConstant.ip ="http://192.168.2.118";//服务ip地址
                        ApiConstant.host = ip+":8020";//服务ip地址
                        ApiConstant.HUB_URL = ip+":8081/signalr/hubs";//地址
                        ApiConstant.MAPBASEURL = "http://192.168.2.118:6080/arcgis/rest/services/SZSW_TYFW/Basemap/MapServer";
                        ApiConstant.MAPPIPEURL = "http://192.168.2.118:6080/arcgis/rest/services/SZSW_TYFW/pipe/MapServer";
                        break;
                    case "1"://测试外网
                        ApiConstant.ip ="http://kaifa.hc-yun.com";//服务ip地址
                        ApiConstant.host = ip+":33027";//服务ip地址
                        ApiConstant.HUB_URL = ip+":33025/signalr/hubs";//地址
                        ApiConstant.MAPBASEURL = ip+":33026/arcgis/rest/services/SZSW_TYFW/Basemap/MapServer";
                        ApiConstant.MAPPIPEURL = ip+":33026/arcgis/rest/services/SZSW_TYFW/pipe/MapServer";
                        break;
                    case "2"://现场本地环境
                        ApiConstant.ip ="http://172.19.1.111";//服务ip地址
                        ApiConstant.host = ip+":8020";//服务ip地址
                        ApiConstant.HUB_URL = ip+":8081/signalr/hubs";//地址
                        ApiConstant.MAPBASEURL = "http://172.19.129.10:6080/arcgis/rest/services/SZSW_TYFW/Basemap/MapServer";
                        ApiConstant.MAPPIPEURL = "http://172.19.129.10:6080/arcgis/rest/services/SZSW_TYFW/pipe/MapServer";
                        break;
                }

            }
        });
        OptionsPickerView<ProvinceBean> pickerView = new OptionsPickerView<>(builder);
        pickerView.setPicker(items);
        pickerView.show();
        hideKeyBoard();
    }

}
