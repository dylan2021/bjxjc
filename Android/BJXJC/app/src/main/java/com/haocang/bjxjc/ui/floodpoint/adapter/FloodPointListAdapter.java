package com.haocang.bjxjc.ui.floodpoint.adapter;

import android.content.Context;

import com.haocang.bjxjc.R;
import com.haocang.bjxjc.ui.floodpoint.bean.FloodPointBean;
import com.haocang.commonlib.recyclerView.BaseAdapter;
import com.haocang.commonlib.recyclerView.BaseViewHolder;
import com.haocang.bjxjc.utils.tools.MyTextUtils;
import com.haocang.bjxjc.utils.tools.OptionsItemsUtils;

/**
 * 创建时间：2019/3/5
 * 编 写 人：ShenC
 * 功能描述：
 */

public class FloodPointListAdapter extends BaseAdapter<FloodPointBean> {

    public FloodPointListAdapter(Context context) {
        super(context, R.layout.item_floodpoint_list);
    }

    @Override
    public void convert(BaseViewHolder holder, FloodPointBean bean) {


        holder.setText(R.id.item_1, bean.getYear())
                .setText(R.id.item_2, MyTextUtils.GetOpitemValue(OptionsItemsUtils.GetFloodPointType(),1,bean.getType()))
                .setText(R.id.item_3, bean.getRoad())
                .setText(R.id.item_4, bean.getFloodUserName())
                .setText(R.id.item_5, bean.getLocation());

        holder.addOnClickListener(R.id.item_lin);
    }

}