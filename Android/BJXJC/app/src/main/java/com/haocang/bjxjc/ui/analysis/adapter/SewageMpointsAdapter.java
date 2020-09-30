package com.haocang.bjxjc.ui.analysis.adapter;

import android.content.Context;

import com.haocang.bjxjc.R;
import com.haocang.bjxjc.ui.analysis.bean.SewageBean;
import com.haocang.commonlib.recyclerView.BaseAdapter;
import com.haocang.commonlib.recyclerView.BaseViewHolder;

/**
 * 创建时间：2018/12/21
 * 编 写 人：ShenC
 * 功能描述：
 */

public class SewageMpointsAdapter extends BaseAdapter<SewageBean> {

    public SewageMpointsAdapter(Context context) {
        super(context, R.layout.item_sewage_select);
    }

    @Override
    public void convert(BaseViewHolder holder, SewageBean bean) {

        holder.setText(R.id.tv_name, bean.getName())
                .setChecked(R.id.cb_select, bean.getCheck())
                .addOnClickListener(R.id.tv_name)
                .addOnClickListener(R.id.cb_select);
        if(bean.getShow()){
            holder.setVisible(R.id.item_lin,true);
        }else{
            holder.setVisible(R.id.item_lin,false);
        }
    }

}


