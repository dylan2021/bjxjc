package com.haocang.bjxjc.ui.reason.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.haocang.bjxjc.R;
import com.haocang.bjxjc.activity.comm.CommMediaActivity;
import com.haocang.bjxjc.requestcenter.DataModel;
import com.haocang.bjxjc.ui.reason.bean.EmergencySolutionBean;
import com.haocang.bjxjc.utils.tools.ApiConstant;
import com.haocang.bjxjc.utils.tools.ToastUtil;
import com.haocang.bjxjc.utils.tools.UrlConst;
import com.haocang.bjxjc.utils.tools.MyTextUtils;
import com.haocang.commonlib.network.interfaces.InitDataOneBeanBack;

import net.tsz.afinal.annotation.view.ViewInject;

public class EmergencySolutionActivity extends CommMediaActivity {
    private Context context;
    @ViewInject(id = R.id.tv_01)
    TextView tv_01;
    @ViewInject(id = R.id.tv_02)
    TextView tv_02;
    @ViewInject(id = R.id.tv_03)
    TextView tv_03;
    @ViewInject(id = R.id.tv_04)
    TextView tv_04;
    @ViewInject(id = R.id.tv_05)
    TextView tv_05;
    @ViewInject(id = R.id.tv_06)
    TextView tv_06;
    @ViewInject(id = R.id.tv_07)
    TextView tv_07;
    @ViewInject(id = R.id.tv_08)
    TextView tv_08;
    @ViewInject(id = R.id.tv_09)
    TextView tv_09;
    @ViewInject(id = R.id.tv_10)
    TextView tv_10;
    @ViewInject(id = R.id.webView)
    WebView webView;
    private String ls_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        initView();
        initData();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_emergency_solution;
    }

    @Override
    protected boolean showNavigation() {
        return true;
    }

    @Override
    protected boolean showMedia() {
        return false;
    }

    @Override
    protected int showMoreIcon() {
        return 0;
    }

    @Override
    protected String showTitleName() {
        return "预案明细";
    }
    private void initView() {
        //支持javascript
        webView.getSettings().setJavaScriptEnabled(true);
        // 设置可以支持缩放
        webView.getSettings().setSupportZoom(true);
        // 设置出现缩放工具
        webView.getSettings().setBuiltInZoomControls(true);
        //扩大比例的缩放
        webView.getSettings().setUseWideViewPort(true);
        //自适应屏幕
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setLoadWithOverviewMode(true);
        backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }

        });
    }
    //获取数据
    private void initData() {
        String ID = getIntent().getStringExtra("ID");
        String ApiUrl = ApiConstant.host + UrlConst.url_getEmergencySolutionDeatil + ID;
        DataModel.requestGETOneMode("EmergencySolutionBean", ApiUrl, new InitDataOneBeanBack<EmergencySolutionBean>() {
            @Override
            public void callbak(boolean isSuccess, EmergencySolutionBean data, String msg) {
                if (!isSuccess || null == data) {
                    ToastUtil.show(R.string.request_failure);
                    return;
                }
                tv_01.setText(MyTextUtils.getString(data.getName()));
                tv_02.setText(MyTextUtils.getString(data.getEmergencyTypeID()));
                tv_03.setText(MyTextUtils.getString(data.getPrecipitation()));
                tv_04.setText(MyTextUtils.getString(data.getPlanLevel()));
                tv_05.setText(MyTextUtils.getString(data.getRainfall()));
                tv_06.setText(MyTextUtils.getString(data.getUserCount()));
                tv_07.setText(MyTextUtils.getString(data.getCarCount()));
                tv_08.setText(MyTextUtils.getString(data.getCreator()));
                tv_09.setText(MyTextUtils.getString(data.getCreateDT()));
                tv_10.setText(MyTextUtils.getString(data.getIsActive()));
                ls_content = data.getContent();
                if (!ls_content.contains(ApiConstant.host)) {
                    ls_content = ls_content.replaceAll("<img src=\"", "<img src=\"" + ApiConstant.host);
                }
                webView.addJavascriptInterface(new Contact(), "AndroidWebView");
                webView.loadUrl("file:///android_asset/CompanyProfile.html");
            }
        });

    }

    private class Contact {
        @JavascriptInterface
        public String getData() {
            return ls_content;
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
