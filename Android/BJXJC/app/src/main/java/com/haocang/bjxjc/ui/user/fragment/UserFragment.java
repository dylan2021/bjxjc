package com.haocang.bjxjc.ui.user.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.haocang.bjxjc.R;
import com.haocang.bjxjc.activity.tool.CacheActivityUtil;
import com.haocang.bjxjc.requestcenter.DataModel;
import com.haocang.bjxjc.ui.user.acts.ChangePwdActivity;
import com.haocang.commonlib.help.HelpActivity;
import com.haocang.commonlib.network.interfaces.InitDataBeanBack;
import com.haocang.commonlib.network.interfaces.InitDataCallBack;
import com.haocang.bjxjc.ui.arrangementbase.activity.ArrangementBaseActivity;
import com.haocang.bjxjc.ui.tools.activity.FileInfoActivity;
import com.haocang.bjxjc.ui.user.bean.ArrangementBaseBean;
import com.haocang.bjxjc.utils.bean.ShareIdNameBean;
import com.haocang.bjxjc.utils.tools.ApiConstant;
import com.haocang.bjxjc.utils.tools.UrlConst;
import com.haocang.bjxjc.utils.tools.CacheData;
import com.haocang.bjxjc.utils.tools.CustomDialog;
import com.haocang.bjxjc.utils.tools.SharedPreferencesUtil;


import java.util.HashMap;
import java.util.List;

/**
 * 用户
 */

public class UserFragment extends Fragment {
    private View view;
    private Context context;
    private ImageView user_iv_IsOnline;
    private LinearLayout user_update_team;
    private LinearLayout user_update_pass;
    private LinearLayout user_info;
    private LinearLayout user_help;
    private TextView user_deptname;
    private TextView user_username;
    private LinearLayout user_delete;
    private LinearLayout user_exit;
    private TextView user_tv_IsOnline_text;
    private TextView user_foldersize;

    private boolean isLeader = false;//是否是队长

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user, container, false);
        context = this.getActivity();

        initView();

        initEvent();

        initData();
        return view;
    }

    private void initView() {
        user_iv_IsOnline = view.findViewById(R.id.user_iv_IsOnline);
        user_tv_IsOnline_text = view.findViewById(R.id.user_tv_IsOnline_text);
        user_update_pass = view.findViewById(R.id.user_update_pass);
        user_update_team = view.findViewById(R.id.user_update_team);
        user_info = view.findViewById(R.id.user_info);
        user_deptname = view.findViewById(R.id.user_deptname);
        user_username = view.findViewById(R.id.user_username);
        user_help = view.findViewById(R.id.user_help);
        user_delete = view.findViewById(R.id.user_delete);
        user_exit = view.findViewById(R.id.user_exit);
        user_foldersize = view.findViewById(R.id.user_foldersize);
    }

    private void initEvent() {
        user_iv_IsOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (user_tv_IsOnline_text.getText().toString().equals("在线")) {

                    CustomDialog.Builder builder = new CustomDialog.Builder(context);
                    builder.setTitle("提示");
                    builder.setMessage("您确定离线?");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            //设置你的操作事项
                            user_iv_IsOnline.setImageResource(R.mipmap.ic_offline_01);
                            user_tv_IsOnline_text.setText("离线");
                            UpdateClockIn(0);

                        }
                    });

                    builder.setNegativeButton("取消",
                            new android.content.DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    builder.create().show();
                } else {

                    CustomDialog.Builder builder = new CustomDialog.Builder(context);
                    builder.setTitle("提示");
                    builder.setMessage("您确定上线?");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            //设置你的操作事项
                            user_iv_IsOnline.setImageResource(R.mipmap.ic_online_01);
                            user_tv_IsOnline_text.setText("在线");
                            UpdateClockIn(1);

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

        });

        user_update_team.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user_tv_IsOnline_text.getText().equals("离线")) {
                    ToastShow("只有在线的情况下才能编辑您的防汛队伍！");
                    return;
                }
                JudgeArrangementBase();
            }

        });


        //修改密码
        user_update_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ToastShow("功能正在研发中！");
                  Intent intent = new Intent(context, ChangePwdActivity.class);
                 startActivity(intent);
            }

        });

        //关于
        user_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastShow("功能正在研发中！");
//                Intent intent = new Intent(context, UserSelectDialog.class);
//                startActivity(intent);
            }

        });

        //帮助
        user_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HelpActivity.class);
                intent.putExtra("URL", UrlConst.Api_HelpDocUrl);
                startActivity(intent);
            }

        });
        //清除缓存
        user_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FileInfoActivity.class);
                startActivity(intent);
            }

        });

        //退出登录
        user_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialog.Builder builder = new CustomDialog.Builder(context);
                builder.setTitle("提示");
                builder.setMessage("是否退出登录？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //设置你的操作事项
                        // UpdateTeamOff();
                        UpdateClockIn(0);
                        SharedPreferencesUtil.SaveUserInfo(context, "", "", "", "", "", "", "", "N");
                        CacheActivityUtil.finishActivity();
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

        });


    }

    private void initData() {
        if (user_deptname != null && user_username != null) {
            user_username.setText(CacheData.UserName());
            user_deptname.setText(CacheData.DeptName());
        } else {
            //ToastShow("请求失败");
        }

        String ApiUrl = ApiConstant.host + UrlConst.Api_GetIsLeader + "?userid=" + CacheData.UserID();
        DataModel.requestGETMode("ShareIdNameBean", ApiUrl, new InitDataBeanBack<ShareIdNameBean>() {
            @Override
            public void callbak(boolean b, List<ShareIdNameBean> list, String msg) {
                if (b) {
                    if (list.size() > 0) {
                        isLeader = true;
                        user_update_team.setVisibility(View.VISIBLE);
                    } else {
                        isLeader = false;
                        user_update_team.setVisibility(View.GONE);
                    }
                } else {
                    isLeader = false;
                    user_update_team.setVisibility(View.GONE);
                    ToastShow(msg);
                }
            }
        });

        String ApiUrl1 = ApiConstant.host + UrlConst.Api_GetClockInValue + "?userid=" + CacheData.UserID();
        DataModel.requestGETMode("ShareIdNameBean", ApiUrl1, new InitDataBeanBack<ShareIdNameBean>() {
            @Override
            public void callbak(boolean b, List<ShareIdNameBean> list, String msg) {
                if (b) {
                    if (list.size() > 0) {
                        user_iv_IsOnline.setImageResource(R.mipmap.ic_online_01);
                        user_tv_IsOnline_text.setText("在线");
                    } else {
                        user_iv_IsOnline.setImageResource(R.mipmap.ic_offline_01);
                        user_tv_IsOnline_text.setText("离线");
                    }
                } else {
                    ToastShow(msg);
                }
            }
        });

    }


    private void UpdateClockIn(final int clockIn) {

        String ApiUrl = ApiConstant.host + UrlConst.Api_PostUpdateClockIn;
        HashMap<String, Object> map = new HashMap<>();
        map.put("F_UserId", CacheData.UserID());
        map.put("ClockIn", clockIn);

        DataModel.requestPOST(context, ApiUrl, map, new InitDataCallBack() {
            @Override
            public void callbak(boolean b, String string) {
                if (b) {
                    if (clockIn == 1) {
                        ToastShow("打卡成功！");
                    } else {
                        ToastShow("离线成功！");
                    }
                } else {
                    ToastShow("打卡失败:" + string);
                }

            }
        });
    }

    private void UpdateTeamOff() {
        String ApiUrl = ApiConstant.host + UrlConst.Api_GetEditArrangementBaseExit + "?userid=" + CacheData.UserID();
        HashMap<String, Object> map = new HashMap<>();
        map.put("ClockIn", 0);
        DataModel.requestPOST(context, ApiUrl, map, new InitDataCallBack() {
            @Override
            public void callbak(boolean b, String string) {
                if (b) {
                    ToastShow("下线成功！");
                } else {
                    ToastShow("下线失败:" + string);
                }
            }
        });
    }


    /**
     * ===============================================
     * 创建时间：2019/5/23 15:29
     * 编 写 人：ShenC
     * 方法说明： 根据队长编号，找到队伍库数据，激活队伍,
     * ================================================
     * 修改内容：      修改时间：      修改人：
     * ===============================================
     */
    private void UpdateTeamOn() {
        String ApiUrl = ApiConstant.host + UrlConst.Api_ActivationArrangementBase + "?userid=" + CacheData.UserID();
        HashMap<String, Object> map = new HashMap<>();
        map.put("ClockIn", 0);
        DataModel.requestPOST(context, ApiUrl, map, new InitDataCallBack() {
            @Override
            public void callbak(boolean b, String string) {
                if (b) {
                    ToastShow("队伍生成成功！");

                } else {
                    ToastShow("队伍生成失败:" + string);
                }
            }
        });
    }


    /**
     * ===============================================
     * 创建时间：2019/5/23 15:39
     * 编 写 人：ShenC
     * 方法说明：判断防汛队长是否存在队伍
     * ================================================
     * 修改内容：      修改时间：      修改人：
     * ===============================================
     */
    private void JudgeArrangementBase() {
        String ApiUrl = ApiConstant.host + UrlConst.Api_JudgeArrangementBase + "?userid=" + CacheData.UserID();
        DataModel.requestGETMode("ArrangementBaseBean", ApiUrl, new InitDataBeanBack<ArrangementBaseBean>() {
            @Override
            public void callbak(boolean b, List<ArrangementBaseBean> list, String msg) {
                if (b) {
                    if (list.size() == 0) {
                        ToastShow("未查询到您的防汛队伍！");
                    } else {
                        ArrangementBaseBean model = list.get(0);
                        Intent intent = new Intent(context, ArrangementBaseActivity.class);
                        intent.putExtra("ID", model.getID());
                        intent.putExtra("TeamName", model.getTeamName());
                        intent.putExtra("TeamMember", model.getTeamMember());
                        intent.putExtra("CarID", model.getCarID());
                        intent.putExtra("CarEquipped", model.getCarEquipped());
                        intent.putExtra("ManagementOffice", model.getManagementOffice());
                        startActivity(intent);
                    }
                } else {
                    ToastShow("防汛队长打卡错误," + msg);
                }
            }
        });


    }


    //接口回调,接受HomeActivity的指令
    public void RefreshPageData() {
        initData();
        Log.i(ApiConstant.LogFlag, "UserFragment开始刷新");
    }

    protected void ToastShow(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

}
