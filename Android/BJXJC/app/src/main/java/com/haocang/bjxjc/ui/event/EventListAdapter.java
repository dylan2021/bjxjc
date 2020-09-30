package com.haocang.bjxjc.ui.event;

import android.content.Context;

import com.haocang.bjxjc.R;
import com.haocang.commonlib.recyclerView.BaseAdapter;
import com.haocang.commonlib.recyclerView.BaseViewHolder;
import com.haocang.bjxjc.utils.tools.MyTextUtils;
import com.haocang.bjxjc.utils.tools.OptionsItemsUtils;

/**
 * 事件列表适配器
 */

public class EventListAdapter extends BaseAdapter<EventBean> {
    public EventListAdapter(Context context) {
        super(context, R.layout.item_event_list);
    }
    @Override
    public void convert(BaseViewHolder holder, EventBean bean) {
        if(bean.getStatus() == 0){
            holder.setText(R.id.item_status,"未处理")
                    .setBackgroundResource(R.id.item_status,R.drawable.bt_red_01);
        }else if(bean.getStatus() == 1){
            holder.setText(R.id.item_status,"处理中")
                    .setBackgroundResource(R.id.item_status,R.drawable.bt_orange_01);
        }else if(bean.getStatus() == 2){
            holder.setText(R.id.item_status,"已处理")
                    .setBackgroundResource(R.id.item_status,R.drawable.bt_green_01);
        }

        holder.setText(R.id.item_1,bean.getMemo()+"<"+ MyTextUtils.GetOpitemValue(OptionsItemsUtils.GetDataItemList("EventType"),1,bean.getTypeID())+">")
                .setText(R.id.item_2, bean.getDeptName())
                .setText(R.id.item_3, MyTextUtils.toYMDHM(bean.getCreateDT()))
                .setText(R.id.item_4, bean.getProcessortorName())
                .setText(R.id.item_5, MyTextUtils.GetOpitemValue(OptionsItemsUtils.GetDataItemList("EventLevel"),1,bean.getLevel()+""));

        holder.addOnClickListener(R.id.item_lin);
    }

}