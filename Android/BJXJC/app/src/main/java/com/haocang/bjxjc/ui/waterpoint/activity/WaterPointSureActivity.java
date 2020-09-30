package com.haocang.bjxjc.ui.waterpoint.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.esri.core.geometry.Point;
import com.haocang.bjxjc.ui.tools.activity.AttachmentListActivity;
import com.haocang.bjxjc.utils.tools.Const;
import com.haocang.bjxjc.utils.tools.DialogUtils;
import com.haocang.commonlib.gismap.GisMapConfig;
import com.haocang.bjxjc.R;
import com.haocang.bjxjc.activity.comm.CommMapFinalActivity;
import com.haocang.bjxjc.activity.tool.CacheActivityUtil;
import com.haocang.bjxjc.requestcenter.DataModel;
import com.haocang.commonlib.network.request.UploadUtil;
import com.haocang.commonlib.network.interfaces.InitDataOneBeanBack;
import com.haocang.bjxjc.ui.waterpoint.bean.WaterPointBean;
import com.haocang.bjxjc.utils.tools.ApiConstant;
import com.haocang.bjxjc.utils.tools.UrlConst;
import com.haocang.commonlib.loadingdialog.LoadingDialog;
import com.haocang.bjxjc.utils.tools.CacheData;
import com.haocang.bjxjc.utils.tools.MyTextUtils;

import net.tsz.afinal.annotation.view.ViewInject;

import org.json.JSONException;
import org.json.JSONObject;

//积水确认
public class WaterPointSureActivity extends CommMapFinalActivity {
    private WaterPointSureActivity context;
    private LoadingDialog dialog;
    private WaterPointBean model;
    @ViewInject(id = R.id.water_loc_et)
    EditText locEt;
    @ViewInject(id = R.id.ck_1)
    CheckBox ck_1;
    @ViewInject(id = R.id.ck_2)
    CheckBox ck_2;
    @ViewInject(id = R.id.ck_3)
    CheckBox ck_3;
    @ViewInject(id = R.id.ck_4)
    CheckBox ck_4;
    @ViewInject(id = R.id.submit_js)
    Button submit_js;
    @ViewInject(id = R.id.submit_no_js)
    Button submit_no_js;
    @ViewInject(id = R.id.submit_lin)
    LinearLayout submitLayout;

    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        CacheActivityUtil.addActivity(WaterPointSureActivity.this);
        initView();
        initEvent();
        initData();
        initMyLocation();
    }

    private void initEvent() {
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
                Intent intent = new Intent(mContext, AttachmentListActivity.class);
                intent.putExtra("BILLID", model.getID());
                startActivity(intent);
            }
        });

        submit_js.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartSubmit();
            }
        });
        submit_no_js.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CancelSubmit();
            }
        });

    }

    private void initData() {
        String ID = getIntent().getStringExtra("ID");
        if (CacheData.IsFloodUser.equals("0")) {
            media.setVisibility(View.GONE);
            submitLayout.setVisibility(View.GONE);
            DialogUtils.showTipDialog(context, context.getString(R.string.not_cur_flood_people));
        }
        dialog = new LoadingDialog(context, "加载中");
        dialog.show();
        String ApiUrl = ApiConstant.host + UrlConst.Api_WaterPointDetail + ID;
        DataModel.requestGETOneMode("WaterPointBean", ApiUrl, new InitDataOneBeanBack<WaterPointBean>() {

            @Override
            public void callbak(boolean b, WaterPointBean data, String msg) {
                if (b) {
                    model = data;
                    if (model.getMX() != 0 && model.getMY() != 0) {
                        Point waterPoint = new Point(model.getMX(), model.getMY());
                        int icon_img;
                        if (model.getIsDelete().equals("Y")) {
                            icon_img = R.mipmap.jsd_gray;
                        } else {
                            icon_img = R.mipmap.jsd_y;
                        }
                        addpoint(waterPoint, icon_img);
                        mMapView.centerAt(waterPoint, true);
                        mMapView.setResolution(GisMapConfig.resolution[GisMapConfig.MAP_DEFAULT_RES]);// 设置当前显示的分辨率
                    }

                    locEt.setText(model.getLocation());
                } else {
                    ToastShow(R.string.request_failure);//请求失败
                }
                dialog.close();
            }
        });

    }

    //确定积水
    private void StartSubmit() {
        try {
            String FileStatus = GetFileStatus();// MyTextUtils.GetOpitemValue(OptionsItemsUtils.GetFileStatusList(), 0, tv_FileStatus.getText().toString());

            JSONObject maps = new JSONObject();
            maps.put("ID", model.getID());
            maps.put("StartWaterDate", MyTextUtils.getNowDatatoString());
            maps.put("ArrivalDT", MyTextUtils.getNowDatatoString());
            maps.put("Status", "0");
            maps.put("IsDelete", "S");
            maps.put("Msg", CacheData.UserName() + ":确定发生积水");
            maps.put("OperationType", "Start");
            selectFileStatus(maps, "请完善积水信息！", "确定积水失败！", false, FileStatus);

        } catch (JSONException e) {
            ToastShow("保存失败," + e.toString());
            e.printStackTrace();
        }

    }

    //取消积水
    private void CancelSubmit() {

        try {
            String FileStatus = GetFileStatus();// MyTextUtils.GetOpitemValue(OptionsItemsUtils.GetFileStatusList(), 0, tv_FileStatus.getText().toString());

            JSONObject maps = new JSONObject();
            maps.put("ID", model.getID());
            maps.put("StartWaterDate", MyTextUtils.getNowDatatoString());
            maps.put("ArrivalDT", MyTextUtils.getNowDatatoString());
            maps.put("IsDelete", "Y");
            maps.put("Msg", CacheData.UserName() + ":确定未积水");
            maps.put("OperationType", "Cancel");
            selectFileStatus(maps, "处理成功！", "确定未积水失败！", true, FileStatus);

        } catch (JSONException e) {
            ToastShow("保存失败," + e.toString());
            e.printStackTrace();
        }

    }

    private void selectFileStatus(final JSONObject maps, final String SuccessLog, final String ErrorLog, final boolean isExit, String FileStatus) {

        if (getPicVideoFileData().size() > 0) {
            if (FileStatus.equals("")) {
                ToastShow("请选择附件状态！");
                return;
            }
            UploadBill(maps, SuccessLog, ErrorLog, isExit, FileStatus);

        } else {
            UploadBill(maps, SuccessLog, ErrorLog, isExit, FileStatus);
        }
    }

    //保存工单和附件的提交方法
    private void UploadBill(JSONObject maps, final String SuccessLog, final String ErrorLog, final boolean isExit, final String FileStatus) {
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
                    Const.file_p, getPicVideoFileData(), param,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject josnobject = new JSONObject(response);
                                if (josnobject.getBoolean("Result")) {

                                    dialog.close();
                                    ToastShow(SuccessLog);
                                    if (!isExit) {
                                        CacheData.WaterPoint_UPDATE = true;
                                        CacheData.mWaterPointID = model.getID();
                                        Intent it = new Intent(context, WaterPointDetailActivity.class);
                                        startActivity(it);
                                    }
                                    finish();
                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
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
            request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
            mQueue.add(request);
        } catch (JSONException e) {
            dialog.close();
            ToastShow(ErrorLog + e.toString());
            e.printStackTrace();
        }
    }

    //获取附件的状态
    private String GetFileStatus() {
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        CacheActivityUtil.finishSingleActivity(WaterPointSureActivity.this);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_sure_water_point;
    }

    @Override
    protected boolean showNavigation() {
        return true;
    }

    @Override
    protected int showMoreIcon() {
        return R.drawable.icon_fujian_01;
    }

    @Override
    protected boolean showMedia() {
        return true;
    }

    @Override
    protected boolean showMapPipe() {
        return false;
    }

    @Override
    protected String showTitleName() {
        return "积水点确认";
    }

    private void initView() {
        mQueue = Volley.newRequestQueue(this);
        SetFileStatus("0");
    }

    //设置附件的状态
    private void SetFileStatus(final String ls_status) {
        switch (ls_status) {
            case "3":
                ck_1.setChecked(true);
                ck_2.setChecked(false);
                ck_3.setChecked(false);
                ck_4.setChecked(false);
                break;
            case "0":
                ck_1.setChecked(false);
                ck_2.setChecked(true);
                ck_3.setChecked(false);
                ck_4.setChecked(false);
                break;
            case "1":
                ck_1.setChecked(false);
                ck_2.setChecked(false);
                ck_3.setChecked(true);
                ck_4.setChecked(false);
                break;
            case "2":
                ck_1.setChecked(false);
                ck_2.setChecked(false);
                ck_3.setChecked(false);
                ck_4.setChecked(true);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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
