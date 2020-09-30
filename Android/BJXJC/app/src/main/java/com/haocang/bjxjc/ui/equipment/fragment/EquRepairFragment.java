package com.haocang.bjxjc.ui.equipment.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.haocang.bjxjc.R;
import com.haocang.bjxjc.requestcenter.DataModel;
import com.haocang.bjxjc.ui.faultrepair.adapter.EquRepairListAdapter;
import com.haocang.bjxjc.ui.faultrepair.bean.EquRepairBean;
import com.haocang.bjxjc.ui.equipment.bean.EquipmentBean;
import com.haocang.bjxjc.utils.tools.ApiConstant;
import com.haocang.bjxjc.utils.tools.UrlConst;
import com.haocang.bjxjc.utils.tools.CacheData;
import com.haocang.commonlib.network.interfaces.InitDataBeanBack;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

/**
 * 创建时间：2019/7/19
 * 编 写 人：ShenC
 * 功能描述：
 */

public class EquRepairFragment extends Fragment {
    private View view;
    private Context context;


    private RefreshLayout refreshLayout;
    private RecyclerView recyclerview;
    private EquRepairListAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_equrepair, container, false);
        context = this.getActivity();

        initView();

        initEvent();

        refreshLayout.autoRefresh();

        return view;
    }

    private void initView() {
        refreshLayout = view.findViewById(R.id.refreshLayout);
        recyclerview = view.findViewById(R.id.recyclerview);

        mAdapter = new EquRepairListAdapter(context);
        recyclerview.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL) {
            @Override
            public boolean canScrollVertically() {
                return true;//是否允许滑动
            }
        });
        recyclerview.setAdapter(mAdapter);

    }

    private void initEvent() {



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
                //recyclerview.scrollToPosition(mEventAdapter.getItemCount()-1);
            }
        });

    }


    private void initData(final RefreshLayout refreshlayout) {

        EquipmentBean model = CacheData.mEquipmentBean;
        String ApiUrl = ApiConstant.host
                + UrlConst.url_get_epair_list
                + "?limit=" + ApiConstant.PAGE_LIMIT
                + "&queryJson={'keyValue':'"+ model.getEID()+"'}";

        DataModel.requestGETMode("EquRepairBean", ApiUrl, new InitDataBeanBack<EquRepairBean>() {
            @Override
            public void callbak(boolean b, List<EquRepairBean> list, String msg) {
                if (b) {
                    mAdapter.setNewData(list);
                    refreshlayout.finishRefresh(true);//刷新完成
                    refreshlayout.finishLoadmore(true);
                } else {
                    ToastShow(msg);
                    refreshlayout.finishRefresh(false);
                    refreshlayout.finishLoadmore(false);
                }
            }
        });
    }

    protected  void ToastShow(String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }


}
