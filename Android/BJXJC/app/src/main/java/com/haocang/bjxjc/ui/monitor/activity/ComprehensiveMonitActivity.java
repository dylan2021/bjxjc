package com.haocang.bjxjc.ui.monitor.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.esri.android.map.Callout;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.haocang.bjxjc.R;
import com.haocang.bjxjc.activity.comm.CommMapFinalActivity;
import com.haocang.bjxjc.requestcenter.DataModel;
import com.haocang.bjxjc.ui.analysis.activity.DataAnalysisActivity;
import com.haocang.bjxjc.ui.monitor.adapter.MapDataAdapter;
import com.haocang.bjxjc.ui.monitor.adapter.MapLegendAdapter;
import com.haocang.bjxjc.ui.monitor.bean.MapDataBean;
import com.haocang.bjxjc.ui.monitor.bean.MapLegendBean;
import com.haocang.bjxjc.ui.monitor.bean.PumpBean;
import com.haocang.bjxjc.ui.monitor.bean.RealDataBean;
import com.haocang.bjxjc.ui.monitor.bean.ShowDataBean;
import com.haocang.bjxjc.utils.tools.ApiConstant;
import com.haocang.bjxjc.utils.tools.Const;
import com.haocang.bjxjc.utils.tools.DialogHelper;
import com.haocang.bjxjc.utils.tools.TimeUtils;
import com.haocang.bjxjc.utils.tools.ToastUtil;
import com.haocang.bjxjc.utils.tools.UrlConst;
import com.haocang.bjxjc.utils.tools.MyTextUtils;
import com.haocang.bjxjc.utils.tools.TextUtil;
import com.haocang.commonlib.gismap.MapUtils;
import com.haocang.commonlib.loadingdialog.LoadingDialog;
import com.haocang.commonlib.network.interfaces.InitDataBeanBack;
import com.haocang.commonlib.recyclerView.BaseAdapter;
import com.yzq.zxinglibrary.common.Constant;

import net.tsz.afinal.annotation.view.ViewInject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * 综合监测
 */
public class ComprehensiveMonitActivity extends CommMapFinalActivity {
    private ComprehensiveMonitActivity context;
    private List<MapDataBean> infoList = new ArrayList<>();
    @ViewInject(id = R.id.tv_map_lable)
    LinearLayout tv_map_lable;
    @ViewInject(id = R.id.recyclerview)
    RecyclerView recyclerView;
    @ViewInject(id = R.id.tv_map_legend)
    LinearLayout tv_map_legend;
    @ViewInject(id = R.id.lin_legend)
    LinearLayout lin_legend;
    @ViewInject(id = R.id.tv_close)
    TextView tv_close;
    private MapDataAdapter mapDataAdapter;
    private List<MapLegendBean> legendList = new ArrayList<>();
    private MapLegendAdapter legendAdapter;
    private Map<Integer, MapDataBean> locDataList = new HashMap<>();
    private int[] numArr = {0, 0, 0, 0, 0, 0, 0};
    private String typeCheckedStr = "A,L,S,G,V,E_,F_" + TimeUtils.getYear();
    private LoadingDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        initView();
        initTypeData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCheckedTypeData();//默认获取所有类型数据
    }

    private void initTypeData() {
        for (int i = 0; i < numArr.length; i++) {
            legendList.add(new MapLegendBean(Const.legendIConId[i], Const.legendNameArr[i],
                    0, true, Const.legendTypeArr[i]));
        }
        legendAdapter.setNewData(legendList);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_comprehensive;
    }

    @Override
    protected String showTitleName() {
        return "综合监测";
    }

    private void initView() {
        //图例列表
        legendAdapter = new MapLegendAdapter(context);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL) {
            //是否允许滑动
            @Override
            public boolean canScrollVertically() {
                return true;
            }
        });
        recyclerView.setAdapter(legendAdapter);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        //详情按钮
        tv_map_lable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isShowLable = !isShowLable;
                //地图上显示数据
                addMonitorPoints();
            }
        });

        tv_map_legend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lin_legend.getVisibility() == View.VISIBLE) {
                    lin_legend.setVisibility(View.GONE);
                } else {
                    lin_legend.setVisibility(View.VISIBLE);
                }
            }
        });

        tv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lin_legend.setVisibility(View.GONE);
            }
        });

        //图例
        legendAdapter.setOnItemChildClickListener(new BaseAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseAdapter adapter, View v, int position) {
                MapLegendBean model = legendAdapter.getItem(position);
                List<MapLegendBean> legendList = new ArrayList<>();
                typeCheckedStr = "";
                for (MapLegendBean info : ComprehensiveMonitActivity.this.legendList) {
                    if (info.getIcon() == model.getIcon()) {
                        info.setChecked(!info.getChecked());
                    }
                    legendList.add(info);

                    //选中
                    if (info.getChecked()) {
                        String type = info.getType();
                        if ("E".equals(type)) {
                            typeCheckedStr = typeCheckedStr + "," + type + "_";
                        } else if ("F".equals(type)) {
                            typeCheckedStr = typeCheckedStr + "," + type + "_" + TimeUtils.getYear();
                        } else {
                            typeCheckedStr = typeCheckedStr + "," + type;
                        }
                    }
                }
                ComprehensiveMonitActivity.this.legendList = legendList;
                legendAdapter.setNewData(ComprehensiveMonitActivity.this.legendList);

                getCheckedTypeData(); //请求选中的条目数据
            }
        });

        //地图单击事情
        mMapView.setOnSingleTapListener(new OnSingleTapListener() {
            @Override
            public void onSingleTap(float x, float y) {
                int[] ids = mapGraphicsLayer.getGraphicIDs(x, y, 5);
                if (ids.length > 0) {
                    final MapDataBean m_model = locDataList.get(ids[0]);

                    if (m_model != null) {
                        //初始化显示气泡
                        final Callout mCallout = mMapView.getCallout();
                        LayoutInflater inflater = LayoutInflater.from(context);
                        View view = inflater.inflate(R.layout.callout_screen_info, null);
                        TextView tv_title = view.findViewById(R.id.tv_name);
                        LinearLayout iv_close = view.findViewById(R.id.iv_close);
                        LinearLayout data_lin = view.findViewById(R.id.data_lin);
                        RecyclerView rv_recyclerview = view.findViewById(R.id.rv_recyclerview);//数据列表
                        LinearLayout lin_pump = view.findViewById(R.id.lin_pump);//泵站信息
                        LinearLayout lin_hedao = view.findViewById(R.id.lin_hedao);//河道信息
                        mapDataAdapter = new MapDataAdapter(context);
                        rv_recyclerview.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL) {
                            @Override
                            public boolean canScrollVertically() {
                                return true;//是否允许滑动
                            }
                        });
                        rv_recyclerview.setAdapter(mapDataAdapter);

                        if (m_model.getF_Category().equals("A")) {//泵站
                            lin_pump.setVisibility(View.VISIBLE);
                            lin_hedao.setVisibility(View.GONE);

                            try {
                                JSONObject pjson = new JSONObject(m_model.getJsonData());
                                TextView pump_Fact_SuperVisor = view.findViewById(R.id.pump_Fact_SuperVisor);
                                TextView pump_Fact_Phone = view.findViewById(R.id.pump_Fact_Phone);
                                TextView pump_SumpArea = view.findViewById(R.id.pump_SumpArea);
                                TextView pump_RainPoolArea = view.findViewById(R.id.pump_RainPoolArea);

                                pump_Fact_SuperVisor.setText(MyTextUtils.GetStringByValue(pjson.getString("Fact_SuperVisor")));
                                pump_Fact_Phone.setText(MyTextUtils.GetStringByValue(pjson.getString("Fact_Phone")));
                                pump_SumpArea.setText(MyTextUtils.GetStringByValue(pjson.getString("SumpArea")) + "m²");
                                pump_RainPoolArea.setText(MyTextUtils.GetStringByValue(pjson.getString("RainPoolArea")) + "m²");

                            } catch (JSONException e) {
                                ToastShow("泵站数据解析错误！" + e.toString());
                            }
                        } else if (m_model.getF_Category().equals("W")) {//河道
                            lin_hedao.setVisibility(View.VISIBLE);
                            lin_pump.setVisibility(View.GONE);
                            WebView wv_webview = view.findViewById(R.id.webview);//河道ht图
                            wv_webview.getSettings().setJavaScriptEnabled(true);
                            // 设置可以支持缩放
                            wv_webview.getSettings().setSupportZoom(true);
                            // 设置出现缩放工具
                            wv_webview.getSettings().setBuiltInZoomControls(false);
                            //扩大比例的缩放
                            wv_webview.getSettings().setUseWideViewPort(true);
                            //自适应屏幕
                            wv_webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
                            wv_webview.getSettings().setLoadWithOverviewMode(true);
                            wv_webview.setWebViewClient(new WebViewClient());
                            try {
                                JSONObject pjson = new JSONObject(m_model.getJsonData());
                                TextView he_FullName = view.findViewById(R.id.he_FullName);//河道名称
                                TextView he_value = view.findViewById(R.id.he_value);//河道液位
                                TextView he_BottomAltitude = view.findViewById(R.id.he_BottomAltitude);//管底高程
                                TextView he_TopAltitude = view.findViewById(R.id.he_TopAltitude);//管顶高程
                                TextView he_DistanceBottom = view.findViewById(R.id.he_DistanceBottom);//距内底高程
                                TextView he_RiverAltitude = view.findViewById(R.id.he_RiverAltitude);//河底高程

                                he_FullName.setText(pjson.getString("FullName"));
                                String ls_LiquidLevel = MyTextUtils.GetStringByValue(pjson.getString("LiquidLevel"));
                                String ls_River = MyTextUtils.GetStringByValue(pjson.getString("River"));
                                String ls_TopAltitude = MyTextUtils.GetStringByValue(pjson.getString("TopAltitude"));
                                String ls_BottomAltitude = MyTextUtils.GetStringByValue(pjson.getString("BottomAltitude"));

                                he_value.setText(ls_LiquidLevel + "m");
                                he_BottomAltitude.setText(ls_BottomAltitude + "m");
                                he_TopAltitude.setText(ls_TopAltitude + "m");
                                he_DistanceBottom.setText(ls_River + "m");
                                he_RiverAltitude.setText(pjson.getString("RiverAltitude") + "m");

                                String Url = ApiConstant.host_ht + "hd_display.html?tag=displays/0/%E9%A6%96%E9%A1%B5%E6%B2%B3%E9%81%93%E4%BF%A1%E6%81%AF%E5%B1%95%E7%A4%BA.json&LiquidLevel=" + ls_LiquidLevel + "&TopAltitude=" + ls_TopAltitude + "&BottomAltitude=" + ls_BottomAltitude;
                                wv_webview.loadUrl(Url);

                            } catch (JSONException e) {
                                ToastShow("河道数据解析错误" + e.toString());
                            }
                        } else {
                            lin_hedao.setVisibility(View.GONE);
                            lin_pump.setVisibility(View.GONE);
                        }

                        List<ShowDataBean> show_list = new ArrayList<>();
                        int index = 0;
                        if (m_model.getPumpList() != null) {

                            for (PumpBean pump : m_model.getPumpList()) {
                                show_list.add(new ShowDataBean(index, "PUMP", pump.getMpointID(), pump.getFullName(), pump.getStatus(), "", "", pump.getDataDT(), ""));
                                index++;
                            }

                        }
                        if (m_model.getRealDataList() != null) {

                            for (RealDataBean realdata : m_model.getRealDataList()) {
                                show_list.add(new ShowDataBean(index, "Data", realdata.getMpointID(), realdata.getShortName(), realdata.getStatus(), realdata.getValue(), realdata.getUnit(), realdata.getDataDT(), realdata.getAlarmName()));
                                index++;
                            }
                        }
                        mapDataAdapter.setNewData(show_list);
                        if (show_list.size() > 0) {
                            data_lin.setVisibility(View.VISIBLE);
                        } else {
                            data_lin.setVisibility(View.GONE);
                        }
                        tv_title.setText(m_model.getFullName());
                        mCallout.setOffset(0, 15);
                        mCallout.show(mMapView.toMapPoint(new Point(x, y)), view);
                        mCallout.setStyle(R.xml.callout_style_02);

                        iv_close.setOnClickListener(
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mCallout.hide();
                                    }
                                }
                        );

                        mapDataAdapter.setOnItemChildClickListener(new BaseAdapter.OnItemChildClickListener() {
                            @Override
                            public void onItemChildClick(BaseAdapter adapter, View v, int position) {
                                ShowDataBean s_model = mapDataAdapter.getItem(position);
                                if (s_model.getType().equals("Data")) {
                                    Intent it = new Intent(context, DataAnalysisActivity.class);
                                    it.putExtra("FromHome", false);
                                    it.putExtra("MPOINT", s_model.getID());
                                    startActivity(it);
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    //获取选中图例对应的数据
    private void getCheckedTypeData() {
        dialog = new LoadingDialog(context, "加载中");
        dialog.show();
        String url = ApiConstant.host + UrlConst.url_get_monitor_data + "?Category=&ID=" + typeCheckedStr;
        DataModel.requestGETMode("MapDataBean", url, new InitDataBeanBack<MapDataBean>() {
            @Override
            public void callbak(boolean b, List<MapDataBean> list, String msg) {
                infoList = list;
                numArr = new int[]{0, 0, 0, 0, 0, 0, 0};
                if (!TextUtil.isEmptyList(infoList)) {
                    for (MapDataBean model : infoList) {
                        String category = model.getF_Category();
                        for (int i = 0; i < Const.legendTypeArr.length; i++) {
                            if (Const.legendTypeArr[i].equals(category)) {
                                numArr[i] += 1;
                                legendList.get(i).setNum(numArr[i]);
                            }
                        }
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        legendAdapter.setNewData(legendList);
                        //选中的数据-地图添加
                        addMonitorPoints();
                    }
                });

            }
        });
    }

    //地图添加数据点
    private void addMonitorPoints() {
        mapGraphicsLayer.removeAll();
        locDataList.clear();
        if (TextUtil.isEmptyList(infoList)) {
            ToastUtil.show(R.string.no_data);
        } else {
            //此时:infoList为请求到选中的图例总数据列表,
            for (MapDataBean info : infoList) {
                String f_category = info.getF_Category();
                if (typeCheckedStr.contains(f_category) && info.getFact_MX() != 0) {
                    Point mapPoint = new Point(info.getFact_MX(), info.getFact_MY());
                    //地图添加坐标和数据Marker
                    Drawable drawable = ContextCompat.getDrawable(context, TextUtil.getTypePic(f_category));
                    PictureMarkerSymbol locationSymbol = new PictureMarkerSymbol(drawable);
                    Graphic graphic = new Graphic(mapPoint, locationSymbol);

                    //显示
                    if (isShowLable) {
                        PictureMarkerSymbol markerSymbol = new PictureMarkerSymbol(
                                MapUtils.createMapBitMap(context, info.getFullName()));
                        Graphic graphictext = new Graphic(mapPoint, markerSymbol);
                        markerSymbol.setOffsetY(-23);
                        locDataList.put(mapGraphicsLayer.addGraphic(graphictext), info);
                    }
                    locDataList.put(mapGraphicsLayer.addGraphic(graphic), info);
                }
            }
        }
        if (dialog != null) {
            dialog.close();
        }
    }

    @Override
    protected boolean showNavigation() {
        return true;
    }

    @Override
    protected boolean showMapPipe() {
        return false;
    }

    @Override
    protected boolean showMedia() {
        return false;
    }

    @Override
    protected int showMoreIcon() {
        return 0;
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
