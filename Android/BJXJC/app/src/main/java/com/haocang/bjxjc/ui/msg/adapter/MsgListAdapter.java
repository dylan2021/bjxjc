package com.haocang.bjxjc.ui.msg.adapter;

import android.content.Context;
import android.util.Log;

import com.haocang.bjxjc.R;
import com.haocang.bjxjc.ui.msg.bean.MsgBean;
import com.haocang.commonlib.recyclerView.BaseAdapter;
import com.haocang.commonlib.recyclerView.BaseViewHolder;
import com.haocang.bjxjc.utils.tools.MyTextUtils;

/**
 * 消息
 */

public class MsgListAdapter extends BaseAdapter<MsgBean> {
    public MsgListAdapter(Context context) {
        super(context, R.layout.item_msg_list);
    }

    @Override
    public void convert(BaseViewHolder holder, MsgBean bean) {
        Log.d(TAG, "消息数据:" + bean.toString());
        if (bean.getType().equals("0")) {
            holder.setText(R.id.tv_name, bean == null ? "公告" : bean.getContext()).
                    setImageResource(R.id.iv_type, R.mipmap.ic_msg_03);
        } else if (bean.getType().equals("1")) {
            holder.setText(R.id.tv_name, "报警消息")
                    .setImageResource(R.id.iv_type, R.drawable.ic_warning_01);
        } else if (bean.getType().equals("2")) {
            holder.setText(R.id.tv_name, "事件任务")
                    .setImageResource(R.id.iv_type, R.mipmap.ic_task_03);
        } else if (bean.getType().equals("3")) {
            holder.setText(R.id.tv_name, "积水点任务")
                    .setImageResource(R.id.iv_type, R.drawable.ic_water_01);
        }

        if (MyTextUtils.IsNull(bean.getBillStatus())) {
            holder.setVisible(R.id.tv_status, false);
        } else {
            holder.setVisible(R.id.tv_status, true);
            switch (bean.getBillStatus()) {
                case "0":
                    holder.setText(R.id.tv_status, "未处理")
                            .setBackgroundResource(R.id.tv_status, R.drawable.bt_red_01);
                    break;
                case "1":
                    holder.setText(R.id.tv_status, "处理中")
                            .setBackgroundResource(R.id.tv_status, R.drawable.bt_orange_01);
                    break;
                case "2":
                    holder.setText(R.id.tv_status, "已处理")
                            .setBackgroundResource(R.id.tv_status, R.drawable.bt_green_01);
                    break;
                default:
                    holder.setVisible(R.id.tv_status, false);
                    break;
            }
        }

        if (MyTextUtils.IsNull(bean.getReadDT())) {
            holder.setVisible(R.id.tv_num, true);
        } else {
            holder.setVisible(R.id.tv_num, false);
        }

        holder.setText(R.id.tv_context, bean.getTitle())
                .setText(R.id.tv_dt, bean.getCreateDT() == null ? "" :
                        MyTextUtils.GetSimpleDtName(bean.getCreateDT()));
    }

}
