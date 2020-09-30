package com.haocang.bjxjc.ui.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.haocang.commonlib.pagegrid.PageGridView;
import com.haocang.bjxjc.R;
import com.haocang.bjxjc.ui.home.bean.HomeMenuBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建时间：2019/3/18
 * 编 写 人：ShenC
 * 功能描述：
 */

public class HomePageMenuAdapter extends PageGridView.PagingAdapter<HomePageMenuAdapter.MyVH> implements PageGridView.OnItemClickListener {

    List<HomeMenuBean> mData = new ArrayList<>();
    private Context context;
    private int width;
    private PageGridViewListener mPageGridViewListener;
    public HomePageMenuAdapter(List<HomeMenuBean> data,Context context) {
        mData.clear();
        this.mData.addAll(data);
        this.context = context;
        this.width = context.getResources().getDisplayMetrics().widthPixels / 4;
    }

    @Override
    public MyVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_home_menu, parent, false);
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = width;
        params.width = width;
        view.setLayoutParams(params);
        return new MyVH(view);
    }
    @Override
    public void onBindViewHolder(MyVH holder, int position) {

        try {
            holder.icon.setVisibility(View.VISIBLE);
            holder.tv_title.setVisibility(View.VISIBLE);
            holder.icon.setImageResource(mData.get(position).getIcon());
            holder.tv_title.setText(mData.get(position).getName());
        }catch (Exception e){
            holder.icon.setVisibility(View.GONE);
            holder.tv_title.setVisibility(View.GONE);
        }
    }



    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public List getData() {
        return mData;
    }

    @Override
    public Object getEmpty() {
        return "";
    }

    @Override
    public void onItemClick(PageGridView pageGridView, int position) {
      try {
          mPageGridViewListener.onItemClickChanged(pageGridView, position, mData.get(position));
      }catch (Exception e){
          mPageGridViewListener.onItemClickChanged(pageGridView, position, null);
      }
    }

    public void setPageGridViewListener(PageGridViewListener pageGridViewListener) {
        this.mPageGridViewListener = pageGridViewListener;
    }

    class MyVH extends RecyclerView.ViewHolder {
        public TextView tv_title;
        public ImageView icon;
        public MyVH(View itemView) {
            super(itemView);
            tv_title =  itemView.findViewById(R.id.tv);
            icon=  itemView.findViewById(R.id.iv_add);
        }
    }

}


