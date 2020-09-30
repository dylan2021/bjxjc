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
import com.haocang.bjxjc.ui.equipment.adapter.EquTaskListAdapter;
import com.haocang.bjxjc.ui.equipment.bean.EquTaskBean;
import com.haocang.bjxjc.ui.equipment.bean.EquipmentBean;
import com.haocang.bjxjc.utils.tools.ApiConstant;
import com.haocang.bjxjc.utils.tools.TextUtil;
import com.haocang.bjxjc.utils.tools.ToastUtil;
import com.haocang.bjxjc.utils.tools.UrlConst;
import com.haocang.bjxjc.utils.tools.CacheData;
import com.haocang.commonlib.network.interfaces.InitDataBeanBack;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

/**
 * 设备详情--保养信息
 */

public class EquMaintainFragment extends Fragment {
    private View view;
    private Context context;
    private RefreshLayout refreshLayout;
    private RecyclerView recyclerview;
    private EquTaskListAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_equmaintain, container, false);
        context = this.getActivity();
        initView();
        initEvent();
        refreshLayout.autoRefresh();
        return view;
    }

    private void initView() {
        refreshLayout = view.findViewById(R.id.refreshLayout);
        recyclerview = view.findViewById(R.id.recyclerview);
        mAdapter = new EquTaskListAdapter(context);
        recyclerview.setLayoutManager(new StaggeredGridLayoutManager(1,
                StaggeredGridLayoutManager.VERTICAL) {
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
                initData();
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore(20000/*,false*/);//传入false表示加载失败
                ApiConstant.PAGE_LIMIT += ApiConstant.PAGE_SHOW_LIMIT;
                initData();
                //recyclerview.scrollToPosition(mEventAdapter.getItemCount()-1);
            }
        });
    }

    private void initData() {
        EquipmentBean model = CacheData.mEquipmentBean;
        String ApiUrl = ApiConstant.host
                + UrlConst.Api_GetTaskList
                + "?limit=" + ApiConstant.PAGE_LIMIT
                + "&ID=" + model.getEID();
        DataModel.requestGETMode("EquTaskBean", ApiUrl, new InitDataBeanBack<EquTaskBean>() {
            @Override
            public void callbak(boolean b, List<EquTaskBean> list, String msg) {
                if (b) {
                    if (TextUtil.isEmptyList(list)) {
                        ToastUtil.show(R.string.no_data);
                        return;
                    }
                    mAdapter.setNewData(list);
                } else {
                    ToastUtil.show(R.string.request_failure);
                }
                if (refreshLayout != null) {
                    refreshLayout.finishRefresh(0);
                    refreshLayout.finishLoadmore(0);
                }
            }
        });
    }

    protected void ToastShow(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }


}
