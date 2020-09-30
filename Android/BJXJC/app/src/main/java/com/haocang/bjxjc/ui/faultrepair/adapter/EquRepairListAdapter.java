package com.haocang.bjxjc.ui.faultrepair.adapter;

import android.content.Context;

import com.haocang.bjxjc.R;
import com.haocang.bjxjc.ui.faultrepair.bean.EquRepairBean;
import com.haocang.bjxjc.utils.tools.CacheData;
import com.haocang.bjxjc.utils.tools.MyTextUtils;
import com.haocang.bjxjc.utils.tools.OptionsItemsUtils;
import com.haocang.commonlib.recyclerView.BaseAdapter;
import com.haocang.commonlib.recyclerView.BaseViewHolder;

/**
 * 维修工单适配器
 */
public class EquRepairListAdapter extends BaseAdapter<EquRepairBean> {
    private Context context;
    public EquRepairListAdapter(Context context) {
        super(context, R.layout.item_equrepair_list);
        this.context = context;
    }

    @Override
    public void convert(BaseViewHolder holder, EquRepairBean bean) {
        String str_result = MyTextUtils.GetOpitemValue(OptionsItemsUtils.GetDataItemList("RepairResult"), 1, bean.getResult());
        holder.setText(R.id.item_name, MyTextUtils.getString(bean.getF_Title()))
                .setText(R.id.item_equname, MyTextUtils.getString(bean.getEquName()))
                .setText(R.id.item_RepairUserName, MyTextUtils.getString(bean.getRepairUserName()))
                .setText(R.id.item_result, str_result)
                .setText(R.id.item_RepairDate, MyTextUtils.toYMDHM(bean.getRepairDate()));

        if(MyTextUtils.IsNull(bean.getDisposeUserID())){
            if(bean.getReceiveUser().contains(CacheData.UserID())){
                holder.setVisible(R.id.item_mytask,true);
            }else{
                holder.setVisible(R.id.item_mytask,false);
            }
        }else{
            if(bean.getDisposeUserID().equals(CacheData.UserID())){
                holder.setVisible(R.id.item_mytask,true);
            }else{
                holder.setVisible(R.id.item_mytask,false);
            }
        }

        if (bean.getStatus() != null) {
            boolean isshow = false;
            String str_text = MyTextUtils.GetOpitemValue(OptionsItemsUtils.GetDataItemList("RepairState"), 1, bean.getStatus());
            int bg = R.drawable.bt_green_01;
            switch (bean.getStatus()) {
                case "0":
                    isshow = true;
                    bg = R.drawable.bt_gray_02;
                    break;
                case "1":
                    isshow = true;
                    bg = R.drawable.bt_gray_02;
                    break;
                case "2":
                    isshow = true;
                    bg = R.drawable.bt_red_01;
                    break;
                case "3":
                    isshow = true;
                    bg = R.drawable.bt_orange_01;
                    break;
                case "4":
                    isshow = true;
                    bg = R.drawable.bt_orange_01;
                    break;
                case "5":
                    isshow = true;
                    bg = R.drawable.bt_gray_02;
                    break;
                case "6":
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