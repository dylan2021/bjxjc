package com.haocang.bjxjc.ui.equipment.activity;

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
import com.haocang.bjxjc.ui.equipment.adapter.EquipmentListAdapter;
import com.haocang.bjxjc.ui.equipment.bean.EquipmentBean;
import com.haocang.bjxjc.utils.bean.ProvinceBean;
import com.haocang.bjxjc.utils.bean.ShareIdNameBean;
import com.haocang.bjxjc.utils.tools.ApiConstant;
import com.haocang.bjxjc.utils.tools.UrlConst;
import com.haocang.bjxjc.utils.tools.CacheData;
import com.haocang.bjxjc.utils.tools.MyTextUtils;
import com.haocang.commonlib.network.interfaces.InitDataBeanBack;
import com.haocang.commonlib.recyclerView.BaseAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import net.tsz.afinal.annotation.view.ViewInject;

import java.util.ArrayList;
import java.util.List;

//设备档案列表
public class EquipmentArchivesListActivity extends CommFinalActivity {
    @ViewInject(id = R.id.recyclerview)
    RecyclerView recyclerview;
    @ViewInject(id = R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @ViewInject(id = R.id.tv_area)
    TextView tv_area;
    @ViewInject(id = R.id.tv_type)
    TextView tv_type;
    @ViewInject(id = R.id.et_location)
    EditText et_location;
    @ViewInject(id = R.id.bt_quert)
    Button bt_quert;
    private EquipmentListAdapter mAdapter;
    private Context context;
    private ArrayList<ProvinceBean> type_OptionsItems = new ArrayList<>();
    private ArrayList<ProvinceBean> area_OptionsItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        initView();
        initDialogData();
        refreshLayout.autoRefresh();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (refreshLayout != null) {
            refreshLayout.autoRefresh();
        }
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_equipment_list;
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
        return "设备档案";
    }

    private void initView() {
        mAdapter = new EquipmentListAdapter(context);
        recyclerview.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL) {
            @Override
            public boolean canScrollVertically() {
                return true;
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

        tv_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseType(area_OptionsItems, 1);
            }
        });

        tv_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseType(type_OptionsItems, 2);
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
                CacheData.mEquipmentBean = mAdapter.getItem(position);
                Intent intent = new Intent(context, EquipmentDeatilActivity.class);
                startActivity(intent);
            }

        });
    }

    //请求列表数据
    private void initData() {
        String area = MyTextUtils.GetOpitemValue(area_OptionsItems, 0, tv_area.getText().toString());
        String location = et_location.getText().toString();
        String type = MyTextUtils.GetOpitemValue(type_OptionsItems, 0, tv_type.getText().toString());
        String ApiUrl = ApiConstant.host + UrlConst.url_equipment_list
                + "?limit=" + ApiConstant.PAGE_LIMIT
                + "&PumpRoomID=" + area
                + "&EquTypeID=" + type
                + "&keyword=" + location;
        DataModel.requestGETMode("EquipmentBean", ApiUrl, new InitDataBeanBack<EquipmentBean>() {
            @Override
            public void callbak(boolean isSuccess, List<EquipmentBean> list, String msg) {
                if (isSuccess) {
                    mAdapter.setNewData(list);
                } else {
                    ToastShow(R.string.request_failure);
                }
                refreshLayout.finishRefresh(0);
                refreshLayout.finishLoadmore(0);
            }
        });
    }

    //获取下拉筛选列表数据
    private void initDialogData() {
        //区域列表数据
        String ApiUrl1 = ApiConstant.host + UrlConst.url_equipmentArea;
        DataModel.requestGETMode("ShareIdNameBean", ApiUrl1, new InitDataBeanBack<ShareIdNameBean>() {
            @Override
            public void callbak(boolean isSuccess, List<ShareIdNameBean> list, String msg) {
                if (isSuccess) {
                    area_OptionsItems.add(new ProvinceBean(0, "全部", "", ""));
                    for (int i = 0; i < list.size(); i++) {
                        area_OptionsItems.add(new ProvinceBean(i + 1, list.get(i).getName(), list.get(i).getID(), ""));
                    }
                } else {
                    ToastShow(msg);
                }
            }
        });
        //设备类型列表数据
        String ApiUrl = ApiConstant.host + UrlConst.url_equipmentType;
        DataModel.requestGETMode("ShareIdNameBean", ApiUrl, new InitDataBeanBack<ShareIdNameBean>() {
            @Override
            public void callbak(boolean isSuccess, List<ShareIdNameBean> list, String msg) {
                if (isSuccess) {
                    type_OptionsItems.add(new ProvinceBean(0, "全部", "", ""));
                    for (int i = 0; i < list.size(); i++) {
                        type_OptionsItems.add(new ProvinceBean(i + 1, list.get(i).getName(), list.get(i).getID(), ""));
                    }
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

    private void chooseType(final List<ProvinceBean> items, final int pos) {
        hideKeyBoard();
        OptionsPickerView.Builder builder = new OptionsPickerView.Builder(mContext, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String itemName = items.get(options1).getPickerViewText();
                switch (pos) {
                    case 1:
                        tv_area.setText(itemName);
                        break;
                    case 2:
                        tv_type.setText(itemName);
                        break;
                }
                refreshLayout.autoRefresh();
            }
        });
        OptionsPickerView<ProvinceBean> pickerView = new OptionsPickerView<>(builder);
        pickerView.setPicker(items);
        pickerView.show();
    }

}
