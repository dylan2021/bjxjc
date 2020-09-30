package com.haocang.bjxjc.ui.tools.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.haocang.bjxjc.R;
import com.haocang.bjxjc.activity.comm.CommFinalActivity;
import com.haocang.bjxjc.activity.tool.CacheActivityUtil;

public class ImageLookActivity extends CommFinalActivity {
    private ImageView imagelook_image;
    private String path;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CacheActivityUtil.addActivity(ImageLookActivity.this);
        initView();

        initEvent();

        initData();

    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_image_look;
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
        return "图片查看";
    }


    private void initData() {
        Bitmap bm = BitmapFactory.decodeFile(path);
        imagelook_image.setImageBitmap(bm);
    }
    private void initEvent() {
        iv_back.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View arg0) {
                finish();
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }

        });
    }
    private void initView() {
        Intent intent = this.getIntent();
        path = intent.getStringExtra("path");
        imagelook_image = findViewById(R.id.imagelook_image);
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
        CacheActivityUtil.finishSingleActivity(ImageLookActivity.this);
    }
}