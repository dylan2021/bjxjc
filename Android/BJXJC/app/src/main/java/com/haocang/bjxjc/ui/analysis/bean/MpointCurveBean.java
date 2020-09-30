package com.haocang.bjxjc.ui.analysis.bean;


import com.haocang.commonlib.curve.CompositeIndexBean;

import java.util.List;

/**
 * 创建时间：2019/1/22
 * 编 写 人：ShenC
 * 功能描述：
 */

public class MpointCurveBean {

    private String SewageFacID;
    private String MpointId;
    private String MpointName ;
    private String m_Avg;
    private String m_Max ;
    private String m_Min ;
    private List<CompositeIndexBean> HisDataList;

    public String getSewageFacID() {
        return SewageFacID;
    }

    public void setSewageFacID(String sewageFacID) {
        SewageFacID = sewageFacID;
    }

    public String getMpointId() {
        return MpointId;
    }

    public void setMpointId(String mpointId) {
        MpointId = mpointId;
    }

    public String getMpointName() {
        return MpointName;
    }

    public void setMpointName(String mpointName) {
        MpointName = mpointName;
    }

    public String getM_Avg() {
        return m_Avg;
    }

    public void setM_Avg(String m_Avg) {
        this.m_Avg = m_Avg;
    }

    public String getM_Max() {
        return m_Max;
    }

    public void setM_Max(String m_Max) {
        this.m_Max = m_Max;
    }

    public String getM_Min() {
        return m_Min;
    }

    public void setM_Min(String m_Min) {
        this.m_Min = m_Min;
    }

    public List<CompositeIndexBean> getHisDataList() {
        return HisDataList;
    }

    public void setHisDataList(List<CompositeIndexBean> hisDataList) {
        HisDataList = hisDataList;
    }
}
