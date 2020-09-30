package com.haocang.bjxjc.ui.tools.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.haocang.bjxjc.R;
import com.haocang.bjxjc.activity.comm.CommFullActivity;
import com.haocang.bjxjc.activity.tool.CacheActivityUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 创建时间：2019/3/5
 * 编 写 人：ShenC
 * 功能描述：展示错误日志
 */

public class ShowlLogActivity extends CommFullActivity {

    private WebView web_view;
    private Button bt_send;
    private Button bt_cancel;
    private TextView bt_copy;

    private String FILE_URL;//文件地址
    private String Data="";//显示内容
    static final String encoding = "utf-8";//文本格式

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CacheActivityUtil.addActivity(ShowlLogActivity.this);
        Intent intent = getIntent();
        FILE_URL = intent.getStringExtra("FILE_URL");

        initView();

        initEvent();

        initData();
    }



    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_showlog;
    }
    private void initView() {


        web_view =  findViewById(R.id.web_view);
        bt_send =  findViewById(R.id.bt_send);
        bt_cancel =  findViewById(R.id.bt_cancel);
        bt_copy =   findViewById(R.id.bt_copy);

        //支持javascript
        web_view.getSettings().setJavaScriptEnabled(true);
        // 设置可以支持缩放
        web_view.getSettings().setSupportZoom(true);
        // 设置出现缩放工具
        web_view.getSettings().setBuiltInZoomControls(true);
        //扩大比例的缩放
        web_view.getSettings().setUseWideViewPort(true);
        web_view.getSettings().setDisplayZoomControls(false);
        //自适应屏幕
        web_view.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        web_view.getSettings().setLoadWithOverviewMode(true);

        web_view.getSettings().setDefaultTextEncodingName("UTF -8");//设置默认为utf-8
        web_view.setWebViewClient(new WebViewClient());

    }

    private void initEvent() {

        bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(ShowlLogActivity.this, "发送完成", Toast.LENGTH_LONG).show();

            }
        });


        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);

            }
        });


        bt_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CopyToClipboard(ShowlLogActivity.this,Data);

                Toast.makeText(ShowlLogActivity.this, "复制完成", Toast.LENGTH_SHORT).show();


            }
        });




    }


    private void initData() {

        try {
            StringBuffer sb = new StringBuffer();

            File file = new File(FILE_URL);
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();

            try {
                Data = sb.toString();
                web_view.loadData(URLEncoder.encode(Data,encoding), "text/html; charset=UTF-8", null);//这种写法可以正确解码
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }catch(Exception E){
            try {
                web_view.loadData(URLEncoder.encode("获取错误日志失败！",encoding), "text/html; charset=UTF-8", null);//这种写法可以正确解码
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    /**===============================================
     * 创建时间：2017/12/21 13:55
     * 编 写 人：ShenC
     * 方法说明：复制文本
     *================================================
     * 修改内容：      修改时间：      修改人：
     *===============================================*/
    public static void CopyToClipboard(Context context, String text){
        ClipboardManager clip = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
        clip.setPrimaryClip(ClipData.newPlainText("text", text));// 复制

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        finish();
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CacheActivityUtil.finishSingleActivity(ShowlLogActivity.this);
    }
}
