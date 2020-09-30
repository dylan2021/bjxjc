package com.haocang.bjxjc.ui.home.adapter;

import android.content.Context;

import com.haocang.bjxjc.R;
import com.haocang.bjxjc.ui.home.bean.HomeMenuBean;
import com.haocang.commonlib.recyclerView.BaseAdapter;
import com.haocang.commonlib.recyclerView.BaseViewHolder;

/**
 * 创建时间：2019/3/18
 * 编 写 人：ShenC
 * 功能描述：
 */

public class HomeMenuAdapter extends BaseAdapter<HomeMenuBean> {

    public HomeMenuAdapter(Context context) {
        super(context, R.layout.item_home_menu);
    }

    @Override
    public void convert(BaseViewHolder holder, HomeMenuBean bean) {

        holder.setText(R.id.tv, bean.getName())
                .setImageResource(R.id.iv_add,bean.getIcon());

        holder.addOnClickListener(R.id.item_lin);
    }

}
