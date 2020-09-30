package com.haocang.bjxjc.ui.tools.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haocang.bjxjc.R;
import com.haocang.bjxjc.activity.comm.CommFinalActivity;
import com.haocang.bjxjc.requestcenter.DataModel;
import com.haocang.commonlib.network.interfaces.InitDataBeanBack;
import com.haocang.bjxjc.ui.tools.adapter.GetTeamMemberAdapter;
import com.haocang.bjxjc.ui.tools.bean.TeamMemberDialogBean;
import com.haocang.bjxjc.ui.tools.enums.MultiSelectEnum;
import com.haocang.bjxjc.ui.user.bean.GroupUserBean;
import com.haocang.bjxjc.utils.bean.ShareIdNameBean;
import com.haocang.bjxjc.utils.bean.UserBean;
import com.haocang.bjxjc.utils.tools.ApiConstant;
import com.haocang.bjxjc.utils.tools.UrlConst;
import com.haocang.commonlib.loadingdialog.LoadingDialog;
import com.haocang.commonlib.recyclerView.BaseAdapter;
import com.haocang.bjxjc.utils.tools.MyUtils;
import com.haocang.bjxjc.utils.tools.MyTextUtils;

import net.tsz.afinal.annotation.view.ViewInject;

import java.util.ArrayList;
import java.util.List;

public class MultiSelectDialog extends CommFinalActivity {


    @ViewInject(id = R.id.recyclerview)
    RecyclerView recyclerview;
    @ViewInject(id = R.id.no)
    TextView no;
    @ViewInject(id = R.id.yes)
    TextView yes;
    @ViewInject(id = R.id.et_editmemo)
    EditText et_editmemo;
    @ViewInject(id = R.id.ll_edit)
    LinearLayout ll_edit;

    @ViewInject(id = R.id.char_Keyboard)
    LinearLayout char_Keyboard;

    private GetTeamMemberAdapter mAdapter;
    List<TeamMemberDialogBean> data_list = new ArrayList<>();

    private String _ID;//查询主键编号，可为空字符
    private String _TYPE;//查询数据的类型
    private String _SELECTED;//传入的默认勾选的数据
    private String _DECIDE;//默认勾选的数据匹配类型 ,ID,Name
    private boolean _IsAlone;//是否是单选
    private boolean _ManuaInput;//是否运行手动输入
    private boolean _ShowIcon;//是否显示图标

    private ArrayList<OnSoftKeyboardStateChangedListener> mKeyboardStateListeners = new ArrayList<>();      //软键盘状态监听列表
    private ViewTreeObserver.OnGlobalLayoutListener mLayoutChangeListener;
    private boolean mIsSoftKeyboardShowing = false;

    public interface OnSoftKeyboardStateChangedListener {
        public void OnSoftKeyboardStateChanged(boolean isKeyBoardShow, int keyboardHeight);
    }


    private LoadingDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();

        initEvent();

        switch (_TYPE) {
            case MultiSelectEnum.USER:
                initUserByID(ApiConstant.host + UrlConst.Api_List_GetUsersByArrangementIDList + "?ID=" + _ID);
                break;
            case MultiSelectEnum.WaterResult:
                initIDNameData(ApiConstant.host + UrlConst.Api_List_GetWaterResultList);
                break;
            case MultiSelectEnum.SeeperReason:
                initIDNameData(ApiConstant.host + UrlConst.Api_List_GetSeeperReasonList);
                break;
            case MultiSelectEnum.Arrangement:
                initIDNameData(ApiConstant.host + UrlConst.Api_List_GetArrangementBaseInfoList);
                break;
            case MultiSelectEnum.Measures:
                initMeasuresList();
                break;
            case MultiSelectEnum.WaterMemo:
                initWaterMemoList();
                break;
            case MultiSelectEnum.GroupUser:
                initGroupUserList(ApiConstant.host + UrlConst.Api_List_GetGroupUserList + _ID);
                break;
            case MultiSelectEnum.FloodCar:
                initIDNameData(ApiConstant.host + UrlConst.Api_List_GetDeptCarList + _ID);
                break;
            case MultiSelectEnum.AllUser:
                initIDNameData(ApiConstant.host + UrlConst.Api_List_GetUserList);
                break;
        }

    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.dialog_getteammembet;
    }

    @Override
    protected boolean showNavigation() {
        return false;
    }

    @Override
    protected int showMoreIcon() {
        return 0;
    }

    @Override
    protected String showTitleName() {
        return "请选择关联的易淹点";
    }

    private void initView() {

        Intent it = getIntent();
        _ID = it.getStringExtra("ID");
        _TYPE = it.getStringExtra("TYPE");
        _SELECTED = it.getStringExtra("SELECTED");
        _DECIDE = it.getStringExtra("DECIDE");
        _IsAlone = it.getBooleanExtra("IsAlone", true);
        _ManuaInput = it.getBooleanExtra("ManuaInput", false);
        _ShowIcon = it.getBooleanExtra("ShowIcon", false);

        if (_ManuaInput && _TYPE.equals("ID")) {
            ToastShow("手动输入模式无法进行编号匹配！");
        }

        if (_ManuaInput) {
            ll_edit.setVisibility(View.VISIBLE);
        } else {
            ll_edit.setVisibility(View.GONE);
        }

        mAdapter = new GetTeamMemberAdapter(MultiSelectDialog.this);

        recyclerview.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL) {
            @Override
            public boolean canScrollVertically() {
                return true;//是否允许滑动
            }
        });
        recyclerview.setAdapter(mAdapter);
    }

    private void initEvent() {

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });


        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<TeamMemberDialogBean> ok_list = new ArrayList<>();

                //添加手动输入项
                String editmemo = et_editmemo.getText().toString().trim();


                String IDS = "", NameS = "";
                for (TeamMemberDialogBean mm : data_list) {
                    if (!mm.isCheck()) continue;
                    ok_list.add(mm);
                }

                if (!MyTextUtils.IsNull(editmemo)) {
                    ok_list.add(new TeamMemberDialogBean("edit_" + MyUtils.getDtOfyyyyMMddHHmmss(), editmemo, true, _ShowIcon, 0));
                }

                for (int i = 0; i < ok_list.size(); i++) {
                    TeamMemberDialogBean ok_mm = ok_list.get(i);
                    if (i == ok_list.size() - 1) {
                        IDS += ok_mm.getID();
                        NameS += ok_mm.getName();
                    } else {
                        IDS += ok_mm.getID() + ",";
                        NameS += ok_mm.getName() + ",";
                    }
                }

                Intent intent = new Intent();
                intent.putExtra("ID", IDS);
                intent.putExtra("Name", NameS);
                setResult(100, intent);
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        mAdapter.setOnItemChildClickListener(new BaseAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseAdapter adapter, View v, int position) {

                if (_IsAlone) {
                    for (int i = 0; i < data_list.size(); i++) {
                        if (i == position) {

                            if (data_list.get(i).isCheck()) {
                                data_list.get(i).setCheck(false);
                            } else {
                                data_list.get(i).setCheck(true);
                            }
                        } else {
                            data_list.get(i).setCheck(false);
                        }
                    }
                } else {
                    if (data_list.get(position).isCheck()) {
                        data_list.get(position).setCheck(false);
                    } else {
                        data_list.get(position).setCheck(true);
                    }
                }
                mAdapter.setNewData(data_list);
            }

        });


        mLayoutChangeListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                WindowManager manager = MultiSelectDialog.this.getWindowManager();
                DisplayMetrics outMetrics = new DisplayMetrics();
                manager.getDefaultDisplay().getMetrics(outMetrics);
                int screenWidth = outMetrics.widthPixels;
                int screenHeight = outMetrics.heightPixels;

                //判断窗口可见区域大小
                Rect r = new Rect();
                getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
                //如果屏幕高度和Window可见区域高度差值大于整个屏幕高度的1/3，则表示软键盘显示中，否则软键盘为隐藏状态。
                int heightDifference = screenHeight - (r.bottom - r.top);
                boolean isKeyboardShowing = heightDifference > screenHeight / 3;

                //如果之前软键盘状态为显示，现在为关闭，或者之前为关闭，现在为显示，则表示软键盘的状态发生了改变
                if ((mIsSoftKeyboardShowing && !isKeyboardShowing) || (!mIsSoftKeyboardShowing && isKeyboardShowing)) {
                    mIsSoftKeyboardShowing = isKeyboardShowing;
                    LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) char_Keyboard.getLayoutParams();

                    if (mIsSoftKeyboardShowing) {
                        linearParams.height = heightDifference - 60;
                    } else {
                        linearParams.height = 0;
                    }
                    char_Keyboard.setLayoutParams(linearParams);

                    for (int i = 0; i < mKeyboardStateListeners.size(); i++) {
                        OnSoftKeyboardStateChangedListener listener = mKeyboardStateListeners.get(i);
                        listener.OnSoftKeyboardStateChanged(mIsSoftKeyboardShowing, heightDifference);
                    }
                }
            }
        };
        //注册布局变化监听
        getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(mLayoutChangeListener);

    }


    private void initMeasuresList() {
        List<ShareIdNameBean> list = new ArrayList<>();
        list.add(new ShareIdNameBean("1", "设置围挡,打开雨水篦子"));
        list.add(new ShareIdNameBean("2", "打开雨水篦子"));

        for (ShareIdNameBean model : list) {
            boolean checked;

            String index_str;
            if (_DECIDE.equals("ID")) {
                index_str = model.getID();
            } else {
                index_str = model.getName();
            }
            if (_SELECTED.indexOf(index_str) > -1) {
                checked = true;
            } else {
                checked = false;
            }

            data_list.add(new TeamMemberDialogBean(model.getID(), model.getName(), checked, _ShowIcon, 0));
        }
        mAdapter.setNewData(data_list);
    }


    private void initWaterMemoList() {
        List<ShareIdNameBean> list = new ArrayList<>();
        list.add(new ShareIdNameBean("1", "影响车辆通行"));
        list.add(new ShareIdNameBean("2", "人员伤亡"));
        list.add(new ShareIdNameBean("3", "发生重大交通事故"));

        for (ShareIdNameBean model : list) {
            boolean checked;

            String index_str;
            if (_DECIDE.equals("ID")) {
                index_str = model.getID();
            } else {
                index_str = model.getName();
            }
            if (_SELECTED.indexOf(index_str) > -1) {
                checked = true;
            } else {
                checked = false;
            }

            data_list.add(new TeamMemberDialogBean(model.getID(), model.getName(), checked, _ShowIcon, 0));
        }
        mAdapter.setNewData(data_list);
    }


    private void initGroupUserList(String ApiUrl) {

        dialog = new LoadingDialog(MultiSelectDialog.this, "保存中");
        dialog.show();
        DataModel.requestGETMode("GroupUserBean", ApiUrl, new InitDataBeanBack<GroupUserBean>() {
            @Override
            public void callbak(boolean b, List<GroupUserBean> list, String msg) {
                if (b) {
                    for (GroupUserBean model : list) {
                        boolean checked;
                        String index_str;
                        if (_DECIDE.equals("ID")) {
                            index_str = model.getF_UserId();
                        } else {
                            index_str = model.getF_RealName();
                        }
                        if (_SELECTED.indexOf(index_str) > -1) {
                            checked = true;
                        } else {
                            checked = false;
                        }
                        data_list.add(new TeamMemberDialogBean(model.getF_UserId(), model.getF_RealName(), checked, _ShowIcon, model.getClockIn().equals("1") ? 1 : 0));
                    }
                    mAdapter.setNewData(data_list);
                } else {
                    ToastShow(msg);
                }

                dialog.close();
            }
        });
    }

    private void initUserByID(String ApiUrl) {
        dialog = new LoadingDialog(MultiSelectDialog.this, "保存中");
        dialog.show();
        DataModel.requestGETMode("ShareIdNameBean", ApiUrl, new InitDataBeanBack<ShareIdNameBean>() {
            @Override
            public void callbak(boolean b, List<ShareIdNameBean> list, String msg) {
                if (b) {
                    for (ShareIdNameBean model : list) {
                        boolean checked;

                        String index_str;
                        if (_DECIDE.equals("ID")) {
                            index_str = model.getID();
                        } else {
                            index_str = model.getName();
                        }
                        if (_SELECTED.indexOf(index_str) > -1) {
                            checked = true;
                        } else {
                            checked = false;
                        }

                        List<UserBean> users = ApiConstant.db.findAllByWhere(UserBean.class, "ID = '" + model.getID() + "'");
                        if (users.size() > 0) {
                            data_list.add(new TeamMemberDialogBean(users.get(0).getID(), users.get(0).getName(), checked, _ShowIcon, 0));
                        } else {
                            data_list.add(new TeamMemberDialogBean(model.getID(), model.getName(), checked, _ShowIcon, 0));
                        }
                    }
                    mAdapter.setNewData(data_list);
                } else {
                    ToastShow(msg);
                }
                dialog.close();
            }
        });
    }

    private void initIDNameData(String ApiUrl) {
        dialog = new LoadingDialog(MultiSelectDialog.this, "保存中");
        dialog.show();
        DataModel.requestGETMode("ShareIdNameBean", ApiUrl, new InitDataBeanBack<ShareIdNameBean>() {
            @Override
            public void callbak(boolean b, List<ShareIdNameBean> list, String msg) {
                if (b) {
                    for (ShareIdNameBean model : list) {
                        boolean checked = false;

                        String index_str;
                        if (_DECIDE.equals("ID")) {
                            index_str = model.getID();
                        } else {
                            index_str = model.getName();
                        }


                        String[] slelect_arr = _SELECTED.split(",");

                        for (String str : slelect_arr) {

                            if (str.equals(index_str)) {
                                checked = true;
                            } else {
                                checked = false;
                            }
                        }

                        data_list.add(new TeamMemberDialogBean(model.getID(), model.getName(),
                                checked, _ShowIcon, "1".equals(model.getClockIn()) ? 1 : 0));
                    }
                    mAdapter.setNewData(data_list);
                } else {
                    ToastShow(msg);
                }
                dialog.close();
            }
        });
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
