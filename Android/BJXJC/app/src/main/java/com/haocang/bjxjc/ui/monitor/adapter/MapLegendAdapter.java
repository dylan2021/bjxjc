package com.haocang.bjxjc.ui.monitor.adapter;

import android.content.Context;

import com.haocang.bjxjc.R;
import com.haocang.bjxjc.ui.monitor.bean.MapLegendBean;
import com.haocang.bjxjc.ui.monitor.bean.ShowDataBean;
import com.haocang.bjxjc.utils.tools.MyTextUtils;
import com.haocang.commonlib.recyclerView.BaseAdapter;
import com.haocang.commonlib.recyclerView.BaseViewHolder;

/**
 * 图例列表适配器
 */

public class MapLegendAdapter extends BaseAdapter<MapLegendBean> {

    public MapLegendAdapter(Context context) {
        super(context, R.layout.item_show_legend);
    }

    @Override
    public void convert(BaseViewHolder holder, MapLegendBean bean) {
        holder.setBackgroundResource(R.id.iv_2, bean.getIcon())
                .setText(R.id.tv_3, bean.getName() + "(" + bean.getNum() + ")");

        if (bean.getChecked()) {
            holder.setBackgroundResource(R.id.iv_1, R.drawable.ic_check_box_yes1);
        } else {
            holder.setBackgroundResource(R.id.iv_1, R.drawable.ic_check_box_no1);
        }

        holder.addOnClickListener(R.id.item_lin);
    }

}
