package com.haocang.bjxjc.ui.home.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建时间：2019/3/18
 * 编 写 人：ShenC
 * 功能描述：
 */

public class HomeTaskBean {

    private String ID;
    private String Status;
    private String Data;
    private String Name;


    public HomeTaskBean(String ID,String Status,String Data, String Name){
        this.ID = ID;
        this.Status = Status;
        this.Data = Data;
        this.Name = Name;
    }


    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
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

    public static List<HomeTaskBean> getHomeTask(){
        List<HomeTaskBean> list = new ArrayList<>();
        list.add(new HomeTaskBean("1","1","2019-03-18","东城路与同心路交叉路口"));
        list.add(new HomeTaskBean("2","2","2019-03-18","同心路路口"));
        list.add(new HomeTaskBean("3","3","2019-03-18","东城路与同心路交叉路口"));
        list.add(new HomeTaskBean("4","2","2019-03-18","东城路路口"));
        list.add(new HomeTaskBean("5","2","2019-03-18","东城路与同心路交叉路口"));
        list.add(new HomeTaskBean("6","1","2019-03-18","东城路与同心路交叉路口"));
        return list;
    }



}
