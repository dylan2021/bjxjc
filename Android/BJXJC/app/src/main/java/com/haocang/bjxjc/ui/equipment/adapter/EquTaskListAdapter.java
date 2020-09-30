package com.haocang.bjxjc.ui.equipment.adapter;

import android.content.Context;

import com.haocang.bjxjc.R;
import com.haocang.bjxjc.ui.equipment.bean.EquTaskBean;
import com.haocang.bjxjc.utils.tools.MyTextUtils;
import com.haocang.bjxjc.utils.tools.OptionsItemsUtils;
import com.haocang.commonlib.recyclerView.BaseAdapter;
import com.haocang.commonlib.recyclerView.BaseViewHolder;

/**

 */

public class EquTaskListAdapter extends BaseAdapter<EquTaskBean> {
    private Context context;
    public EquTaskListAdapter(Context context) {

        super(context, R.layout.item_equtask_list);
        this.context = context;
    }

    @Override
    public void convert(BaseViewHolder holder, EquTaskBean bean) {
        String str_EquCycle = MyTextUtils.GetOpitemValue(OptionsItemsUtils.GetDataItemList("EquCycle"), 1, bean.getCycle());

        holder.setText(R.id.item_name, "计划名称："+MyTextUtils.getString(bean.getFullName()))
                 .setText(R.id.item_Content, MyTextUtils.getString(bean.getContent()))
                 .setText(R.id.item_Executor, MyTextUtils.getString(bean.getRealName()))
                .setText(R.id.item_DefectNum, "缺陷:"+bean.getDefectNum())
                .setText(R.id.item_EquCycle, "周期:"+ MyTextUtils.getString(str_EquCycle))
                .setText(R.id.item_RepairDate, MyTextUtils.toYMDHM(bean.getTasksDT()));
        if(MyTextUtils.IsNull(bean.getDefectNum())){
            holder.setVisible(R.id.item_DefectNum,false);
        }else{
            holder.setVisible(R.id.item_DefectNum,true);
        }
        if (bean.getStatus() != null) {
            boolean isshow = false;
            String str_text = MyTextUtils.GetOpitemValue(OptionsItemsUtils.GetDataItemList("EquState"), 1, bean.getStatus());
            int bg = R.drawable.bt_green_01;
            switch (bean.getStatus()) {
                case "0":
                    isshow = true;
                    bg = R.drawable.bt_gray_02;
                    break;
                case "1":
                    isshow = true;
                    bg = R.drawable.bt_orange_01;
                    break;
                case "2":
                    isshow = true;
                    bg = R.drawable.bt_orange_01;
                    break;
                case "3":
                    isshow = true;
                    bg = R.drawable.bt_orange_01;
                    break;
                case "4":
                    isshow = true;
                    bg = R.drawable.bt_gray_02;
                    break;
                case "5":
                    isshow = true;
                    bg = R.drawable.bt_gray_02;
                    break;
                case "9":
                    isshow = true;
                    bg = R.drawable.bt_green_01;
                    break;
            }
            holder.setVisible(R.id.item_AssetState, isshow);
            holder.setText(R.id.item_AssetState, str_text)
                    .setBackgroundResource(R.id.item_AssetState, bg);
        }
        holder.addOnClickListener(R.id.item_lin);
    }

}