package com.haocang.bjxjc;

import android.app.Application;
import android.app.Notification;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.haocang.bjxjc.utils.tools.ApiConstant;
import com.haocang.bjxjc.utils.crashhandler.CrashHandler;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

import net.tsz.afinal.FinalDb;

import org.android.agoo.huawei.HuaWeiRegister;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 创建时间：2019/3/4
 * 编 写 人：ShenC
 * 功能描述：程序入口
 */

public class App extends Application {
    private static App applicstion =null;
    private static Context context;


    @Override
    public void onCreate() {
        super.onCreate();
        applicstion = this;
        context = getApplicationContext();

        //华为Push初始化
        HuaWeiRegister.register(applicstion);

        UMConfigure.init(applicstion, "5d12ddc44ca35785fe00007d", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, "f405b4a97fb2096dd5945ab5a44c458d");
        PushAgent mPushAgent = PushAgent.getInstance(applicstion);
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回deviceToken deviceToken是推送消息的唯一标志
                Log.i(ApiConstant.LogFlag,"注册成功：deviceToken：-------->  " + deviceToken);
            }
            @Override
            public void onFailure(String s, String s1) {
                Log.e(ApiConstant.LogFlag,"注册失败：-------->  " + "s:" + s + ",s1:" + s1);
            }
        });

        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler(){

            @Override
            public void dealWithCustomAction(Context context, UMessage msg){
                Toast.makeText(context,msg.custom,Toast.LENGTH_SHORT).show();
                Log.e(ApiConstant.LogFlag,msg.custom);
            }

        };
        mPushAgent.setNotificationClickHandler(notificationClickHandler);


        UmengMessageHandler messageHandler = new UmengMessageHandler() {
            @Override
            public Notification getNotification(Context context, UMessage msg) {
                Log.i(ApiConstant.LogFlag, "友盟push > 触发广播 > 信息：" + msg.custom);
                Intent intent1 = new Intent("com.haocang.bjxjc.receiver");
                intent1.putExtra("msg", msg.custom);
                intent1.setComponent(new ComponentName("com.haocang.bjxjc", "com.haocang.bjxjc.push.receiver.MyReceiver"));
                sendBroadcast(intent1);
                return super.getNotification(context, msg);
            }
        };
        mPushAgent.setMessageHandler(messageHandler);

        requestQueue = Volley.newRequestQueue(getApplicationContext());
    }
    public static RequestQueue requestQueue;
    public static App getInstance(){
        return applicstion;
    }

    public static Context getAppContext()
    {
        return context;
    }


    public static void initConfig(){

        //初始化数据库
        ApiConstant.db = FinalDb.create(applicstion, "bjxjc.db", true,
                ApiConstant.db_version,new FinalDb.DbUpdateListener() {

            public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
                // TODO Auto-generated method stub
                try {
                    InputStream in = applicstion.getAssets().open("patch.sql");
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                    String sqlUpdate = null;
                    while ((sqlUpdate = bufferedReader.readLine()) != null) {
                        if (!TextUtils.isEmpty(sqlUpdate)) {
                            db.execSQL(sqlUpdate);
                        }
                    }
                    bufferedReader.close();
                    in.close();
                } catch (SQLException e) {
                    System.out.println(e.toString());
                } catch (IOException e) {
                    System.out.println(e.toString());
                }
            }
        });

        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(applicstion);

    }


    @Override
    public void onTerminate() {
        super.onTerminate();
    }

}
