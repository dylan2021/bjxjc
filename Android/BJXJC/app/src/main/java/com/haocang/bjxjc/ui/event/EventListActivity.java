package com.haocang.bjxjc.ui.event;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
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
import com.haocang.bjxjc.utils.tools.TextUtil;
import com.haocang.commonlib.gismap.MapUtils;
import com.haocang.commonlib.network.interfaces.InitDataBeanBack;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/*
  事件监测
 */
public class EventListActivity extends CommMapFinalActivity {
    @ViewInject(id = R.id.tv_map_addpoint)
    TextView tv_map_addpoint;
    @ViewInject(id = R.id.map_layout)
    RelativeLayout mapLayout;
    @ViewInject(id = R.id.list)
    LinearLayout eventLayout;
    @ViewInject(id = R.id.tv_year)
    TextView tv_year;
    @ViewInject(id = R.id.tv_status)
    TextView tv_status;
    @ViewInject(id = R.id.tv_warm)
    TextView tv_warm;
    @ViewInject(id = R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @ViewInject(id = R.id.recyclerview)
    RecyclerView recyclerView;
    private EventListAdapter adapter;
    protected GraphicsLayer eventLayer;
    private Map<Integer, EventBean> locList;
    private EventListActivity context;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_event_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = EventListActivity.this;
        CacheActivityUtil.addActivity(context);

        initView();
    }

    private void initView() {
        eventLayer = new GraphicsLayer();
        mMapView.addLayer(eventLayer);

        locList = new HashMap<>();
        adapter = new EventListAdapter(context);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL) {
            @Override
            public boolean canScrollVertically() {
                return true;//是否允许滑动
            }
        });
        recyclerView.setAdapter(adapter);
        tv_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseYear();
            }

        });
        tv_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchStatus(OptionsItemsUtils.GetEventStatus(), 1);
            }

        });
        tv_warm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchStatus(OptionsItemsUtils.GetAllReasonList(), 2);
            }
        });

        adapter.setOnItemChildClickListener(new BaseAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseAdapter a, View v, int position) {
                EventBean model = adapter.getItem(position);
                CacheData.mEventBean = model;
                CacheData.Event_UPDATE = true;
                Intent intent = new Intent(context, EventDetailActivity.class);
                startActivity(intent);
            }

        });

        mMapView.setOnSingleTapListener(new OnSingleTapListener() {
            @Override
            public void onSingleTap(float x, float y) {
                int[] ids = eventLayer.getGraphicIDs(x, y, 5);
                if (ids.length > 0) {
                    final EventBean mEventpoint = locList.get(ids[0]);

                    CacheData.Event_UPDATE = true;
                    CacheData.mEventBean = mEventpoint;
                    Intent intent = new Intent(context, EventDetailActivity.class);
                    startActivity(intent);

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
                    ToastShow("请在地图上设置事件位置");
                    return;
                }
                final String ApiUrl = ApiConstant.MAPCoordTransformUrl +
                        "?inSR=%7B%22wkt%22%3A%22PROJCS%5B%5C%22BNAH%5C%22%2CGEOGCS%5B%5C%22GCS_WGS_1984%5C%22%2CDATUM%5B%5C%22D_WGS_1984%5C%22%2CSPHEROID%5B%5C%22WGS_1984%5C%22%2C6378137.0%2C298.257223563%5D%5D%2CPRIMEM%5B%5C%22Greenwich%5C%22%2C0.0%5D%2CUNIT%5B%5C%22Degree%5C%22%2C0.0174532925199433%5D%5D%2CPROJECTION%5B%5C%22Berghaus_Star%5C%22%5D%2CPARAMETER%5B%5C%22False_Easting%5C%22%2C500000.0%5D%2CPARAMETER%5B%5C%22False_Northing%5C%22%2C500000.0%5D%2CPARAMETER%5B%5C%22Central_Meridian%5C%22%2C116.385%5D%2CPARAMETER%5B%5C%22Latitude_Of_Origin%5C%22%2C39.461%5D%2CPARAMETER%5B%5C%22XY_Plane_Rotation%5C%22%2C-7.0%5D%2CUNIT%5B%5C%22Meter%5C%22%2C1.0%5D%5D%22%7D&outSR=4326&geometries=%7B%22geometryType%22%3A%22esriGeometryPoint%22%2C%22geometries%22%3A%5B%7B%22x%22%3A" + SingleTap_X + "%2C%22y%22%3A" + SingleTap_Y + "%2C%22spatialReference%22%3A%7B%22wkt%22%3A%22PROJCS%5B%5C%22BNAH%5C%22%2CGEOGCS%5B%5C%22GCS_WGS_1984%5C%22%2CDATUM%5B%5C%22D_WGS_1984%5C%22%2CSPHEROID%5B%5C%22WGS_1984%5C%22%2C6378137.0%2C298.257223563%5D%5D%2CPRIMEM%5B%5C%22Greenwich%5C%22%2C0.0%5D%2CUNIT%5B%5C%22Degree%5C%22%2C0.0174532925199433%5D%5D%2CPROJECTION%5B%5C%22Berghaus_Star%5C%22%5D%2CPARAMETER%5B%5C%22False_Easting%5C%22%2C500000.0%5D%2CPARAMETER%5B%5C%22False_Northing%5C%22%2C500000.0%5D%2CPARAMETER%5B%5C%22Central_Meridian%5C%22%2C116.385%5D%2CPARAMETER%5B%5C%22Latitude_Of_Origin%5C%22%2C39.461%5D%2CPARAMETER%5B%5C%22XY_Plane_Rotation%5C%22%2C-7.0%5D%2CUNIT%5B%5C%22Meter%5C%22%2C1.0%5D%5D%22%7D%7D%5D%7D&transformation=&transformForward=true&vertical=false&f=json";
                DataModel.requestGET(context, ApiUrl, new InitDataCallBack() {
                    @Override
                    public void callbak(boolean b, String string) {
                        if (b) {
                            try {
                                Gson gson = new Gson();
                                JSONObject jsonobject = new JSONObject(string);
                                List<XyBean> datalist = gson.fromJson(jsonobject.getString(
                                        "geometries"), new TypeToken<List<XyBean>>() {
                                }.getType());
                                if (datalist.size() > 0) {
                                    //跳转到积水明细上报界面
                                    CacheData.Event_UPDATE = false;
                                    CacheData.mEventBean = null;
                                    Intent intent = new Intent(context, EventDetailActivity.class);
                                    intent.putExtra("X", datalist.get(0).getX());
                                    intent.putExtra("Y", datalist.get(0).getY());
                                    intent.putExtra("MX", SingleTap_X);
                                    intent.putExtra("MY", SingleTap_Y);
                                    startActivity(intent);
                                } else {
                                    ToastShow("转换为WGS84坐标失败");
                                }
                            } catch (Exception e) {
                                ToastShow("转换为WGS84坐标失败:" + e.toString());
                            }
                        } else {
                            ToastShow("转换为WGS84坐标失败:" + string);
                        }
                    }
                });

            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        //地图和控价布局切换
        iv_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchMapAndLayout();
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

    private void getData() {
        String year = tv_year.getText().toString();
        //  全部:空 ,未处理:0  处理中:1  已处理:2
        String statusText = tv_status.getText().toString();
        String warmText = tv_warm.getText().toString();

        String status = MyTextUtils.GetOpitemValue(OptionsItemsUtils.GetEventStatus(), 0, statusText);
        String WarningID = MyTextUtils.GetOpitemValue(OptionsItemsUtils.GetAllReasonList(), 0,
                warmText);

        String url = ApiConstant.host + UrlConst.Api_Event + "?limit=" + ApiConstant.PAGE_LIMIT
                + "&year=" + year + "&status=" + status + "&WarningID=" + WarningID;
        DataModel.requestGETMode("EventBean", url, new InitDataBeanBack<EventBean>() {
            @Override
            public void callbak(boolean isSuccess, List<EventBean> list, String msg) {
                if (mContext == null) {
                    return;
                }
                if (isSuccess) {
                    if (TextUtil.isEmptyList(list)) {
                        ToastShow(getString(R.string.no_data));
                    }
                    adapter.setNewData(list);
                    addMarkPoint(list);
                } else {
                    ToastShow(msg);
                }

                refreshLayout.finishRefresh(0);
            }
        });
    }

    private void addMarkPoint(final List<EventBean> list) {
        eventLayer.removeAll();
        locList.clear();
        for (EventBean m : list) {
            addEvnetpoint(m);
        }
    }

    //添加点
    private void addEvnetpoint(EventBean model) {
        Point mapPoint = new Point(model.getMX(), model.getMY());
        PictureMarkerSymbol locationSymbol = new PictureMarkerSymbol(mContext.getResources().getDrawable(R.mipmap.map_default));
        Graphic graphic = new Graphic(mapPoint, locationSymbol);

        if (isShowLable) {
            PictureMarkerSymbol markerSymbol = new PictureMarkerSymbol(MapUtils.createMapBitMap(context, model.getMemo()));
            Graphic graphictext = new Graphic(mapPoint, markerSymbol);
            markerSymbol.setOffsetY(-23);
            // Graphic graphic_start_p0 = new Graphic(startPoint,new SimpleMarkerSymbol(start_color,19, SimpleMarkerSymbol.STYLE.CIRCLE));
            locList.put(eventLayer.addGraphic(graphictext), model);
        }

        locList.put(eventLayer.addGraphic(graphic), model);
    }


    //选择年份
    private void chooseYear() {
        TimePickerView.Builder builder = new TimePickerView.Builder(context, new TimePickerView.OnTimeSelectListener() {
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

    //处理状态  筛选
    private void switchStatus(final List<ProvinceBean> items, final int pos) {
        hideKeyBoard();
        OptionsPickerView.Builder builder = new OptionsPickerView.Builder(mContext, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String itemName = items.get(options1).getPickerViewText();
                switch (pos) {
                    case 1://处理状态
                        tv_status.setText(itemName);
                        break;
                    case 2://所属预警
                        tv_warm.setText(itemName);
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

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    //切换地图和数据view
    private void switchMapAndLayout() {
        if (eventLayout.getVisibility() == View.GONE) {
            eventLayout.setVisibility(View.VISIBLE);
            mapLayout.setVisibility(View.GONE);
            eventLayout.setAnimation(AnimationUtils.makeInAnimation(mContext, true));
            mapLayout.setAnimation(AnimationUtils.makeOutAnimation(mContext, true));
            iv_more_icon.setBackgroundResource(R.drawable.ic_map);
        } else {
            eventLayout.setVisibility(View.GONE);
            mapLayout.setVisibility(View.VISIBLE);
            eventLayout.setAnimation(AnimationUtils.makeOutAnimation(mContext, false));
            mapLayout.setAnimation(AnimationUtils.makeInAnimation(mContext, false));
            iv_more_icon.setBackgroundResource(R.drawable.ic_list);
        }
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
        return "事件监测";
    }

    @Override
    protected boolean showMapPipe() {
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CacheActivityUtil.finishSingleActivity(context);
    }
}
