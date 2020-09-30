package com.haocang.bjxjc.push.receiver;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.PowerManager;
import android.util.Log;

import com.haocang.bjxjc.activity.tool.CacheActivityUtil;
import com.haocang.bjxjc.push.activity.PushDisplayActivity;
import com.haocang.bjxjc.utils.tools.ApiConstant;
import com.haocang.bjxjc.utils.tools.CacheData;

import static android.content.Context.POWER_SERVICE;

/**
 * 创建时间：2019/6/26
 * 编 写 人：ShenC
 * 功能描述：
 */

public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(ApiConstant.LogFlag, "onReceive: 收到广播");
        //Log.i(Config.LogFlag, intent.getAction());
        //拿到传来过来数据
        String msg = intent.getStringExtra("msg");
        String dt = intent.getStringExtra("dt");

        //播放声音
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(context, sound);
        r.play();

        //拿到锁屏管理者
        KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        if (km.isKeyguardLocked()) {   //为true就是锁屏状态下
            //启动Activity
            Intent alarmIntent = new Intent(context, PushDisplayActivity.class);
            CacheData.PUSH_MSG = msg;
            CacheData.PUSH_DT = dt;
            if(CacheActivityUtil.FindActivityByClass(PushDisplayActivity.class)){
                alarmIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }else{
                alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(alarmIntent);
            screenOn(context);
        }else{
            //Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
        }
    }

    //屏幕亮起
    public void screenOn(Context context) {
        PowerManager mPowerManager = (PowerManager) context.getSystemService(POWER_SERVICE);
        PowerManager.WakeLock mWakeLock = mPowerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "tag");
        mWakeLock.acquire();
        mWakeLock.release();
    }
}