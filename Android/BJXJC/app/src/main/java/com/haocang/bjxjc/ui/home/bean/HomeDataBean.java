package com.haocang.bjxjc.ui.home.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建时间：2019/3/18
 * 编 写 人：ShenC
 * 功能描述：
 */

public class HomeDataBean {

    private String Data;
    private String Name;

    public HomeDataBean(String Data, String name){
        this.Data = Data;
        this.Name = name;
    }


    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public static List<HomeDataBean> getHomeData_zb(){
        List<HomeDataBean> list = new ArrayList<>();
        list.add(new HomeDataBean("2365932(m³)","今日进水总量"));
        list.add(new HomeDataBean("2302562(m³)","今日出水总量"));
        list.add(new HomeDataBean("100/213(个)","今日工单完成"));
        list.add(new HomeDataBean("165(件)","今日热线投诉"));
        return list;
    }

    public static List<HomeDataBean> getHomeData_sj(){
        List<HomeDataBean> list = new ArrayList<>();
        list.add(new HomeDataBean("33处","全部"));
        list.add(new HomeDataBean("21处","处置中"));
        list.add(new HomeDataBean("11处","待处理"));
        list.add(new HomeDataBean("1处","已处理"));
        return list;
    }

    public static List<HomeDataBean> getHomeData_jsd(){
        List<HomeDataBean> list = new ArrayList<>();
        list.add(new HomeDataBean("26处","全部"));
        list.add(new HomeDataBean("12处","处置中"));
        list.add(new HomeDataBean("5处","待处理"));
        list.add(new HomeDataBean("9处","已处理"));
        return list;
    }

}
