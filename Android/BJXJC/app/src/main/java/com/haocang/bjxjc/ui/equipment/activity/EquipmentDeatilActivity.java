package com.haocang.bjxjc.ui.equipment.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haocang.bjxjc.R;
import com.haocang.bjxjc.activity.comm.CommFinalActivity;
import com.haocang.bjxjc.ui.equipment.bean.EquipmentBean;
import com.haocang.bjxjc.ui.equipment.fragment.EquipmentFileFragment;
import com.haocang.bjxjc.ui.equipment.fragment.EquInfoFragment;
import com.haocang.bjxjc.ui.equipment.fragment.EquMaintainFragment;
import com.haocang.bjxjc.ui.equipment.fragment.EquParamFragment;
import com.haocang.bjxjc.ui.equipment.fragment.EquPartFragment;
import com.haocang.bjxjc.ui.equipment.fragment.EquRepairFragment;
import com.haocang.bjxjc.utils.tools.CacheData;

import net.tsz.afinal.annotation.view.ViewInject;

import java.util.ArrayList;
import java.util.List;

//设备详情
public class EquipmentDeatilActivity extends CommFinalActivity implements View.OnClickListener{
    @ViewInject(id = R.id.lin_tab_1)
    LinearLayout lin_tab_1;
    @ViewInject(id = R.id.tv_tab_1_text)
    TextView tv_tab_1_text;
    @ViewInject(id = R.id.tv_tab_1)
    TextView tv_tab_1;
    @ViewInject(id = R.id.lin_tab_2)
    LinearLayout lin_tab_2;
    @ViewInject(id = R.id.tv_tab_2_text)
    TextView tv_tab_2_text;
    @ViewInject(id = R.id.tv_tab_2)
    TextView tv_tab_2;
    @ViewInject(id = R.id.lin_tab_3)
    LinearLayout lin_tab_3;
    @ViewInject(id = R.id.tv_tab_3_text)
    TextView tv_tab_3_text;
    @ViewInject(id = R.id.tv_tab_3)
    TextView tv_tab_3;

    @ViewInject(id = R.id.lin_tab_4)
    LinearLayout lin_tab_4;
    @ViewInject(id = R.id.tv_tab_4_text)
    TextView tv_tab_4_text;
    @ViewInject(id = R.id.tv_tab_4)
    TextView tv_tab_4;

    @ViewInject(id = R.id.lin_tab_5)
    LinearLayout lin_tab_5;
    @ViewInject(id = R.id.tv_tab_5_text)
    TextView tv_tab_5_text;
    @ViewInject(id = R.id.tv_tab_5)
    TextView tv_tab_5;

    @ViewInject(id = R.id.lin_tab_6)
    LinearLayout lin_tab_6;
    @ViewInject(id = R.id.tv_tab_6_text)
    TextView tv_tab_6_text;
    @ViewInject(id = R.id.tv_tab_6)
    TextView tv_tab_6;

    List<TextView> tab_text = new ArrayList<>();
    List<TextView> tab_block = new ArrayList<>();


    @ViewInject(id = R.id.tv_FullName) TextView tv_FullName;
    @ViewInject(id = R.id.tv_EquNo) TextView tv_EquNo;
    @ViewInject(id = R.id.tv_EquTypeName) TextView tv_EquTypeName;
    @ViewInject(id = R.id.tv_PumpRoomName) TextView tv_PumpRoomName;
    @ViewInject(id = R.id.tv_AssetState) TextView tv_AssetState;
    @ViewInject(id = R.id.tv_ABC) TextView tv_ABC;
    @ViewInject(id = R.id.tv_F_CompanyId) TextView tv_F_CompanyId;
    @ViewInject(id = R.id.tv_F_ParentId) TextView tv_F_ParentId;

    private Fragment currentFragment = new Fragment();
    private EquInfoFragment mEquInfoFragment;
    private EquPartFragment mEquPartFragment;
    private EquParamFragment mEquParamFragment;
    private EquipmentFileFragment mEquipmentFileFragment;
    private EquRepairFragment mEquRepairFragment;
    private EquMaintainFragment mEquMaintainFragment;

    private Context context;
    private  EquipmentBean model;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        initView();


        initEvent();

        initData();

    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_equipment_deatil;
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
        return "设备详情";
    }

    //初始化
    private void initView() {

        lin_tab_1.setOnClickListener(this);
        lin_tab_2.setOnClickListener(this);
        lin_tab_3.setOnClickListener(this);
        lin_tab_4.setOnClickListener(this);
        lin_tab_5.setOnClickListener(this);
        lin_tab_6.setOnClickListener(this);
        tab_text.add(tv_tab_1_text);
        tab_text.add(tv_tab_2_text);
        tab_text.add(tv_tab_3_text);
        tab_text.add(tv_tab_4_text);
        tab_text.add(tv_tab_5_text);
        tab_text.add(tv_tab_6_text);
        tab_block.add(tv_tab_1);
        tab_block.add(tv_tab_2);
        tab_block.add(tv_tab_3);
        tab_block.add(tv_tab_4);
        tab_block.add(tv_tab_5);
        tab_block.add(tv_tab_6);

        mEquInfoFragment = new EquInfoFragment();
        mEquPartFragment = new EquPartFragment();
        mEquParamFragment = new EquParamFragment();
        mEquipmentFileFragment = new EquipmentFileFragment();
        mEquRepairFragment = new EquRepairFragment();
        mEquMaintainFragment = new EquMaintainFragment();//保养信息
        switchFragment(mEquInfoFragment).commit();
        changeStatus(0);
    }


    private void initEvent() {


        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }

    private void initData(){
        model =  CacheData.mEquipmentBean;


        tv_FullName.setText(model.getFullName());
        tv_EquNo.setText(model.getEquNo());
        tv_EquTypeName.setText(model.getEquTypeName());
        tv_PumpRoomName.setText(model.getPumpRoomName());
        tv_ABC.setText(model.getABC());
        tv_F_CompanyId.setText(model.getF_CompanyId());
        tv_F_ParentId.setText(model.getF_ParentId());

        if(model.getAssetState().equals("1")){
            tv_AssetState.setText("启用");
        }else if(model.getAssetState().equals("2")){
            tv_AssetState.setText("封存");
        }else if(model.getAssetState().equals("9")){
            tv_AssetState.setText("报废");
        }

    }

    private FragmentTransaction switchFragment(Fragment targetFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if (!targetFragment.isAdded()) {
            //第一次使用switchFragment()时currentFragment为null，所以要判断一下      
            if (currentFragment != null) {
                transaction.hide(currentFragment);
            }
            transaction.add(R.id.rl_container, targetFragment, targetFragment.getClass().getName());
        } else {
            transaction.hide(currentFragment).show(targetFragment);
        }
        currentFragment = targetFragment;
        return transaction;
    }


    private void changeStatus(int index) {
        for(int i = 0;i< tab_text.size();i++){
            if(i == index){
                tab_text.get(i).setSelected(true);
                tab_text.get(i).animate().scaleX(1.1f).scaleY(1.1f);
                tab_block.get(i).setBackgroundColor(ContextCompat.getColor(context,R.color.app_title_color));
            }else{
                tab_text.get(i).setSelected(false);
                tab_text.get(i).animate().scaleX(1.0f).scaleY(1.0f);
                tab_block.get(i).setBackgroundColor(ContextCompat.getColor(context,R.color.white));
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lin_tab_1:
                changeStatus(0);
                switchFragment(mEquInfoFragment).commit();
                break;
            case R.id.lin_tab_2:
                changeStatus(1);
                switchFragment(mEquPartFragment).commit();
                break;
            case R.id.lin_tab_3:
                changeStatus(2);
                switchFragment(mEquParamFragment).commit();
                break;
            case R.id.lin_tab_4:
                changeStatus(3);
                switchFragment(mEquipmentFileFragment).commit();
                break;
            case R.id.lin_tab_5:
                changeStatus(4);
                switchFragment(mEquRepairFragment).commit();
                break;
            case R.id.lin_tab_6:
                changeStatus(5);
                switchFragment(mEquMaintainFragment).commit();
                break;
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
