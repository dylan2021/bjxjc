package com.haocang.bjxjc.ui.waterpoint.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.haocang.bjxjc.R;
import com.haocang.bjxjc.activity.comm.CommMapFinalActivity;
import com.haocang.bjxjc.activity.tool.CacheActivityUtil;
import com.haocang.bjxjc.requestcenter.DataModel;
import com.haocang.bjxjc.utils.bean.XyBean;
import com.haocang.bjxjc.utils.tools.KeyConst;
import com.haocang.bjxjc.utils.tools.TextUtil;
import com.haocang.commonlib.gismap.MapUtils;
import com.haocang.commonlib.network.interfaces.InitDataBeanBack;
import com.haocang.bjxjc.ui.waterpoint.adapter.WaterPointListAdapter;
import com.haocang.bjxjc.ui.waterpoint.bean.WaterPointBean;
import com.haocang.bjxjc.ui.waterpoint.utils.ComputeTools;
import com.haocang.bjxjc.utils.bean.ProvinceBean;
import com.haocang.bjxjc.utils.tools.ApiConstant;
import com.haocang.bjxjc.utils.tools.UrlConst;
import com.haocang.commonlib.network.interfaces.InitDataCallBack;
import com.haocang.commonlib.recyclerView.BaseAdapter;
import com.haocang.bjxjc.utils.tools.CacheData;
import com.haocang.bjxjc.utils.tools.MyTextUtils;
import com.haocang.bjxjc.utils.tools.OptionsItemsUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import net.tsz.afinal.annotation.view.ViewInject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * 积水检测
 */
public class WaterPointListActivity extends CommMapFinalActivity {
    @ViewInject(id = R.id.tv_map_addpoint)
    TextView tv_map_addpoint;
    @ViewInject(id = R.id.map_layout)
    RelativeLayout map_lin;
    @ViewInject(id = R.id.list)
    LinearLayout list;
    @ViewInject(id = R.id.et_year)
    EditText et_year;
    @ViewInject(id = R.id.tv_status_water)
    TextView tv_status;
    @ViewInject(id = R.id.tv_warm)
    TextView tv_reason;
    @ViewInject(id = R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @ViewInject(id = R.id.recyclerview)
    RecyclerView recyclerview;
    @ViewInject(id = R.id.lin_tj)
    LinearLayout lin_tj;
    @ViewInject(id = R.id.water_tj_a)
    TextView water_tj_a;
    @ViewInject(id = R.id.water_tj_b)
    TextView water_tj_b;
    @ViewInject(id = R.id.water_tj_c)
    TextView water_tj_c;
    @ViewInject(id = R.id.water_tj_d)
    TextView water_tj_d;
    @ViewInject(id = R.id.water_tj_e)
    TextView water_tj_e;
    private WaterPointListAdapter mAdapter;
    protected GraphicsLayer gWaterLayer;//自定义图层
    private Map<Integer, WaterPointBean> mLocationMapList;
    private String TYPE = "";
    private WaterPointListActivity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CacheActivityUtil.addActivity(WaterPointListActivity.this);
        context = this;
        initView();
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
        mapGraphicsLayer.removeAll();
        SingleTap_X = 0;
        SingleTap_Y = 0;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CacheActivityUtil.finishSingleActivity(WaterPointListActivity.this);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_water_point_list;
    }

    @Override
    protected boolean showNavigation() {
        return true;
    }

    @Override
    protected boolean showMedia() {
        return false;
    }

    @Override
    protected int showMoreIcon() {
        return R.drawable.ic_list;
    }

    @Override
    protected String showTitleName() {
        return "积水监测";
    }

    @Override
    protected boolean showMapPipe() {
        return false;
    }

    private void initView() {
        Intent it = getIntent();
        TYPE = it.getStringExtra(KeyConst.TYPE);

        //实时积水监测  or 历史积水库
        if (TYPE.equals("NOW")) {
            map_lin.setVisibility(View.VISIBLE);
            list.setVisibility(View.GONE);
            tv_name.setText("积水监测");
            et_year.setVisibility(View.GONE);
            tv_reason.setVisibility(View.GONE);
            iv_more_icon.setBackgroundResource(R.drawable.ic_list);
        } else if (TYPE.equals("OLD")) {
            lin_tj.setVisibility(View.GONE);
            map_lin.setVisibility(View.GONE);
            list.setVisibility(View.VISIBLE);
            tv_name.setText("历史积水库");
            tv_status.setVisibility(View.GONE);
            et_year.setVisibility(View.GONE);
            tv_reason.setVisibility(View.VISIBLE);
            iv_more_icon.setBackgroundResource(R.drawable.ic_map);
        }

        gWaterLayer = new GraphicsLayer();
        mMapView.addLayer(gWaterLayer);

        mLocationMapList = new HashMap<>();
        mAdapter = new WaterPointListAdapter(context);
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

        iv_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchMapAndLayout();
            }

        });

        mMapView.setOnSingleTapListener(new OnSingleTapListener() {
            @Override
            public void onSingleTap(float x, float y) {
                int[] ids = gWaterLayer.getGraphicIDs(x, y, 5);
                if (ids.length > 0) {
                    final WaterPointBean bean = mLocationMapList.get(ids[0]);
                    if (bean.getStatus() == null) bean.setStatus("0");
                    if (bean.getStatus().equals("0") && !bean.getIsDelete().equals("S")) {// && bean.getIsHandle() == 0){
                        Intent intent = new Intent(context, WaterPointSureActivity.class);
                        intent.putExtra("ID", bean.getID());
                        startActivity(intent);
                    } else {
                        CacheData.WaterPoint_UPDATE = true;
                        CacheData.mWaterPointID = bean.getID();
                        Intent it = new Intent(context, WaterPointDetailActivity.class);
                        startActivity(it);
                    }
                } else {
                    Point mapPoint = mMapView.toMapPoint(x, y);
                    if (mapPoint != null) {
                        SingleTap_X = mapPoint.getX();
                        SingleTap_Y = mapPoint.getY();
                    }
                    addpoint(mapPoint, R.mipmap.jsd_add);
                }
            }
        });

        tv_map_addpoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SingleTap_X == 0 || SingleTap_Y == 0) {
                    ToastShow("请在地图上设置积水位置");
                    return;
                }
                final String ApiUrl = ApiConstant.MAPCoordTransformUrl + "?inSR=%7B%22wkt%22%3A%22PROJCS%5B%5C%22BNAH%5C%22%2CGEOGCS%5B%5C%22GCS_WGS_1984%5C%22%2CDATUM%5B%5C%22D_WGS_1984%5C%22%2CSPHEROID%5B%5C%22WGS_1984%5C%22%2C6378137.0%2C298.257223563%5D%5D%2CPRIMEM%5B%5C%22Greenwich%5C%22%2C0.0%5D%2CUNIT%5B%5C%22Degree%5C%22%2C0.0174532925199433%5D%5D%2CPROJECTION%5B%5C%22Berghaus_Star%5C%22%5D%2CPARAMETER%5B%5C%22False_Easting%5C%22%2C500000.0%5D%2CPARAMETER%5B%5C%22False_Northing%5C%22%2C500000.0%5D%2CPARAMETER%5B%5C%22Central_Meridian%5C%22%2C116.385%5D%2CPARAMETER%5B%5C%22Latitude_Of_Origin%5C%22%2C39.461%5D%2CPARAMETER%5B%5C%22XY_Plane_Rotation%5C%22%2C-7.0%5D%2CUNIT%5B%5C%22Meter%5C%22%2C1.0%5D%5D%22%7D&outSR=4326&geometries=%7B%22geometryType%22%3A%22esriGeometryPoint%22%2C%22geometries%22%3A%5B%7B%22x%22%3A" + SingleTap_X + "%2C%22y%22%3A" + SingleTap_Y + "%2C%22spatialReference%22%3A%7B%22wkt%22%3A%22PROJCS%5B%5C%22BNAH%5C%22%2CGEOGCS%5B%5C%22GCS_WGS_1984%5C%22%2CDATUM%5B%5C%22D_WGS_1984%5C%22%2CSPHEROID%5B%5C%22WGS_1984%5C%22%2C6378137.0%2C298.257223563%5D%5D%2CPRIMEM%5B%5C%22Greenwich%5C%22%2C0.0%5D%2CUNIT%5B%5C%22Degree%5C%22%2C0.0174532925199433%5D%5D%2CPROJECTION%5B%5C%22Berghaus_Star%5C%22%5D%2CPARAMETER%5B%5C%22False_Easting%5C%22%2C500000.0%5D%2CPARAMETER%5B%5C%22False_Northing%5C%22%2C500000.0%5D%2CPARAMETER%5B%5C%22Central_Meridian%5C%22%2C116.385%5D%2CPARAMETER%5B%5C%22Latitude_Of_Origin%5C%22%2C39.461%5D%2CPARAMETER%5B%5C%22XY_Plane_Rotation%5C%22%2C-7.0%5D%2CUNIT%5B%5C%22Meter%5C%22%2C1.0%5D%5D%22%7D%7D%5D%7D&transformation=&transformForward=true&vertical=false&f=json";
                DataModel.requestGET(context, ApiUrl, new InitDataCallBack() {
                    @Override
                    public void callbak(boolean b, String string) {
                        if (b) {
                            try {
                                Gson gson = new Gson();
                                JSONObject jsonobject = new JSONObject(string);
                                List<XyBean> datalist = gson.fromJson(jsonobject.getString("geometries"), new TypeToken<List<XyBean>>() {
                                }.getType());
                                if (datalist.size() > 0) {
                                    //跳转到积水明细上报界面
                                    CacheData.WaterPoint_UPDATE = false;
                                    CacheData.mWaterPointID = "";
                                    Intent intent = new Intent(context, WaterPointAddActivity.class);
                                    intent.putExtra("X", datalist.get(0).getX());
                                    intent.putExtra("Y", datalist.get(0).getY());
                                    intent.putExtra("MX", SingleTap_X);
                                    intent.putExtra("MY", SingleTap_Y);
                                    startActivity(intent);
                                } else {
                                    ToastShow("转换为wgs84坐标失败");
                                }
                            } catch (Exception e) {
                                ToastShow("转换为wgs84坐标失败:" + e.toString());
                            }
                        } else {
                            ToastShow("转换为wgs84坐标失败:" + string);
                        }
                    }
                });
            }
        });

        //处理状态
        tv_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TYPE.equals("NOW")) {
                    ArrayList<ProvinceBean> items = OptionsItemsUtils.GetAllWaterPointStatus();
                    switchStatus(items, 1);   //实时积水
                } else if (TYPE.equals("OLD")) {
                    switchStatus(OptionsItemsUtils.GetAllOldWaterPointStatus(), 1);
                }
            }

        });

        tv_reason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchStatus(OptionsItemsUtils.GetOldAllReasonList(), 2);
            }

        });

        mAdapter.setOnItemChildClickListener(new BaseAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseAdapter adapter, View v, int position) {
                WaterPointBean bean = mAdapter.getItem(position);
                if (bean.getStatus().equals("0") && !bean.getIsDelete().equals("S")) {// && bean.getIsHandle() == 0){
                    Intent intent = new Intent(context, WaterPointSureActivity.class);
                    intent.putExtra("ID", bean.getID());
                    startActivity(intent);
                } else {
                    CacheData.WaterPoint_UPDATE = true;
                    CacheData.mWaterPointID = bean.getID();
                    Intent it = new Intent(context, WaterPointDetailActivity.class);
                    startActivity(it);
                }
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

    //地图和数据列表 切换
    private void switchMapAndLayout() {
        if (list.getVisibility() == View.GONE) {
            list.setVisibility(View.VISIBLE);
            map_lin.setVisibility(View.GONE);
            list.setAnimation(AnimationUtils.makeInAnimation(mContext, true));
            map_lin.setAnimation(AnimationUtils.makeOutAnimation(mContext, true));
            iv_more_icon.setBackgroundResource(R.drawable.ic_map);
        } else {
            list.setVisibility(View.GONE);
            map_lin.setVisibility(View.VISIBLE);
            list.setAnimation(AnimationUtils.makeOutAnimation(mContext, false));
            map_lin.setAnimation(AnimationUtils.makeInAnimation(mContext, false));
            iv_more_icon.setBackgroundResource(R.drawable.ic_list);
        }
    }

    //实时积水监测
    private void getData() {
        if (TYPE.equals("OLD")) {
            initDataHistory();//历史积水库
            return;
        }
        String stutasStr = tv_status.getText().toString();
        String status = MyTextUtils.GetOpitemValue(OptionsItemsUtils.GetAllWaterPointStatus(),
                0, stutasStr);
        String ApiUrl = ApiConstant.host
                + UrlConst.Api_WaterPoint
                + "?limit=" + ApiConstant.PAGE_LIMIT
                + "&status=" + status
                + "&WarningID=" + CacheData.Cache_WaringID
                + "&UserID=" + CacheData.UserID();
        DataModel.requestGETMode("WaterPointBean", ApiUrl, new InitDataBeanBack<WaterPointBean>() {
            @Override
            public void callbak(boolean b, List<WaterPointBean> list, String msg) {
                if (context == null) {
                    return;
                }
                mAdapter.setNewData(list);
                markWaterPoint(list);
                if (b) {
                    if (TextUtil.isEmptyList(list)) {
                        ToastShow(R.string.no_data);
                    }
                    String[] nubber_arr = ComputeTools.StatisticsWaterNumber(list);
                    water_tj_a.setText(nubber_arr[0]);
                    water_tj_b.setText(nubber_arr[1]);
                    water_tj_c.setText(nubber_arr[2]);
                    water_tj_d.setText(nubber_arr[3]);
                    water_tj_e.setText(nubber_arr[4]);

                } else {
                    ToastShow(R.string.request_failure);
                }
                refreshLayout.finishRefresh(0);//刷新完成
                refreshLayout.finishLoadmore(0);
            }
        });

    }

    //历史积水库
    private void initDataHistory() {
        String year = et_year.getText().toString();
        String status = MyTextUtils.GetOpitemValue(OptionsItemsUtils.GetAllWaterPointStatus(), 0, tv_status.getText().toString());
        String reason = MyTextUtils.GetOpitemValue(OptionsItemsUtils.GetAllReasonList(), 0, tv_reason.getText().toString());

        String ApiUrl = ApiConstant.host
                + UrlConst.url_his_water_point
                + "?limit=" + ApiConstant.PAGE_LIMIT
                + "&year=" + year
                + "&status=2"
                + "&WarningID=" + reason
                + "&UserID=" + CacheData.UserID();

        DataModel.requestGETMode("WaterPointBean", ApiUrl, new InitDataBeanBack<WaterPointBean>() {
            @Override
            public void callbak(boolean isSuccess, List<WaterPointBean> list, String msg) {
                if (isSuccess) {
                    if (TextUtil.isEmptyList(list)) {
                        ToastShow(R.string.no_data);
                    }
                    mAdapter.setNewData(list);
                    markWaterPoint(list);
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

    //标记获取的积水点位置
    private void markWaterPoint(final List<WaterPointBean> list) {
        gWaterLayer.removeAll();
        mLocationMapList.clear();
        if (TextUtil.isEmptyList(list)) {
            return;
        }
        for (WaterPointBean bean : list) {
            addWaterPoint(bean);
        }
    }

    //添加点
    private void addWaterPoint(final WaterPointBean model) {
        Point mapPoint = new Point(model.getMX(), model.getMY());
        int icon = 0;
        if (model.getIsDelete().equals("S")) {
            switch (model.getStatus()) {
                case "0":
                    icon = R.mipmap.jsd_r;
                    break;
                case "1":
                    icon = R.mipmap.jsd_y;
                    break;
                case "2":
                    icon = R.mipmap.jsd_green;
                    break;
            }
        } else if (model.getIsDelete().equals("N")) {
            icon = R.mipmap.jsd_l;
        } else if (model.getIsDelete().equals("Y")) {
            icon = R.mipmap.jsd_gray;
        } else {
            icon = R.mipmap.jsd_gray;
        }

        PictureMarkerSymbol locationSymbol = new PictureMarkerSymbol(mContext.getResources().getDrawable(icon));
        Graphic graphic = new Graphic(mapPoint, locationSymbol);


        if (isShowLable) {
            PictureMarkerSymbol markerSymbol = new PictureMarkerSymbol(MapUtils.createMapBitMap(context, model.getLocation()));
            Graphic graphictext = new Graphic(mapPoint, markerSymbol);
            markerSymbol.setOffsetY(-23);
            // Graphic graphic_start_p0 = new Graphic(startPoint,new SimpleMarkerSymbol(start_color,19, SimpleMarkerSymbol.STYLE.CIRCLE));
            mLocationMapList.put(gWaterLayer.addGraphic(graphictext), model);
        }
        mLocationMapList.put(gWaterLayer.addGraphic(graphic), model);
    }

    private void switchStatus(final List<ProvinceBean> items, final int pos) {
        hideKeyBoard();
        OptionsPickerView.Builder builder = new OptionsPickerView.Builder(context,
                new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String itemName = items.get(options1).getPickerViewText();
                switch (pos) {
                    case 1:
                        tv_status.setText(itemName);
                        break;
                    case 2:
                        tv_reason.setText(itemName);
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
