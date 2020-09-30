package com.haocang.bjxjc.activity.adapter;

import android.content.Context;

import com.haocang.bjxjc.R;
import com.haocang.bjxjc.activity.bean.FileIndexBean;
import com.haocang.commonlib.recyclerView.BaseAdapter;
import com.haocang.commonlib.recyclerView.BaseViewHolder;

/**
 * 图片附件
 */

public class ImageFileAdapter extends BaseAdapter<FileIndexBean> {

    public ImageFileAdapter(Context context) {
        super(context, R.layout.item_pic_list);
    }

    @Override
    public void convert(BaseViewHolder holder, FileIndexBean bean) {
        if (bean.isAdd()) {
            holder.setImageResource(R.id.iv_add, R.mipmap.ic_attachment_add);
            holder.setVisible(R.id.iv_close, false);
        } else {
            holder.setImageFile(R.id.iv_add, bean.getFileUrl());
            holder.setVisible(R.id.iv_close, true);
        }

        holder.addOnClickListener(R.id.iv_close);
        holder.addOnClickListener(R.id.iv_add);
    }

}
