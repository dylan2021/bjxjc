package com.haocang.bjxjc.ui.tools.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.haocang.bjxjc.R;
import com.haocang.bjxjc.activity.comm.CommFinalActivity;
import com.haocang.bjxjc.activity.tool.CacheActivityUtil;
import com.haocang.bjxjc.utils.tools.ApiConstant;

public class ImageLookWebActivity extends CommFinalActivity {

    private WebView imagelook_image;
    private String filename;
    private String path;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CacheActivityUtil.addActivity(ImageLookWebActivity.this);
        initView();
        initData();
        initEvent();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_image_weblook;
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
        return "文件浏览";
    }

    private void initData() {
        String imgUrl = ApiConstant.host+path;
        //支持javascript
        imagelook_image.getSettings().setJavaScriptEnabled(true);
        // 设置可以支持缩放
        imagelook_image.getSettings().setSupportZoom(true);
        // 设置出现缩放工具
        imagelook_image.getSettings().setBuiltInZoomControls(true);
        //扩大比例的缩放
        imagelook_image.getSettings().setUseWideViewPort(true);
        //自适应屏幕
        imagelook_image.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        imagelook_image.getSettings().setLoadWithOverviewMode(true);

        imagelook_image.setWebViewClient(new WebViewClient());
        imagelook_image.loadUrl(imgUrl);



    }
    private void initEvent() {
        // TODO 自动生成的方法存根

        iv_back.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View arg0) {
                // TODO 自动生成的方法存根
                finish();
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }

        });
    }
    private void initView() {
        // TODO 自动生成的方法存根
        Intent nowintent = this.getIntent();
        filename = nowintent.getStringExtra("filename");
        path = nowintent.getStringExtra("path");
        imagelook_image = (WebView) findViewById(R.id.wv_webview);
    }

    //退出程序
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
       finish();
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CacheActivityUtil.finishSingleActivity(ImageLookWebActivity.this);
    }
}
