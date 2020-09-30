package com.haocang.bjxjc.push.receiver;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.haocang.bjxjc.utils.tools.ApiConstant;
import com.huawei.hms.support.api.push.PushReceiver;

/**
 * 创建时间：2019/6/26
 * 编 写 人：ShenC
 * 功能描述：
 */

public class HUAWEIPushRevicer extends PushReceiver {

    @Override
    public void onToken(Context context, String token, Bundle bundle) {
        super.onToken(context, token, bundle);
        Log.i(ApiConstant.LogFlag, "华为Push > Token >"+token);
    }

    @Override
    public boolean onPushMsg(Context context, byte[] msg, Bundle bundle) {
        try {
            String content = new String(msg, "UTF-8");
            Log.e(ApiConstant.LogFlag, "华为Push > Msg >"+ content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onEvent(Context context, PushReceiver.Event event, Bundle extras) {
       // PushManager.getInstance().notifyPush(extras.getString(BOUND_KEY.pushMsgKey));
        if (Event.NOTIFICATION_OPENED.equals(event) || Event.NOTIFICATION_CLICK_BTN.equals(event)) {
            int notifyId = extras.getInt(BOUND_KEY.pushNotifyId, 0);
            if (0 != notifyId) {
                NotificationManager manager = (NotificationManager) context
                        .getSystemService(Context.NOTIFICATION_SERVICE);
                manager.cancel(notifyId);
            }
            String content =  extras.getString
                    (BOUND_KEY.pushMsgKey);
            Log.e(ApiConstant.LogFlag, content);
        }
        super.onEvent(context, event, extras);
    }



    @Override
    public void onPushState(Context context, boolean b) {
            Log.i(ApiConstant.LogFlag, "华为Push > State > "+b);
    }
}
