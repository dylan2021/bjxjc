package com.haocang.commonlib.curve;

import com.haocang.commonlib.otherutil.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建时间：2019/4/15
 * 编 写 人：ShenC
 * 功能描述：
 */

public class CompositeIndexBean {

    private String MpointId;
    private double Value;
    private String DataDT;
    public CompositeIndexBean(String tradeDate,double rate){
        this.DataDT = tradeDate;
        this.Value = rate;
    }

    public String getMpointId() {
        return MpointId;
    }

    public void setMpointId(String mpointId) {
        MpointId = mpointId;
    }

    public double getValue() {
        return Value;
    }

    public void setValue(double value) {
        Value = value;
    }

    public String getDataDT() {
        return TextUtils.formatDateToMD(DataDT,"yyyy-MM-dd HH:mm:ss","yyyy-MM-dd HH:mm");
    }

    public void setDataDT(String dataDT) {
        DataDT = dataDT;
    }

    public static List<CompositeIndexBean> getTestList(){

        List<CompositeIndexBean> list = new ArrayList<>();
        list.add(new CompositeIndexBean("00:00",1.41));
        list.add(new CompositeIndexBean("04:00",2.41));
        list.add(new CompositeIndexBean("08:00",3.46));
        list.add(new CompositeIndexBean("12:00",1.17));
        list.add(new CompositeIndexBean("16:00",3.13));
        list.add(new CompositeIndexBean("20:00",1.27));
        return list;
    }
    public static List<CompositeIndexBean> getTestList1(){

        List<CompositeIndexBean> list = new ArrayList<>();
        list.add(new CompositeIndexBean("00:00",1.23));
        list.add(new CompositeIndexBean("04:00",1.41));
        list.add(new CompositeIndexBean("08:00",2.46));
        list.add(new CompositeIndexBean("12:00",3.17));
        list.add(new CompositeIndexBean("16:00",1.13));
        list.add(new CompositeIndexBean("20:00",3.27));
        return list;
    }
    public static List<CompositeIndexBean> getTestList2(){

        List<CompositeIndexBean> list = new ArrayList<>();
        list.add(new CompositeIndexBean("00:00",1.43));
        list.add(new CompositeIndexBean("04:00",1.41));
        list.add(new CompositeIndexBean("08:00",3.46));
        list.add(new CompositeIndexBean("12:00",1.17));
        list.add(new CompositeIndexBean("16:00",2.13));
        list.add(new CompositeIndexBean("20:00",3.77));
        return list;
    }

}
