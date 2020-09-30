package com.haocang.bjxjc.ui.floodpoint.activity;

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
import com.bigkoo.pickerview.TimePickerView;
import com.haocang.bjxjc.R;
import com.haocang.bjxjc.activity.comm.CommFinalActivity;
import com.haocang.bjxjc.requestcenter.DataModel;
import com.haocang.commonlib.network.interfaces.InitDataBeanBack;
import com.haocang.bjxjc.ui.floodpoint.adapter.FloodPointListAdapter;
import com.haocang.bjxjc.ui.floodpoint.bean.FloodPointBean;
import com.haocang.bjxjc.utils.bean.ProvinceBean;
import com.haocang.bjxjc.utils.tools.ApiConstant;
import com.haocang.bjxjc.utils.tools.UrlConst;
import com.haocang.commonlib.recyclerView.BaseAdapter;
import com.haocang.bjxjc.utils.tools.CacheData;
import com.haocang.bjxjc.utils.tools.MyTextUtils;
import com.haocang.bjxjc.utils.tools.OptionsItemsUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import net.tsz.afinal.annotation.view.ViewInject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/*
  易淹点列表
 */
public class FloodPointListActivity extends CommFinalActivity {
    @ViewInject(id = R.id.recyclerview)
    RecyclerView recyclerview;
    @ViewInject(id = R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @ViewInject(id = R.id.tv_year)
    TextView tv_year;
    @ViewInject(id = R.id.tv_type)
    TextView tv_type;
    @ViewInject(id = R.id.et_location)
    EditText et_location;
    @ViewInject(id = R.id.bt_quert)
    Button bt_quert;
    private FloodPointListAdapter mAdapter;
    private FloodPointListActivity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = FloodPointListActivity.this;
        initView();
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_flood_point;
    }

    @Override
    protected boolean showNavigation() {
        return true;
    }

    @Override
    protected int showMoreIcon() {
        return R.drawable.add_selector_01;
    }

    @Override
    protected String showTitleName() {
        return "易淹点列表";
    }

    private void initView() {
        mAdapter = new FloodPointListAdapter(context);
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
        tv_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseTime();
            }

        });
        tv_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //选择类型
                chooseType(OptionsItemsUtils.GetALLFloodPointType(), 1);
            }
        });
        iv_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FloodPointAddDeatilActivity.class);//新增
                CacheData.FloodPoint_UPDATE = false;
                startActivity(intent);
            }
        });

        mAdapter.setOnItemChildClickListener(new BaseAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseAdapter adapter, View v, int position) {
                FloodPointBean model = mAdapter.getItem(position);
                Intent intent = new Intent(context, FloodPointAddDeatilActivity.class);//详情
                CacheData.mFloodPointBean = model;
                CacheData.FloodPoint_UPDATE = true;
                startActivity(intent);
            }

        });
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                ApiConstant.PAGE_LIMIT = ApiConstant.PAGE_SHOW_LIMIT;
                getData();
            }
        });
        refreshLayout.autoRefresh();
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                ApiConstant.PAGE_LIMIT += ApiConstant.PAGE_SHOW_LIMIT;
                getData();
            }
        });
    }

    //请求数据
    private void getData() {
        String year = tv_year.getText().toString();
        String location = et_location.getText().toString();
        String type = MyTextUtils.GetOpitemValue(OptionsItemsUtils.GetALLFloodPointType(), 0, tv_type.getText().toString());

        String ApiUrl = ApiConstant.host
                + UrlConst.Api_FloodPoint
                + "?limit=" + ApiConstant.PAGE_LIMIT
                + "&queryJson={'Type':'" + type + "','Year':'" + year + "','Location':'" + location + "'}";
        DataModel.requestGETMode("FloodPointBean", ApiUrl, new InitDataBeanBack<FloodPointBean>() {
            @Override
            public void callbak(boolean b, List<FloodPointBean> list, String msg) {
                if (b) {
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

    //选择年份
    private void chooseTime() {
        hideKeyBoard();
        TimePickerView.Builder builder = new TimePickerView.Builder(FloodPointListActivity.this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy", Locale.getDefault());
                String time = MyTextUtils.date2String(date, simpleDateFormat);
                tv_year.setText(time);
                getData();
            }
        });
        builder.setType(new boolean[]{true, false, false, false, false, false});
        TimePickerView pvTime = builder.build();
        pvTime.setDate(Calendar.getInstance());
        pvTime.show();
    }

    private void chooseType(final List<ProvinceBean> items, final int pos) {
        hideKeyBoard();
        OptionsPickerView.Builder builder = new OptionsPickerView.Builder(mContext, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String itemName = items.get(options1).getPickerViewText();
                switch (pos) {
                    case 1:
                        tv_type.setText(itemName);
                        break;
                }
                getData();
            }
        });
        OptionsPickerView<ProvinceBean> pickerView = new OptionsPickerView<>(builder);
        pickerView.setPicker(items);
        pickerView.show();
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
