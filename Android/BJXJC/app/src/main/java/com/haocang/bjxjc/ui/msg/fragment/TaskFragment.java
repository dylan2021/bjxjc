package com.haocang.bjxjc.ui.msg.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.haocang.bjxjc.R;
import com.haocang.bjxjc.requestcenter.DataModel;
import com.haocang.bjxjc.utils.tools.TextUtil;
import com.haocang.commonlib.network.interfaces.InitDataBeanBack;
import com.haocang.commonlib.network.interfaces.InitDataCallBack;
import com.haocang.bjxjc.ui.home.interfaces.SendMsaFromFragment;
import com.haocang.bjxjc.ui.msg.adapter.TaskListAdapter;
import com.haocang.bjxjc.ui.waterpoint.activity.WaterPointSureActivity;
import com.haocang.bjxjc.ui.waterpoint.activity.WaterPointDetailActivity;
import com.haocang.bjxjc.ui.waterpoint.bean.WaterPointBean;
import com.haocang.bjxjc.utils.bean.ArrangementBaseInfoBean;
import com.haocang.bjxjc.utils.bean.ProvinceBean;
import com.haocang.bjxjc.utils.tools.ApiConstant;
import com.haocang.bjxjc.utils.tools.UrlConst;
import com.haocang.bjxjc.utils.tools.CacheData;
import com.haocang.bjxjc.utils.tools.MyTextUtils;
import com.haocang.bjxjc.utils.tools.OptionsItemsUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yanzhenjie.recyclerview.swipe.SwipeItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.HashMap;
import java.util.List;

/**
 * 任务
 */

public class TaskFragment extends Fragment {
    private View view;
    private Context context;
    private TextView tv_name;
    private SwipeMenuRecyclerView recyclerview;
    private RefreshLayout refreshLayout;
    private TaskListAdapter mListAdapter;
    private SendMsaFromFragment sendMessage;
    private LinearLayout lin_more;
    private String ls_Reasonid = "";

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        sendMessage = (SendMsaFromFragment) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_msg, container, false);
        context = this.getActivity();
        initView();
        initEvent();
        refreshLayout.autoRefresh();
        return view;
    }

    private boolean isHidden;
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        isHidden = hidden;
        if (!hidden) {
            initData();
        }
    }

    private void initView() {
        tv_name = view.findViewById(R.id.tv_name);
        tv_name.setText("我的任务");
        recyclerview = view.findViewById(R.id.recyclerview);
        refreshLayout = view.findViewById(R.id.refreshLayout);
        lin_more = view.findViewById(R.id.lin_more);
        lin_more.setVisibility(View.VISIBLE);
    }

    private void initEvent() {
        mListAdapter = new TaskListAdapter(context);
        recyclerview.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL) {
            @Override
            public boolean canScrollVertically() {
                return true;//是否允许滑动
            }
        });
        lin_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter(OptionsItemsUtils.GetTaskReasonList(), 1);
            }

        });
        recyclerview.setSwipeMenuCreator(new SwipeMenuCreator() {
            @Override
            public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
                SwipeMenuItem Item = new SwipeMenuItem(context)
                        .setBackground(R.color.grey)
                        .setText("标记已读")
                        .setTextColor(Color.WHITE)
                        .setTextSize(14)
                        // .setImage(R.drawable.icon_delete_02)
                        .setHeight(ViewGroup.LayoutParams.MATCH_PARENT)//设置高，这里使用match_parent，就是与item的高相同
                        .setWidth(250);//设置宽
                SwipeMenuItem deleteItem = new SwipeMenuItem(context)
                        .setBackground(R.color.red)
                        .setText("不再显示")
                        .setTextColor(Color.WHITE)
                        .setTextSize(14)
                        // .setImage(R.drawable.icon_delete_02)
                        .setHeight(ViewGroup.LayoutParams.MATCH_PARENT)//设置高，这里使用match_parent，就是与item的高相同
                        .setWidth(200);//设置宽
                // swipeRightMenu.addMenuItem(Item);//设置右边的侧滑
                // swipeRightMenu.addMenuItem(deleteItem);//设置右边的侧滑
            }
        });
        //设置侧滑菜单的点击事件
        recyclerview.setSwipeMenuItemClickListener(new SwipeMenuItemClickListener() {
            @Override
            public void onItemClick(SwipeMenuBridge menuBridge) {
                menuBridge.closeMenu();
                //int direction = menuBridge.getDirection(); // 左侧还是右侧菜单。0是左，右是1，暂时没有用到
                int adapterPosition = menuBridge.getAdapterPosition(); // RecyclerView的Item的position。
                int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。
                WaterPointBean bean = mListAdapter.getItem(adapterPosition);
                // UpdateMsg(bean.getID(), menuPosition);
            }
        });
        recyclerview.setSwipeItemClickListener(new SwipeItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                WaterPointBean bean = mListAdapter.getItem(position);
                if (bean.getIsDelete().equals("Y")) {//未积水
                    Intent intent = new Intent(context, WaterPointSureActivity.class);
                    intent.putExtra("ID", bean.getID());
                    startActivity(intent);
                } else if (bean.getIsDelete().equals("N")) {//未确认
                    Intent intent = new Intent(context, WaterPointSureActivity.class);
                    intent.putExtra("ID", bean.getID());
                    startActivity(intent);
                } else {//已经积水的
                    CacheData.WaterPoint_UPDATE = true;
                    CacheData.mWaterPointID = bean.getID();
                    Intent it = new Intent(context, WaterPointDetailActivity.class);
                    startActivity(it);
                }
            }
        });

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                sendMessage.RefreshNumber();//主页消息数量刷新
                ApiConstant.PAGE_LIMIT = ApiConstant.PAGE_SHOW_LIMIT;
                refreshlayout.finishRefresh(20000/*,false*/);//传入false表示刷新失败
                initData();
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore(20000/*,false*/);//传入false表示加载失败
                ApiConstant.PAGE_LIMIT += ApiConstant.PAGE_SHOW_LIMIT;
                initData();
            }
        });
        recyclerview.setAdapter(mListAdapter);
    }

    private void initData() {
        String ApiUrl = ApiConstant.host
                + UrlConst.Api_GetWaterPointByUserID
                + "?limit=" + ApiConstant.PAGE_LIMIT
                + "&userid=" + CacheData.UserID()
                + "&reasonid=" + ls_Reasonid;

        DataModel.requestGETMode("WaterPointBean", ApiUrl, new InitDataBeanBack<WaterPointBean>() {
            @Override
            public void callbak(boolean b, List<WaterPointBean> list, String msg) {
                if (b) {
                    if (TextUtil.isEmptyList(list) && !isHidden) {
                        ToastShow(getString(R.string.no_data));//请求任务数据失败
                    }
                    mListAdapter.setNewData(list);
                } else {
                    if (!isHidden) {
                        ToastShow(getString(R.string.request_failure));
                    }
                }
                if (refreshLayout != null) {
                    refreshLayout.finishRefresh(0);
                    refreshLayout.finishLoadmore(0);
                }
            }
        });

    }

    //更新消息的状态
    private void UpdateMsg(String MsgID, int Type) {
        HashMap<String, Object> maps = new HashMap<>();
        maps.put("MsgID", MsgID);
        maps.put("UserID", CacheData.UserID());
        if (Type == 0) {
            maps.put("ReadDT", MyTextUtils.getNowDatatoString());
        } else if (Type == 1) {
            maps.put("IsDelete", "1");
        }
        DataModel.requestPOST(context, ApiConstant.host + UrlConst.Api_SaveMsgStatus, maps, new InitDataCallBack() {
            @Override
            public void callbak(boolean b, String string) {
                if (b) {
                    initData();
                    sendMessage.RefreshNumber();//主页消息数量刷新
                } else {
                    //ToastShow("修改状态失败," + string);
                }
            }
        });
    }

    //接口回调,接受HomeActivity的指令
    public void RefreshPageData() {
        initData();
        initArrangementBaseInfoList();
        Log.d("进来任务", "");
    }

    private void initArrangementBaseInfoList() {
        String ApiUrl = ApiConstant.host + UrlConst.Api_List_GetArrangementBaseInfoList;
        DataModel.requestGETMode("ArrangementBaseInfoBean", ApiUrl, new InitDataBeanBack<ArrangementBaseInfoBean>() {
            @Override
            public void callbak(boolean b, List<ArrangementBaseInfoBean> list, String msg) {
                if (b) {
                    ApiConstant.db.deleteByWhere(ArrangementBaseInfoBean.class, "name like '%%'");
                    for (ArrangementBaseInfoBean model : list) {
                        ArrangementBaseInfoBean mm = new ArrangementBaseInfoBean();
                        mm.setID(model.getID());
                        mm.setName(model.getName());
                        ApiConstant.db.save(mm);
                    }
                }
            }
        });
    }

    //筛选按钮
    private void filter(final List<ProvinceBean> items, final int pos) {
        OptionsPickerView.Builder builder = new OptionsPickerView.Builder(context, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                ls_Reasonid = items.get(options1).getDescription();
                String ls_ReasonName = items.get(options1).getName();
                if (MyTextUtils.IsNull(ls_Reasonid)) {
                    tv_name.setText("我的任务");
                } else {
                    tv_name.setText(ls_ReasonName + "任务");
                }
                initData();
            }
        });
        OptionsPickerView<ProvinceBean> pickerView = new OptionsPickerView<>(builder);
        pickerView.setPicker(items);
        pickerView.show();
    }

    protected void ToastShow(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

}
