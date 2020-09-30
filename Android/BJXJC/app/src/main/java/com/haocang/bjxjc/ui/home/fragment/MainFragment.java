package com.haocang.bjxjc.ui.home.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.haocang.bjxjc.KeepOrder.KeepOrderListActivity;
import com.haocang.bjxjc.ui.analysis.activity.DataAnalysisActivity;
import com.haocang.bjxjc.ui.equipment.activity.EquipmentArchivesListActivity;
import com.haocang.bjxjc.ui.faultrepair.activity.FaultRepairListActivity;
import com.haocang.bjxjc.ui.home.activity.MainActivity;
import com.haocang.bjxjc.ui.home.bean.WeatherBean;
import com.haocang.bjxjc.ui.home.util.HomeUtils;
import com.haocang.bjxjc.ui.ht.ProcessScreenActivity;
import com.haocang.bjxjc.ui.monitor.activity.ComprehensiveMonitActivity;
import com.haocang.bjxjc.ui.reason.activity.EmergencySolutionActivity;
import com.haocang.bjxjc.ui.reason.activity.ReasonActivity;
import com.haocang.bjxjc.utils.tools.MyTextUtils;
import com.haocang.bjxjc.utils.tools.ToastUtil;
import com.haocang.commonlib.pagegrid.MyPageIndicator;
import com.haocang.commonlib.pagegrid.PageGridView;
import com.haocang.bjxjc.R;
import com.haocang.bjxjc.requestcenter.DataModel;
import com.haocang.commonlib.network.interfaces.InitDataBeanBack;
import com.haocang.commonlib.network.interfaces.InitDataCallBack;
import com.haocang.bjxjc.ui.event.EventListActivity;
import com.haocang.bjxjc.ui.floodpoint.activity.FloodPointListActivity;
import com.haocang.bjxjc.ui.home.activity.WeatherActivity;
import com.haocang.bjxjc.ui.home.adapter.HomeDataAdapter;
import com.haocang.bjxjc.ui.home.adapter.HomePageMenuAdapter;
import com.haocang.bjxjc.ui.home.adapter.HomeTaskAdapter;
import com.haocang.bjxjc.ui.home.adapter.PageGridViewListener;
import com.haocang.bjxjc.ui.home.bean.HomeDataBean;
import com.haocang.bjxjc.ui.home.bean.HomeMenuBean;
import com.haocang.bjxjc.ui.home.bean.HomeTaskBean;
import com.haocang.bjxjc.ui.home.bean.MsgNumberBean;
import com.haocang.bjxjc.ui.waterpoint.activity.WaterPointListActivity;
import com.haocang.bjxjc.utils.bean.EarlyWarningActionBean;
import com.haocang.bjxjc.utils.bean.ReasonBean;
import com.haocang.bjxjc.utils.tools.ApiConstant;
import com.haocang.bjxjc.utils.tools.UrlConst;
import com.haocang.bjxjc.utils.tools.CacheData;
import com.haocang.bjxjc.utils.view.ObservableScrollView;
import com.haocang.bjxjc.utils.view.ScrollViewListener;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.bean.ZxingConfig;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 主页
 */
public class MainFragment extends Fragment implements ScrollViewListener, PageGridViewListener {
    private View view;
    private MainActivity activity;
    private ImageView home_scarn;
    PageGridView pageGridView;
    MyPageIndicator pageIndicator;
    private HomePageMenuAdapter mHomePageMenuAdapter;
    private RecyclerView recyclerview_shijian, recyclerview_jsd;
    private HomeDataAdapter mHomeDataAdapter_shijian, mHomeDataAdapter_jsd;
    private HomeTaskAdapter mHomeTaskAdapter;
    private ObservableScrollView scrollview;
    private LinearLayout fl_layout, home_reason_lin, lin_event_more, lin_water_more;
    private TextView app_name, tv_city, tv_reason_name, tv_reason_start_dt, tv_reason_level, tv_solution_name, tv_waterpoint_num, tv_user_count, tv_car_count;
    private LinearLayout home_weather_lin;
    private ImageView iv_home_weather;
    private TextView tv_home_weather_wd, tv_home_weather_memo;
    private final int REQUEST_CODE_SCAN = 1993;
    private String ls_EmergencySolutionID = "";
    private String TAG = MainFragment.class.getSimpleName();
    @ViewInject(id = R.id.warm_layout_iv)
    ImageView layout_iv;
    @ViewInject(id = R.id.warm_layout_title)
    LinearLayout layout_title;
    @ViewInject(id = R.id.warm_layout_level)
    LinearLayout layout_level;
    private long lastTimeM;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        FinalActivity.initInjectedView(this, view);
        activity = (MainActivity) getActivity();
        initView();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getWarmData();
        activity.getMsgNum();
        showLocCityName();
        getWeatherData();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        onStart();
    }

    private void initView() {
        app_name = view.findViewById(R.id.app_name);
        tv_city = view.findViewById(R.id.tv_city);
        home_scarn = view.findViewById(R.id.home_scarn);
        fl_layout = view.findViewById(R.id.fl_layout);
        scrollview = view.findViewById(R.id.scrollview);
        pageIndicator = view.findViewById(R.id.pageindicator);
        pageGridView = view.findViewById(R.id.pagingGridView);
        recyclerview_shijian = view.findViewById(R.id.recyclerview_shijian);
        recyclerview_jsd = view.findViewById(R.id.recyclerview_jsd);
        tv_reason_name = view.findViewById(R.id.tv_reason_name);

        home_reason_lin = view.findViewById(R.id.home_reason_lin);
        tv_reason_start_dt = view.findViewById(R.id.tv_reason_start_dt);
        tv_reason_level = view.findViewById(R.id.tv_reason_level);
        tv_solution_name = view.findViewById(R.id.tv_solution_name);
        tv_waterpoint_num = view.findViewById(R.id.tv_waterpoint_num);
        tv_user_count = view.findViewById(R.id.tv_user_count);
        tv_car_count = view.findViewById(R.id.tv_car_count);
        lin_event_more = view.findViewById(R.id.lin_event_more);
        lin_water_more = view.findViewById(R.id.lin_water_more);
        iv_home_weather = view.findViewById(R.id.iv_home_weather);
        tv_home_weather_wd = view.findViewById(R.id.tv_home_weather_wd);
        tv_home_weather_memo = view.findViewById(R.id.tv_home_weather_memo);
        home_weather_lin = view.findViewById(R.id.home_weather_lin);

        //控制指示器的显示
        if (HomeMenuBean.getHomeMenu().size() > 8) {
            pageIndicator.setVisibility(View.VISIBLE);
        } else {
            pageIndicator.setVisibility(View.GONE);
        }

        mHomePageMenuAdapter = new HomePageMenuAdapter(HomeMenuBean.getHomeMenu(), activity);
        mHomeDataAdapter_shijian = new HomeDataAdapter(activity);
        mHomeDataAdapter_jsd = new HomeDataAdapter(activity);
        mHomeTaskAdapter = new HomeTaskAdapter(activity);
        pageGridView.setOnItemClickListener(mHomePageMenuAdapter);
        pageGridView.setAdapter(mHomePageMenuAdapter);
        pageGridView.setPageIndicator(pageIndicator);
        scrollview.setScrollViewListener(this);
        mHomePageMenuAdapter.setPageGridViewListener(this);

        recyclerview_shijian.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL) {
            @Override
            public boolean canScrollVertically() {
                return false;//是否允许滑动
            }
        });
        recyclerview_jsd.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL) {
            @Override
            public boolean canScrollVertically() {
                return false;//是否允许滑动
            }
        });
        recyclerview_shijian.setAdapter(mHomeDataAdapter_shijian);
        recyclerview_jsd.setAdapter(mHomeDataAdapter_jsd);
        mHomeTaskAdapter.setNewData(HomeTaskBean.getHomeTask());
        home_weather_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(activity, WeatherActivity.class);
                startActivity(it);
            }
        });

        lin_event_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(activity, EventListActivity.class);
                startActivity(it);
            }
        });

        lin_water_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, WaterPointListActivity.class);//实时积水监测
                intent.putExtra("TYPE", "NOW");
                startActivity(intent);
            }
        });

        View.OnClickListener warmListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyTextUtils.IsNull(CacheData.Cache_WaringID)) {
                    ToastShow("当前无预警行动！");
                    return;
                }
                Intent intent = new Intent(activity, ReasonActivity.class);//预警行动
                intent.putExtra("ID", CacheData.Cache_WaringID);
                startActivity(intent);
            }
        };
        layout_iv.setOnClickListener(warmListener);
        layout_title.setOnClickListener(warmListener);
        layout_level.setOnClickListener(warmListener);
        tv_reason_name.setOnClickListener(warmListener);
        tv_solution_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyTextUtils.IsNull(ls_EmergencySolutionID)) {
                    ToastShow("当前无预案！");
                    return;
                }
                Intent intent = new Intent(activity, EmergencySolutionActivity.class);//预案内容
                intent.putExtra("ID", ls_EmergencySolutionID);
                startActivity(intent);
            }
        });
        home_scarn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, CaptureActivity.class);
                ZxingConfig config = new ZxingConfig();
                config.setShowbottomLayout(true);//底部布局（包括闪光灯和相册）
                config.setShake(true);//是否震动
                config.setShowFlashLight(true);//是否显示闪光灯
                config.setPlayBeep(false);//是否播放提示音
                config.setShowAlbum(false);//是否显示相册
                intent.putExtra(com.yzq.zxinglibrary.common.Constant.INTENT_ZXING_CONFIG, config);
                startActivityForResult(intent, REQUEST_CODE_SCAN);
            }
        });
    }

    protected void ToastShow(String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }

    //单位城市名称
    private void showLocCityName() {
        if (CacheData.LocationCity != null && !CacheData.LocationCity.equals("")) {
            String cityname = CacheData.LocationCity + CacheData.LocationDistrict;
            tv_city.setText(cityname);
        } else {
            tv_city.setText("北京新机场");
        }
    }

    //更新面板统计数据
    public void getBottomData(MsgNumberBean model) {
        List<HomeDataBean> event_list = new ArrayList<>();
        event_list.add(new HomeDataBean(model.getEvent_SUM() + "处", "全部"));
        event_list.add(new HomeDataBean(model.getEvent_S2() + "处", "已处理"));
        event_list.add(new HomeDataBean(model.getEvent_S1() + "处", "处理中"));
        event_list.add(new HomeDataBean(model.getEvent_S0() + "处", "未处理"));
        mHomeDataAdapter_shijian.setNewData(event_list);

        List<HomeDataBean> water_list = new ArrayList<>();
        water_list.add(new HomeDataBean(model.getWaterPoint_SUM() + "处", "全部"));
        water_list.add(new HomeDataBean(model.getWaterPoint_S2() + "处", "已处置"));
        water_list.add(new HomeDataBean(model.getWaterPoint_S1() + "处", "处置中"));
        water_list.add(new HomeDataBean(model.getWaterPoint_S0() + "处", "未处置"));
        water_list.add(new HomeDataBean(model.getWaterPoint_S3() + "处", "未巡查"));
        water_list.add(new HomeDataBean(model.getWaterPoint_S4() + "处", "未积水"));
        mHomeDataAdapter_jsd.setNewData(water_list);
    }

    //更新预警行动的信息
    public void getWarmData() {
        if (System.currentTimeMillis() - lastTimeM < 1000) {
            return;
        }
        lastTimeM = System.currentTimeMillis();
        //获取当前的预警行动
        String url = ApiConstant.host + UrlConst.url_get_is_run_reason;
        Log.d(TAG, "接口请求地址" + url);
        DataModel.requestGETMode("EarlyWarningActionBean", url, new InitDataBeanBack<EarlyWarningActionBean>() {
            @Override
            public void callbak(boolean isSuccess, List<EarlyWarningActionBean> list, String msg) {
                if (isSuccess) {
                    if (list == null || list.size() == 0) {
                        home_reason_lin.setVisibility(View.GONE);
                        CacheData.Cache_WaringID = "";
                    } else {
                        home_reason_lin.setVisibility(View.VISIBLE);

                        tv_solution_name.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//添加下划线
                        EarlyWarningActionBean model = list.get(0);
                        CacheData.Cache_WaringID = model.getID();

                        tv_reason_name.setText(model.getName());
                        tv_reason_start_dt.setText(model.getStartTime());
                        tv_reason_level.setText(model.getPrecipitation());
                        tv_solution_name.setText(model.getEmergencySolutionName());
                        tv_user_count.setText(model.getUserCount());
                        tv_car_count.setText(model.getCarCount());

                        if (!MyTextUtils.IsNull(model.getTaskWaterPoint()) && !MyTextUtils.IsNull(model.getAllWaterPoint())) {
                            tv_waterpoint_num.setText(model.getTaskWaterPoint() + "/" + model.getAllWaterPoint());
                        } else {
                            tv_waterpoint_num.setVisibility(View.GONE);
                        }

                        ls_EmergencySolutionID = model.getEmergencySolutionID();
                    }
                } else {
                    home_reason_lin.setVisibility(View.GONE);
                    ToastUtil.show(R.string.request_failure);
                }
            }
        });
        //获取预警数据
        getWarmList();
    }

    //获取预警行动数据
    private void getWarmList() {
        String ApiUrl = ApiConstant.host + UrlConst.url_warm_list;
        DataModel.requestGETMode("ReasonBean", ApiUrl, new InitDataBeanBack<ReasonBean>() {
            @Override
            public void callbak(boolean b, List<ReasonBean> list, String msg) {
                if (b) {
                    ApiConstant.db.deleteByWhere(ReasonBean.class, "name like '%%'");
                    for (ReasonBean model : list) {
                        ReasonBean mm = new ReasonBean();
                        mm.setID(model.getID());
                        mm.setName(model.getName());
                        ApiConstant.db.save(mm);
                    }
                }
            }
        });
    }

    //todo 获取实时天气
    private void getWeatherData() {
        DataModel.requestGET(activity, ApiConstant.WeatherURL, new InitDataCallBack() {
            @Override
            public void callbak(boolean isSuccess, String string) {
                if (isSuccess) {
                    try {
                        JSONObject jsondata = new JSONObject(string);
                        Gson gson = new Gson();
                        if (jsondata.getInt("Code") == 0) {
                            JSONObject jsonroot = new JSONObject(jsondata.getString("Data"));
                            List<WeatherBean> datalist = gson.fromJson(jsonroot.getString("Weather"),
                                    new TypeToken<List<WeatherBean>>() {
                                    }.getType());

                            if (datalist.size() > 0) {
                                WeatherBean model = datalist.get(0);
                                String str_low = model.getLow().replace("低温 ", "");
                                String str_high = model.getHigh().replace("高温 ", "");
                                String str_wendu = str_low + "~" + str_high;
                                String str_type = model.getDay().getType();
                                String str_fx = model.getDay().getFengxiang();
                                String str_fl = model.getDay().getFengli().toString().replace
                                        ("{#cdata-section=", "").replace("}", "");
                                String Memo = "[查看详情]" + model.getDate() + ":"
                                        + str_type + "," + str_fx + "," + str_fl;
                                tv_home_weather_wd.setText(str_wendu);
                                tv_home_weather_memo.setText(Memo);
                                // FinalBitmap fb = FinalBitmap.create(activity);
                                // fb.display(iv_home_weather, Config.host + "/Content/images/weather/" + str_type + ".png");
                                iv_home_weather.setImageResource(HomeUtils.GetWeatherImg(str_type));
                            } else {
                                tv_home_weather_wd.setText("暂无数据");
                                tv_home_weather_memo.setText("暂无数据");
                            }
                            return;
                        }
                        Log.d(TAG, "天气获取失败," + jsondata.getString("Message"));
                    } catch (Exception e) {
                        Log.d(TAG, "天气数据解析失败," + e.toString());
                    }
                }
                ToastShow("天气获取失败," + string);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == -1) {
            if (data != null) {
                String content = data.getStringExtra(com.yzq.zxinglibrary.common.Constant.CODED_CONTENT);
                ToastShow("结果:" + content);
            }
        }

    }

    @Override
    public void onItemClickChanged(PageGridView pageGridView, int position, HomeMenuBean bean) {
        if (bean == null) {
            return;
        }
        Intent intent;
        switch (bean.getIcon()) {
            case R.mipmap.ico_homemenu_yyd:
                startActivity(new Intent(activity, FloodPointListActivity.class));//易淹点
                break;
            case R.mipmap.ico_homemenu_quxian://数据曲线
                startActivity(new Intent(activity, DataAnalysisActivity.class));
                break;
            case R.mipmap.ico_homemenu_shijian:
                intent = new Intent(activity, EventListActivity.class);//事件监测
                startActivity(intent);
                break;
            case R.mipmap.ico_homemenu_jsjc:
                intent = new Intent(activity, WaterPointListActivity.class);//实时积水监测
                intent.putExtra("TYPE", "NOW");
                startActivity(intent);
                break;
            case R.mipmap.ico_home_jsk:
                intent = new Intent(activity, WaterPointListActivity.class);//历史积水库
                intent.putExtra("TYPE", "OLD");
                startActivity(intent);
                break;
            case R.mipmap.ico_homemenu_zhjc:
                startActivity(new Intent(activity, ComprehensiveMonitActivity.class));//综合监测
                break;
            case R.mipmap.ico_homemenu_gongyi:
                startActivity(new Intent(activity, ProcessScreenActivity.class));//工艺画面
                break;
            case R.mipmap.ico_homemenu_shebei:
                startActivity(new Intent(activity, EquipmentArchivesListActivity.class));//设备档案
                break;
            case R.mipmap.ico_homemenu_weixiu:
                startActivity(new Intent(activity, FaultRepairListActivity.class));//维修工单
                break;
            case R.mipmap.ico_homemenu_baoyang:
                startActivity(new Intent(activity, KeepOrderListActivity.class));//保养工单
                break;
            default:
                ToastShow("功能正在研发中");
                break;
        }
    }

    @Override
    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
        if (y > 0 && y < 255) {
            fl_layout.setBackgroundColor(Color.argb(y, 0, 150, 136));
            app_name.setTextColor(Color.argb(y, 255, 255, 255));
        }
        if (y >= 255) {
            fl_layout.setBackgroundColor(Color.argb(255, 0, 150, 136));
            app_name.setTextColor(Color.argb(255, 255, 255, 255));
        }
        if (y == 0) {
            fl_layout.setBackgroundColor(Color.argb(0, 0, 150, 136));
            app_name.setTextColor(Color.argb(0, 255, 255, 255));
            fl_layout.setVisibility(View.GONE);
            //滑到最顶端
            if (System.currentTimeMillis() - lastTimeM > 1000) {
                getWarmData();
                activity.getMsgNum();
            }
        } else {
            fl_layout.setVisibility(View.VISIBLE);
        }
        showLocCityName();
    }
}
