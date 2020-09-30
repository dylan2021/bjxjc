package com.haocang.bjxjc.ui.user.acts;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haocang.bjxjc.R;
import com.haocang.bjxjc.activity.tool.CacheActivityUtil;
import com.haocang.bjxjc.requestcenter.DataModel;
import com.haocang.bjxjc.ui.dialog.SimpleDialogFragment;
import com.haocang.bjxjc.ui.login.activity.LoginActivity;
import com.haocang.bjxjc.utils.tools.ApiConstant;
import com.haocang.bjxjc.utils.tools.UrlConst;
import com.haocang.bjxjc.utils.tools.CacheData;
import com.haocang.bjxjc.utils.tools.DialogHelper;
import com.haocang.bjxjc.utils.tools.PhoneUtils;
import com.haocang.bjxjc.utils.tools.ToastUtil;
import com.haocang.commonlib.network.interfaces.InitDataCallBack;
import com.haocang.commonlib.network.request.MD5Utils;

import java.util.HashMap;


/**
 * 修改密码
 * Gool Lee
 */
public class ChangePwdActivity extends BaseFgActivity {

    private Button bt_find_pwd;
    private EditText et_old_pwd, newPwdET1;
    private SharedPreferences.Editor editor;
    private ChangePwdActivity context;
    private String tokenSp = "";
    private String spPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initStatusBar();
        this.setContentView(R.layout.activity_change_pwd);

        initTitleBackBt("");
        ((TextView) findViewById(R.id.left_bt)).setText(R.string.cancel);
        context = ChangePwdActivity.this;

        spPwd = CacheData.getSP_PWD();
        bt_find_pwd = (Button) findViewById(R.id.bt_find_pwd);
        et_old_pwd = (EditText) findViewById(R.id.old_pwd_et);
        newPwdET1 = (EditText) findViewById(R.id.new_pwd_et1);

        bt_find_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneUtils.hideKeyBoard(context);
                String oldPwdStr = et_old_pwd.getText().toString().trim();
                String newPwdETStr1 = newPwdET1.getText().toString().trim();

                if (oldPwdStr == null || oldPwdStr.length() <= 0) {
                    ToastUtil.show("原密码不能为空");
                    return;
                }
                if (!oldPwdStr.equals(spPwd)) {
                    ToastUtil.show("原密码不正确");
                    return;
                }
                if (oldPwdStr.equals(newPwdETStr1)) {
                    ToastUtil.show("新密码和原密码不能相同");
                    return;
                }
                if (newPwdETStr1 == null || newPwdETStr1.length() <= 0) {
                    ToastUtil.show("请输入新密码");
                    return;
                }
                if (newPwdETStr1.length() < 6) {
                    ToastUtil.show("新密码不能少于六位");
                    return;
                }

                changePwd(oldPwdStr, newPwdETStr1);
            }
        });
    }

    //修改密码
    private void changePwd(final String oldPwdStr, final String newPwdETStr1) {
        String url = ApiConstant.host + UrlConst.url_change_pwd;
        final FragmentManager fm = getSupportFragmentManager();
        DialogHelper.showWaiting(fm, "加载中...");
        if (PhoneUtils.isNoNetwork(context)) {
            ToastUtil.show(getString(R.string.no_network));
            return;
        }
        url=url+"?userId="+CacheData.UserID()+
            "&password="+MD5Utils.MD5(newPwdETStr1)+"&oldPassword="+MD5Utils.MD5(oldPwdStr);
        HashMap<String, Object> map = new HashMap<>();
      /*  map.put(KeyConst.userld, CacheData.UserID());
        map.put(KeyConst.password, MD5Utils.MD5(newPwdETStr1));
        map.put(KeyConst.oldPassword, MD5Utils.MD5(oldPwdStr));*/
        DataModel.requestPOST(context, url, map, new InitDataCallBack() {
            @Override
            public void callbak(boolean isSuccess, String msg) {
                DialogHelper.hideWaiting(fm);
                if (isSuccess) {
                    showDialog(getString(R.string.pwd_change_success_reLogin_msg));
                } else {
                    ToastUtil.show("修改失败,稍后重试" + msg);
                }
            }
        });
    }

    private void showDialog(String msg) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        final SimpleDialogFragment dialogFragment = new SimpleDialogFragment();
        dialogFragment.setDialogWidth(220);
        dialogFragment.setCancelable(false);

        TextView tv = new TextView(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams
                .MATCH_PARENT, ViewGroup
                .LayoutParams.MATCH_PARENT);
        params.setMargins(0, 0, 0, 0);
        params.gravity = Gravity.CENTER;
        tv.setLayoutParams(params);
        tv.setGravity(Gravity.CENTER);
        tv.setText(msg);
        tv.setTextColor(getResources().getColor(R.color.color666));
        dialogFragment.setContentView(tv);

        dialogFragment.setNegativeButton(R.string.reLogin, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFragment.dismiss();
                context.finish();
                CacheActivityUtil.finishActivity();
                Intent intent = new Intent(context, LoginActivity.class);
                startActivity(intent);
            }
        });
        dialogFragment.show(ft, "successDialog");
    }
}
