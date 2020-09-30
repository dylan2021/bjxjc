package com.haocang.bjxjc.ui.msg.adapter;

import android.content.Context;

import com.haocang.bjxjc.R;
import com.haocang.bjxjc.ui.waterpoint.bean.WaterPointBean;
import com.haocang.commonlib.recyclerView.BaseAdapter;
import com.haocang.commonlib.recyclerView.BaseViewHolder;
import com.haocang.bjxjc.utils.tools.CacheData;
import com.haocang.bjxjc.utils.tools.MyTextUtils;

/**
 * 任务
 */

public class TaskListAdapter extends BaseAdapter<WaterPointBean> {

    public TaskListAdapter(Context context) {
        super(context, R.layout.item_task_list);
    }

    @Override
    public void convert(BaseViewHolder holder, WaterPointBean bean) {
        holder.setText(R.id.tv_name, bean.getLocation())
                .setImageResource(R.id.iv_type, R.mipmap.ic_task_03);

        if (MyTextUtils.IsNull(bean.getStatus())) {
            holder.setVisible(R.id.tv_status, false);
        } else {
            holder.setVisible(R.id.tv_status, true);
            switch (bean.getStatus()) {
                case "0":
                    if (bean.getIsDelete().equals("N")) {
                        holder.setText(R.id.tv_status, "未巡查")
                                .setBackgroundResource(R.id.tv_status, R.drawable.bt_gray_02);
                    } else if (bean.getIsDelete().equals("Y")) {
                        holder.setText(R.id.tv_status, "未积水")
                                .setBackgroundResource(R.id.tv_status, R.drawable.bt_green_01);
                    } else {
                        holder.setText(R.id.tv_status, "未处置")
                                .setBackgroundResource(R.id.tv_status, R.drawable.bt_red_01);
                    }
                    break;
                case "1":
                    holder.setText(R.id.tv_status, "处置中")
                            .setBackgroundResource(R.id.tv_status, R.drawable.bt_orange_01);
                    break;
                case "2":
                    holder.setText(R.id.tv_status, "已处置")
                            .setBackgroundResource(R.id.tv_status, R.drawable.bt_green_01);
                    break;
                default:
                    holder.setVisible(R.id.tv_status, false);
                    break;
            }

        }
        if (MyTextUtils.IsNull(bean.getCreateDT())) {
            holder.setVisible(R.id.tv_num, false);
        } else {
            holder.setVisible(R.id.tv_num, false);
        }

        if (bean.getIsUrgentHandle() == 0) {
            holder.setVisible(R.id.tv_IsUrgentHandle, false);
        } else {
            holder.setVisible(R.id.tv_IsUrgentHandle, true);
        }

        if (bean.getDisposingPerson() != null && bean.getDisposingPerson().contains(CacheData.UserID())) {
            holder.setVisible(R.id.tv_mytast, true);
        } else {
            holder.setVisible(R.id.tv_mytast, false);
        }

        holder.setText(R.id.tv_context, bean.getSeeperReason())
                .setText(R.id.tv_dt, bean.getStartWaterDate() == null ? "" : MyTextUtils.GetSimpleDtName(bean.getStartWaterDate()));
    }

}
