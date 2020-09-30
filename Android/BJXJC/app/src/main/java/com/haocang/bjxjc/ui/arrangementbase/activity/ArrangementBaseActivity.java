package com.haocang.bjxjc.ui.arrangementbase.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.haocang.bjxjc.R;
import com.haocang.bjxjc.activity.comm.CommFinalActivity;
import com.haocang.bjxjc.requestcenter.DataModel;
import com.haocang.commonlib.network.interfaces.InitDataCallBack;
import com.haocang.bjxjc.ui.tools.activity.MultiSelectDialog;
import com.haocang.bjxjc.ui.tools.enums.MultiSelectEnum;
import com.haocang.bjxjc.utils.bean.FloodCarBean;
import com.haocang.bjxjc.utils.bean.UserBean;
import com.haocang.bjxjc.utils.tools.ApiConstant;
import com.haocang.bjxjc.utils.tools.UrlConst;
import com.haocang.commonlib.loadingdialog.LoadingDialog;

import net.tsz.afinal.annotation.view.ViewInject;

import java.util.HashMap;
import java.util.List;

public class ArrangementBaseActivity extends CommFinalActivity {

    @ViewInject(id = R.id.tv_1)
    TextView tv_1;
    @ViewInject(id = R.id.tv_5)
    TextView tv_5;
    @ViewInject(id = R.id.tv_2)
    TextView tv_2;
    @ViewInject(id = R.id.tv_3)
    TextView tv_3;
    @ViewInject(id = R.id.et_4)
    TextView et_4;
    @ViewInject(id = R.id.submit)
    Button submit;

    private LoadingDialog dialog;
    private Context context;

    private String ID, ManagementOffice, TeamMemberIDs, CarIDs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        context = ArrangementBaseActivity.this;
        initView();

        initEvent();

        initData();


    }


    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_arrangement_base;
    }

    @Override
    protected boolean showNavigation() {
        return true;
    }

    @Override
    protected int showMoreIcon() {
        return 0;
    }

    @Override
    protected String showTitleName() {
        return "防汛队伍编辑";
    }


    private void initView() {
    }

    private void initEvent() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initSubmit();
            }
        });

        tv_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(context, MultiSelectDialog.class);
                it.putExtra("ID", ManagementOffice.equals("D0039") ? "5557257596251453384" : "5557257596251453383");
                it.putExtra("DECIDE", "ID");
                it.putExtra("TYPE", MultiSelectEnum.GroupUser);
                it.putExtra("SELECTED", TeamMemberIDs);
                it.putExtra("IsAlone", false);
                it.putExtra("ShowIcon", true);
                startActivityForResult(it, 1);
            }
        });

        tv_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(context, MultiSelectDialog.class);
                it.putExtra("ID", ManagementOffice);
                it.putExtra("DECIDE", "ID");
                it.putExtra("TYPE", MultiSelectEnum.FloodCar);
                it.putExtra("SELECTED", CarIDs);
                it.putExtra("IsAlone", false);
                startActivityForResult(it, 2);
            }
        });

    }

    private void initData() {
        Intent it = getIntent();
        ID = it.getStringExtra("ID");
        String TeamName = it.getStringExtra("TeamName");
        TeamMemberIDs = it.getStringExtra("TeamMember");
        CarIDs = it.getStringExtra("CarID");
        String CarEquipped = it.getStringExtra("CarEquipped");
        ManagementOffice = it.getStringExtra("ManagementOffice");

        String[] teamuser_arr = TeamMemberIDs.split(",");
        String teamuser_Name = "";
        for (int i = 0; i < teamuser_arr.length; i++) {
            List<UserBean> users = ApiConstant.db.findAllByWhere(UserBean.class, "ID = '" + teamuser_arr[i] + "'");
            if (users.size() > 0) {
                if (i == teamuser_arr.length - 1) {
                    teamuser_Name += users.get(0).getName();
                } else {
                    teamuser_Name += users.get(0).getName() + ",";
                }
            }
        }

        String[] car_arr = CarIDs.split(",");
        String car_Name = "";
        for (int i = 0; i < car_arr.length; i++) {
            List<FloodCarBean> carlist = ApiConstant.db.findAllByWhere(FloodCarBean.class, "ID = '" + car_arr[i] + "'");
            if (carlist.size() > 0) {
                if (i == car_arr.length - 1) {
                    car_Name += carlist.get(0).getName();
                } else {
                    car_Name += carlist.get(0).getName() + ",";
                }
            }
        }

        tv_1.setText(TeamName);
        tv_2.setText(teamuser_Name);
        tv_3.setText(car_Name);
        tv_5.setText(ManagementOffice.equals("D0039") ? "玉凤管理所" : "新光管理所");
        et_4.setText(CarEquipped);
    }


    private void initSubmit() {

        String ApiUrl = ApiConstant.host + UrlConst.Api_SaveArangementMobileEntity;
        HashMap<String, Object> maps = new HashMap<>();
        maps.put("ID", ID);
        maps.put("TeamMember", TeamMemberIDs);
        maps.put("CarID", CarIDs);
        maps.put("CarEquipped", et_4.getText().toString());

        dialog = new LoadingDialog(context, "保存中");
        dialog.show();

        DataModel.requestPOST(context, ApiUrl, maps, new InitDataCallBack() {
            @Override
            public void callbak(boolean b, String string) {
                if (b) {
                    ToastShow("修改成功");
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                } else {
                    ToastShow("防汛队伍修改失败," + string);
                }
                dialog.close();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == 100) {
            TeamMemberIDs = data.getStringExtra("ID");
            String w_name = data.getStringExtra("Name");
            tv_2.setText(w_name);
        }

        if (requestCode == 2 && resultCode == 100) {
            CarIDs = data.getStringExtra("ID");
            String w_name = data.getStringExtra("Name");
            tv_3.setText(w_name);
        }

    }

    //退出程序
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            return super.onKeyDown(keyCode, event);
        } else {
            return false;
        }
    }


}
