package com.haocang.bjxjc.ui.floodpoint.adapter;

/**
 * 创建时间：2018/12/13
 * 编 写 人：ShenC
 * 功能描述：
 */

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;


import com.haocang.bjxjc.utils.bean.ShareIdNameBean;
import com.haocang.bjxjc.utils.dropdownmenu.adapter.MenuAdapter;
import com.haocang.bjxjc.utils.dropdownmenu.adapter.SimpleTextAdapter;
import com.haocang.bjxjc.utils.dropdownmenu.interfaces.OnFilterDoneListener;
import com.haocang.bjxjc.utils.dropdownmenu.interfaces.OnFilterItemClickListener;
import com.haocang.bjxjc.utils.dropdownmenu.typeview.SingleListView;
import com.haocang.bjxjc.utils.dropdownmenu.util.UIUtil;
import com.haocang.bjxjc.utils.dropdownmenu.view.FilterCheckedTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * author: baiiu
 * date: on 16/1/17 21:14
 * description:
 */
public class FloodPointDropMenuAdapter implements MenuAdapter {
    private final Context mContext;
    private OnFilterDoneListener onFilterDoneListener;
    private String[] titles;

    public FloodPointDropMenuAdapter(Context context, String[] titles, OnFilterDoneListener onFilterDoneListener) {
        this.mContext = context;
        this.titles = titles;
        this.onFilterDoneListener = onFilterDoneListener;
    }

    @Override
    public int getMenuCount() {
        return titles.length;
    }

    @Override
    public String getMenuTitle(int position) {
        return titles[position];
    }

    @Override
    public int getBottomMargin(int position) {
        if (position == 3) {
            return 0;
        }

        return UIUtil.dp(mContext, 140);
    }

    @Override
    public View getView(int position, FrameLayout parentContainer) {
        View view = parentContainer.getChildAt(position);


        switch (position) {
            case 0:
                view = createSingleListView("0");
                break;
            case 1:
                view = createSingleListView("1");
                break;
        }

        return view;
    }

    private View createSingleListView(final String index) {

        SingleListView<ShareIdNameBean> singleListView = new SingleListView<ShareIdNameBean>(mContext)
                .adapter(new SimpleTextAdapter<ShareIdNameBean>(null, mContext) {
                    @Override
                    public String provideText(ShareIdNameBean string) {
                        return string.getName();
                    }

                    protected void initCheckedTextView(FilterCheckedTextView checkedTextView) {
                        int dp = UIUtil.dp(mContext, 15);
                        checkedTextView.setPadding(dp, dp, 0, dp);
                    }
                })
                .onItemClick(new OnFilterItemClickListener<ShareIdNameBean>() {
                    @Override
                    public void onItemClick(ShareIdNameBean item) {
                        onFilterDone(item,index);
                    }
                });

        if(index .equals("0")){
            List<ShareIdNameBean> list = new ArrayList<>();
            list.add(new ShareIdNameBean("","全部"));
           // list.addAll(GetListData.getBranchList());
            singleListView.setList(list, -1);
        }
//        if(index .equals("1")){
//            singleListView.setList(GetListData.getProblemStatusList(), -1);
//        }

        return singleListView;
    }


    private void onFilterDone(ShareIdNameBean item,String index) {
        if (onFilterDoneListener != null) {
            onFilterDoneListener.onFilterDone(item.getID(), item.getName(), index);
        }
    }

}
