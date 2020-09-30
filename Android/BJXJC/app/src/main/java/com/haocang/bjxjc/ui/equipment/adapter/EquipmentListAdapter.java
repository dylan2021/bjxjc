package com.haocang.bjxjc.ui.equipment.adapter;

import android.content.Context;

import com.haocang.bjxjc.R;
import com.haocang.bjxjc.ui.equipment.bean.EquipmentBean;
import com.haocang.bjxjc.utils.tools.ApiConstant;
import com.haocang.bjxjc.utils.tools.MyTextUtils;
import com.haocang.commonlib.recyclerView.BaseAdapter;
import com.haocang.commonlib.recyclerView.BaseViewHolder;

import net.tsz.afinal.FinalBitmap;

/**
 * 创建时间：2019/3/5
 * 编 写 人：ShenC
 * 功能描述：
 */

public class EquipmentListAdapter extends BaseAdapter<EquipmentBean> {
    private Context context;
    FinalBitmap fb;

    public EquipmentListAdapter(Context context) {
        super(context, R.layout.item_equipment_list);
        this.context = context;
        fb = FinalBitmap.create(context);
    }

    @Override
    public void convert(BaseViewHolder holder, EquipmentBean bean) {

        holder.setText(R.id.item_name, bean.getFullName())
                .setText(R.id.item_no, bean.getEquNo())
                .setText(R.id.item_EquTypeID, bean.getEquTypeName())
                .setText(R.id.item_Spec, bean.getSpec())
                .setText(R.id.item_Models, bean.getModels())
                .setText(R.id.item_PumpRoomID, bean.getPumpRoomName());

        if(!MyTextUtils.IsNull(bean.getPhoto())){
            fb.display(holder.getView(R.id.item_photo), ApiConstant.host+bean.getPhoto());
        }


        if(bean.getAssetState().equals("1")){
            holder.setVisible(R.id.item_AssetState,true);
            holder.setText(R.id.item_AssetState,"启用")
                    .setBackgroundResource(R.id.item_AssetState,R.drawable.bt_green_01);
        }else if(bean.getAssetState().equals("2")){
            holder.setVisible(R.id.item_AssetState,true);
            holder.setText(R.id.item_AssetState,"封存")
                    .setBackgroundResource(R.id.item_AssetState,R.drawable.bt_orange_01);
        }else if(bean.getAssetState().equals("9")){
            holder.setVisible(R.id.item_AssetState,true);
            holder.setText(R.id.item_AssetState,"报废")
                    .setBackgroundResource(R.id.item_AssetState,R.drawable.bt_gray_02);
        }else{
            holder.setVisible(R.id.item_AssetState,false);
        }

        holder.addOnClickListener(R.id.item_lin);
    }

}