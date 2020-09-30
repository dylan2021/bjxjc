package com.haocang.bjxjc.ui.waterpoint.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.bigkoo.pickerview.OptionsPickerView;
import com.haocang.bjxjc.R;
import com.haocang.bjxjc.activity.comm.CommMediaActivity;
import com.haocang.bjxjc.activity.tool.CacheActivityUtil;
import com.haocang.bjxjc.requestcenter.DataModel;
import com.haocang.bjxjc.utils.tools.Const;
import com.haocang.commonlib.network.request.UploadUtil;
import com.haocang.commonlib.network.interfaces.InitDataBeanBack;
import com.haocang.bjxjc.ui.tools.activity.MultiSelectDialog;
import com.haocang.bjxjc.ui.tools.enums.MultiSelectEnum;
import com.haocang.bjxjc.ui.waterpoint.utils.ComputeTools;
import com.haocang.bjxjc.utils.bean.ProvinceBean;
import com.haocang.bjxjc.utils.bean.ShareIdNameBean;
import com.haocang.bjxjc.utils.tools.ApiConstant;
import com.haocang.bjxjc.utils.tools.UrlConst;
import com.haocang.commonlib.loadingdialog.LoadingDialog;
import com.haocang.bjxjc.utils.tools.CacheData;
import com.haocang.bjxjc.utils.tools.MyTextUtils;
import com.haocang.bjxjc.utils.tools.OptionsItemsUtils;

import net.tsz.afinal.annotation.view.ViewInject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class WaterPointAddActivity extends CommMediaActivity {
    @ViewInject(id = R.id.belong_warm_tv)
    TextView tv_WarningID;
    @ViewInject(id = R.id.tv_seeper_area)
    TextView tv_SeeperArea;
    @ViewInject(id = R.id.tv_SeeperDepth)
    TextView tv_SeeperDepth;
    @ViewInject(id = R.id.seeper_depth)
    SeekBar sb_SeeperDepth;
    @ViewInject(id = R.id.sb_SeeperArea)
    SeekBar sb_SeeperArea;
    @ViewInject(id = R.id.tv_SeeperLevel)
    TextView tv_SeeperLevel;
    @ViewInject(id = R.id.water_loc_et)
    EditText tv_Location;
    @ViewInject(id = R.id.dispose_team_tv)
    TextView tv_ArrangementID;
    @ViewInject(id = R.id.water_reason)
    TextView tv_SeeperReason;
    @ViewInject(id = R.id.dispose_person_tv)
    TextView tv_DisposingPerson;
    @ViewInject(id = R.id.warm_detail_desc_tv)
    TextView tv_Memo;
    @ViewInject(id = R.id.cb_IsCarPass)
    CheckBox cb_IsCarPass;
    @ViewInject(id = R.id.cb_is_urgent_handle)
    CheckBox cb_IsUrgentHandle;
    @ViewInject(id = R.id.ck_1)
    CheckBox ck_1;
    @ViewInject(id = R.id.ck_2)
    CheckBox ck_2;
    @ViewInject(id = R.id.ck_3)
    CheckBox ck_3;
    @ViewInject(id = R.id.ck_4)
    CheckBox ck_4;
    @ViewInject(id = R.id.ck_status_0)
    CheckBox ck_status_0;
    @ViewInject(id = R.id.ck_status_1)
    CheckBox ck_status_1;
    @ViewInject(id = R.id.ck_status_2)
    CheckBox ck_status_2;
    @ViewInject(id = R.id.submit)
    Button submit;
    private double SingleTap_X = 0, SingleTap_Y = 0,SingleTap_MX = 0, SingleTap_MY = 0;
    private RequestQueue mQueue;
    private LoadingDialog dialog;
    private String ls_ArrangementID = "", ls_DisposingPersonID = "", ls_DisposingPersonName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CacheActivityUtil.addActivity(WaterPointAddActivity.this);
        initView();
        initEvent();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CacheActivityUtil.finishSingleActivity(WaterPointAddActivity.this);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_water_point_add;
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
        return 0;
    }

    @Override
    protected String showTitleName() {
        return "积水点上报";
    }

    private void initView() {
        mQueue = Volley.newRequestQueue(this);
        Intent it = getIntent();


        SingleTap_X = it.getDoubleExtra("X", 0);
        SingleTap_Y = it.getDoubleExtra("Y", 0);
        SingleTap_MX = it.getDoubleExtra("MX", 0);
        SingleTap_MY = it.getDoubleExtra("MY", 0);
        tv_SeeperLevel.setText("轻度积水");
        tv_SeeperReason.setText("雨势过大雨水篦排水不足");
        SetBillStatus(0);
        SetFileStatus(0);

    }

    private void initEvent() {
        backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        tv_SeeperLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseDeviceCode(OptionsItemsUtils.GetWaterPointSeeperLevel(), 2);
            }
        });

        ck_status_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            SetBillStatus(0);
            }
        });
        ck_status_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetBillStatus(1);
            }
        });
        ck_status_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetBillStatus(2);
            }
        });

        ck_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetFileStatus(3);
            }
        });
        ck_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetFileStatus(0);
            }
        });
        ck_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetFileStatus(1);
            }
        });
        ck_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetFileStatus(2);
            }
        });

        tv_WarningID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseDeviceCode(OptionsItemsUtils.getWarmList(), 6);
            }
        });
        tv_ArrangementID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(WaterPointAddActivity.this, MultiSelectDialog.class);
                it.putExtra("ID", "");
                it.putExtra("DECIDE", "ID");
                it.putExtra("TYPE", MultiSelectEnum.Arrangement);
                it.putExtra("SELECTED", ls_ArrangementID);
                it.putExtra("IsAlone", true);
                startActivityForResult(it, 1);
            }
        });
        tv_SeeperReason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(WaterPointAddActivity.this, MultiSelectDialog.class);
                it.putExtra("ID", "");
                it.putExtra("DECIDE", "Name");
                it.putExtra("TYPE", MultiSelectEnum.SeeperReason);
                it.putExtra("SELECTED", tv_SeeperReason.getText().toString());
                it.putExtra("IsAlone", false);
                it.putExtra("ManuaInput", true);
                startActivityForResult(it, 2);
            }
        });
        tv_Memo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(WaterPointAddActivity.this, MultiSelectDialog.class);
                it.putExtra("ID", "");
                it.putExtra("DECIDE", "Name");
                it.putExtra("TYPE", MultiSelectEnum.WaterMemo);
                it.putExtra("SELECTED", tv_Memo.getText().toString());
                it.putExtra("IsAlone", false);
                it.putExtra("ManuaInput", true);
                startActivityForResult(it, 4);
            }
        });
        tv_DisposingPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyTextUtils.IsNull(ls_ArrangementID)) {
                    ToastShow("请选择处置队伍！");
                    return;
                }
                Intent it = new Intent(WaterPointAddActivity.this, MultiSelectDialog.class);
                it.putExtra("ID", ls_ArrangementID);
                it.putExtra("DECIDE", "ID");
                it.putExtra("TYPE", MultiSelectEnum.USER);
                it.putExtra("SELECTED", ls_DisposingPersonID);
                it.putExtra("IsAlone", false);
                startActivityForResult(it, 3);
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initSubmit();
            }
        });
        sb_SeeperDepth.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // 当拖动条的滑块位置发生改变时触发该方法,在这里直接使用参数progress，即当前滑块代表的进度值
                tv_SeeperDepth.setText(Integer.toString(progress));

                ComputeTools.ComputeWaterLevel(tv_SeeperLevel,sb_SeeperDepth.getProgress(),sb_SeeperArea.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.e("------------", "开始滑动！");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.e("------------", "停止滑动！");
                int max = sb_SeeperDepth.getMax();
                if( seekBar.getProgress() > (max*9/10)){
                    sb_SeeperDepth.setMax(max * 2);
                }
                if( seekBar.getProgress() < (max/10)){
                    sb_SeeperDepth.setMax(max/2);
                }
            }
        });
        sb_SeeperArea.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // 当拖动条的滑块位置发生改变时触发该方法,在这里直接使用参数progress，即当前滑块代表的进度值
                tv_SeeperArea.setText(Integer.toString(progress));
                ComputeTools.ComputeWaterLevel(tv_SeeperLevel,sb_SeeperDepth.getProgress(),sb_SeeperArea.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.e("------------", "开始滑动！");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.e("------------", "停止滑动！");
                int max = sb_SeeperArea.getMax();
                if( seekBar.getProgress() > (max*9/10)){
                    sb_SeeperArea.setMax(max * 2);
                }
                if( seekBar.getProgress() < (max/10)){
                    sb_SeeperArea.setMax(max/2);
                }
            }
        });

    }

    private void initData() {
        if (!CacheData.Cache_WaringID.equals("")) {
            tv_WarningID.setText(MyTextUtils.GetOpitemValue(OptionsItemsUtils.getWarmList(), 1, CacheData.Cache_WaringID));
        }
        String ApiUrl = ApiConstant.host + UrlConst.Api_List_GetTeamNameListByUserID+"?userid="+CacheData.UserID();
        DataModel.requestGETMode("ShareIdNameBean", ApiUrl, new InitDataBeanBack<ShareIdNameBean>() {
            @Override
            public void callbak(boolean b, List<ShareIdNameBean> list, String msg) {
                if (b) {
                    if(list.size()>0){
                        ls_ArrangementID = list.get(0).getID();
                        tv_ArrangementID.setText(list.get(0).getName());
                    }
                } else {
                    ToastShow(msg);
                }
            }
        });
    }

    //提交工单
    private void initSubmit() {
        if (SingleTap_X == 0 || SingleTap_Y == 0) {
            ToastShow("请设置坐标");
            return;
        }
        String Location = tv_Location.getText().toString();
        String Memo = tv_Memo.getText().toString();
        String SeeperDepth = tv_SeeperDepth.getText().toString();
        String SeeperArea = tv_SeeperArea.getText().toString();
        String SeeperReason = tv_SeeperReason.getText().toString();
        String SeeperLevel = MyTextUtils.GetOpitemValue(OptionsItemsUtils.GetWaterPointSeeperLevel(), 0, tv_SeeperLevel.getText().toString());
        String Status = GetBillStatus();
        String WarningID = MyTextUtils.GetOpitemValue(OptionsItemsUtils.getWarmList(), 0, tv_WarningID.getText().toString());
        //根据用户id获取其队伍编号
        String FileStatus = GetFileStatus();
        if (MyTextUtils.IsNull(WarningID)) {
            ToastShow("所属预警不能为空");
            return;
        }
        if (MyTextUtils.IsNull(SeeperDepth) || MyTextUtils.IsNull(SeeperArea)) {
            ToastShow("积水深度或面积不能为空");
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

        if (getPicFilelist().size() > 0 && MyTextUtils.IsNull(FileStatus)) {
            ToastShow("附件状态不能为空");
            return;
        }

        try {
            JSONObject maps = new JSONObject();
            maps.put("CreateDT", MyTextUtils.getNowDatatoString());
            maps.put("Creator", CacheData.UserID());//上报人编号
            maps.put("IsDelete", "S");
            maps.put("Location", Location);
            maps.put("SeeperDepth", SeeperDepth);
            maps.put("SeeperArea", SeeperArea);
            maps.put("IsCarPass", cb_IsCarPass.isChecked() ? 1 : 0);
            maps.put("IsUrgentHandle", cb_IsUrgentHandle.isChecked() ? 1 : 0);
            maps.put("IsHandle", 0);
            maps.put("SeeperLevel", SeeperLevel);
            maps.put("SeeperReason", SeeperReason);
            maps.put("Memo", Memo);
            maps.put("Status", Status);
            maps.put("WarningID", WarningID);
            maps.put("X", SingleTap_X);
            maps.put("Y", SingleTap_Y);
            maps.put("MX", SingleTap_MX);
            maps.put("MY", SingleTap_MY);
            maps.put("ArrivalDT", MyTextUtils.getNowDatatoString());
            maps.put("StartWaterDate", MyTextUtils.getNowDatatoString());
            maps.put("ArrangementID", ls_ArrangementID);
            maps.put("DisposingPersonName", ls_DisposingPersonName);//处置人名称
            maps.put("DisposingPerson", ls_DisposingPersonID);//处置人编号
            maps.put("Msg", CacheData.UserName()+":上报积水点。");
            maps.put("OperationType", "ADD");
            JSONObject param = new JSONObject();
            param.put("info", maps);
            param.put("filename", "WaterPoint");
            param.put("CreateUserId", CacheData.UserID());
            param.put("CreateUserName", CacheData.UserName());
            param.put("FileStatus", FileStatus);

            dialog = new LoadingDialog(WaterPointAddActivity.this, "保存中");
            dialog.show();
            UploadUtil request = new UploadUtil(
                    ApiConstant.host + UrlConst.fileUpload, Const.file_p, getPicFilelist(), param,
                   new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject josnobject = new JSONObject(response);
                        if (josnobject.getBoolean("Result")) {

                            dialog.close();
                            ToastShow("上报成功");
                            finish();
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        } else {
                            dialog.close();
                            ToastShow("保存失败，" + josnobject.getString("Msg"));
                        }
                    } catch (JSONException e) {

                        dialog.close();
                        ToastShow("保存失败," + e.toString());
                        e.printStackTrace();
                    }
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dialog.close();
                    ToastShow("请求错误,请求超时！");
                    error.printStackTrace();
                }
            });
            request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
            mQueue.add(request);

        } catch (JSONException e) {
            ToastShow("保存失败," + e.toString());
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

    //获取附件的状态
    private String GetBillStatus() {
        if (ck_status_0.isChecked()) {
            return "0";
        } else if (ck_status_1.isChecked()) {
            return "1";
        } else if (ck_status_2.isChecked()) {
            return "2";
        }
        return "0";
    }

    //设置工单的状态
    private void SetBillStatus(int Status) {
        switch (Status) {
            case 0:
                ck_status_0.setChecked(true);
                ck_status_1.setChecked(false);
                ck_status_2.setChecked(false);
                break;
            case 1:
                ck_status_0.setChecked(false);
                ck_status_1.setChecked(true);
                ck_status_2.setChecked(false);
                break;
            case 2:
                ck_status_0.setChecked(false);
                ck_status_1.setChecked(false);
                ck_status_2.setChecked(true);
                break;
        }
    }

    //设置附件的状态
    private void SetFileStatus(int Status) {
        switch (Status) {
            case 3:
                ck_1.setChecked(true);
                ck_2.setChecked(false);
                ck_3.setChecked(false);
                ck_4.setChecked(false);
                break;
            case 0:
                ck_1.setChecked(false);
                ck_2.setChecked(true);
                ck_3.setChecked(false);
                ck_4.setChecked(false);
                break;
            case 1:
                ck_1.setChecked(false);
                ck_2.setChecked(false);
                ck_3.setChecked(true);
                ck_4.setChecked(false);
                break;
            case 2:
                ck_1.setChecked(false);
                ck_2.setChecked(false);
                ck_3.setChecked(false);
                ck_4.setChecked(true);
                break;
        }
    }

    private void chooseDeviceCode(final List<ProvinceBean> items, final int pos) {
        hideKeyBoard();
        OptionsPickerView.Builder builder = new OptionsPickerView.Builder(mContext, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String itemName = items.get(options1).getPickerViewText();
                String itemValue = items.get(options1).getDescription();
                switch (pos) {
                    case 2:
                        tv_SeeperLevel.setText(itemName);
                        break;
                    case 6:
                        tv_WarningID.setText(itemName);
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
        if (requestCode == 1 && resultCode == 100) {
            ls_ArrangementID = data.getStringExtra("ID");
            String w_name = data.getStringExtra("Name");
            tv_ArrangementID.setText(w_name);
        }
        if (requestCode == 2 && resultCode == 100) {
            String w_name = data.getStringExtra("Name");
            tv_SeeperReason.setText(w_name);
        }
        if (requestCode == 3 && resultCode == 100) {
            ls_DisposingPersonID = data.getStringExtra("ID");
            ls_DisposingPersonName = data.getStringExtra("Name");
            tv_DisposingPerson.setText(ls_DisposingPersonName);
        }
        if (requestCode == 4 && resultCode == 100) {
            String w_name = data.getStringExtra("Name");
            tv_Memo.setText(w_name);
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
