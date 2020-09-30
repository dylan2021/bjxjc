package com.haocang.bjxjc.ui.tools.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.haocang.bjxjc.R;
import com.haocang.bjxjc.utils.bean.FileBean;
import com.haocang.bjxjc.utils.tools.ApiConstant;
import com.haocang.bjxjc.utils.tools.MyTextUtils;
import net.tsz.afinal.FinalBitmap;
import java.util.ArrayList;
import java.util.List;

/**
 * 创建时间：2019/3/15
 * 编 写 人：ShenC
 * 功能描述：
 */

public class FileListAdapter extends BaseAdapter {
    List<FileBean> mList = new ArrayList<>();
    private Context context;
    private FinalBitmap fb;

    public FileListAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<FileBean> list) {
        mList.clear();
        for (FileBean model : list) {
            mList.add(model);
        }
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public FileBean getItem(int location) {
        return mList.get(location);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        ViewHolder viewHolder = null;
        FileBean model = mList.get(position);
        if (viewHolder == null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_imgdoc, null);
            viewHolder = new ViewHolder();

            viewHolder.imgdocname = convertView.findViewById(R.id.item_imgdoc_name);
            viewHolder.imgdocstatus = convertView.findViewById(R.id.item_imgdoc_status);
            viewHolder.imgdocwrite = convertView.findViewById(R.id.item_imgdoc_writer);
            viewHolder.imgdoctime = convertView.findViewById(R.id.item_imgdoc_time);
            viewHolder.imgdocwriteimge = convertView.findViewById(R.id.item_imgdoc_image);
            viewHolder.item_img_play = convertView.findViewById(R.id.item_img_play);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (model.getF_Memo() == null) {
            viewHolder.imgdocstatus.setText("");
        } else {
            switch (model.getF_Memo()) {
                case "0":
                    viewHolder.imgdocstatus.setVisibility(View.VISIBLE);
                    viewHolder.imgdocstatus.setText("未处理");
                    viewHolder.imgdocstatus.setBackgroundResource(R.drawable.bt_red_01);
                    break;
                case "1":
                    viewHolder.imgdocstatus.setVisibility(View.VISIBLE);
                    viewHolder.imgdocstatus.setText("处理中");
                    viewHolder.imgdocstatus.setBackgroundResource(R.drawable.bt_orange_01);
                    break;
                case "2":
                    viewHolder.imgdocstatus.setVisibility(View.VISIBLE);
                    viewHolder.imgdocstatus.setText("已处理");
                    viewHolder.imgdocstatus.setBackgroundResource(R.drawable.bt_green_01);
                    break;
                default:
                    viewHolder.imgdocstatus.setVisibility(View.GONE);
                    break;
            }

        }

        viewHolder.imgdocname.setText(model.getF_FileName());
        viewHolder.imgdocwrite.setText(model.getF_CreateUserName());
        viewHolder.imgdoctime.setText(model.getF_CreateDate() == null?"":MyTextUtils.toYMDHM(model.getF_CreateDate()));
        String imgUrl = ApiConstant.host + model.getF_FilePath().substring(1);
        fb = FinalBitmap.create(context);
        String thumb_url = "";


        if (model.getF_Category() != null && !model.getF_Category().equals("")) {
            thumb_url = imgUrl.replace(model.getF_Category(), model.getF_Category() + "_Thumb");
        }

        if (model.getF_FileType().equals("mp4") || model.getF_FileType().equals("MP4")) {
            thumb_url = thumb_url.replace("mp4", "jpg");
            thumb_url = thumb_url.replace("MP4", "jpg");
            viewHolder.item_img_play.setVisibility(View.VISIBLE);
        }else
        if (model.getF_FileType().equals("wmv") || model.getF_FileType().equals("WMV")) {
            thumb_url = thumb_url.replace("wmv", "jpg");
            thumb_url = thumb_url.replace("WMV", "jpg");
            viewHolder.item_img_play.setVisibility(View.VISIBLE);
        } else {
            viewHolder.item_img_play.setVisibility(View.GONE);
        }

        fb.display(viewHolder.imgdocwriteimge, thumb_url);

        return convertView;
    }

    class ViewHolder {
        private TextView imgdocname;
        private TextView imgdocstatus;
        private TextView imgdoctime;
        private TextView imgdocwrite;
        private ImageView imgdocwriteimge;
        private ImageView item_img_play;

    }
}
