package com.haocang.bjxjc.ui.tools.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.haocang.bjxjc.R;
import com.haocang.bjxjc.activity.comm.CommFinalActivity;
import com.haocang.bjxjc.requestcenter.DataModel;
import com.haocang.commonlib.network.interfaces.InitDataBeanBack;
import com.haocang.bjxjc.ui.floodpoint.bean.FloodPointBean;
import com.haocang.bjxjc.ui.tools.adapter.GetFloodPointListAdapter;
import com.haocang.bjxjc.utils.tools.ApiConstant;
import com.haocang.bjxjc.utils.tools.UrlConst;
import com.haocang.commonlib.recyclerView.BaseAdapter;
import com.haocang.bjxjc.utils.tools.CacheData;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import net.tsz.afinal.annotation.view.ViewInject;

import java.util.List;

public class GetFloodPointActivity extends CommFinalActivity {


    @ViewInject(id = R.id.recyclerview)
    RecyclerView recyclerview;
    @ViewInject(id = R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @ViewInject(id = R.id.et_location)
    EditText et_location;
    @ViewInject(id = R.id.bt_quert)
    Button bt_quert;
    private GetFloodPointListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();

        initEvent();

        refreshLayout.autoRefresh();

    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_get_flood_point;
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
        return "请选择关联的易淹点";
    }


    private void initView() {
        mAdapter = new GetFloodPointListAdapter(GetFloodPointActivity.this);
        recyclerview.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL) {
            @Override
            public boolean canScrollVertically() {
                return true;//是否允许滑动
            }
        });
        recyclerview.setAdapter(mAdapter);
    }


    private void initEvent(){

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
        });


        bt_quert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshLayout.autoRefresh();
            }
        });

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                ApiConstant.PAGE_LIMIT = ApiConstant.PAGE_SHOW_LIMIT;
                refreshlayout.finishRefresh(20000/*,false*/);//传入false表示刷新失败
                initData(refreshlayout);
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore(20000/*,false*/);//传入false表示加载失败
                ApiConstant.PAGE_LIMIT += ApiConstant.PAGE_SHOW_LIMIT;
                initData(refreshlayout);
            }
        });

        mAdapter.setOnItemChildClickListener(new BaseAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseAdapter adapter, View v, int position) {

                CacheData.mFloodPointBean = mAdapter.getItem(position);
                Intent intent = new Intent();
                GetFloodPointActivity.this.setResult(100, intent);
                GetFloodPointActivity.this.finish();
            }

        });

    }
    private void  initData(final RefreshLayout refreshlayout) {

        String location  = et_location.getText().toString();

        String ApiUrl = ApiConstant.host
                + UrlConst.Api_FloodPoint
                +"?limit="+ ApiConstant.PAGE_LIMIT
                +"&queryJson={'Location':'"+location+"'}";

        DataModel.requestGETMode("FloodPointBean", ApiUrl, new InitDataBeanBack<FloodPointBean>() {
            @Override
            public void callbak(boolean b, List<FloodPointBean> list , String msg) {
                if(b){
                    mAdapter.setNewData(list);
                    refreshlayout.finishRefresh(true);//刷新完成
                    refreshlayout.finishLoadmore(true);
                }else{
                    ToastShow(msg);
                    refreshlayout.finishRefresh(false);
                    refreshlayout.finishLoadmore(false);
                }
            }
        });
    }

    //退出程序
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            return super.onKeyDown(keyCode, event);
        }else{
            return  false;
        }
    }

}
