package com.haocang.bjxjc.KeepOrder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.haocang.bjxjc.R;
import com.haocang.bjxjc.activity.comm.CommFinalActivity;
import com.haocang.bjxjc.requestcenter.DataModel;
import com.haocang.bjxjc.ui.equipment.bean.EquTaskBean;
import com.haocang.bjxjc.utils.bean.ProvinceBean;
import com.haocang.bjxjc.utils.tools.CacheData;
import com.haocang.bjxjc.utils.tools.ApiConstant;
import com.haocang.bjxjc.utils.tools.OptionsItemsUtils;
import com.haocang.bjxjc.utils.tools.TextUtil;
import com.haocang.bjxjc.utils.tools.ToastUtil;
import com.haocang.bjxjc.utils.tools.UrlConst;
import com.haocang.commonlib.network.interfaces.InitDataBeanBack;
import com.haocang.commonlib.recyclerView.BaseAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import net.tsz.afinal.annotation.view.ViewInject;

import java.util.List;

/*
   保养工单列表
 */
public class KeepOrderListActivity extends CommFinalActivity {
    @ViewInject(id = R.id.recyclerview)
    RecyclerView recyclerview;
    @ViewInject(id = R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @ViewInject(id = R.id.tv_status)
    TextView tv_status;
    @ViewInject(id = R.id.tv_over_time)
    TextView tv_over_time;
    @ViewInject(id = R.id.tv_Result)
    TextView tv_trouble;
    @ViewInject(id = R.id.et_equname)
    EditText et_equname;
    @ViewInject(id = R.id.bt_quert)
    Button bt_quert;
    private KeepOrderListAdapter mAdapter;
    private Context context;
    private String IsOverdue="",Status="",Error="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        initView();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_keep_order_list;
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
        return "保养工单";
    }

    private void initView() {
        mAdapter = new KeepOrderListAdapter(context);
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
        //逾期
        tv_over_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseType(OptionsItemsUtils.getOverTimeItems(), tv_over_time,0);
            }
        });
        //状态
        tv_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseType(OptionsItemsUtils.getKeepStatusItems(), tv_status,1);
            }
        });
        //故障
        tv_trouble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseType(OptionsItemsUtils.getTroubleItems(), tv_trouble,2);
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
                CacheData.mEquTaskBean = mAdapter.getItem(position);
                Intent intent = new Intent(context, KeepOrderDetailActivity.class);
                startActivity(intent);
            }
        });
    }

    //获取数据
    private void initData() {
        String keyword = et_equname.getText().toString();

        //bbdate 日期筛选
      String url = ApiConstant.host + UrlConst.url_get_keep_order_list
                + "?limit=" + ApiConstant.PAGE_LIMIT+"&userId="+CacheData.UserID()
                + "&queryJson={'keyword':'" + keyword + "','IsOverdue':'"
                + IsOverdue + "','Status':'" + Status + "','Error':'"+Error+"'}";

        Log.d("保养数据", "保养数据:"+url);
        DataModel.requestGETMode("EquTaskBean", url, new
                InitDataBeanBack<EquTaskBean>() {
            @Override
            public void callbak(boolean isSuccess, List<EquTaskBean> list, String msg) {
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
    private void chooseType(final List<ProvinceBean> items, final TextView tv,final int type) {
        OptionsPickerView.Builder builder = new OptionsPickerView.Builder(context, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                ProvinceBean bean = items.get(options1);
                String desc = bean.getDescription();
                tv.setText(bean.getPickerViewText());
                switch (type) {
                    case 0:
                        IsOverdue=desc;
                        break;
                    case 1:
                        Status=desc;
                        break;
                    case 2:
                        Error=desc;
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
