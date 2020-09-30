package com.haocang.bjxjc.KeepOrder;

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
import com.haocang.bjxjc.ui.equipment.bean.EquTaskBean;
import com.haocang.bjxjc.ui.faultrepair.activity.FaultFlowActivity;
import com.haocang.bjxjc.ui.faultrepair.activity.RepairOrderActivity;
import com.haocang.bjxjc.ui.tools.activity.MultiSelectDialog;
import com.haocang.bjxjc.ui.tools.enums.MultiSelectEnum;
import com.haocang.bjxjc.utils.tools.CacheData;
import com.haocang.bjxjc.utils.tools.ApiConstant;
import com.haocang.bjxjc.utils.tools.CustomDialog;
import com.haocang.bjxjc.utils.tools.MyTextUtils;
import com.haocang.bjxjc.utils.tools.MyUtils;
import com.haocang.bjxjc.utils.tools.UrlConst;
import com.haocang.commonlib.loadingdialog.LoadingDialog;
import com.haocang.commonlib.network.interfaces.InitDataCallBack;

import net.tsz.afinal.annotation.view.ViewInject;

import java.util.HashMap;

//保养工单详情
public class KeepOrderDetailActivity extends CommFinalActivity {
    @ViewInject(id = R.id.tv_EquName)
    TextView tv_EquName;
    @ViewInject(id = R.id.tv_F_Title)
    EditText tv_F_Title;
    @ViewInject(id = R.id.tv_task_date)
    TextView tv_task_date;
    @ViewInject(id = R.id.executorNameTv)
    TextView executorNameTv;
    @ViewInject(id = R.id.keep_content_tv)
    TextView keep_content_tv;
    @ViewInject(id = R.id.overtime_reson_tv)
    EditText overdue_reson_tv;
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
    private EquTaskBean info;
    private Context context;

    private String ls_DisposeUser = "", ls_DisposeUserName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        initView();
        setView();

        //如果后期需要修改功能,修改后这里重新请求数据.
        getData();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_keep_order_detail;
    }

    //设置界面
    private void setView() {
        if (info == null) {
            return;
        }
        tv_F_Title.setText(MyTextUtils.getString(info.getFullName()));
        tv_EquName.setText(MyTextUtils.getString(info.getEquName()));
        tv_task_date.setText(MyTextUtils.getString(info.getTasksDT()));
        executorNameTv.setText(MyTextUtils.getString(info.getRealName()));

        keep_content_tv.setText(MyTextUtils.getString(info.getContent()));
        overdue_reson_tv.setText(MyTextUtils.getString(info.getOverdueDescription()));
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
        return "工单详情";
    }

    private void initView() {
        info = CacheData.mEquTaskBean;
        ShowButton(false, false, false, false, false, false, false, false, false);
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
                it.putExtra("ID", info.getRepairID());
                startActivity(it);
            }
        });

 /*       executorNameTv.setOnClickListener(new View.OnClickListener() {
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
        });*/

        submit_qd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyTextUtils.IsNull(ls_DisposeUser)) {
                    ToastShow("处理人不能为空");
                    return;
                }
                HashMap<String, Object> maps = new HashMap<>();
                maps.put("RepairID", info.getRepairID());//*
                maps.put("EquID", info.getEquID());//*
                maps.put("USERID", CacheData.UserID());//*
                maps.put("Status", 2);//*

                maps.put("ReceiveDate", MyUtils.getNowDatatoString());//接受时间
                maps.put("ReceivedUserID", CacheData.UserID());//接受人
                maps.put("ReceivedName", CacheData.UserName());//接受人名称
                maps.put("DisposeUserID", ls_DisposeUser);//处理人
                maps.put("DisposeUserName", ls_DisposeUserName);//处理人名称
                maps.put("Description", overdue_reson_tv.getText().toString());//故障描叙
                maps.put("F_Title", tv_F_Title.getText().toString());//标题
                maps.put("Remark", tv_Remark.getText().toString());//备注
                //选择处理人
                SureUploadBill(maps, "是否确认？", 2);
            }
        });

        submit_ks.setOnClickListener(new View.OnClickListener() {//开始
            @Override
            public void onClick(View v) {
                HashMap<String, Object> maps = new HashMap<>();
                maps.put("RepairID", info.getRepairID());//*
                maps.put("EquID", info.getEquID());//*
                maps.put("USERID", CacheData.UserID());//*
                maps.put("Status", 3);//*
                SureUploadBill(maps, "是否确定开始处理维修工单？", 3);
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
                maps.put("RepairID", info.getRepairID());//*
                maps.put("EquID", info.getEquID());//*
                maps.put("USERID", CacheData.UserID());//*
                maps.put("Status", 5);//*
                SureUploadBill(maps, "确定挂起？只有处理人能恢复工单", 5);
            }
        });
        submit_hf.setOnClickListener(new View.OnClickListener() {//恢复
            @Override
            public void onClick(View v) {
                HashMap<String, Object> maps = new HashMap<>();
                maps.put("RepairID", info.getRepairID());//*
                maps.put("EquID", info.getEquID());//*
                maps.put("USERID", CacheData.UserID());//*
                maps.put("Status", 3);//*
                SureUploadBill(maps, "确定恢复此维修工单？", 100);
            }
        });

        submit_zp.setOnClickListener(new View.OnClickListener() {//转派
            @Override
            public void onClick(View v) {


                String ls_Remark = tv_Remark.getText().toString();

                if (MyTextUtils.IsNull(ls_Remark)) {
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

                if (MyTextUtils.IsNull(ls_Remark)) {
                    ToastShow("关闭工单需要输入备注!");
                    return;
                }

                HashMap<String, Object> maps = new HashMap<>();
                maps.put("RepairID", info.getRepairID());//*
                maps.put("EquID", info.getEquID());//*
                maps.put("USERID", CacheData.UserID());//*
                maps.put("Status", 6);//*
                maps.put("Remark", ls_Remark);//备注

                SureUploadBill(maps, "确定关闭此维修工单？", 6);
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

    //请求数据
    private void getData() {


    }

    private void SubmitBill(HashMap<String, Object> maps, final int status) {
        dialog = new LoadingDialog(context, "保存中");
        dialog.show();
        DataModel.requestPOST(mContext, ApiConstant.host + UrlConst.Api_PostSaveFaultRepair, maps, new InitDataCallBack() {

            @Override
            public void callbak(boolean b, String string) {
                String msg = "";
                switch (status) {
                    case 2:
                        msg = "确认";
                        break;
                    case 3:
                        msg = "开始";
                        break;
                    case 4:
                        msg = "转派";
                        break;
                    case 5:
                        msg = "挂起";
                        break;
                    case 6:
                        msg = "关闭";
                        break;
                    case 9:
                        msg = "结束";
                        break;
                    case 100:
                        msg = "恢复";
                        break;
                }
                if (b) {
                    ToastShow(msg + "成功");
                    getData();
                } else {
                    ToastShow(msg + "失败" + string);
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
            executorNameTv.setText(ls_DisposeUserName);
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
            maps.put("RepairID", info.getRepairID());//*
            maps.put("EquID", info.getEquID());//*
            maps.put("USERID", CacheData.UserID());//*
            maps.put("Status", 4);//*
            maps.put("DisposeUserID", ls_DisposeUser);//处理人
            maps.put("DisposeUserName", ls_DisposeUserName);//处理人名称
            maps.put("ZP_Remark", ls_Remark);//*
            maps.put("ZP_Executor", ls_DisposeUser);//*

            SubmitBill(maps, 4);
        }

        if (requestCode == 103 && resultCode == 100) {
            //向 HC_Equ_FaultRepair 表插入完成情况数据
            HashMap<String, Object> maps = new HashMap<>();
            maps.put("RepairID", info.getRepairID());//*
            maps.put("EquID", info.getEquID());//*
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


    private void SureUploadBill(final HashMap<String, Object> maps, String ls_title, final int status) {
        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage(ls_title);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //设置你的操作事项
                SubmitBill(maps, status);
            }
        });

        builder.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }


}
