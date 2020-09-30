package com.haocang.bjxjc.ui.waterpoint.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haocang.bjxjc.R;
import com.haocang.bjxjc.ui.waterpoint.bean.WaterFlowBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建时间：2019/3/5
 * 编 写 人：ShenC
 * 功能描述：
 */

public class WaterFlowAdapter extends android.widget.BaseAdapter {
    List<WaterFlowBean> mList = new ArrayList<>();
    private Context context;
    public WaterFlowAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<WaterFlowBean> list) {
        mList.clear();
        for (WaterFlowBean model : list) {
            mList.add(model);
        }
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public WaterFlowBean getItem(int location) {
        return mList.get(location);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        ViewHolder viewHolder = null;
        WaterFlowBean model = mList.get(position);
        if (viewHolder == null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.item_waterflow_list, null);
            viewHolder = new ViewHolder();

            viewHolder.tv_time = convertView.findViewById(R.id.tv_time);
            viewHolder.tv_dt = convertView.findViewById(R.id.tv_dt);
            viewHolder.tv_msg = convertView.findViewById(R.id.tv_msg);
            viewHolder.ll_img = convertView.findViewById(R.id.ll_img);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_dt.setText(model.getCreateDT().substring(0,10));
        viewHolder.tv_time.setText(model.getCreateDT().substring(11,16));
        viewHolder.tv_msg.setText(model.getContext());

        if(model.getContext().indexOf("无法处置,原因") > -1){
            viewHolder.tv_msg.setText(model.getContext()+"[附件:"+model.getF_FileSize()+",点击查看]");
            viewHolder.tv_msg.setBackgroundResource(R.drawable.bt_flow_msg_02);
        }else{
            viewHolder.tv_msg.setText(model.getContext());
            viewHolder.tv_msg.setBackgroundResource(R.drawable.bt_flow_msg_01);
        }

        if(mList.size() == 1){
            viewHolder.ll_img.setBackgroundResource(R.mipmap.ic_flow_onenode);
        }else {
            if (position == 0) {
                viewHolder.ll_img.setBackgroundResource(R.mipmap.ic_flow_end);
            } else if (position == mList.size() - 1) {
                viewHolder.ll_img.setBackgroundResource(R.mipmap.ic_flow_start);
            } else {
                viewHolder.ll_img.setBackgroundResource(R.mipmap.ic_flow_in);
            }
        }
        return convertView;
    }

    class ViewHolder {
        private TextView tv_dt;
        private TextView tv_time;
        private TextView tv_msg;
        private LinearLayout ll_img;

    }

}