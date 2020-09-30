package com.haocang.bjxjc.ui.reason.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.haocang.bjxjc.R;
import com.haocang.bjxjc.activity.comm.CommMapFinalActivity;
import com.haocang.bjxjc.requestcenter.DataModel;
import com.haocang.bjxjc.ui.event.EventBean;
import com.haocang.bjxjc.ui.reason.adapter.ReasonDataAdapter;
import com.haocang.bjxjc.ui.reason.bean.WarningDetailBean;
import com.haocang.bjxjc.ui.reason.bean.WarningShowBean;
import com.haocang.bjxjc.ui.waterpoint.bean.WaterPointBean;
import com.haocang.bjxjc.utils.tools.ApiConstant;
import com.haocang.bjxjc.utils.tools.UrlConst;
import com.haocang.bjxjc.utils.tools.MyTextUtils;
import com.haocang.bjxjc.utils.tools.OptionsItemsUtils;
import com.haocang.commonlib.gismap.MapUtils;
import com.haocang.commonlib.network.interfaces.InitDataOneBeanBack;

import net.tsz.afinal.annotation.view.ViewInject;

import java.util.ArrayList;
import java.util.List;

//预警行动
public class ReasonActivity extends CommMapFinalActivity {
    @ViewInject(id = R.id.tv_reason_start_dt)
    TextView tv_reason_startdt;
    @ViewInject(id = R.id.tv_reason_name)
    TextView tv_reason_name;
    @ViewInject(id = R.id.tv_reason_level)
    TextView tv_reason_level;
    @ViewInject(id = R.id.tv_user_count)
    TextView tv_user_count;
    @ViewInject(id = R.id.tv_car_count)
    TextView tv_car_count;
    @ViewInject(id = R.id.tv_waterpoint_num)
    TextView tv_waterpoint_num;
    @ViewInject(id = R.id.reason_jsd)
    TextView reason_jsd;
    @ViewInject(id = R.id.reason_shijian)
    TextView reason_shijian;
    @ViewInject(id = R.id.reason_team)
    TextView reason_team;
    @ViewInject(id = R.id.reason_user)
    TextView reason_user;
    @ViewInject(id = R.id.reason_jsd_lin)
    TextView reason_jsd_lin;
    @ViewInject(id = R.id.reason_shijian_lin)
    TextView reason_shijian_lin;
    @ViewInject(id = R.id.reason_team_lin)
    TextView reason_team_lin;
    @ViewInject(id = R.id.reason_user_lin)
    TextView reason_user_lin;
    @ViewInject(id = R.id.recyclerview)
    RecyclerView recyclerview;
    private Context context;
    private ReasonDataAdapter mReasonDataAdapter;
    private WarningDetailBean model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        initView();
        initData();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_reason;
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

    @Override
    protected String showTitleName() {
        return "防汛行动";
    }


    private void initView() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }

        });

        reason_jsd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPageData(1);
            }

        });
        reason_shijian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPageData(2);
            }
        });
        reason_team.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPageData(3);
            }

        });
        reason_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPageData(4);
            }

        });
    }

    private void showPageLayout(final int index) {
        switch (index) {
            case 1:
                reason_jsd_lin.setBackgroundColor(ContextCompat.getColor(context, R.color.app_title_color));
                reason_shijian_lin.setBackgroundColor(Color.WHITE);
                reason_team_lin.setBackgroundColor(Color.WHITE);
                reason_user_lin.setBackgroundColor(Color.WHITE);
                break;
            case 2:
                reason_jsd_lin.setBackgroundColor(Color.WHITE);
                reason_shijian_lin.setBackgroundColor(ContextCompat.getColor(context, R.color.app_title_color));
                reason_team_lin.setBackgroundColor(Color.WHITE);
                reason_user_lin.setBackgroundColor(Color.WHITE);
                break;
            case 3:
                reason_jsd_lin.setBackgroundColor(Color.WHITE);
                reason_shijian_lin.setBackgroundColor(Color.WHITE);
                reason_team_lin.setBackgroundColor(ContextCompat.getColor(context, R.color.app_title_color));
                reason_user_lin.setBackgroundColor(Color.WHITE);
                break;
            case 4:
                reason_jsd_lin.setBackgroundColor(Color.WHITE);
                reason_shijian_lin.setBackgroundColor(Color.WHITE);
                reason_team_lin.setBackgroundColor(Color.WHITE);
                reason_user_lin.setBackgroundColor(ContextCompat.getColor(context, R.color.app_title_color));
                break;
        }
    }

    //设置下面每个tab数据
    private void initPageData(final int index) {
        showPageLayout(index);
        List<WarningShowBean> list = new ArrayList<>();
        if (index == 1) {
            if (model.getWaterPoint() != null && model.getWaterPoint().size() > 0) {
                for (int i = 0; i < model.getWaterPoint().size(); i++) {
                    list.add(new WarningShowBean(model.getWaterPoint().get(i).getID(), model.getWaterPoint().get(i).getLocation(), model.getWaterPoint().get(i).getStatus(), model.getWaterPoint().get(i).getIsDelete()));
                }
            }
            MarkWaterPoint(model.getWaterPoint());
        } else if (index == 2) {
            if (model.getEvent() != null && model.getEvent().size() > 0) {
                for (int i = 0; i < model.getEvent().size(); i++) {
                    list.add(new WarningShowBean(model.getEvent().get(i).getID(),
                            model.getEvent().get(i).getMemo(), model.getEvent().get(i).getStatus() + "", "S"));
                }
            }
            MarkEventPoint(model.getEvent());
        } else if (index == 3) {
            if (model.getFloodTeam() != null && model.getFloodTeam().size() > 0) {
                for (int i = 0; i < model.getFloodTeam().size(); i++) {
                    list.add(new WarningShowBean(model.getFloodTeam().get(i).getID(), "【组长】:" + model.getFloodTeam().get(i).getLeader() + "    【组员】:" + model.getFloodTeam().get(i).getTeamMemberName(), "", "S"));
                }
            }
        } else if (index == 4) {
            if (model.getFloodUser() != null && model.getFloodUser().size() > 0) {
                for (int i = 0; i < model.getFloodUser().size(); i++) {
                    String ls_type = MyTextUtils.GetOpitemValue(OptionsItemsUtils.GetDataItemList("userType"), 1, model.getFloodUser().get(i).getType());
                    list.add(new WarningShowBean(model.getFloodUser().get(i).getID(), "【名称】:" + model.getFloodUser().get(i).getUserName() + "    【职责】:" + ls_type, "", "S"));
                }
            }
        }

        mReasonDataAdapter = new ReasonDataAdapter(context);
        recyclerview.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL) {
            @Override
            public boolean canScrollVertically() {
                return true;//是否允许滑动
            }
        });
        recyclerview.setAdapter(mReasonDataAdapter);
        mReasonDataAdapter.setNewData(list);
    }

    private void initData() {
        String ID = getIntent().getStringExtra("ID");
        String ApiUrl = ApiConstant.host + UrlConst.Api_GetReasonDeatil + ID;
        DataModel.requestGETOneMode("WarningDetailBean", ApiUrl, new InitDataOneBeanBack<WarningDetailBean>() {
            @Override
            public void callbak(boolean b, WarningDetailBean data, String msg) {
                model = data;
                //显示预警内容
                if (data.getReasonDetail() != null && data.getReasonDetail().size() > 0) {
                    tv_reason_name.setText(data.getReasonDetail().get(0).getName());
                    tv_reason_startdt.setText(data.getReasonDetail().get(0).getStartTime());
                    tv_reason_level.setText(data.getReasonDetail().get(0).getPrecipitation());
                    tv_user_count.setText(data.getReasonDetail().get(0).getUserCount());
                    tv_car_count.setText(data.getReasonDetail().get(0).getCarCount());
                    if (!MyTextUtils.IsNull(data.getReasonDetail().get(0).getTaskWaterPoint()) && !MyTextUtils.IsNull(data.getReasonDetail().get(0).getAllWaterPoint())) {
                        tv_waterpoint_num.setText(data.getReasonDetail().get(0).getTaskWaterPoint() + "/" + data.getReasonDetail().get(0).getAllWaterPoint());
                    } else {
                        tv_waterpoint_num.setVisibility(View.GONE);
                    }
                }

                if (data.getWaterPoint() != null && data.getWaterPoint().size() > 0) {
                    showPageLayout(1);
                    mReasonDataAdapter = new ReasonDataAdapter(context);
                    recyclerview.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL) {
                        @Override
                        public boolean canScrollVertically() {
                            return true;//是否允许滑动
                        }
                    });
                    recyclerview.setAdapter(mReasonDataAdapter);

                    List<WarningShowBean> list = new ArrayList<>();

                    for (int i = 0; i < data.getWaterPoint().size(); i++) {
                        list.add(new WarningShowBean(data.getWaterPoint().get(i).getID(), data.getWaterPoint().get(i).getLocation(), data.getWaterPoint().get(i).getStatus(), data.getWaterPoint().get(i).getIsDelete()));
                    }
                    mReasonDataAdapter.setNewData(list);
                    MarkWaterPoint(data.getWaterPoint());
                }


            }
        });

    }


    private void MarkWaterPoint(final List<WaterPointBean> list) {
        mapGraphicsLayer.removeAll();
        for (WaterPointBean m : list) {
            addwaterpoint(m);
        }
    }

    //添加点
    private void addwaterpoint(final WaterPointBean model) {
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
            mapGraphicsLayer.addGraphic(graphictext);
        }
        mapGraphicsLayer.addGraphic(graphic);
    }


    private void MarkEventPoint(final List<EventBean> list) {
        mapGraphicsLayer.removeAll();
        for (EventBean m : list) {
            addeventpoint(m);
        }
    }

    //添加点
    private void addeventpoint(final EventBean model) {
        Point mapPoint = new Point(model.getMX(), model.getMY());
        int icon = 0;
        icon = R.mipmap.jsd_gray;

        PictureMarkerSymbol locationSymbol = new PictureMarkerSymbol(mContext.getResources().getDrawable(icon));
        Graphic graphic = new Graphic(mapPoint, locationSymbol);


        if (isShowLable) {
            PictureMarkerSymbol markerSymbol = new PictureMarkerSymbol(MapUtils.createMapBitMap(context, model.getMemo()));
            Graphic graphictext = new Graphic(mapPoint, markerSymbol);
            markerSymbol.setOffsetY(-23);
            mapGraphicsLayer.addGraphic(graphictext);
        }
        mapGraphicsLayer.addGraphic(graphic);
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
