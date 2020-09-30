package com.haocang.bjxjc.ui.tools.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.haocang.commonlib.control.treeview.holder.IconTreeItemHolder;
import com.haocang.commonlib.control.treeview.holder.ProfileHolder;
import com.haocang.commonlib.control.treeview.model.TreeNode;
import com.haocang.commonlib.control.treeview.view.AndroidTreeView;
import com.haocang.bjxjc.R;
import com.haocang.bjxjc.activity.comm.CommFinalActivity;
import com.haocang.bjxjc.requestcenter.DataModel;
import com.haocang.commonlib.network.interfaces.InitDataBeanBack;
import com.haocang.bjxjc.utils.bean.ShareIdNameBean;
import com.haocang.bjxjc.utils.tools.ApiConstant;
import com.haocang.bjxjc.utils.tools.UrlConst;
import net.tsz.afinal.annotation.view.ViewInject;
import java.util.List;

public class UserSelectDialog extends CommFinalActivity {


    @ViewInject(id = R.id.recyclerview)
    RecyclerView recyclerview;
    @ViewInject(id = R.id.no)
    TextView no;
    @ViewInject(id = R.id.yes)
    TextView yes;

    private AndroidTreeView tView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();

        initEvent();

        initDeptList(savedInstanceState);

    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.dialog_getuser;
    }

    @Override
    protected boolean showNavigation() {
        return false;
    }

    @Override
    protected int showMoreIcon() {
        return 0;
    }

    @Override
    protected String showTitleName() {
        return "请选择关联的易淹点";
    }

    private void initView() {

    }

    private void initEvent() {

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });


        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                setResult(1001, intent);
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

    }


    private void initDeptList(final Bundle savedInstanceState) {


        String ApiUrl = ApiConstant.host+ UrlConst.Api_List_GetDeptTreeList;
        DataModel.requestGETMode("ShareIdNameBean", ApiUrl, new InitDataBeanBack<ShareIdNameBean>() {
            @Override
            public void callbak(boolean b, List<ShareIdNameBean> list, String msg) {

                ViewGroup containerView = findViewById(R.id.container_tree);
                TreeNode root = TreeNode.root();
                FindChiledNodeTree(root,list);
                tView = new AndroidTreeView(mContext,root);
                tView.setDefaultAnimation(true);
                containerView.addView(tView.getView());
                tView.setSelectionModeEnabled(true);

                if (savedInstanceState != null) {
                    String state = savedInstanceState.getString("tState");
                    if(!TextUtils.isEmpty(state)){
                        tView.restoreState(state);
                    }
                }
            }
        });

    }


    private void FindChiledNodeTree(TreeNode folder,List<ShareIdNameBean> list) {
        TreeNode currentNode = folder;
        for (int a = 0; a < list.size(); a++) {
            ShareIdNameBean a_model = list.get(a);
            if(a_model.getParent().equals("0")){
                TreeNode file = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_folder,list.get(a).getName()),list.get(a).getID()).setViewHolder(new ProfileHolder(mContext));
                file.setExpanded(true);
                for (int b = 0; b < list.size(); b++) {
                    ShareIdNameBean b_model = list.get(b);
                    if(b_model.getParent().equals(a_model.getID())){
                        TreeNode file1 = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_folder, b_model.getName()), b_model.getID()).setViewHolder(new ProfileHolder(mContext));
                        file.addChild(file1);
                    }
                }
                currentNode.addChild(file);
            }
        }
    }


    //退出程序
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            return super.onKeyDown(keyCode, event);
        } else {
            return false;
        }
    }
}
