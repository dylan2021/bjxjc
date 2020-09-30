package com.haocang.bjxjc.activity.bean;

/**
 * 创建时间：2019/3/4
 * 编 写 人：ShenC
 * 功能描述：
 */

public class FileIndexBean {
    private  int  No;
    private  String FileUrl;
    private  boolean isAdd;

    public  FileIndexBean(int i,String file){
        this.No = i;
        this.FileUrl = file;
    }
    public  FileIndexBean(boolean isAdd){
        this.isAdd = isAdd;
    }

    public int getNo() {
        return No;
    }

    public void setNo(int no) {
        No = no;
    }

    public String getFileUrl() {
        return FileUrl;
    }

    public void setFileUrl(String file) {
        FileUrl = file;
    }

    public boolean isAdd() {
        return isAdd;
    }

    public void setAdd(boolean add) {
        isAdd = add;
    }
}
