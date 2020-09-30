package com.haocang.bjxjc.ui.login.bean;

/**
 * 创建时间：2019/5/10
 * 编 写 人：ShenC
 * 功能描述：
 */

public class AppLogBean {
    private String ID;
    private String CreateDT;
    private String Code;
    private String Content;
    private String Memo;


    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getCreateDT() {
        return CreateDT;
    }

    public void setCreateDT(String createDT) {
        CreateDT = createDT;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getMemo() {
        return Memo;
    }

    public void setMemo(String memo) {
        Memo = memo;
    }
}
