package com.haocang.bjxjc.ui.monitor.bean;

/**
 * 创建时间：2019/6/14
 * 编 写 人：ShenC
 * 功能描述：
 */

public class MapLegendBean {

    private int icon;
    private String Name;
    private String Type;
    private int Num;
    private Boolean IsChecked;


    public MapLegendBean(int icon, String name, int num, Boolean isChecked,String Type) {
        this.icon = icon;
        this.Name = name;
        this.Type = Type;
        this.Num = num;
        this.IsChecked = isChecked;
    }

    public int getIcon() {
        return icon;
    }

    public String getName() {
        return Name;
    }

    public int getNum() {
        return Num;
    }

    public Boolean getChecked() {
        return IsChecked;
    }

    public void setChecked(Boolean checked) {
        IsChecked = checked;
    }

    public String getType() {
        return Type;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setType(String type) {
        Type = type;
    }

    public void setNum(int num) {
        Num = num;
    }
}
