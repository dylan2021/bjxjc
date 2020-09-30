package com.haocang.bjxjc.KeepOrder;

import android.content.Context;

import com.haocang.bjxjc.R;
import com.haocang.bjxjc.ui.equipment.bean.EquTaskBean;
import com.haocang.bjxjc.utils.bean.ProvinceBean;
import com.haocang.bjxjc.utils.tools.MyTextUtils;
import com.haocang.bjxjc.utils.tools.OptionsItemsUtils;
import com.haocang.bjxjc.utils.tools.TextUtil;
import com.haocang.commonlib.recyclerView.BaseAdapter;
import com.haocang.commonlib.recyclerView.BaseViewHolder;

import java.util.ArrayList;

/**
 * 保养工单
 */

public class KeepOrderListAdapter extends BaseAdapter<EquTaskBean> {
    private Context context;

    public KeepOrderListAdapter(Context context) {
        super(context, R.layout.item_keep_order_list);
        this.context = context;
    }

    @Override
    public void convert(BaseViewHolder holder, EquTaskBean bean) {
        String str_EquCycle = MyTextUtils.GetOpitemValue(OptionsItemsUtils.GetDataItemList("EquCycle"), 1, bean.getCycle());

        holder.setText(R.id.item_name, MyTextUtils.getString(bean.getFullName()))
                .setText(R.id.item_equ_name, MyTextUtils.getString(bean.getEquName()))
                .setText(R.id.item_Content, MyTextUtils.getString(bean.getContent()))
                .setText(R.id.item_Executor, MyTextUtils.getString(bean.getRealName()))

                .setText(R.id.item_EquCycle, "周期:" + MyTextUtils.getString(str_EquCycle))
                .setText(R.id.item_RepairDate, MyTextUtils.toYMDHM(bean.getTasksDT()));
        if (bean.getStatus() != null) {
            //是否逾期
            String isOverdueStr = bean.getIsOverdue();

            boolean isOverdue = !(TextUtil.isEmpty(isOverdueStr) || "0".equals(isOverdueStr));
            holder.setVisible(R.id.item_over_tv, isOverdue);
            if (isOverdue) {
                holder.setText(R.id.item_over_tv, "已逾期");
            }

            boolean isshow = false;
            String status = bean.getStatus();
            String str_text = MyTextUtils.
                    GetOpitemValue(OptionsItemsUtils.getKeepStatusItems(),
                            1, status);
            int bg = R.drawable.bt_green_01;
            switch (bean.getStatus()) {
                case "0":
                    isshow = true;
                    bg = R.drawable.bt_gray_02;
                    break;
                case "1":
                    isshow = true;
                    bg = R.drawable.bt_orange_01;
                    break;
                case "2":
                    isshow = true;
                    bg = R.drawable.bt_orange_01;
                    break;
                case "3":
                    isshow = true;
                    bg = R.drawable.bt_orange_01;
                    break;
                case "4":
                    isshow = true;
                    bg = R.drawable.bt_orange_01;
                    break;
                case "5":
                    isshow = true;
                    bg = R.drawable.bt_gray_02;
                    break;
                case "6":
                    isshow = true;
                    bg = R.drawable.bt_gray_02;
                case "7":
                    isshow = true;
                    bg = R.drawable.bt_green_01;
                    break;
            }
            holder.setVisible(R.id.item_AssetState, isshow);
            holder.setText(R.id.item_AssetState, str_text)
                    .setBackgroundResource(R.id.item_AssetState, bg);
        }
        holder.addOnClickListener(R.id.item_lin);
    }

}