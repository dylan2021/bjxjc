package com.haocang.bjxjc.ui.waterpoint.adapter;

import android.content.Context;
import android.util.Log;

import com.haocang.bjxjc.R;
import com.haocang.bjxjc.ui.waterpoint.bean.WaterPointBean;
import com.haocang.bjxjc.utils.tools.TextUtil;
import com.haocang.commonlib.recyclerView.BaseAdapter;
import com.haocang.commonlib.recyclerView.BaseViewHolder;
import com.haocang.bjxjc.utils.tools.MyTextUtils;

import org.w3c.dom.Text;

/**
 * 积水库列表适配器
 */

public class WaterPointListAdapter extends BaseAdapter<WaterPointBean> {
    public WaterPointListAdapter(Context context) {
        super(context, R.layout.item_waterpoint_list);
    }

    @Override
    public void convert(BaseViewHolder holder, WaterPointBean bean) {
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

        holder.setText(R.id.item_1, bean.getLocation());
        String startWaterDate = bean.getStartWaterDate();
        String seeperDepth = bean.getSeeperDepth();
        Log.d(TAG,  bean.getArrivalDT()+"数据中心:"+startWaterDate);
        String seeperArea = bean.getSeeperArea();
        holder.setText(R.id.item_2, bean.getArrangementName());
        holder.setText(R.id.item_3, MyTextUtils.toYMDHM(TextUtil.isEmpty(startWaterDate) ?
                bean.getArrivalDT() : startWaterDate));
        holder.setText(R.id.item_4, (TextUtil.isEmpty(seeperDepth) ? 0 : seeperDepth) + "cm");
        holder.setText(R.id.item_5, (TextUtil.isEmpty(seeperArea) ? 0 : seeperArea) + "m³");

        holder.addOnClickListener(R.id.item_lin);
    }

}