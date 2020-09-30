package com.haocang.bjxjc.ui.tools.bean;

/**
 * 创建时间：2019/5/6
 * 编 写 人：ShenC
 * 功能描述：
 */

public class TeamMemberDialogBean {
    private String ID;
    private String Name;
    private boolean IsCheck;
    private boolean ShowIcon;
    private int Icon;


    public TeamMemberDialogBean(String id, String name, boolean ischeck,boolean ShowIcon, int Icon) {
        this.ID = id;
        this.Name = name;
        this.IsCheck = ischeck;
        this.ShowIcon = ShowIcon;
        this.Icon = Icon;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public boolean isCheck() {
        return IsCheck;
    }

    public void setCheck(boolean check) {
        IsCheck = check;
    }

    public int getIcon() {
        return Icon;
    }

    public void setIcon(int icon) {
        Icon = icon;
    }

    public boolean isShowIcon() {
        return ShowIcon;
    }

    public void setShowIcon(boolean showIcon) {
        ShowIcon = showIcon;
    }
}
