package com.haocang.bjxjc.ui.faultrepair.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.haocang.bjxjc.R;
import com.haocang.bjxjc.activity.comm.CommFinalActivity;
import com.haocang.bjxjc.requestcenter.DataModel;
import com.haocang.bjxjc.ui.faultrepair.adapter.FaultFlowAdapter;
import com.haocang.bjxjc.ui.faultrepair.bean.FaultFlowBean;
import com.haocang.bjxjc.utils.tools.ApiConstant;
import com.haocang.bjxjc.utils.tools.UrlConst;
import com.haocang.commonlib.network.interfaces.InitDataBeanBack;
import net.tsz.afinal.annotation.view.ViewInject;
import java.util.List;

public class FaultFlowActivity extends CommFinalActivity {

    @ViewInject(id = R.id.list)
    ListView list;
    @ViewInject(id = R.id.yes)
    TextView yes;

    private Context context;
    private FaultFlowAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = FaultFlowActivity.this;
        initView();

        initEvent();

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

        mAdapter = new FaultFlowAdapter(context);
        list.setAdapter(mAdapter);

    }


    private void initEvent() {

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });


    }

    private void initData() {

        Intent it = getIntent();
        String ID = it.getStringExtra("ID");
        String ApiUrl = ApiConstant.host + UrlConst.Api_GetMobileFaultRepairFlow + ID;
        DataModel.requestGETMode("FaultFlowBean", ApiUrl, new InitDataBeanBack<FaultFlowBean>() {
            @Override
            public void callbak(boolean b, List<FaultFlowBean> list, String msg) {
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
