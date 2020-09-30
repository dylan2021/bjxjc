package com.haocang.bjxjc.ui.msg.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import com.haocang.bjxjc.R;
import com.haocang.bjxjc.activity.comm.CommFinalActivity;
import com.haocang.bjxjc.utils.tools.ApiConstant;
import com.haocang.bjxjc.utils.tools.MyTextUtils;
import com.haocang.bjxjc.utils.tools.OptionsItemsUtils;

import net.tsz.afinal.annotation.view.ViewInject;

public class MsgDetailActivity extends CommFinalActivity {

    @ViewInject(id = R.id.msg_title) TextView msg_title;
    @ViewInject(id = R.id.msg_user) TextView msg_user;
    @ViewInject(id = R.id.msg_dt) TextView msg_dt;


    @ViewInject(id = R.id.webView) WebView webView;
    private String ls_context ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initEvent();
        initData();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_msg_detail;
    }

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
        return "消息内容";
    }


    private void initView(){
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
    }

    private void initEvent(){
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

    }


    private void initData(){
        try {
            Intent it = getIntent();
            String title = it.getStringExtra("title");
            String user = it.getStringExtra("user");
            String dt = it.getStringExtra("dt");
            String context = it.getStringExtra("context");

            msg_title.setText(title);
            msg_user.setText(MyTextUtils.GetOpitemValue(OptionsItemsUtils.GetUserList(),1,user));
            msg_dt.setText(MyTextUtils.toYMDHM(dt));

            if(!context.contains(ApiConstant.host) ) {
                ls_context = context.replaceAll("<img src=\"", "<img src=\"" + ApiConstant.host);
            }else{
                ls_context = context;
            }
            webView.addJavascriptInterface(new Contact(), "AndroidWebView");
            webView.loadUrl("file:///android_asset/CompanyProfile.html");


        }catch (Exception e){
            ToastShow(e.toString());
        }

    }

    private  class Contact {
        @JavascriptInterface
        public String getData() {
            return ls_context;
        }
    }


    //退出程序
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            return super.onKeyDown(keyCode, event);
        }else{
            return  false;
        }
    }

}
