package com.haocang.bjxjc.ui.home.bean;

import com.haocang.bjxjc.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建时间：2019/3/18
 * 编 写 人：ShenC
 * 功能描述：
 */

public class HomeMenuBean {

    private int Icon;
    private String Name;

    public HomeMenuBean(int icon,String name){
        this.Icon = icon;
        this.Name = name;
    }


    public int getIcon() {
        return Icon;
    }

    public void setIcon(int icon) {
        Icon = icon;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public static List<HomeMenuBean> getHomeMenu(){
        List<HomeMenuBean> list = new ArrayList<>();
        list.add(new HomeMenuBean(R.mipmap.ico_homemenu_zhjc,"综合监测"));
        list.add(new HomeMenuBean(R.mipmap.ico_homemenu_jsjc,"积水监测"));
        list.add(new HomeMenuBean(R.mipmap.ico_homemenu_shijian,"事件监测"));
        list.add(new HomeMenuBean(R.mipmap.ico_homemenu_quxian,"数据曲线"));
        list.add(new HomeMenuBean(R.mipmap.ico_homemenu_yyd,"易淹点"));
        list.add(new HomeMenuBean(R.mipmap.ico_homemenu_gongyi,"工艺画面"));
        list.add(new HomeMenuBean(R.mipmap.ico_homemenu_shebei,"设备档案"));
        list.add(new HomeMenuBean(R.mipmap.ico_homemenu_weixiu,"维修工单"));
        list.add(new HomeMenuBean(R.mipmap.ico_homemenu_baoyang,"保养工单"));
        list.add(new HomeMenuBean(R.mipmap.ico_home_jsk,"历史积水库"));
        return list;
    }


}
