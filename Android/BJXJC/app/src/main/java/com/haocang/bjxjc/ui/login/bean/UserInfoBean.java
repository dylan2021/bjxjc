package com.haocang.bjxjc.ui.login.bean;

/**
 * 创建时间：2019/3/14
 * 编 写 人：ShenC
 * 功能描述：
 */
public class UserInfoBean {
    private String F_UserId;//用户主键
    private String F_Account;//登录账户
    private String F_Password;//登录密码
    private String F_RealName;//真实姓名
    private String F_HeadIcon;//头像
    private String F_DepartmentId;//部门id
    private String F_DepartmentName;

    public String getF_UserId() {
        return F_UserId;
    }

    public void setF_UserId(String f_UserId) {
        F_UserId = f_UserId;
    }

    public String getF_Account() {
        return F_Account;
    }

    public void setF_Account(String f_Account) {
        F_Account = f_Account;
    }

    public String getF_Password() {
        return F_Password;
    }

    public void setF_Password(String f_Password) {
        F_Password = f_Password;
    }

    public String getF_RealName() {
        return F_RealName;
    }

    public void setF_RealName(String f_RealName) {
        F_RealName = f_RealName;
    }

    public String getF_HeadIcon() {
        return F_HeadIcon;
    }

    public void setF_HeadIcon(String f_HeadIcon) {
        F_HeadIcon = f_HeadIcon;
    }

    public String getF_DepartmentId() {
        return F_DepartmentId;
    }

    public void setF_DepartmentId(String f_DepartmentId) {
        F_DepartmentId = f_DepartmentId;
    }

    public String getF_DepartmentName() {
        return F_DepartmentName;
    }

    public void setF_DepartmentName(String f_DepartmentName) {
        F_DepartmentName = f_DepartmentName;
    }
}
