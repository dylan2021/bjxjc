package com.haocang.bjxjc.ui.ht;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.haocang.bjxjc.R;
import com.haocang.bjxjc.activity.comm.CommFinalActivity;
import com.haocang.bjxjc.requestcenter.DataModel;
import com.haocang.bjxjc.ui.ht.bean.ScreenTreeBean;
import com.haocang.bjxjc.utils.tools.ApiConstant;
import com.haocang.commonlib.control.treeview.holder.IconTreeItemHolder;
import com.haocang.commonlib.control.treeview.holder.ProfileHolder;
import com.haocang.commonlib.control.treeview.holder.SelectableHeader2Holder;
import com.haocang.commonlib.control.treeview.holder.SelectableHeader3Holder;
import com.haocang.commonlib.control.treeview.model.TreeNode;
import com.haocang.commonlib.control.treeview.myholder.MyItemHtNodeHolder;
import com.haocang.commonlib.control.treeview.view.AndroidTreeView;
import com.haocang.commonlib.network.interfaces.InitDataBeanBack;

import net.tsz.afinal.annotation.view.ViewInject;

import java.util.List;

/**
 * 工艺画面
 */

public class ProcessScreenActivity extends CommFinalActivity {
    private static Context context;
    @ViewInject(id = R.id.iv_back)
    LinearLayout iv_back;
    @ViewInject(id = R.id.wv_webview)
    WebView webView;
    @ViewInject(id = R.id.tv_close)
    LinearLayout treeListDismissBt;
    @ViewInject(id = R.id.lin_pipe_open)
    LinearLayout treeListShowBt;
    @ViewInject(id = R.id.lin_pipe)
    LinearLayout lin_pipe;
    private AndroidTreeView treeView;
    private TreeNode root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        initView();
        initData(savedInstanceState);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_ht_process;
    }

    private void initView() {
        //支持javascript
        webView.getSettings().setJavaScriptEnabled(true);
        // 设置可以支持缩放
        webView.getSettings().setSupportZoom(true);
        // 设置出现缩放工具
        webView.getSettings().setBuiltInZoomControls(false);
        //扩大比例的缩放
        webView.getSettings().setUseWideViewPort(true);
        //自适应屏幕
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.setWebViewClient(new WebViewClient());

        iv_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        //右侧筛选列表,显示,隐藏
        treeListShowBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                treeListShowBt.setVisibility(View.GONE);
                lin_pipe.setVisibility(View.VISIBLE);
            }
        });
        treeListDismissBt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                lin_pipe.setVisibility(View.GONE);
                treeListShowBt.setVisibility(View.VISIBLE);
            }
        });
    }


    //获取列表tree
    private void initData(final Bundle savedInstanceState) {
        String ApiUrl = ApiConstant.host + "/Common/GeHTScreenTree";
        DataModel.requestGETModeinfo("ScreenTreeBean", ApiUrl, new InitDataBeanBack<ScreenTreeBean>() {
            @Override
            public void callbak(boolean b, List<ScreenTreeBean> list, String msg) {
                if (b) {
                    initTree(savedInstanceState, list);
                } else {
                    ToastShow(R.string.request_failure);
                }
            }
        });
    }

    //初始化控件
    private void initTree(final Bundle savedInstanceState, List<ScreenTreeBean> list) {
        ViewGroup layout = findViewById(R.id.container);
        root = TreeNode.root();
        getChiledNodeTree(root, list);

        treeView = new AndroidTreeView(context, root);
        treeView.setDefaultAnimation(true);
        layout.addView(treeView.getView());
        treeView.setSelectionModeEnabled(false);
        if (savedInstanceState != null) {
            String state = savedInstanceState.getString("tState");
            if (!TextUtils.isEmpty(state)) {
                treeView.restoreState(state);
            }
        }
    }

    //解析tree列表--字节点(第二层)
    private void getChiledNodeTree(TreeNode folder, List<ScreenTreeBean> list) {
        TreeNode currentNode = folder;
        for (int a = 0; a < list.size(); a++) {
            TreeNode file = new TreeNode(new IconTreeItemHolder.IconTreeItem(com.haocang.commonlib.R.string.ic_folder, list.get(a)
                    .getText()), list.get(a).getId(), list.get(a).getValue()).setViewHolder(new ProfileHolder(context));
            file.setExpanded(true);
            List<ScreenTreeBean> bbb = list.get(a).getChildNodes();
            if (bbb != null && bbb.size() > 0) {
                file.setTParent(true);
                for (int b = 0; b < bbb.size(); b++) {
                    TreeNode file1 = new TreeNode(new IconTreeItemHolder.IconTreeItem(com.haocang.commonlib.R.string.ic_folder, bbb.get(b)
                            .getText()), bbb.get(b).getId(), bbb.get(b).getValue()).setViewHolder(new MyItemHtNodeHolder(context));

                    List<ScreenTreeBean> ccc = bbb.get(b).getChildNodes();
                    if (ccc != null && ccc.size() > 0) {
                        file1.setTParent(true);
                        for (int c = 0; c < ccc.size(); c++) {
                            TreeNode file2 = new TreeNode(new IconTreeItemHolder.IconTreeItem(com.haocang.commonlib.R.string.ic_null, ccc.get(c)
                                    .getText()), ccc.get(c).getId(), ccc.get(c).getValue()).setViewHolder(new SelectableHeader2Holder(context));

                            List<ScreenTreeBean> ddd = ccc.get(c).getChildNodes();
                            if (ddd != null && ddd.size() > 0) {
                                file2.setTParent(true);

                                for (int d = 0; d < ddd.size(); d++) {
                                    TreeNode file3 = new TreeNode(new IconTreeItemHolder.IconTreeItem(com.haocang.commonlib.R.string.ic_folder, ddd.get(d)
                                            .getText()), ddd.get(d).getId(), ddd.get(d).getValue()).setViewHolder(new SelectableHeader3Holder(context));
                                    file2.addChild(file3);
                                    file3.setTParent(false);
                                    file3.setClickListener(nodeClickListener);
                                }
                            } else {
                                file2.setTParent(false);
                            }

                            file1.addChild(file2);
                            file2.setClickListener(nodeClickListener);
                        }
                    } else {
                        file1.setTParent(false);
                    }

                    file.addChild(file1);
                    file1.setClickListener(nodeClickListener);
                }
            } else {
                file.setTParent(false);
            }
            currentNode.addChild(file);
            file.setClickListener(nodeClickListener);
        }
    }

    // 点击右边筛选框---> 请求数据
    private TreeNode.TreeNodeClickListener nodeClickListener = new TreeNode.TreeNodeClickListener() {
        @Override
        public void onClick(TreeNode node, Object value) {
            if (node != null && !node.isTParent()) {
                String turl = node.getTurl();
                if (TextUtils.isEmpty(turl)) {
                    ToastShow("无数据");
                } else {
                    webView.loadUrl(ApiConstant.host_ht + turl);
                }
            }

/*            for (TreeNode n : root.getChildren()) {
                if (n.equals(node)) {
                    break;
                }
//                else {
//                    treeView.collapseNode(n);//关闭已展开的TreeNode
//                }
            }*/
        }
    };


    @Override
    protected boolean showNavigation() {
        return true;
    }

    @Override
    protected int showMoreIcon() {
        return 0;
    }

    @Override
    protected String showTitleName() {
        return "工艺画面";
    }
}

