package com.haocang.bjxjc.ui.tools.adapter;

import android.content.Context;

import com.haocang.bjxjc.R;
import com.haocang.bjxjc.ui.tools.bean.TeamMemberDialogBean;
import com.haocang.commonlib.recyclerView.BaseAdapter;
import com.haocang.commonlib.recyclerView.BaseViewHolder;

/**
 * 创建时间：2019/3/5
 * 编 写 人：ShenC
 * 功能描述：
 */

public class GetTeamMemberAdapter extends BaseAdapter<TeamMemberDialogBean> {

    public GetTeamMemberAdapter(Context context) {
        super(context, R.layout.item_teammember);
    }

    @Override
    public void convert(BaseViewHolder holder, TeamMemberDialogBean bean) {



        if(bean.isShowIcon()){
            holder.setVisible(R.id.tv_clock,true);
            if(bean.getIcon() == 1){
                holder.setBackgroundResource(R.id.tv_clock,R.drawable.bt_clock_01);
            }else{
                holder.setBackgroundResource(R.id.tv_clock,R.drawable.bt_clock_00);
            }
        }else{
            holder.setVisible(R.id.tv_clock,false);
        }

        holder.setText(R.id.tv, bean.getName());
        if(bean.isCheck()){
            holder.setBackgroundResource(R.id.iv_add,R.drawable.ic_check_box_yes1);
        }else{
            holder.setBackgroundResource(R.id.iv_add,R.drawable.ic_check_box_no1);
        }

        holder.addOnClickListener(R.id.item_lin);
    }

}