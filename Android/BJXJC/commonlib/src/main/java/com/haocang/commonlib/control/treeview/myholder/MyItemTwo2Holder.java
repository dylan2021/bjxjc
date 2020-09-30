package com.haocang.commonlib.control.treeview.myholder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.github.johnkil.print.PrintView;
import com.haocang.commonlib.R;
import com.haocang.commonlib.control.treeview.holder.IconTreeItemHolder;
import com.haocang.commonlib.control.treeview.model.TreeNode;

/**
 * Created by Bogdan Melnychuk on 2/15/15.
 */
public class MyItemTwo2Holder extends TreeNode.BaseNodeViewHolder<IconTreeItemHolder.IconTreeItem> {
    private TextView tvValue;
    private PrintView arrowView;
    private TextView nodeSelector;

    public MyItemTwo2Holder(Context context) {
        super(context);
    }

    @Override
    public View createNodeView(final TreeNode node, IconTreeItemHolder.IconTreeItem value) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.layout_myitem_22_header, null, false);

        tvValue = (TextView) view.findViewById(R.id.node_value);
        tvValue.setText(value.text);

        // final PrintView iconView = (PrintView) view.findViewById(R.id.icon);
        // iconView.setIconText(context.getResources().getString(value.icon));

        arrowView = (PrintView) view.findViewById(R.id.arrow_icon);
        if (node.isLeaf()) {
            arrowView.setVisibility(View.INVISIBLE);
        }

        nodeSelector = (TextView) view.findViewById(R.id.node_selector);
//        nodeSelector.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                node.setSelected(isChecked);
//                for (TreeNode n : node.getChildren()) {
//                    getTreeView().selectNode(n, isChecked);
//                }
//            }
//        });
        nodeSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (nodeSelector.getText().equals("1")) {
                    node.setSelected(false);
                    nodeSelector.setText("0");
                    nodeSelector.setBackgroundResource(R.drawable.ic_check_box_no1);
                }else{
                    node.setSelected(true);
                    nodeSelector.setText("1");
                    nodeSelector.setBackgroundResource(R.drawable.ic_check_box_yes1);
                }
            }
        });


        tvValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nodeSelector.getText().equals("1")) {
                    node.setSelected(false);
                    nodeSelector.setText("0");
                    nodeSelector.setBackgroundResource(R.drawable.ic_check_box_no1);
                }else{
                    node.setSelected(true);
                    nodeSelector.setText("1");
                    nodeSelector.setBackgroundResource(R.drawable.ic_check_box_yes1);
                }
            }
        });


        if(node.isSelected()){
            nodeSelector.setText("1");
            nodeSelector.setBackgroundResource(R.drawable.ic_check_box_yes1);
        }else{
            nodeSelector.setText("0");
            nodeSelector.setBackgroundResource(R.drawable.ic_check_box_no1);
        }

        return view;
    }

    @Override
    public void toggle(boolean active) {
        arrowView.setIconText(context.getResources().getString(active ? R.string.ic_keyboard_arrow_down : R.string.ic_keyboard_arrow_right));
    }

    @Override
    public void toggleSelectionMode(boolean editModeEnabled) {
        nodeSelector.setVisibility(editModeEnabled ? View.VISIBLE : View.GONE);

        if(mNode.isSelected()){
            nodeSelector.setText("1");
            nodeSelector.setBackgroundResource(R.drawable.ic_check_box_yes1);
        }else{
            nodeSelector.setText("0");
            nodeSelector.setBackgroundResource(R.drawable.ic_check_box_no1);
        }


    }
}
