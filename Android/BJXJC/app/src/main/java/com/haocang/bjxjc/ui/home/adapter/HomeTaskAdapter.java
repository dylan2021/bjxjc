package com.haocang.bjxjc.ui.home.adapter;

import android.content.Context;

import com.haocang.bjxjc.R;
import com.haocang.bjxjc.ui.home.bean.HomeTaskBean;
import com.haocang.commonlib.recyclerView.BaseAdapter;
import com.haocang.commonlib.recyclerView.BaseViewHolder;

/**
 * 创建时间：2019/3/18
 * 编 写 人：ShenC
 * 功能描述：
 */

public class HomeTaskAdapter extends BaseAdapter<HomeTaskBean> {

    public HomeTaskAdapter(Context context) {
        super(context, R.layout.item_home_task);
    }

    @Override
    public void convert(BaseViewHolder holder, HomeTaskBean bean) {


        if(bean.getStatus().equals("1")){
            holder.setText(R.id.tv_name, bean.getName())
                    .setText(R.id.tv_data, bean.getData())
                    .setText(R.id.tv_status, "普通")
                    .setBackgroundResource(R.id.tv_status,R.drawable.bt_yellow_01);
        }else  if(bean.getStatus().equals("2")){
            holder.setText(R.id.tv_name, bean.getName())
                    .setText(R.id.tv_data, bean.getData())
                    .setText(R.id.tv_status, "重要")
                    .setBackgroundResource(R.id.tv_status,R.drawable.bt_orange_01);
        }else  if(bean.getStatus().equals("3")){
            holder.setText(R.id.tv_name, bean.getName())
                    .setText(R.id.tv_data, bean.getData())
                    .setText(R.id.tv_status, "严重")
                    .setBackgroundResource(R.id.tv_status,R.drawable.bt_red_01);
        }


        holder.addOnClickListener(R.id.item_lin);
    }

}
