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

import com.haocang.bjxjc.R;
import com.haocang.bjxjc.requestcenter.DataModel;
import com.haocang.bjxjc.utils.tools.TextUtil;
import com.haocang.commonlib.network.interfaces.InitDataBeanBack;
import com.haocang.commonlib.network.interfaces.InitDataCallBack;
import com.haocang.bjxjc.ui.home.interfaces.SendMsaFromFragment;
import com.haocang.bjxjc.ui.msg.activity.MsgDetailActivity;
import com.haocang.bjxjc.ui.msg.adapter.MsgListAdapter;
import com.haocang.bjxjc.ui.msg.bean.MsgBean;
import com.haocang.bjxjc.utils.tools.ApiConstant;
import com.haocang.bjxjc.utils.tools.UrlConst;
import com.haocang.bjxjc.utils.tools.CacheData;
import com.haocang.bjxjc.utils.tools.MyTextUtils;
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
 * 创建时间：2018/12/21
 * 编 写 人：ShenC
 * 功能描述：消息界面
 */
public class MsgFragment extends Fragment {
    private View view;
    private Context context;
    private TextView tv_name;
    private SwipeMenuRecyclerView recyclerview;
    private RefreshLayout refreshLayout;
    private MsgListAdapter mMsgListAdapter;
    private SendMsaFromFragment sendMessage;
    private LinearLayout lin_more;

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
        tv_name.setText("我的消息");

        recyclerview = view.findViewById(R.id.recyclerview);
        refreshLayout = view.findViewById(R.id.refreshLayout);
        lin_more = view.findViewById(R.id.lin_more);
        lin_more.setVisibility(View.INVISIBLE);
    }

    private void initEvent() {
        mMsgListAdapter = new MsgListAdapter(context);
        recyclerview.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL) {
            @Override
            public boolean canScrollVertically() {
                return true;//是否允许滑动
            }
        });

        //设置侧滑菜单
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
                swipeRightMenu.addMenuItem(Item);//设置右边的侧滑
                swipeRightMenu.addMenuItem(deleteItem);//设置右边的侧滑
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
                MsgBean bean = mMsgListAdapter.getItem(adapterPosition);
                UpdateMsg(bean.getID(), menuPosition);
            }
        });

        recyclerview.setSwipeItemClickListener(new SwipeItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                MsgBean bean = mMsgListAdapter.getItem(position);
                if (bean.getType().equals("0")) {
                    UpdateMsg(bean.getID(), 0);//修改消息已读状态
                    Intent it = new Intent(context, MsgDetailActivity.class);
                    it.putExtra("title", bean.getTitle());
                    it.putExtra("user", bean.getCreator());
                    it.putExtra("dt", bean.getCreateDT());
                    it.putExtra("context", bean.getContext());
                    startActivity(it);
                }
            }
        });
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                sendMessage.RefreshNumber();//主页消息数量刷新
                refreshlayout.finishRefresh(20000/*,false*/);//传入false表示刷新失败
                ApiConstant.PAGE_LIMIT = ApiConstant.PAGE_SHOW_LIMIT;
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

        recyclerview.setAdapter(mMsgListAdapter);
    }

    private void initData() {
        String ApiUrl = ApiConstant.host
                + UrlConst.Api_Msg
                + "?limit=" + ApiConstant.PAGE_LIMIT
                + "&userid=" + CacheData.UserID()
                + "&type=msg";

        DataModel.requestGETMode("MsgBean", ApiUrl, new InitDataBeanBack<MsgBean>() {
            @Override
            public void callbak(boolean b, List<MsgBean> list, String msg) {
                if (b) {
                    if (TextUtil.isEmptyList(list) && !isHidden) {
                        ToastShow(getString(R.string.no_data));//请求任务数据失败
                    }
                    mMsgListAdapter.setNewData(list);
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

        String url = ApiConstant.host + UrlConst.Api_SaveMsgStatus;
        Log.d("消息", "消息更新:" + url);
        Log.d("消息", "消息参数:" + maps.toString());
        DataModel.requestPOST(context, url, maps, new InitDataCallBack() {
            @Override
            public void callbak(boolean b, String string) {
                Log.d("消息", b + "消息更新:" + string);
                if (b) {
                    initData();
                    sendMessage.RefreshNumber();//主页消息数量刷新
                } else {
                    //ToastShow("消息已读状态更新失败");
                }
            }
        });
    }

    //接口回调,接受HomeActivity的指令
    public void RefreshPageData() {
        initData();
    }

    protected void ToastShow(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

}
