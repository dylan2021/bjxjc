package com.haocang.bjxjc.ui.monitor.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.haocang.bjxjc.R;
import com.haocang.bjxjc.ui.monitor.bean.ShowDataBean;
import com.haocang.bjxjc.utils.tools.MyTextUtils;
import com.haocang.commonlib.recyclerView.BaseAdapter;
import com.haocang.commonlib.recyclerView.BaseViewHolder;

/**
 * 创建时间：2019/6/14
 * 编 写 人：ShenC
 * 功能描述：
 */

public class MapDataAdapter extends BaseAdapter<ShowDataBean> {


    private Context context;

    public MapDataAdapter(Context context) {
        super(context, R.layout.item_show_mpdata);
        this.context = context;
    }

    @Override
    public void convert(BaseViewHolder holder, ShowDataBean bean) {


        int res = 0,value_color;

        if (bean.getType().equals("PUMP")) {
            if (bean.getStatus().equals("0")) {
                res = R.mipmap.icfont_pump_off;
            } else {
                res = R.mipmap.icfont_pump_on;
            }
            value_color = R.color.black;
        } else {

            if (bean.getStatus() == null || bean.getStatus().equals("0")) {
                res = 0;
                value_color = R.color.black;
            } else {
                switch (bean.getStatus()) {
                    case "Greater"://上限
                        res = R.mipmap.icfont_shangxian;
                        value_color = R.color.red;
                        break;
                    case "Less"://下限
                        res = R.mipmap.icfont_xiaxian;
                        value_color = R.color.red;
                        break;
                    case "TimeOut"://超时
                        res = R.mipmap.icfont_chaoshi;
                        value_color = R.color.red;
                        break;
                    case "Interrupt"://中断
                        res = R.mipmap.icfont_zhongduan;
                        value_color = R.color.red;
                        break;
                    default:
                        value_color = R.color.black;
                        break;
                }

            }
        }

        if(res != 0){
            holder.setBackgroundResource(R.id.tv_2, res)
                    .setVisible(R.id.tv_2,true);
        }else{
            holder.setVisible(R.id.tv_2,false);
        }

        holder.setText(R.id.tv_1, bean.getName())
                .setText(R.id.tv_3, bean.getValues())
                .setTextColor(R.id.tv_3, ContextCompat.getColor(context, value_color))
                .setText(R.id.tv_4, bean.getUnit())
                .setText(R.id.tv_5, MyTextUtils.toYMDHM(bean.getDataDT()));
        if (bean.getIndex() % 2 == 1) {
            holder.setBackgroundColor(R.id.item_lin, ContextCompat.getColor(context, R.color.item_color_1));
        }

        holder.addOnClickListener(R.id.item_lin);
    }

}
