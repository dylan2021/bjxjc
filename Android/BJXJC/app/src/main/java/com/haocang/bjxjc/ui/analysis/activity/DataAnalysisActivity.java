package com.haocang.bjxjc.ui.analysis.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.github.mikephil.charting.charts.LineChart;
import com.haocang.bjxjc.R;
import com.haocang.bjxjc.activity.comm.CommFinalActivity;
import com.haocang.bjxjc.requestcenter.DataModel;
import com.haocang.bjxjc.ui.analysis.bean.MpointCurveBean;
import com.haocang.bjxjc.ui.ht.bean.ScreenTreeBean;
import com.haocang.bjxjc.utils.tools.ApiConstant;
import com.haocang.bjxjc.utils.tools.TextUtil;
import com.haocang.bjxjc.utils.tools.TimeUtils;
import com.haocang.bjxjc.utils.tools.ToastUtil;
import com.haocang.bjxjc.utils.tools.UrlConst;
import com.haocang.bjxjc.utils.tools.MyUtils;
import com.haocang.bjxjc.utils.tools.MyTextUtils;
import com.haocang.commonlib.control.treeview.holder.IconTreeItemHolder;
import com.haocang.commonlib.control.treeview.holder.ProfileHolder;
import com.haocang.commonlib.control.treeview.holder.SelectableHeader3Holder;
import com.haocang.commonlib.control.treeview.model.TreeNode;
import com.haocang.commonlib.control.treeview.myholder.MyItemTwo1Holder;
import com.haocang.commonlib.control.treeview.myholder.MyItemThree1Holder;
import com.haocang.commonlib.control.treeview.myholder.MyItemTwo2Holder;
import com.haocang.commonlib.control.treeview.view.AndroidTreeView;
import com.haocang.commonlib.curve.CompositeIndexBean;
import com.haocang.commonlib.curve.LineChartManager;
import com.haocang.commonlib.loadingdialog.LoadingDialog;
import com.haocang.commonlib.network.interfaces.InitDataBeanBack;
import com.haocang.commonlib.otherutil.TextUtils;

import net.tsz.afinal.annotation.view.ViewInject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/*
 * 数据曲线
 */
public class DataAnalysisActivity extends CommFinalActivity {
    @ViewInject(id = R.id.tv_select)
    TextView showOrDismissListBt;
    @ViewInject(id = R.id.tv_quert)
    TextView tv_quert;
    @ViewInject(id = R.id.tv_Reset)
    TextView resetTv;
    @ViewInject(id = R.id.spread_line_chart)
    LineChart mLineChart;
    @ViewInject(id = R.id.chart_lable_lin)
    LinearLayout chart_lable_layout;
    @ViewInject(id = R.id.chart_lable0)
    TextView lable0;
    @ViewInject(id = R.id.chart_lable_text0)
    TextView lableTv0;
    @ViewInject(id = R.id.chart_lable1)
    TextView lable1;
    @ViewInject(id = R.id.chart_lable_text1)
    TextView lableTv1;
    @ViewInject(id = R.id.chart_lable2)
    TextView lable2;
    @ViewInject(id = R.id.chart_lable_text2)
    TextView lableTv2;
    @ViewInject(id = R.id.chart_lable3)
    TextView lable3;
    @ViewInject(id = R.id.chart_lable_text3)
    TextView lableTv3;
    @ViewInject(id = R.id.chart_lable4)
    TextView lable4;
    @ViewInject(id = R.id.chart_lable_text4)
    TextView lableTv4;
    @ViewInject(id = R.id.recyclerview_lin)
    LinearLayout recyclerview_lin;
    @ViewInject(id = R.id.tv_dt)
    TextView tv_dt;
    @ViewInject(id = R.id.tv_type_1)
    TextView monthBt;
    @ViewInject(id = R.id.tv_type_2)
    TextView weekBt;
    @ViewInject(id = R.id.tv_type_3)
    TextView dayBt;
    @ViewInject(id = R.id.tv_type_4)
    TextView hourBt;
    @ViewInject(id = R.id.tv_left)
    TextView timePreviousBt;
    @ViewInject(id = R.id.tv_right)
    TextView timeNextBt;
    private int seletedType = 4;
    private LoadingDialog dialog;
    private Context context;
    private LineChartManager lineChartManager1;
    private AndroidTreeView treeView;
    private TreeNode root;
    private boolean B_FromHome = true;
    private String S_MpointID = "";
    private List<ScreenTreeBean> tree_list = new ArrayList<>();
    private Bundle b_savedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        b_savedInstanceState = savedInstanceState;
        Intent i = getIntent();
        S_MpointID = i.getStringExtra("MPOINT");
        B_FromHome = i.getBooleanExtra("FromHome", true);

        initView();

        if (B_FromHome) {
            initData(savedInstanceState);
        } else {
            showOrDismissListBt.setVisibility(View.GONE);
            recyclerview_lin.setVisibility(View.GONE);
            getMoreLineData(S_MpointID);
        }
    }

    //初始化控件
    private void initView() {
        chart_lable_layout.setVisibility(View.GONE);
        tv_dt.setText(MyUtils.getLastStrDataBYHour(0));
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        tv_dt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseTime(1);
            }
        });
        tv_quert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (B_FromHome) {
                    getMoreLineData(getSeletedTreeIdsStr());
                } else {
                    getMoreLineData(S_MpointID);
                }
            }
        });

        //重置
        resetTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLineChart.clear();
                initData(b_savedInstanceState);
            }
        });

        //显示或者隐藏列表
        showOrDismissListBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recyclerview_lin.getVisibility() == View.GONE) {
                    recyclerview_lin.setVisibility(View.VISIBLE);
                    showOrDismissListBt.setText(R.string.dismiss_list);
                } else {
                    recyclerview_lin.setVisibility(View.GONE);
                    showOrDismissListBt.setText(R.string.show_list);
                }
            }
        });
        //月
        monthBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seletedType = 1;
                monthBt.setBackgroundResource(R.drawable.bt_witle_01);
                monthBt.setTextColor(ContextCompat.getColor(context, R.color.app_title_color));
                weekBt.setBackgroundResource(R.drawable.bt_tm_02);
                weekBt.setTextColor(ContextCompat.getColor(context, R.color.white));
                dayBt.setBackgroundResource(R.drawable.bt_tm_02);
                dayBt.setTextColor(ContextCompat.getColor(context, R.color.white));
                hourBt.setBackgroundResource(R.drawable.bt_tm_02);
                hourBt.setTextColor(ContextCompat.getColor(context, R.color.white));
                if (B_FromHome) {
                    getMoreLineData(getSeletedTreeIdsStr());
                } else {
                    getMoreLineData(S_MpointID);
                }
            }
        });

        //周
        weekBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seletedType = 2;
                monthBt.setBackgroundResource(R.drawable.bt_tm_02);
                monthBt.setTextColor(ContextCompat.getColor(context, R.color.white));
                weekBt.setBackgroundResource(R.drawable.bt_witle_01);
                weekBt.setTextColor(ContextCompat.getColor(context, R.color.app_title_color));
                dayBt.setBackgroundResource(R.drawable.bt_tm_02);
                dayBt.setTextColor(ContextCompat.getColor(context, R.color.white));
                hourBt.setBackgroundResource(R.drawable.bt_tm_02);
                hourBt.setTextColor(ContextCompat.getColor(context, R.color.white));

                if (B_FromHome) {
                    getMoreLineData(getSeletedTreeIdsStr());
                } else {
                    getMoreLineData(S_MpointID);
                }
            }
        });

        dayBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seletedType = 3;
                monthBt.setBackgroundResource(R.drawable.bt_tm_02);
                monthBt.setTextColor(ContextCompat.getColor(context, R.color.white));
                weekBt.setBackgroundResource(R.drawable.bt_tm_02);
                weekBt.setTextColor(ContextCompat.getColor(context, R.color.white));
                dayBt.setBackgroundResource(R.drawable.bt_witle_01);
                dayBt.setTextColor(ContextCompat.getColor(context, R.color.app_title_color));
                hourBt.setBackgroundResource(R.drawable.bt_tm_02);
                hourBt.setTextColor(ContextCompat.getColor(context, R.color.white));
                if (B_FromHome) {
                    getMoreLineData(getSeletedTreeIdsStr());
                } else {
                    getMoreLineData(S_MpointID);
                }
            }
        });

        hourBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seletedType = 4;
                monthBt.setBackgroundResource(R.drawable.bt_tm_02);
                monthBt.setTextColor(ContextCompat.getColor(context, R.color.white));
                weekBt.setBackgroundResource(R.drawable.bt_tm_02);
                weekBt.setTextColor(ContextCompat.getColor(context, R.color.white));
                dayBt.setBackgroundResource(R.drawable.bt_tm_02);
                dayBt.setTextColor(ContextCompat.getColor(context, R.color.white));
                hourBt.setBackgroundResource(R.drawable.bt_witle_01);
                hourBt.setTextColor(ContextCompat.getColor(context, R.color.app_title_color));
                if (B_FromHome) {
                    getMoreLineData(getSeletedTreeIdsStr());
                } else {
                    getMoreLineData(S_MpointID);
                }
            }
        });

        //日期 <-
        timePreviousBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //月 日 周 时 ---->   各减1
                String dt = TimeUtils.getPreviousTime(tv_dt.getText().toString(), seletedType);
                tv_dt.setText(dt);

                if (B_FromHome) {
                    getMoreLineData(getSeletedTreeIdsStr());
                } else {
                    getMoreLineData(S_MpointID);
                }
            }
        });

        //月 日 周 时 ----> 各加1
        timeNextBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //月 日 周 时 ---->  各加1
                String dt = TimeUtils.getNextTime(tv_dt.getText().toString(), seletedType);
                tv_dt.setText(dt);
                
                if (B_FromHome) {
                    getMoreLineData(getSeletedTreeIdsStr());
                } else {
                    getMoreLineData(S_MpointID);
                }
            }
        });
    }

    //主界面进来,初始化数据
    private void initData(final Bundle savedInstanceState) {
        dialog = new LoadingDialog(context);
        dialog.show();
        String url = ApiConstant.host + UrlConst.Api_GetDataAnalysisTree;
        DataModel.requestGETMode("ScreenTreeBean", url, new InitDataBeanBack<ScreenTreeBean>() {
            @Override
            public void callbak(boolean isSuccess, List<ScreenTreeBean> list, String msg) {
                if (isSuccess) {
                    //设置图标tree
                    tree_list = list;
                    if (TextUtil.isEmptyList(list) && mContext == null) {
                        ToastUtil.show(R.string.no_data);
                        return;
                    }
                    setTree(savedInstanceState, tree_list);
                } else {
                    ToastUtil.show(R.string.request_failure);
                }
                dialog.close();
            }
        });
    }


    //获取勾选的测点数据Ids
    private String getSeletedTreeIdsStr() {
        if (treeView == null) {
            return "";
        }
        //选中的tree节点 数据
        List<TreeNode> treeNodeSeletedList = treeView.getSelected();
        if (treeNodeSeletedList.size() == 0) {
            ToastShow("请至少勾选一个测点");
            return "";
        } else if (TextUtil.isEmpty(tv_dt.getText() + "")) {
            ToastShow("时间不能为空");
            return "";
        } else {
            String points = "";
            for (int i = 0; i < treeNodeSeletedList.size(); i++) {
                TreeNode model = treeNodeSeletedList.get(i);
                if (i == treeNodeSeletedList.size() - 1) {
                    points += model.getTurl();
                } else {
                    points += model.getTurl() + ",";
                }
            }

            if (points.split(",").length > 5) {
                ToastShow("最多只能选择5条曲线");
                return "";
            } else {
                return points;
            }
        }
    }

    //测点列表tree
    private void setTree(final Bundle savedInstanceState, List<ScreenTreeBean> list) {
        ViewGroup treeLayout = findViewById(R.id.container);
        treeLayout.removeAllViews();

        root = TreeNode.root();
        FindChiledNodeTree(root, list);

        treeView = new AndroidTreeView(context, root);
        treeView.setDefaultAnimation(true);

        //添加
        treeLayout.addView(treeView.getView());
        treeView.setSelectionModeEnabled(true);

        if (savedInstanceState != null) {
            String state = savedInstanceState.getString("tState");
            if (!android.text.TextUtils.isEmpty(state)) {
                treeView.restoreState(state);
            }
        }
    }

    //解析管线图层树
    private void FindChiledNodeTree(TreeNode folder, List<ScreenTreeBean> list) {
        TreeNode currentNode = folder;
        for (int a = 0; a < list.size(); a++) {
            //获取一级节点的子代
            List<ScreenTreeBean> bbb = list.get(a).getChildNodes();
            //当节点没有子节点时，不显示。
            if (bbb == null || bbb.size() <= 0) {
                continue;
            }
            //定义一级节点样式
            TreeNode file = new TreeNode(new IconTreeItemHolder.IconTreeItem(com.haocang.commonlib.R.string.ic_folder, list.get(a)
                    .getText()), list.get(a).getId(), list.get(a).getValue()).setViewHolder(new ProfileHolder(context));
            file.setExpanded(true);
            file.setTParent(true);
            for (int b = 0; b < bbb.size(); b++) {
                List<ScreenTreeBean> ccc = bbb.get(b).getChildNodes();
                TreeNode file1;
                if (ccc != null && ccc.size() > 0) {
                    file1 = new TreeNode(new IconTreeItemHolder.IconTreeItem(com.haocang.commonlib.R.string.ic_folder, bbb.get(b)
                            .getText()), bbb.get(b).getId(), bbb.get(b).getValue()).setViewHolder(new MyItemTwo1Holder(context));
                } else {
                    file1 = new TreeNode(new IconTreeItemHolder.IconTreeItem(com.haocang.commonlib.R.string.ic_folder, bbb.get(b)
                            .getText()), bbb.get(b).getId(), bbb.get(b).getValue()).setViewHolder(new MyItemTwo2Holder(context));
                }

                file1.setTParent(true);

                if (ccc != null && ccc.size() > 0) {
                    for (int c = 0; c < ccc.size(); c++) {
                        List<ScreenTreeBean> ddd = ccc.get(c).getChildNodes();
                        TreeNode file2;
                        if (ddd != null && ddd.size() > 0) {
                            file2 = new TreeNode(new IconTreeItemHolder.IconTreeItem(com.haocang.commonlib.R.string.ic_folder, ccc.get(c)
                                    .getText()), ccc.get(c).getId(), ccc.get(c).getValue()).setViewHolder(new MyItemTwo1Holder(context));
                            file2.setTParent(true);
                        } else {
                            file2 = new TreeNode(new IconTreeItemHolder.IconTreeItem(com.haocang.commonlib.R.string.ic_folder, ccc.get(c)
                                    .getText()), ccc.get(c).getId(), ccc.get(c).getValue()).setViewHolder(new MyItemThree1Holder(context));
                            file2.setTParent(false);
                        }
                        if (ddd != null && ddd.size() > 0) {
                            for (int d = 0; d < ddd.size(); d++) {
                                TreeNode file3 = new TreeNode(new IconTreeItemHolder.IconTreeItem(com.haocang.commonlib.R.string.ic_folder, ddd.get(d)
                                        .getText()), ddd.get(d).getId(), ddd.get(d).getValue()).setViewHolder(new SelectableHeader3Holder(context));
                                file2.addChild(file3);
                                file3.setTParent(false);
                            }
                        }
                        file1.addChild(file2);
                    }
                }
                file.addChild(file1);
            }
            currentNode.addChild(file);
        }
    }

    //获取曲线数据
    private void getMoreLineData(String treeSeletedIdsStr) {
        if (TextUtil.isEmpty(treeSeletedIdsStr)) {
            return;
        }
        dialog = new LoadingDialog(context);
        dialog.show();

        String lastdate = tv_dt.getText().toString();
        if (seletedType != 4) {
            tv_dt.setText(TextUtils.formatDateToMD(lastdate, "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd") + " 00:00:00");
        }

        lastdate = tv_dt.getText().toString();

        //获取前一个时间
        String nowdate = TimeUtils.getPreviousTime(lastdate, seletedType);

        String url = ApiConstant.host + UrlConst.Api_GetDataAnalysisHisData + "mpoints=" + treeSeletedIdsStr + "&startdt=" + nowdate + "&enddt=" + lastdate;
        url = url.replace(" ", "%20");

        DataModel.requestGETMode("MpointCurveBean", url, new InitDataBeanBack<MpointCurveBean>() {
            @Override
            public void callbak(boolean b, List<MpointCurveBean> data_list, String msg) {
                if (b) {
                    if (data_list == null || data_list.size() == 0) {
                        mLineChart.clear();
                        Toast.makeText(context, "当前数据为空！", Toast.LENGTH_SHORT).show();
                        dialog.close();
                        return;
                    }
                    showLine(data_list);
                } else {
                    ToastShow(msg);
                }
                dialog.close();
            }
        });
    }

    //显示多条曲线
    private void showLine(List<MpointCurveBean> dataList) {
        mLineChart.clear();
        lineChartManager1 = new LineChartManager(mLineChart);
        int[] color = new int[]{
                getResources().getColor(R.color.data_chat_1),
                getResources().getColor(R.color.data_chat_2),
                getResources().getColor(R.color.data_chat_3),
                getResources().getColor(R.color.data_chat_4),
                getResources().getColor(R.color.data_chat_5)
        };

        //根据数据dataList找出最大值和最小值
        float li_min = 0;
        float li_max = 0;

        for (int i = 0; i < dataList.size(); i++) {
            List<CompositeIndexBean> his_datalist = dataList.get(i).getHisDataList();
            //根据数据dataList找出最大值和最小值
            float min = 0;
            float max = 0;
            if (his_datalist != null && his_datalist.size() > 0) {
                min = (float) his_datalist.get(0).getValue();
                max = (float) his_datalist.get(0).getValue();
            }
            for (int K = 0; K < his_datalist.size(); K++) {
                if (min > his_datalist.get(K).getValue()) {
                    min = (float) his_datalist.get(K).getValue();
                }
                if (max < his_datalist.get(K).getValue()) {
                    max = (float) his_datalist.get(K).getValue();
                }
            }
            if (max > li_max) {
                li_max = max;
            }
            if (min < li_min) {
                li_min = min;
            }
            String ls_name = MyTextUtils.getString(dataList.get(i).getMpointName());
            int li_color = color[i];

            if (i == 0) {//添加模板，第一条数据
                lineChartManager1.showLineChartY(his_datalist, ls_name, li_color, li_min, li_max);
            } else {
                lineChartManager1.addLineY(his_datalist, ls_name, li_color, li_min, li_max);
            }

        }
        showLable(dataList);
        lineChartManager1.setMarkerView(context);
    }

    //显示底部曲线类型标识
    private void showLable(List<MpointCurveBean> dataList) {
        chart_lable_layout.setVisibility(View.VISIBLE);

        lable0.setVisibility(View.GONE);
        lableTv0.setVisibility(View.GONE);
        lable1.setVisibility(View.GONE);
        lableTv1.setVisibility(View.GONE);
        lable2.setVisibility(View.GONE);
        lableTv2.setVisibility(View.GONE);
        lable3.setVisibility(View.GONE);
        lableTv3.setVisibility(View.GONE);
        lable4.setVisibility(View.GONE);
        lableTv4.setVisibility(View.GONE);
        switch (dataList.size()) {
            case 1:
                lableTv0.setText(MyTextUtils.getString(dataList.get(0).getMpointName()));
                lable0.setVisibility(View.VISIBLE);
                lableTv0.setVisibility(View.VISIBLE);
                break;
            case 2:
                lableTv0.setText(MyTextUtils.getString(dataList.get(0).getMpointName()));
                lableTv1.setText(MyTextUtils.getString(dataList.get(1).getMpointName()));
                lable0.setVisibility(View.VISIBLE);
                lableTv0.setVisibility(View.VISIBLE);
                lable1.setVisibility(View.VISIBLE);
                lableTv1.setVisibility(View.VISIBLE);
                break;
            case 3:
                lableTv0.setText(MyTextUtils.getString(dataList.get(0).getMpointName()));
                lableTv1.setText(MyTextUtils.getString(dataList.get(1).getMpointName()));
                lableTv2.setText(MyTextUtils.getString(dataList.get(2).getMpointName()));
                lable0.setVisibility(View.VISIBLE);
                lableTv0.setVisibility(View.VISIBLE);
                lable1.setVisibility(View.VISIBLE);
                lableTv1.setVisibility(View.VISIBLE);
                lable2.setVisibility(View.VISIBLE);
                lableTv2.setVisibility(View.VISIBLE);
                break;
            case 4:
                lableTv0.setText(MyTextUtils.getString(dataList.get(0).getMpointName()));
                lableTv1.setText(MyTextUtils.getString(dataList.get(1).getMpointName()));
                lableTv2.setText(MyTextUtils.getString(dataList.get(2).getMpointName()));
                lableTv3.setText(MyTextUtils.getString(dataList.get(3).getMpointName()));
                lable0.setVisibility(View.VISIBLE);
                lableTv0.setVisibility(View.VISIBLE);
                lable1.setVisibility(View.VISIBLE);
                lableTv1.setVisibility(View.VISIBLE);
                lable2.setVisibility(View.VISIBLE);
                lableTv2.setVisibility(View.VISIBLE);
                lable3.setVisibility(View.VISIBLE);
                lableTv3.setVisibility(View.VISIBLE);
                break;
            case 5:
                lableTv0.setText(MyTextUtils.getString(dataList.get(0).getMpointName()));
                lableTv1.setText(MyTextUtils.getString(dataList.get(1).getMpointName()));
                lableTv2.setText(MyTextUtils.getString(dataList.get(2).getMpointName()));
                lableTv3.setText(MyTextUtils.getString(dataList.get(3).getMpointName()));
                lableTv4.setText(MyTextUtils.getString(dataList.get(4).getMpointName()));

                lableTv0.setVisibility(View.VISIBLE);
                lableTv1.setVisibility(View.VISIBLE);
                lableTv2.setVisibility(View.VISIBLE);
                lableTv3.setVisibility(View.VISIBLE);
                lableTv4.setVisibility(View.VISIBLE);

                lable0.setVisibility(View.VISIBLE);
                lable1.setVisibility(View.VISIBLE);
                lable2.setVisibility(View.VISIBLE);
                lable3.setVisibility(View.VISIBLE);
                lable4.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }


    }

    private void chooseTime(final int position) {
        hideKeyBoard();
        TimePickerView.Builder builder = new TimePickerView.Builder(mContext, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                String time = TextUtils.date2String(date, simpleDateFormat);
                switch (position) {

                    case 1:
                        tv_dt.setText(time);
                        if (B_FromHome) {
                            String mps = getSeletedTreeIdsStr();
                            if (!mps.equals("")) {
                                getMoreLineData(getSeletedTreeIdsStr());
                            }
                        } else {
                            getMoreLineData(S_MpointID);
                        }
                        break;
                }

            }
        });
        builder.setType(new boolean[]{true, true, true, true, true, true});
        TimePickerView pvTime = builder.build();
        pvTime.setDate(Calendar.getInstance());
        pvTime.show();
    }

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
    protected int getLayoutResourceId() {
        return R.layout.activity_data_analysis;
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
        return "数据曲线";
    }
}
