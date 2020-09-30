package com.haocang.bjxjc.ui.reason.adapter;

import android.content.Context;

import com.haocang.bjxjc.R;
import com.haocang.bjxjc.ui.reason.bean.WarningShowBean;
import com.haocang.bjxjc.utils.tools.MyTextUtils;
import com.haocang.commonlib.recyclerView.BaseAdapter;
import com.haocang.commonlib.recyclerView.BaseViewHolder;

/**
 * 创建时间：2019/3/18
 * 编 写 人：ShenC
 * 功能描述：
 */

public class ReasonDataAdapter extends BaseAdapter<WarningShowBean> {

    public ReasonDataAdapter(Context context) {
        super(context, R.layout.item_reason_data);
    }

    @Override
    public void convert(BaseViewHolder holder, WarningShowBean bean) {
        holder.setText(R.id.item_name, bean.getLocation());
        if(MyTextUtils.IsNull(bean.getStatus())){
            holder.setVisible(R.id.tv_status, false);
        }else{
            holder.setVisible(R.id.tv_status, true);
            switch (bean.getStatus()) {
                case "0":
                    if(bean.getIsDelete().equals("N")) {
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
        holder.addOnClickListener(R.id.item_lin);
    }

}
