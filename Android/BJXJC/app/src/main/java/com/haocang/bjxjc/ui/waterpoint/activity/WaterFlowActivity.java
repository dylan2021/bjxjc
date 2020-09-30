package com.haocang.bjxjc.ui.waterpoint.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.haocang.bjxjc.R;
import com.haocang.bjxjc.activity.comm.CommFinalActivity;
import com.haocang.bjxjc.requestcenter.DataModel;
import com.haocang.commonlib.network.interfaces.InitDataBeanBack;
import com.haocang.bjxjc.ui.tools.activity.AttachmentListActivity;
import com.haocang.bjxjc.ui.waterpoint.adapter.WaterFlowAdapter;
import com.haocang.bjxjc.ui.waterpoint.bean.WaterFlowBean;
import com.haocang.bjxjc.utils.tools.ApiConstant;
import com.haocang.bjxjc.utils.tools.UrlConst;

import net.tsz.afinal.annotation.view.ViewInject;

import java.util.List;

//积水处理流程图
public class WaterFlowActivity extends CommFinalActivity {
    @ViewInject(id = R.id.list)
    ListView list;
    @ViewInject(id = R.id.yes)
    TextView yes;

    private Context context;
    private WaterFlowAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = WaterFlowActivity.this;
        initView();
        initData();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_water_flow;
    }

    @Override
    protected boolean showNavigation() {
        return false;
    }

    @Override
    protected int showMoreIcon() {
        return 0;
    }

    @Override
    protected String showTitleName() {
        return "积水处理流程图";
    }

    private void initView() {

        mAdapter = new WaterFlowAdapter(context);
        list.setAdapter(mAdapter);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                WaterFlowBean mbean = mAdapter.getItem(arg2);
                if(mbean.getF_FileSize().equals("0")) {
                    ToastShow("该转单节点无附件！");
                }else{
                    Intent intent = new Intent(context, AttachmentListActivity.class);
                    intent.putExtra("BILLID", mbean.getID());
                    startActivity(intent);
                }
            }
        });

    }

    private void initData() {

        Intent it = getIntent();
        String ID = it.getStringExtra("ID");
        String ApiUrl = ApiConstant.host + UrlConst.Api_GetWaterMsgList + ID;
        DataModel.requestGETMode("WaterFlowBean", ApiUrl, new InitDataBeanBack<WaterFlowBean>() {
            @Override
            public void callbak(boolean b, List<WaterFlowBean> list, String msg) {
                if (b) {
                    mAdapter.setList(list);
                    mAdapter.notifyDataSetChanged();
                } else {
                    ToastShow(msg);
                }
            }
        });


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
