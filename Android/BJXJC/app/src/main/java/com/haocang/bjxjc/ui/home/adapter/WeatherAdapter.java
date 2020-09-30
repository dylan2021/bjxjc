package com.haocang.bjxjc.ui.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.haocang.bjxjc.R;
import com.haocang.bjxjc.ui.home.bean.WeatherBean;
import com.haocang.bjxjc.ui.home.util.HomeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建时间：2019/3/5
 * 编 写 人：ShenC
 * 功能描述：
 */

public class WeatherAdapter extends BaseAdapter {
    List<WeatherBean> mList = new ArrayList<>();
    private Context context;

    public WeatherAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<WeatherBean> list) {
        mList.clear();
        for (WeatherBean model : list) {
            mList.add(model);
        }
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public WeatherBean getItem(int location) {
        return mList.get(location);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        ViewHolder viewHolder = null;
        WeatherBean model = mList.get(position);
        if (viewHolder == null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_weather_list, null);
            viewHolder = new ViewHolder();

            viewHolder.tv_time = convertView.findViewById(R.id.tv_time);
            viewHolder.tv_wd = convertView.findViewById(R.id.tv_wd);
            viewHolder.tv_memo = convertView.findViewById(R.id.tv_memo);
            viewHolder.iv_img = convertView.findViewById(R.id.iv_img);
            viewHolder.tv_memo_night = convertView.findViewById(R.id.tv_memo_night);
            viewHolder.iv_img_night = convertView.findViewById(R.id.iv_img_night);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String str_low = model.getLow().replace("低温 ", "");
        String str_high = model.getHigh().replace("高温 ", "");
        String str_wendu = str_low + "~" + str_high;
        String str_type = model.getDay().getType();
        String str_fx = model.getDay().getFengxiang();
        String str_fl = model.getDay().getFengli().toString().replace("{#cdata-section=", "").replace("}", "");
        String Memo = str_type + "," + str_fx + "," + str_fl;


        String str_type_night = model.getNight().getType();
        String str_fx_night = model.getNight().getFengxiang();
        String str_fl_night = model.getNight().getFengli().toString().replace("{#cdata-section=", "").replace("}", "");
        String Memo_night = str_type_night + "," + str_fx_night + "," + str_fl_night;


        viewHolder.tv_time.setText(model.getDate());
        viewHolder.tv_wd.setText(str_wendu);

        viewHolder.tv_memo.setText(Memo);
        viewHolder.tv_memo_night.setText(Memo_night);

        viewHolder.iv_img.setImageResource(HomeUtils.GetWeatherImg(str_type));

        viewHolder.iv_img_night.setImageResource(HomeUtils.GetWeatherImg(str_type_night));

        return convertView;
    }

    class ViewHolder {
        private TextView tv_time;
        private TextView tv_wd;
        private TextView tv_memo;
        private ImageView iv_img;
        private TextView tv_memo_night;
        private ImageView iv_img_night;

    }

}