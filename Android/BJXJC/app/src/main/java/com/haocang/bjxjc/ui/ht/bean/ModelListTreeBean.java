package com.haocang.bjxjc.ui.ht.bean;

import java.util.List;

/**
 * 创建时间：2019/6/11
 * 编 写 人：ShenC
 * 功能描述：
 */

public class ModelListTreeBean {

    private String id;
    private String text;
    private String leaf;
    private String expanded;
    private String typeid;
    private String icon;
    private Boolean checked;
    private Boolean disabled;
    private Boolean selected = false;
    private List<ModelListTreeBean> children;
    private int NodeLevel;
    private Boolean ischecked;
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
    public String getLeaf() {
        return leaf;
    }
    public void setLeaf(String leaf) {
        this.leaf = leaf;
    }
    public String getExpanded() {
        return expanded;
    }
    public void setExpanded(String expanded) {
        this.expanded = expanded;
    }
    public String getIcon() {
        return icon;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }
    public Boolean getChecked() {
        return checked;
    }
    public void setChecked(Boolean checked) {
        this.checked = checked;
    }
    public Boolean getSelected() {
        return selected;
    }
    public void setSelected(Boolean selected) {
        this.selected = selected;
    }
    public List<ModelListTreeBean> getChildren() {
        return children;
    }
    public void setChildren(List<ModelListTreeBean> children) {
        this.children = children;
    }
    public Boolean getDisabled() {
        return disabled;
    }
    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }
    public int getNodeLevel() {
        return NodeLevel;
    }
    public void setNodeLevel(int nodeLevel) {
        NodeLevel = nodeLevel;
    }
    public Boolean getIschecked() {
        return ischecked;
    }
    public void setIschecked(Boolean ischecked) {
        this.ischecked = ischecked;
    }

    public String getTypeid() {
        return typeid;
    }

    public void setTypeid(String typeid) {
        this.typeid = typeid;
    }
}
