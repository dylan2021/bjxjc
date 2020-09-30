package com.haocang.bjxjc.ui.faultrepair.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.haocang.bjxjc.R;
import com.haocang.bjxjc.activity.comm.CommFinalActivity;
import com.haocang.bjxjc.requestcenter.DataModel;
import com.haocang.bjxjc.ui.faultrepair.adapter.EquRepairListAdapter;
import com.haocang.bjxjc.ui.faultrepair.bean.EquRepairBean;
import com.haocang.bjxjc.utils.bean.ProvinceBean;
import com.haocang.bjxjc.utils.tools.ApiConstant;
import com.haocang.bjxjc.utils.tools.TextUtil;
import com.haocang.bjxjc.utils.tools.ToastUtil;
import com.haocang.bjxjc.utils.tools.UrlConst;
import com.haocang.bjxjc.utils.tools.CacheData;
import com.haocang.bjxjc.utils.tools.MyTextUtils;
import com.haocang.bjxjc.utils.tools.OptionsItemsUtils;
import com.haocang.commonlib.network.interfaces.InitDataBeanBack;
import com.haocang.commonlib.recyclerView.BaseAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import net.tsz.afinal.annotation.view.ViewInject;

import java.util.List;

/*
   设备维修工单列表
 */
public class FaultRepairListActivity extends CommFinalActivity {
    @ViewInject(id = R.id.recyclerview)
    RecyclerView recyclerview;
    @ViewInject(id = R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @ViewInject(id = R.id.tv_status)
    TextView tv_status;
    @ViewInject(id = R.id.tv_Result)
    TextView tv_Result;
    @ViewInject(id = R.id.et_equname)
    EditText et_equname;
    @ViewInject(id = R.id.bt_quert)
    Button bt_quert;

    private EquRepairListAdapter mAdapter;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        initView();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_fault_repair_list;
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
        return "维修工单";
    }

    private void initView() {
        mAdapter = new EquRepairListAdapter(context);
        recyclerview.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL) {
            @Override
            public boolean canScrollVertically() {
                return true;//是否允许滑动
            }
        });
        recyclerview.setAdapter(mAdapter);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        bt_quert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshLayout.autoRefresh();
            }
        });

        tv_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseType(OptionsItemsUtils.GetDataItemAllList("RepairState"), 1);
            }
        });


        tv_Result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseType(OptionsItemsUtils.GetDataItemAllList("RepairResult"), 2);
            }
        });

        iv_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                ApiConstant.PAGE_LIMIT = ApiConstant.PAGE_SHOW_LIMIT;
                refreshlayout.finishRefresh(20000/*,false*/);//传入false表示刷新失败
                initData();
            }
        });
        refreshLayout.autoRefresh();
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore(20000/*,false*/);//传入false表示加载失败
                ApiConstant.PAGE_LIMIT += ApiConstant.PAGE_SHOW_LIMIT;
                initData();
                //recyclerview.scrollToPosition(mEventAdapter.getItemCount()-1);
            }
        });

        mAdapter.setOnItemChildClickListener(new BaseAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseAdapter adapter, View v, int position) {
                CacheData.mEquRepairBean = mAdapter.getItem(position);
                Intent intent = new Intent(context, FaultRepairDetailActivity.class);
                startActivity(intent);
            }

        });
    }

    //获取数据
    private void initData( ) {
        String ls_status = MyTextUtils.GetOpitemValue(OptionsItemsUtils.GetDataItemAllList("RepairState"), 0, tv_status.getText().toString());
        String ls_equname = et_equname.getText().toString();
        String ls_Result = MyTextUtils.GetOpitemValue(OptionsItemsUtils.GetDataItemAllList("RepairResult"), 0, tv_Result.getText().toString());
        String ApiUrl = ApiConstant.host + UrlConst.url_get_epair_list
                + "?limit=" + ApiConstant.PAGE_LIMIT
                + "&queryJson={'EquName':'" + ls_equname + "','status':'"
                + ls_status + "','result':'" + ls_Result + "','userid':'" + CacheData.UserID() + "'}";

        DataModel.requestGETMode("EquRepairBean", ApiUrl, new InitDataBeanBack<EquRepairBean>() {
            @Override
            public void callbak(boolean isSuccess, List<EquRepairBean> list, String msg) {
                if (isSuccess) {
                    if (TextUtil.isEmptyList(list)) {
                        ToastUtil.show(R.string.no_data);
                    }
                    mAdapter.setNewData(list);
                } else {
                    ToastShow(R.string.request_failure);
                }
                if (refreshLayout != null) {
                    refreshLayout.finishRefresh(0);
                    refreshLayout.finishLoadmore(0);
                }
            }
        });
    }
    //筛选
    private void chooseType(final List<ProvinceBean> items, final int pos) {
        OptionsPickerView.Builder builder = new OptionsPickerView.Builder(context, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String itemName = items.get(options1).getPickerViewText();
                switch (pos) {
                    case 1:
                        tv_status.setText(itemName);
                        break;
                    case 2:
                        tv_Result.setText(itemName);
                        break;
                }
                refreshLayout.autoRefresh();
            }
        });
        OptionsPickerView<ProvinceBean> pickerView = new OptionsPickerView<>(builder);
        pickerView.setPicker(items);
        pickerView.show();
        hideKeyBoard();
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
