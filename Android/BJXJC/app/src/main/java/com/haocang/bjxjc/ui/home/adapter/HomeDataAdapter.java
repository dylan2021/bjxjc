package com.haocang.bjxjc.ui.home.adapter;

import android.content.Context;

import com.haocang.bjxjc.R;
import com.haocang.bjxjc.ui.home.bean.HomeDataBean;
import com.haocang.commonlib.recyclerView.BaseAdapter;
import com.haocang.commonlib.recyclerView.BaseViewHolder;

/**
 * 创建时间：2019/3/18
 * 编 写 人：ShenC
 * 功能描述：
 */

public class HomeDataAdapter extends BaseAdapter<HomeDataBean> {

    public HomeDataAdapter(Context context) {
        super(context, R.layout.item_home_data);
    }

    @Override
    public void convert(BaseViewHolder holder, HomeDataBean bean) {

        holder.setText(R.id.tv_name, bean.getName())
                .setText(R.id.tv_data, bean.getData());

        switch (bean.getName()) {
            case "全部":
                holder.setImageResource(R.id.tv_icon, R.mipmap.ic_status_0);
                break;
            case "已处置":
                holder.setImageResource(R.id.tv_icon, R.mipmap.ic_status_2);
                break;
            case "处置中":
                holder.setImageResource(R.id.tv_icon, R.mipmap.ic_status_1);
                break;
            case "未处置":
                holder.setImageResource(R.id.tv_icon, R.mipmap.ic_status_0);
                break;
            case "未巡查":
                holder.setImageResource(R.id.tv_icon, R.mipmap.ic_status_1);
                break;
            case "未积水":
                holder.setImageResource(R.id.tv_icon, R.mipmap.ic_status_2);
                break;
            default:
                holder.setImageResource(R.id.tv_icon, R.mipmap.ic_status_0);
                break;

        }


        holder.addOnClickListener(R.id.item_lin);
    }

}
