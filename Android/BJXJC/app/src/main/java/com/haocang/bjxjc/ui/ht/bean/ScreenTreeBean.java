package com.haocang.bjxjc.ui.ht.bean;

import java.util.List;

/**
 * 创建时间：2019/6/12
 * 编 写 人：ShenC
 * 功能描述：
 */

public class ScreenTreeBean {

    private String id;
    private String text;
    private String title;
    private String value;
    private String icon;
    private boolean showcheck;
    private String checkstate;
    private boolean  hasChildren;
    private boolean isexpand;
    private boolean  complete;
    private List<ScreenTreeBean> ChildNodes;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public boolean isShowcheck() {
        return showcheck;
    }

    public void setShowcheck(boolean showcheck) {
        this.showcheck = showcheck;
    }

    public String getCheckstate() {
        return checkstate;
    }

    public void setCheckstate(String checkstate) {
        this.checkstate = checkstate;
    }

    public boolean isHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

    public boolean isIsexpand() {
        return isexpand;
    }

    public void setIsexpand(boolean isexpand) {
        this.isexpand = isexpand;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public List<ScreenTreeBean> getChildNodes() {
        return ChildNodes;
    }

    public void setChildNodes(List<ScreenTreeBean> childNodes) {
        ChildNodes = childNodes;
    }
}
