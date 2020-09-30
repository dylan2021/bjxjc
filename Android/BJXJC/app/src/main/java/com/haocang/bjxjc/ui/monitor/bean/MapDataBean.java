package com.haocang.bjxjc.ui.monitor.bean;

import java.util.List;

/**
 * 创建时间：2019/6/14
 * 编 写 人：ShenC
 * 功能描述：
 */

public class MapDataBean {


    private String Fcat_ID;
    private String F_ParentId;
    private String F_Category;
    private String FullName;
    private String P_Category;
    private String JsonData;
    private String Fact_LatItude;
    private String Fact_LongItude;
    private String Fact_MX;
    private String Fact_MY;
    private String F_ChannelLevel;
    private String SortCode;
    private String Status;
    private List<PumpBean> PumpList;
    private List<RealDataBean> RealDataList;

    public String getFcat_ID() {
        return Fcat_ID;
    }

    public void setFcat_ID(String fcat_ID) {
        Fcat_ID = fcat_ID;
    }

    public String getF_ParentId() {
        return F_ParentId;
    }

    public void setF_ParentId(String f_ParentId) {
        F_ParentId = f_ParentId;
    }

    public String getF_Category() {
        return F_Category;
    }

    public void setF_Category(String f_Category) {
        F_Category = f_Category;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getP_Category() {
        return P_Category;
    }

    public void setP_Category(String p_Category) {
        P_Category = p_Category;
    }

    public String getJsonData() {
        return JsonData;
    }

    public void setJsonData(String jsonData) {
        JsonData = jsonData;
    }

    public String getFact_LatItude() {
        return Fact_LatItude;
    }

    public void setFact_LatItude(String fact_LatItude) {
        Fact_LatItude = fact_LatItude;
    }

    public String getFact_LongItude() {
        return Fact_LongItude;
    }

    public float getFact_MX() {
        if (Fact_MX == null || Fact_MX.equals("")) return 0;
        return Float.parseFloat(Fact_MX);
    }

    public void setFact_MX(String fact_MX) {
        Fact_MX = fact_MX;
    }

    public String getF_ChannelLevel() {
        return F_ChannelLevel;
    }

    public void setF_ChannelLevel(String f_ChannelLevel) {
        F_ChannelLevel = f_ChannelLevel;
    }

    public float getFact_MY() {
        if (Fact_MY == null || Fact_MY.equals("")) return 0;
        return Float.parseFloat(Fact_MY);
    }

    public void setFact_MY(String fact_MY) {
        Fact_MY = fact_MY;
    }

    public void setFact_LongItude(String fact_LongItude) {
        Fact_LongItude = fact_LongItude;
    }

    public String getSortCode() {
        return SortCode;
    }

    public void setSortCode(String sortCode) {
        SortCode = sortCode;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public List<PumpBean> getPumpList() {
        return PumpList;
    }

    public void setPumpList(List<PumpBean> pumpList) {
        PumpList = pumpList;
    }

    public List<RealDataBean> getRealDataList() {
        return RealDataList;
    }

    public void setRealDataList(List<RealDataBean> realDataList) {
        RealDataList = realDataList;
    }
}
