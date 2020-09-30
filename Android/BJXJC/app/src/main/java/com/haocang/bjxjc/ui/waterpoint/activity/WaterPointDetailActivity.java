package com.haocang.bjxjc.ui.waterpoint.activity;

import android.content.Intent;
import android.net.ParseException;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.haocang.bjxjc.App;
import com.haocang.bjxjc.R;
import com.haocang.bjxjc.activity.bean.FileIndexBean;
import com.haocang.bjxjc.activity.comm.CommMediaActivity;
import com.haocang.bjxjc.activity.tool.CacheActivityUtil;
import com.haocang.bjxjc.requestcenter.DataModel;
import com.haocang.bjxjc.utils.tools.Const;
import com.haocang.bjxjc.utils.tools.TextUtil;
import com.haocang.bjxjc.utils.tools.TimeUtils;
import com.haocang.commonlib.network.request.UploadUtil;
import com.haocang.commonlib.network.interfaces.InitDataOneBeanBack;
import com.haocang.bjxjc.ui.tools.activity.EditMemoActivity;
import com.haocang.bjxjc.ui.tools.activity.AttachmentListActivity;
import com.haocang.bjxjc.ui.tools.activity.MultiSelectDialog;
import com.haocang.bjxjc.ui.tools.enums.MultiSelectEnum;
import com.haocang.bjxjc.ui.waterpoint.bean.WaterPointBean;
import com.haocang.bjxjc.ui.waterpoint.utils.ComputeTools;
import com.haocang.bjxjc.utils.bean.ProvinceBean;
import com.haocang.bjxjc.utils.tools.ApiConstant;
import com.haocang.bjxjc.utils.tools.UrlConst;
import com.haocang.commonlib.loadingdialog.LoadingDialog;
import com.haocang.bjxjc.utils.tools.CacheData;
import com.haocang.bjxjc.utils.tools.MyUtils;
import com.haocang.bjxjc.utils.tools.MyTextUtils;
import com.haocang.bjxjc.utils.tools.OptionsItemsUtils;
import com.haocang.commonlib.otherutil.TextUtils;

import net.tsz.afinal.annotation.view.ViewInject;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

//积水监测详情
public class WaterPointDetailActivity extends CommMediaActivity {
    @ViewInject(id = R.id.ll_more_process)
    LinearLayout processHistoryLayout;
    @ViewInject(id = R.id.water_loc_et)
    EditText waterLocEt;
    @ViewInject(id = R.id.tv_seeper_area)
    TextView waterAreaTv;
    @ViewInject(id = R.id.tv_SeeperDepth)
    TextView waterDepthTv;
    @ViewInject(id = R.id.seeper_depth)
    SeekBar sb_SeeperDepth;
    @ViewInject(id = R.id.sb_SeeperArea)
    SeekBar sb_SeeperArea;
    @ViewInject(id = R.id.tv_StartWaterDate)
    TextView waterStartTimeTv;
    @ViewInject(id = R.id.water_reason)
    TextView waterReasonTv;
    @ViewInject(id = R.id.warm_detail_desc_tv)
    TextView warmDescTv;
    @ViewInject(id = R.id.tv_SeeperLevel)
    TextView tv_SeeperLevel;
    @ViewInject(id = R.id.belong_warm_tv)
    TextView belongWarmTv;
    @ViewInject(id = R.id.dispose_person_tv)
    TextView disposePersonTv;
    @ViewInject(id = R.id.dispose_date_tv)
    TextView disposeDateTv;
    @ViewInject(id = R.id.dispose_measures_tv)
    TextView disposeMeasuresTv;
    @ViewInject(id = R.id.dispose_team_tv)
    TextView disposeTeamTv;
    @ViewInject(id = R.id.report_center_Tv)
    Button reportCenterTv;
    @ViewInject(id = R.id.cb_is_urgent_handle)
    CheckBox ckIsUrgentHandle;//是否紧急处理
    @ViewInject(id = R.id.ck_1)
    CheckBox ck_1;
    @ViewInject(id = R.id.ck_2)
    CheckBox ck_2;
    @ViewInject(id = R.id.ck_3)
    CheckBox ck_3;
    @ViewInject(id = R.id.ck_tv_0)
    TextView ck_tv_0;
    @ViewInject(id = R.id.ck_tv_1)
    TextView ck_tv_1;
    @ViewInject(id = R.id.ck_tv_2)
    TextView ck_tv_2;
    @ViewInject(id = R.id.ck_4)
    CheckBox ck_4;
    @ViewInject(id = R.id.ck_status_0)
    CheckBox disposeStatusCK0;
    @ViewInject(id = R.id.ck_status_1)
    CheckBox disposeStatusCK1;
    @ViewInject(id = R.id.ck_status_2)
    CheckBox disposeStatusCK2;
    @ViewInject(id = R.id.detail_submit_bt)
    Button submitBt;
    @ViewInject(id = R.id.layout_attachment)
    LinearLayout attachLayout;
    @ViewInject(id = R.id.ck_file_tv_1)
    TextView ck_file_tv_1;
    @ViewInject(id = R.id.ck_file_tv_2)
    TextView ck_file_tv_2;
    @ViewInject(id = R.id.ck_file_tv_3)
    TextView ck_file_tv_3;
    @ViewInject(id = R.id.ck_file_tv_4)
    TextView ck_file_tv_4;
    @ViewInject(id = R.id.iv_more_icon)
    ImageView iv_more;
    private WaterPointDetailActivity context;
    protected double SingleTap_X = 0, SingleTap_Y = 0;
    private LoadingDialog dialog;
    private WaterPointBean model;
    private String ls_ArrangementID = "", ls_DisposingPersonID = "", ls_DisposingPersonName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        CacheActivityUtil.addActivity(context);
        initView();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CacheActivityUtil.finishSingleActivity(context);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_water_point_detail;
    }

    @Override
    protected boolean showNavigation() {
        return true;
    }

    @Override
    protected boolean showMedia() {
        return true;
    }

    @Override
    protected int showMoreIcon() {
        return R.drawable.icon_fujian_01;
    }

    @Override
    protected String showTitleName() {
        return "积水点信息";
    }


    private void initView() {
        //处理流程图控件
        processHistoryLayout.setVisibility(View.VISIBLE);
        processHistoryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WaterFlowActivity.class);
                intent.putExtra("ID", model.getID());
                startActivity(intent);
            }
        });
        backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        //提交
        submitBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postSubmit();
            }
        });
        //上报控制中心
        reportCenterTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(context, EditMemoActivity.class);
                startActivityForResult(it, 112);
            }
        });

        //积水开始时间
        waterStartTimeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseTime(1);
            }
        });

        //处置人
        disposePersonTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(context, MultiSelectDialog.class);
                it.putExtra("ID", model.getArrangementID());
                it.putExtra("DECIDE", "ID");
                it.putExtra("TYPE", MultiSelectEnum.USER);
                it.putExtra("SELECTED", ls_DisposingPersonID);
                it.putExtra("IsAlone", false);
                startActivityForResult(it, 101);
            }
        });

        //警情描述
        warmDescTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(context, MultiSelectDialog.class);
                it.putExtra("ID", "");
                it.putExtra("DECIDE", "Name");
                it.putExtra("TYPE", MultiSelectEnum.WaterMemo);
                it.putExtra("SELECTED", warmDescTv.getText().toString());
                it.putExtra("IsAlone", false);
                it.putExtra("ManuaInput", true);
                startActivityForResult(it, 105);
            }
        });
        //处置措施
        disposeMeasuresTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(context, MultiSelectDialog.class);
                it.putExtra("ID", "");
                it.putExtra("DECIDE", "Name");
                it.putExtra("TYPE", MultiSelectEnum.WaterResult);
                it.putExtra("SELECTED", disposeMeasuresTv.getText().toString());
                it.putExtra("IsAlone", false);
                it.putExtra("ManuaInput", true);
                startActivityForResult(it, 102);
            }
        });

        //积水原因
        waterReasonTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(context, MultiSelectDialog.class);
                it.putExtra("ID", "");
                it.putExtra("DECIDE", "Name");
                it.putExtra("TYPE", MultiSelectEnum.SeeperReason);
                it.putExtra("SELECTED", waterReasonTv.getText().toString());
                it.putExtra("IsAlone", false);
                it.putExtra("ManuaInput", true);
                startActivityForResult(it, 103);
            }
        });

        //处置时间
        disposeDateTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseTime(2);
            }
        });

        //所属预警列表
        belongWarmTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseType(OptionsItemsUtils.getWarmList(), 6);
            }
        });

        //处置队伍
        disposeTeamTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(context, MultiSelectDialog.class);
                it.putExtra("ID", "");
                it.putExtra("DECIDE", "ID");
                it.putExtra("TYPE", MultiSelectEnum.Arrangement);
                it.putExtra("SELECTED", ls_ArrangementID);
                it.putExtra("IsAlone", true);
                startActivityForResult(it, 1);
            }
        });
        fileStatusCKList = new ArrayList<>(Arrays.asList(ck_2, ck_3, ck_4, ck_1));
        fileStatusTvList = new ArrayList<>(Arrays.asList(ck_file_tv_2, ck_file_tv_3, ck_file_tv_4
                , ck_file_tv_1));
        disposeStatusTvList = new ArrayList<>(Arrays.asList(ck_tv_0, ck_tv_1, ck_tv_2));
        disposeStatusCKList = new ArrayList<>(Arrays.asList(disposeStatusCK0, disposeStatusCK1, disposeStatusCK2));
        tvList = new ArrayList<>(Arrays.asList(waterStartTimeTv, disposePersonTv, warmDescTv,
                disposeMeasuresTv, disposeDateTv, waterReasonTv, belongWarmTv, disposeTeamTv));

        ck_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFileStatus(3);
            }
        });
        ck_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFileStatus(0);
            }
        });
        ck_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFileStatus(1);
            }
        });
        ck_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFileStatus(2);
            }
        });

        disposeStatusCK0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDisposeStatus(0);
                setFileStatus(0);
                disposeDateTv.setText("");

            }
        });
        disposeStatusCK1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDisposeStatus(1);
                setFileStatus(1);
                disposeDateTv.setText("");
            }
        });
        disposeStatusCK2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDisposeStatus(2);
                setFileStatus(2);
                disposeDateTv.setText(MyUtils.getNowDatatoString());
                disposePersonTv.setText(CacheData.UserName());
                ls_DisposingPersonName = CacheData.UserName();
                ls_DisposingPersonID = CacheData.UserID();
                if (disposeMeasuresTv.getText().toString().trim().equals("")) {
                    disposeMeasuresTv.setText("清疏雨水箅加快排水");
                }
            }
        });

        sb_SeeperDepth.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                waterDepthTv.setText(Integer.toString(progress));
                ComputeTools.ComputeWaterLevel(tv_SeeperLevel, sb_SeeperDepth.getProgress(), sb_SeeperArea.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.e("------------", "开始滑动！");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int max = sb_SeeperDepth.getMax();
                if (seekBar.getProgress() > (max * 9 / 10)) {
                    sb_SeeperDepth.setMax(max * 2);
                }
                if (seekBar.getProgress() < (max / 10)) {
                    sb_SeeperDepth.setMax(max / 2);
                }
            }
        });

        sb_SeeperArea.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                waterAreaTv.setText(Integer.toString(progress));
                ComputeTools.ComputeWaterLevel(tv_SeeperLevel, sb_SeeperDepth.getProgress(), sb_SeeperArea.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.e("------------", "开始滑动！");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int max = sb_SeeperArea.getMax();
                if (seekBar.getProgress() > (max * 9 / 10)) {
                    sb_SeeperArea.setMax(max * 2);
                }
                if (seekBar.getProgress() < (max / 10)) {
                    sb_SeeperArea.setMax(max / 2);
                }
            }
        });

        //附件列表
        iv_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AttachmentListActivity.class);
                intent.putExtra("BILLID", model.getID());
                startActivity(intent);
            }
        });

    }

    //加载页面数据
    private void initData() {
        dialog = new LoadingDialog(context, "加载中");
        dialog.show();
        String url = ApiConstant.host
                + UrlConst.Api_WaterPointDetail
                + CacheData.mWaterPointID;

        DataModel.requestGETOneMode("WaterPointBean", url, new InitDataOneBeanBack<WaterPointBean>() {

            @Override
            public void callbak(boolean isSuccess, WaterPointBean data, String msg) {
                if (isSuccess) {
                    model = data;
                    if (data == null) {
                        return;
                    }
                    int status = TextUtil.isEmpty(model.getStatus()) ? 0 : Integer.parseInt(model.getStatus());

                    if (2 == status || CacheData.IsFloodUser.equals("0")) {
                        fileLayout.setVisibility(View.GONE);
                        //已处置积水点/非当前防汛人员,不可以编辑
                        setViewUnable(status);
                    } else {
                        attachLayout.setVisibility(View.VISIBLE);
                        submitBt.setVisibility(View.VISIBLE);
                        reportCenterTv.setVisibility(View.VISIBLE);

                        //设置处置状态
                        setDisposeStatus(status);
                        setFileStatus(status);
                    }
                    SingleTap_X = model.getX();
                    SingleTap_Y = model.getY();
                    waterLocEt.setText(model.getLocation());
                    if (model.getSeeperDepth() != null && !model.getSeeperDepth().equals("")) {
                        sb_SeeperDepth.setProgress(Integer.parseInt(model.getSeeperDepth()));
                        waterDepthTv.setText(model.getSeeperDepth());
                    } else {
                        sb_SeeperDepth.setProgress(0);
                        waterDepthTv.setText("0");
                    }
                    if (model.getSeeperArea() != null && !model.getSeeperArea().equals("")) {
                        sb_SeeperArea.setProgress(Integer.parseInt(model.getSeeperArea()));
                        waterAreaTv.setText(model.getSeeperArea());
                    } else {
                        sb_SeeperArea.setProgress(0);
                        waterAreaTv.setText("0");
                    }
                    waterStartTimeTv.setText(model.getStartWaterDate());
                    tv_SeeperLevel.setText(MyTextUtils.GetOpitemValue(OptionsItemsUtils.GetWaterPointSeeperLevel(), 1, model.getSeeperLevel()));
                    waterReasonTv.setText(model.getSeeperReason());
                    warmDescTv.setText(model.getMemo());
                    belongWarmTv.setText(MyTextUtils.GetOpitemValue(OptionsItemsUtils.getWarmList(), 1, model.getWarningID()));
                    disposePersonTv.setText(model.getDisposingPersonName());
                    disposeDateTv.setText(model.getEndWaterDate());


                    disposeMeasuresTv.setText(model.getWaterResult());
                    ckIsUrgentHandle.setChecked(model.getIsUrgentHandle() == 0 ? false : true);
                    ls_ArrangementID = model.getArrangementID() == null ? "" : model.getArrangementID();
                    ls_DisposingPersonID = model.getDisposingPerson() == null ? "" : model.getDisposingPerson();
                    disposeTeamTv.setText(MyTextUtils.GetOpitemValue(OptionsItemsUtils.GetArrangementBaseInfoList(), 1, model.getArrangementID()));

                } else {
                    ToastShow("数据加载出错：" + msg);
                    finish();
                }
                dialog.close();

            }
        });
    }
    private ArrayList<TextView> tvList, disposeStatusTvList, fileStatusTvList;
    private List<CheckBox> disposeStatusCKList, fileStatusCKList;

    //获取附件的状态
    private String getFileStatus() {
        if (ck_1.isChecked()) {
            return "3";
        } else if (ck_2.isChecked()) {
            return "0";
        } else if (ck_3.isChecked()) {
            return "1";
        } else if (ck_4.isChecked()) {
            return "2";
        }
        return "0";
    }

    //提交工单
    private void postSubmit() {
        String Location = waterLocEt.getText().toString();
        String SeeperDepth = waterDepthTv.getText().toString();
        String SeeperArea = waterAreaTv.getText().toString();
        String SeeperReason = waterReasonTv.getText().toString();
        String Memo = warmDescTv.getText().toString().trim();
        String StartWaterDate = waterStartTimeTv.getText().toString();
        String Status = getAttachmentStatus() + "";
        String EndWaterDate = disposeDateTv.getText().toString();
        String WaterResult = disposeMeasuresTv.getText().toString();
        String WarningID = MyTextUtils.GetOpitemValue(OptionsItemsUtils.getWarmList(), 0, belongWarmTv.getText().toString());
        String SeeperLevel = MyTextUtils.GetOpitemValue(OptionsItemsUtils.GetWaterPointSeeperLevel(), 0, tv_SeeperLevel.getText().toString());
        ls_DisposingPersonName = disposePersonTv.getText().toString();

        String FileStatus = getFileStatus();
        if (MyTextUtils.IsNull(WarningID)) {
            ToastShow("所属预警不能为空");
            return;
        }
        if (MyTextUtils.IsNull(Location)) {
            ToastShow("积水位置不能为空");
            return;
        }

        if (MyTextUtils.IsNull(SeeperReason)) {
            ToastShow("积水原因不能为空");
            return;
        }

        if (!MyTextUtils.IsNull(EndWaterDate) && !TextUtils.CompareStrData(StartWaterDate, EndWaterDate)) {
            ToastShow("处置时间不能小于积水起时！");
            return;
        }

        try {
            JSONObject maps = new JSONObject();
            if (CacheData.WaterPoint_UPDATE) {//修改
                maps.put("ID", model.getID());
            } else { //新增
                maps.put("CreateDT", MyTextUtils.getNowDatatoString());
                maps.put("Creator", CacheData.UserID());//上报人编号
                maps.put("IsDelete", "S");
            }

            maps.put("Location", Location);
            maps.put("SeeperDepth", SeeperDepth);
            maps.put("SeeperArea", SeeperArea);
            maps.put("Memo", Memo);
            maps.put("StartWaterDate", MyTextUtils.IsNull(StartWaterDate) ? null : StartWaterDate);
            maps.put("SeeperReason", SeeperReason);
            maps.put("Status", Status);
            maps.put("DisposingPersonName", ls_DisposingPersonName);//处置人名称
            maps.put("DisposingPerson", ls_DisposingPersonID);//处置人编号
            maps.put("EndWaterDate", MyTextUtils.IsNull(EndWaterDate) ? null : EndWaterDate);
            maps.put("WaterResult", WaterResult);
            maps.put("WarningID", WarningID);
            maps.put("SeeperLevel", SeeperLevel);
            maps.put("ArrangementID", ls_ArrangementID);
            maps.put("IsUrgentHandle", ckIsUrgentHandle.isChecked() ? 1 : 0);
            maps.put("X", SingleTap_X);
            maps.put("Y", SingleTap_Y);

            //计算出积水历时，
            if (!MyTextUtils.IsNull(StartWaterDate) && !MyTextUtils.IsNull(EndWaterDate)) {
                Date date_s = TimeUtils.strToDateLong(StartWaterDate);
                Date date_e = TimeUtils.strToDateLong(EndWaterDate);
                try {
                    long min = (date_e.getTime() - date_s.getTime()) / (60 * 1000);
                    //ToastShow("相隔的分钟="+min);
                    maps.put("SeeperTime", min);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            if (Status.equals("2")) {
                maps.put("Msg", CacheData.UserName() + ":积水点处置完成。");
                maps.put("OperationType", "Submit");
            }

            selectFileStatus(maps, "保存成功", "保存失败！", true, FileStatus);

        } catch (JSONException e) {
            ToastShow("保存失败," + e.toString());
            e.printStackTrace();
        }
    }


    private void forwardSubmit(String Memo) {
        try {
            String FileStatus = getFileStatus();
            JSONObject maps = new JSONObject();
            maps.put("ID", model.getID());
            maps.put("Msg", CacheData.UserName() + ":" + model.getLocation() + "积水点无法处置,原因:" + Memo + "。");
            maps.put("OperationType", "Forward");
            maps.put("Status", "0");
            maps.put("DisposingPerson", "");
            maps.put("IsBack", 1);
            maps.put("DisposingPersonName", "");
            maps.put("ArrangementID", "");
            selectFileStatus(maps, "上报成功！", "上报控制室失败！", true, FileStatus);
        } catch (JSONException e) {
            ToastShow("保存失败");
            e.printStackTrace();
        }
    }

    private void selectFileStatus(final JSONObject maps, final String SuccessLog, final String ErrorLog, final boolean isExit, String FileStatus) {
        if (getPicFilelist().size() > 0) {
            if (FileStatus.equals("")) {
                ToastShow("请选择附件状态");
                return;
            } else {
                uploadBill(maps, SuccessLog, ErrorLog, isExit, FileStatus);
            }
        } else {
            uploadBill(maps, SuccessLog, ErrorLog, isExit, FileStatus);
        }
    }

    //保存工单和附件的提交方法
    private void uploadBill(JSONObject maps, final String SuccessLog, final String ErrorLog, final boolean isExit, final String FileStatus) {
        try {
            JSONObject param = new JSONObject();
            param.put("info", maps);
            param.put("filename", "WaterPoint");
            param.put("CreateUserId", CacheData.UserID());
            param.put("CreateUserName", CacheData.UserName());
            param.put("FileStatus", FileStatus);
            dialog = new LoadingDialog(context, "保存中");
            dialog.show();
            UploadUtil request = new UploadUtil(ApiConstant.host + UrlConst.fileUpload,
                    Const.file_p, getPicFilelist(), param,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject josnobject = new JSONObject(response);
                                if (josnobject.getBoolean("Result")) {

                                    dialog.close();
                                    ToastShow(SuccessLog);
                                    if (isExit) {
                                        finish();
                                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                    } else {
                                        //清空附件
                                        vedioFilelist.clear();
                                        picFilelist.clear();
                                        picFilelist.add(new FileIndexBean(true));
                                        picListAdapter.setNewData(picFilelist);
                                        videoCloseBt.setVisibility(View.GONE);
                                        videoAddBt.setImageResource(R.mipmap.ic_attachment_add);
                                        initData();
                                    }


                                } else {
                                    dialog.close();
                                    ToastShow(ErrorLog + josnobject.getString("Msg"));
                                }
                            } catch (JSONException e) {

                                dialog.close();
                                ToastShow(ErrorLog + e.toString());
                                e.printStackTrace();
                            }
                        }

                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dialog.close();
                    ToastShow(ErrorLog + ",请求超时！");
                    error.printStackTrace();
                }
            });
            App.requestQueue.add(request);
        } catch (JSONException e) {
            dialog.close();
            ToastShow(ErrorLog + e.toString());
            e.printStackTrace();
        }
    }
    //让界面不能操作
    private void setViewUnable(int status) {
        for (TextView tv : tvList) {
            tv.setOnClickListener(null);
            tv.setBackground(null);
        }
        waterAreaTv.setPadding(35, 0, 0, 0);
        waterDepthTv.setPadding(35, 0, 0, 0);
        sb_SeeperDepth.setVisibility(View.GONE);
        sb_SeeperArea.setVisibility(View.GONE);
        waterLocEt.setEnabled(false);
        ckIsUrgentHandle.setButtonDrawable(null);
        ckIsUrgentHandle.setText(ckIsUrgentHandle.isChecked() ? "是" : "否");

        //已处理//不是当前防汛人员--->都隐藏
        for (int i = 0; i < disposeStatusTvList.size(); i++) {
            //勾选框统一不显示,status对应的文字显示
            disposeStatusTvList.get(i).setVisibility( i==status ? View.VISIBLE : View.GONE);
            disposeStatusCKList.get(i).setVisibility(View.GONE);
        }
        for (int i = 0; i < fileStatusCKList.size(); i++) {
            fileStatusTvList.get(i).setVisibility( i==status ? View.VISIBLE : View.GONE);
            fileStatusCKList.get(i).setVisibility(View.GONE);
        }
    }

    //设置处置状态
    private void setDisposeStatus(int status) {
        int size = disposeStatusTvList.size();
        for (int i = 0; i < size; i++) {
            boolean isEqual = (status == i);
            disposeStatusCKList.get(i).setChecked(isEqual);
        }
    }

    //设置附件的状态
    private void setFileStatus(int status) {
        int size = fileStatusTvList.size();
        for (int i = 0; i < size; i++) {
            boolean isEqual = status == i;
            int v = isEqual ? View.VISIBLE : View.GONE;
            fileStatusCKList.get(i).setChecked(isEqual);
        }
    }

    //获取附件的状态
    private int getAttachmentStatus() {
        if (disposeStatusCK1.isChecked()) {
            return 1;
        } else if (disposeStatusCK2.isChecked()) {
            return 2;
        }
        return 0;
    }

    //选择时间
    private void chooseTime(final int position) {
        hideKeyBoard();
        TimePickerView.Builder builder = new TimePickerView.Builder(context, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                String time = MyTextUtils.date2String(date, simpleDateFormat);
                switch (position) {
                    case 1:
                        waterStartTimeTv.setText(time);
                        break;
                    case 2:
                        disposeDateTv.setText(time);
                        break;
                }

            }
        });
        builder.setType(new boolean[]{true, true, true, true, true, true});
        TimePickerView pvTime = builder.build();
        pvTime.setDate(Calendar.getInstance());
        pvTime.show();
    }

    private void chooseType(final List<ProvinceBean> items, final int pos) {
        hideKeyBoard();
        OptionsPickerView.Builder builder = new OptionsPickerView.Builder(context, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String itemName = items.get(options1).getPickerViewText();
                //String itemValue = items.get(options1).getDescription();
                switch (pos) {
                    case 4:
                        disposePersonTv.setText(itemName);
                        break;
                    case 6:
                        belongWarmTv.setText(itemName);
                        break;
                }
            }
        });
        OptionsPickerView<ProvinceBean> pickerView = new OptionsPickerView<>(builder);
        pickerView.setPicker(items);
        pickerView.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101 && resultCode == 100) {
            ls_DisposingPersonID = data.getStringExtra("ID");
            ls_DisposingPersonName = data.getStringExtra("Name");
            disposePersonTv.setText(ls_DisposingPersonName);
        }
        if (requestCode == 102 && resultCode == 100) {
            String w_name = data.getStringExtra("Name");
            disposeMeasuresTv.setText(w_name);
        }
        if (requestCode == 103 && resultCode == 100) {
            String w_name = data.getStringExtra("Name");
            waterReasonTv.setText(w_name);
        }

        if (requestCode == 105 && resultCode == 100) {
            String w_name = data.getStringExtra("Name");
            warmDescTv.setText(w_name);
        }

        if (requestCode == 112 && resultCode == 112) {
            String w_name = data.getStringExtra("Name");
            forwardSubmit(w_name);
        }

        if (requestCode == 1 && resultCode == 100) {
            String w_id = data.getStringExtra("ID");
            String w_name = data.getStringExtra("Name");
            ls_ArrangementID = w_id;
            disposeTeamTv.setText(w_name);
        }

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
