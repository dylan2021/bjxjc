package com.haocang.bjxjc.ui.waterpoint.bean;

/**
 * 创建时间：2019/3/12
 * 编 写 人：ShenC
 * 功能描述：
 */

public class WaterPointBean {

    private String ID;
    private String Year;
    private String SeepeNo;
    private String ManagementOffice;
    private String StreetOperation;
    private String Community;
    private String Road;
    private String Location;
    private String FloodUser;
    private String FloodUserName;
    private String DistrictPhone;
    private String ArrivalDT;
    private String SeeperArea;
    private String SeeperDepth;
    private String StartWaterDate;
    private String Creator;
    private String CreateDT;
    private String SeeperSource;
    private String IsCarPass;
    private String SeeperLevel;
    private String SeeperTime;
    private String Radius;
    private String SeeperReason;
    private String Memo;
    private float X;
    private float Y;
    private float Z;
    private float MX;
    private float MY;
    private String WarningID;
    private String DisposingPerson;
    private String DisposingPersonName;
    private String EndWaterDate;
    private String Status;
    private String DeliveryUser;
    private String DeliveryCar;
    private String WaterResult;
    private String CorrectiveMeasures;
    private String IsDelete;
    private String ArrangementID;
    private String ArrangementName;
    private int IsUrgentHandle;//是否紧急处理
    private String FloodPointID;
    private int IsRegression;//积水是否消退

    private int IsBack;//是否转单 0 否,1 是

    public String getArrangementID() {
        return ArrangementID;
    }

    public String getArrangementName() {
        return ArrangementName;
    }

    public void setArrangementName(String arrangementName) {
        ArrangementName = arrangementName;
    }

    public String getFloodUserName() {
        return FloodUserName;
    }

    public void setFloodUserName(String floodUserName) {
        FloodUserName = floodUserName;
    }

    public void setArrangementID(String arrangementID) {
        ArrangementID = arrangementID;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        Year = year;
    }

    public String getSeepeNo() {
        return SeepeNo;
    }


    public int getIsUrgentHandle() {
        return IsUrgentHandle;
    }

    public void setIsUrgentHandl(int isUrgentHandl) {
        IsUrgentHandle = isUrgentHandl;
    }


    public void setSeepeNo(String seepeNo) {
        SeepeNo = seepeNo;
    }

    public String getSeeperArea() {
        return SeeperArea;
    }

    public int getIsBack() {
        return IsBack;
    }

    public void setIsBack(int isBack) {
        IsBack = isBack;
    }

    public void setSeeperArea(String seeperArea) {
        SeeperArea = seeperArea;
    }

    public String getManagementOffice() {
        return ManagementOffice;
    }

    public void setManagementOffice(String managementOffice) {
        ManagementOffice = managementOffice;
    }

    public String getStreetOperation() {
        return StreetOperation;
    }

    public void setStreetOperation(String streetOperation) {
        StreetOperation = streetOperation;
    }

    public String getCommunity() {
        return Community;
    }

    public void setCommunity(String community) {
        Community = community;
    }

    public String getRoad() {
        return Road;
    }

    public void setRoad(String road) {
        Road = road;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getFloodUser() {
        return FloodUser;
    }

    public void setFloodUser(String floodUser) {
        FloodUser = floodUser;
    }

    public String getDistrictPhone() {
        return DistrictPhone;
    }

    public void setDistrictPhone(String districtPhone) {
        DistrictPhone = districtPhone;
    }

    public String getArrivalDT() {
        return ArrivalDT;
    }

    public void setArrivalDT(String arrivalDT) {
        ArrivalDT = arrivalDT;
    }

    public String getSeeperDepth() {
        return SeeperDepth;
    }

    public void setSeeperDepth(String seeperDepth) {
        SeeperDepth = seeperDepth;
    }

    public String getStartWaterDate() {
        return StartWaterDate;
    }

    public void setStartWaterDate(String startWaterDate) {
        StartWaterDate = startWaterDate;
    }

    public String getCreator() {
        return Creator;
    }

    public void setCreator(String creator) {
        Creator = creator;
    }

    public String getCreateDT() {
        return CreateDT;
    }

    public void setCreateDT(String createDT) {
        CreateDT = createDT;
    }

    public void setIsUrgentHandle(int isUrgentHandle) {
        IsUrgentHandle = isUrgentHandle;
    }

    public int getIsRegression() {
        return IsRegression;
    }

    public void setIsRegression(int isRegression) {
        IsRegression = isRegression;
    }

    public String getSeeperSource() {
        return SeeperSource;
    }

    public void setSeeperSource(String seeperSource) {
        SeeperSource = seeperSource;
    }

    public String getIsCarPass() {
        return IsCarPass;
    }

    public void setIsCarPass(String isCarPass) {
        IsCarPass = isCarPass;
    }

    public String getSeeperLevel() {
        return SeeperLevel;
    }

    public void setSeeperLevel(String seeperLevel) {
        SeeperLevel = seeperLevel;
    }

    public String getSeeperTime() {
        return SeeperTime;
    }

    public void setSeeperTime(String seeperTime) {
        SeeperTime = seeperTime;
    }

    public String getRadius() {
        return Radius;
    }

    public void setRadius(String radius) {
        Radius = radius;
    }

    public String getSeeperReason() {
        return SeeperReason;
    }

    public void setSeeperReason(String seeperReason) {
        SeeperReason = seeperReason;
    }

    public String getMemo() {
        return Memo;
    }

    public void setMemo(String memo) {
        Memo = memo;
    }

    public float getX() {
        return X;
    }

    public void setX(float x) {
        X = x;
    }

    public float getY() {
        return Y;
    }

    public void setY(float y) {
        Y = y;
    }

    public float getZ() {
        return Z;
    }

    public void setZ(float z) {
        Z = z;
    }

    public String getWarningID() {
        return WarningID;
    }

    public void setWarningID(String warningID) {
        WarningID = warningID;
    }

    public String getDisposingPerson() {
        return DisposingPerson;
    }

    public void setDisposingPerson(String disposingPerson) {
        DisposingPerson = disposingPerson;
    }

    public String getDisposingPersonName() {
        return DisposingPersonName;
    }

    public void setDisposingPersonName(String disposingPersonName) {
        DisposingPersonName = disposingPersonName;
    }

    public String getEndWaterDate() {
        return EndWaterDate;
    }

    public void setEndWaterDate(String endWaterDate) {
        EndWaterDate = endWaterDate;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getDeliveryUser() {
        return DeliveryUser;
    }

    public void setDeliveryUser(String deliveryUser) {
        DeliveryUser = deliveryUser;
    }

    public String getDeliveryCar() {
        return DeliveryCar;
    }

    public void setDeliveryCar(String deliveryCar) {
        DeliveryCar = deliveryCar;
    }

    public String getWaterResult() {
        return WaterResult;
    }

    public void setWaterResult(String waterResult) {
        WaterResult = waterResult;
    }

    public String getCorrectiveMeasures() {
        return CorrectiveMeasures;
    }

    public void setCorrectiveMeasures(String correctiveMeasures) {
        CorrectiveMeasures = correctiveMeasures;
    }

    public String getIsDelete() {
        return IsDelete;
    }

    public void setIsDelete(String isDelete) {
        IsDelete = isDelete;
    }

    public float getMX() {
        return MX;
    }

    public void setMX(float MX) {
        this.MX = MX;
    }

    public float getMY() {
        return MY;
    }

    public void setMY(float MY) {
        this.MY = MY;
    }

    public String getFloodPointID() {
        return FloodPointID;
    }

    public void setFloodPointID(String floodPointID) {
        FloodPointID = floodPointID;
    }
}
