package com.haocang.bjxjc.ui.faultrepair.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.haocang.bjxjc.R;
import com.haocang.bjxjc.activity.comm.CommFinalActivity;
import com.haocang.bjxjc.requestcenter.DataModel;
import com.haocang.bjxjc.ui.faultrepair.bean.EquRepairBean;
import com.haocang.bjxjc.ui.tools.activity.MultiSelectDialog;
import com.haocang.bjxjc.ui.tools.enums.MultiSelectEnum;
import com.haocang.bjxjc.utils.tools.ApiConstant;
import com.haocang.bjxjc.utils.tools.UrlConst;
import com.haocang.bjxjc.utils.tools.CacheData;
import com.haocang.bjxjc.utils.tools.CustomDialog;
import com.haocang.bjxjc.utils.tools.MyUtils;
import com.haocang.bjxjc.utils.tools.MyTextUtils;
import com.haocang.bjxjc.utils.tools.OptionsItemsUtils;
import com.haocang.commonlib.loadingdialog.LoadingDialog;
import com.haocang.commonlib.network.interfaces.InitDataBeanBack;
import com.haocang.commonlib.network.interfaces.InitDataCallBack;

import net.tsz.afinal.annotation.view.ViewInject;

import java.util.HashMap;
import java.util.List;

public class FaultRepairDetailActivity extends CommFinalActivity {
    @ViewInject(id = R.id.tv_EquName)
    TextView tv_EquName;
    @ViewInject(id = R.id.tv_F_Title)
    EditText tv_F_Title;
    @ViewInject(id = R.id.tv_RepairUser)
    TextView tv_RepairUser;
    @ViewInject(id = R.id.tv_DisposeUser)
    TextView tv_DisposeUser;
    @ViewInject(id = R.id.tv_Result)
    TextView tv_Result;
    @ViewInject(id = R.id.tv_Description)
    EditText tv_Description;
    @ViewInject(id = R.id.tv_Remark)
    EditText tv_Remark;
    @ViewInject(id = R.id.submit_qd)
    Button submit_qd;
    @ViewInject(id = R.id.submit_ks)
    Button submit_ks;
    @ViewInject(id = R.id.submit_sc)
    Button submit_sc;
    @ViewInject(id = R.id.submit_gq)
    Button submit_gq;
    @ViewInject(id = R.id.submit_zp)
    Button submit_zp;
    @ViewInject(id = R.id.submit_gb)
    Button submit_gb;
    @ViewInject(id = R.id.submit_js)
    Button submit_js;
    @ViewInject(id = R.id.submit_bc)
    Button submit_bc;
    @ViewInject(id = R.id.submit_hf)
    Button submit_hf;

    private boolean IsRepairUser = false;//当前人员是否是上报人
    private boolean IsReceiveUser = false;//当前人员是否是选择的接收人
    private boolean IsReceivedUser = false;//当前人员是否是接收人
    private boolean IsDisposeUser = false;//当前人员是否是处理人

    private LoadingDialog dialog;
    private EquRepairBean model;
    private Context context;

    private String ls_DisposeUser = "", ls_DisposeUserName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        initView();
        initEvent();
        initData();
    }
    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_fault_repair_detail;
    }
    @Override
    protected boolean showNavigation() {
        return true;
    }
    @Override
    protected int showMoreIcon() {
        return R.drawable.ic_flow_01;
    }
    @Override
    protected String showTitleName() {
        return "维修工单明细";
    }
    private void initView() {
        model = CacheData.mEquRepairBean;
        ShowButton(false, false, false, false, false, false, false, false, false);
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
                Intent it = new Intent(context, FaultFlowActivity.class);
                it.putExtra("ID", model.getRepairID());
                startActivity(it);
            }
        });

        tv_DisposeUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(context, MultiSelectDialog.class);
                it.putExtra("ID", "");
                it.putExtra("DECIDE", "ID");
                it.putExtra("TYPE", MultiSelectEnum.AllUser);
                it.putExtra("SELECTED", ls_DisposeUser);
                it.putExtra("IsAlone", true);
                it.putExtra("ShowIcon", true);
                startActivityForResult(it, 101);
            }
        });

        submit_qd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyTextUtils.IsNull(ls_DisposeUser)) {
                    ToastShow("处理人不能为空");
                    return;
                }
                HashMap<String, Object> maps = new HashMap<>();
                maps.put("RepairID", model.getRepairID());//*
                maps.put("EquID", model.getEquID());//*
                maps.put("USERID", CacheData.UserID());//*
                maps.put("Status", 2);//*

                maps.put("ReceiveDate", MyUtils.getNowDatatoString());//接受时间
                maps.put("ReceivedUserID", CacheData.UserID());//接受人
                maps.put("ReceivedName", CacheData.UserName());//接受人名称
                maps.put("DisposeUserID", ls_DisposeUser);//处理人
                maps.put("DisposeUserName", ls_DisposeUserName);//处理人名称
                maps.put("Description", tv_Description.getText().toString());//故障描叙
                maps.put("F_Title", tv_F_Title.getText().toString());//标题
                maps.put("Remark", tv_Remark.getText().toString());//备注
                //选择处理人
                SureUploadBill(maps,"是否确认？",2);
            }
        });

        submit_ks.setOnClickListener(new View.OnClickListener() {//开始
            @Override
            public void onClick(View v) {
                HashMap<String, Object> maps = new HashMap<>();
                maps.put("RepairID", model.getRepairID());//*
                maps.put("EquID", model.getEquID());//*
                maps.put("USERID", CacheData.UserID());//*
                maps.put("Status", 3);//*
                SureUploadBill(maps,"是否确定开始处理维修工单？",3);
            }
        });

        submit_sc.setOnClickListener(new View.OnClickListener() {//删除
            @Override
            public void onClick(View v) {
                ToastShow("无法操作");
            }
        });

        submit_gq.setOnClickListener(new View.OnClickListener() {//挂起
            @Override
            public void onClick(View v) {
                HashMap<String, Object> maps = new HashMap<>();
                maps.put("RepairID", model.getRepairID());//*
                maps.put("EquID", model.getEquID());//*
                maps.put("USERID", CacheData.UserID());//*
                maps.put("Status", 5);//*
                SureUploadBill(maps,"确定挂起？只有处理人能恢复工单",5);
            }
        });
        submit_hf.setOnClickListener(new View.OnClickListener() {//恢复
            @Override
            public void onClick(View v) {
                HashMap<String, Object> maps = new HashMap<>();
                maps.put("RepairID", model.getRepairID());//*
                maps.put("EquID", model.getEquID());//*
                maps.put("USERID", CacheData.UserID());//*
                maps.put("Status", 3);//*
                SureUploadBill(maps,"确定恢复此维修工单？",100);
            }
        });

        submit_zp.setOnClickListener(new View.OnClickListener() {//转派
            @Override
            public void onClick(View v) {


                String ls_Remark = tv_Remark.getText().toString();

                if(MyTextUtils.IsNull(ls_Remark)){
                    ToastShow("请在备注中需要输入转派说明!");
                    return;
                }

                Intent it = new Intent(context, MultiSelectDialog.class);
                it.putExtra("ID", "");
                it.putExtra("DECIDE", "ID");
                it.putExtra("TYPE", MultiSelectEnum.AllUser);
                it.putExtra("SELECTED", ls_DisposeUser);
                it.putExtra("IsAlone", true);
                it.putExtra("ShowIcon", true);
                startActivityForResult(it, 102);
            }
        });
        submit_gb.setOnClickListener(new View.OnClickListener() {//关闭
            @Override
            public void onClick(View v) {
                String ls_Remark = tv_Remark.getText().toString();

                if(MyTextUtils.IsNull(ls_Remark)){
                    ToastShow("关闭工单需要输入备注!");
                    return;
                }

                HashMap<String, Object> maps = new HashMap<>();
                maps.put("RepairID", model.getRepairID());//*
                maps.put("EquID", model.getEquID());//*
                maps.put("USERID", CacheData.UserID());//*
                maps.put("Status", 6);//*
                maps.put("Remark", ls_Remark);//备注

                SureUploadBill(maps,"确定关闭此维修工单？",6);
            }
        });
        submit_js.setOnClickListener(new View.OnClickListener() {//结束
            @Override
            public void onClick(View v) {
                Intent it = new Intent(context, RepairOrderActivity.class);
                startActivityForResult(it, 103);
                ToastShow("请填写结束信息！");

            }
        });

    }

    private void initData() {
        String ApiUrl = ApiConstant.host
                + UrlConst.url_get_epair_list
                + "?limit=" + ApiConstant.PAGE_LIMIT
                + "&queryJson={'RepairID':'" + model.getRepairID() + "'}";

        DataModel.requestGETMode("EquRepairBean", ApiUrl, new InitDataBeanBack<EquRepairBean>() {
            @Override
            public void callbak(boolean b, List<EquRepairBean> list, String msg) {
                if (b) {

                    if (list.size() > 0) {
                        model = list.get(0);
                        CacheData.mEquRepairBean = model;
                        tv_EquName.setText(MyTextUtils.getString(model.getEquName()));
                        tv_F_Title.setText(MyTextUtils.getString(model.getF_Title()));
                        tv_RepairUser.setText(MyTextUtils.getString(model.getRepairUserName()));
                        tv_DisposeUser.setText(MyTextUtils.getString(model.getDisposeUserName()));
                        tv_Result.setText(MyTextUtils.GetOpitemValue(OptionsItemsUtils.GetDataItemList("RepairResult"), 1, model.getResult()));
                        tv_Description.setText(MyTextUtils.getString(model.getDescription()));
                        if (MyTextUtils.getString(model.getRepairUser()).equals(CacheData.UserID())) {
                            IsRepairUser = true;
                        } else {
                            IsRepairUser = false;
                        }
                        if (MyTextUtils.getString(model.getReceivedUserID()).equals(CacheData.UserID())) {
                            IsReceivedUser = true;
                        } else {
                            IsReceivedUser = false;
                        }
                        if (MyTextUtils.getString(model.getReceiveUser()).contains(CacheData.UserID())) {
                            IsReceiveUser = true;
                        } else {
                            IsReceiveUser = false;
                        }
                        if (MyTextUtils.getString(model.getDisposeUserID()).equals(CacheData.UserID())) {
                            IsDisposeUser = true;
                        } else {
                            IsDisposeUser = false;
                        }

                        /**
                         1.待确认： 报修人可以删除，关闭，保存；选择接受人只能确认；当存在接受人时，选择接受人无法操作；接受人只能确认
                         2.待处理 > 处理人可以执行 开始，挂起，关闭，转派（换了个处理人）
                         3.处理中 > 处理人可以执行 挂起，关闭，转派，结束
                         4.挂起 > 处理人 可以恢复
                         5.已结束或关闭 > status=9已结束 或 status=6已关闭的工单不能任何操作
                         6.确认后的工单不能删除
                         */

                        if (model.getStatus().equals("1")) {//该工单待确认,
                            if (IsRepairUser) {//保修人
                                if (IsReceivedUser && IsReceiveUser) {//是接受人，也是选择接受人时》 1确认，3删除，6关闭，7保存
                                    ShowButton(true, false, true, false, false, true, false, false, false);
                                } else if (!IsReceivedUser && IsReceiveUser) {//只是选择接受人时
                                    if (MyTextUtils.IsNull(model.getReceivedUserID())) {//当
                                        ShowButton(true, false, true, false, false, true, false, false, false);
                                    }
                                }
                            } else {//非报修人
                                if (IsReceivedUser && IsReceiveUser) {//只能确认
                                    ShowButton(true, false, false, false, false, false, false, false, false);
                                } else if (!IsReceivedUser && IsReceiveUser) {
                                    if (MyTextUtils.IsNull(model.getReceivedUserID())) {//当
                                        ShowButton(true, false, false, false, false, false, false, false, false);
                                    }
                                }
                            }
                        } else if (model.getStatus().equals("2")) {//待处理-开始，挂起，转派，关闭
                            if (IsDisposeUser) {
                                ShowButton(false, true, false, false, false, false, false, false, false);
                            }else{
                                ShowButton(false, false, false, false, false, false, false, false, false);
                            }
                        } else if (model.getStatus().equals("3")) {//处理中
                            if (IsDisposeUser) {
                                ShowButton(false, false, false, true, true, true, false, true, false);
                            }else{
                                ShowButton(false, false, false, false, false, false, false, false, false);
                            }
                        } else if (model.getStatus().equals("4")) {//转派-待处理换了处理人
                            if (IsDisposeUser) {
                                ShowButton(false, false, false, true, true, true, false, true, false);
                            }else{
                                ShowButton(false, false, false, false, false, false, false, false, false);
                            }
                        } else if (model.getStatus().equals("5")) {//挂起
                            if (IsDisposeUser) {
                                ShowButton(false, false, false, false, false, false, false, false, true);
                            }else{
                                ShowButton(false, false, false, false, false, false, false, false, false);
                            }
                        }else{
                            ShowButton(false, false, false, false, false, false, false, false, false);
                        }

                    } else {
                        ToastShow("未发现该工单数据！");
                    }

                } else {
                    ToastShow(msg);
                }
            }
        });
    }

    private void SubmitBill(HashMap<String, Object> maps,final int status) {



        dialog = new LoadingDialog(context, "保存中");
        dialog.show();
        DataModel.requestPOST(mContext, ApiConstant.host + UrlConst.Api_PostSaveFaultRepair, maps, new InitDataCallBack() {

            @Override
            public void callbak(boolean b, String string) {
                String msg = "";
                switch (status){
                    case 2:msg = "确认";break;
                    case 3:msg = "开始";break;
                    case 4:msg = "转派";break;
                    case 5:msg = "挂起";break;
                    case 6:msg = "关闭";break;
                    case 9:msg = "结束";break;
                    case 100:msg = "恢复";break;
                }
                if (b) {
                    ToastShow(msg+"成功");
                    initData();
                } else {
                    ToastShow(msg+"失败" + string);
                }
                dialog.close();
            }
        });

    }

    /**
     * @param a 确定
     * @param b 开始
     * @param c 删除
     * @param d 挂起
     * @param e 转派
     * @param f 关闭
     * @param g 保存
     * @param h 结束
     * @param i 恢复
     */
    private void ShowButton(boolean a, boolean b, boolean c, boolean d, boolean e, boolean f, boolean g, boolean h, boolean i) {
        if (a) {
            submit_qd.setVisibility(View.VISIBLE);
        } else {
            submit_qd.setVisibility(View.GONE);
        }
        if (b) {
            submit_ks.setVisibility(View.VISIBLE);
        } else {
            submit_ks.setVisibility(View.GONE);
        }
        if (c) {
            submit_sc.setVisibility(View.VISIBLE);
        } else {
            submit_sc.setVisibility(View.GONE);
        }
        if (d) {
            submit_gq.setVisibility(View.VISIBLE);
        } else {
            submit_gq.setVisibility(View.GONE);
        }
        if (e) {
            submit_zp.setVisibility(View.VISIBLE);
        } else {
            submit_zp.setVisibility(View.GONE);
        }
        if (f) {
            submit_gb.setVisibility(View.VISIBLE);
        } else {
            submit_gb.setVisibility(View.GONE);
        }
        if (g) {
            submit_bc.setVisibility(View.VISIBLE);
        } else {
            submit_bc.setVisibility(View.GONE);
        }
        if (h) {
            submit_js.setVisibility(View.VISIBLE);
        } else {
            submit_js.setVisibility(View.GONE);
        }
        if (i) {
            submit_hf.setVisibility(View.VISIBLE);
        } else {
            submit_hf.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101 && resultCode == 100) {
            ls_DisposeUser = data.getStringExtra("ID");
            ls_DisposeUserName = data.getStringExtra("Name");
            tv_DisposeUser.setText(ls_DisposeUserName);
        }
        if (requestCode == 102 && resultCode == 100) {

            ls_DisposeUser = data.getStringExtra("ID");
            ls_DisposeUserName = data.getStringExtra("Name");
            String ls_Remark = tv_Remark.getText().toString();

            if (MyTextUtils.IsNull(ls_DisposeUser)) {
                ToastShow("处理人不能为空");
                return;
            }

            HashMap<String, Object> maps = new HashMap<>();
            maps.put("RepairID", model.getRepairID());//*
            maps.put("EquID", model.getEquID());//*
            maps.put("USERID", CacheData.UserID());//*
            maps.put("Status", 4);//*
            maps.put("DisposeUserID", ls_DisposeUser);//处理人
            maps.put("DisposeUserName", ls_DisposeUserName);//处理人名称
            maps.put("ZP_Remark", ls_Remark);//*
            maps.put("ZP_Executor", ls_DisposeUser);//*

            SubmitBill(maps,4);
        }

        if (requestCode == 103 && resultCode == 100) {
            //向 HC_Equ_FaultRepair 表插入完成情况数据
            HashMap<String, Object> maps = new HashMap<>();
            maps.put("RepairID", model.getRepairID());//*
            maps.put("EquID", model.getEquID());//*
            maps.put("USERID", CacheData.UserID());//*
            maps.put("Status", 9);//*
            SubmitBill(maps, 9);
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


    private void SureUploadBill(final HashMap<String, Object> maps,String ls_title,final int status){
        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage(ls_title);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //设置你的操作事项
                SubmitBill(maps,status);
            }
        });

        builder.setNegativeButton("取消",
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }


}
