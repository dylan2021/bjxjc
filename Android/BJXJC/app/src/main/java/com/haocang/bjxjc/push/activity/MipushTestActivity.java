package com.haocang.bjxjc.push.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.haocang.bjxjc.R;
import com.haocang.bjxjc.utils.tools.ApiConstant;
import com.umeng.message.UmengNotifyClickActivity;
import org.android.agoo.common.AgooConstants;
public class MipushTestActivity extends UmengNotifyClickActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mipush_test);
    }

    @Override
    public void onMessage(Intent intent) {
        super.onMessage(intent);  //此方法必须调用，否则无法统计打开数
        String body = intent.getStringExtra(AgooConstants.MESSAGE_BODY);
        Log.i(ApiConstant.LogFlag, body);
    }
}
