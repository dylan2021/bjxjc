package com.haocang.bjxjc.push.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haocang.bjxjc.R;
import com.haocang.bjxjc.activity.comm.CommFullActivity;
import com.haocang.bjxjc.activity.tool.CacheActivityUtil;
import com.haocang.bjxjc.ui.home.activity.MainActivity;
import com.haocang.bjxjc.utils.tools.CacheData;
import com.haocang.bjxjc.utils.tools.MyTextUtils;

import net.tsz.afinal.annotation.view.ViewInject;

public class PushDisplayActivity extends CommFullActivity {

    @ViewInject(id = R.id.tv_context)
    TextView tv_context;
    @ViewInject(id = R.id.tv_dt)
    TextView tv_dt;
    @ViewInject(id = R.id.item_lin)
    LinearLayout item_lin;
    @ViewInject(id = R.id.tv_now_time)
    TextView tv_now_time;
    @ViewInject(id = R.id.tv_now_day)
    TextView tv_now_day;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CacheActivityUtil.addActivity(PushDisplayActivity.this);
        final Window win = getWindow();
        context = this;
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        initEvent();

    }

    @Override
    protected void onPause() {
        super.onPause();
        initData();
    }


    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_push_display;
    }

    private void initEvent() {
        item_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(context, MainActivity.class);
                it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
                startActivity(it);
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }

    private void initData() {
        tv_now_time.setText( MyTextUtils.getNowTimetoString());
        tv_now_day.setText( MyTextUtils.getNowDaytoString());
        //拿到传过来的数据
        String msg = CacheData.PUSH_MSG;
        String dt = MyTextUtils.GetSimpleDtName( CacheData.PUSH_DT);

        tv_context.setText(msg);
        tv_dt.setText(dt);
    }


    //退出程序
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_BACK) {
            Intent it = new Intent(context, MainActivity.class);
            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
            startActivity(it);
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            return super.onKeyDown(keyCode, event);
        }else{
            return  false;
        }
    }


}
