package com.haocang.bjxjc.ui.user.acts;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.haocang.bjxjc.R;
import com.haocang.bjxjc.utils.tools.StatusBarUtil;

/**
 * @author Gool Lee
 * @Date
 */
@SuppressLint("WrongConstant")
public class BaseFgActivity extends FragmentActivity {

    public ConnectionChangeReceiver myReceiver;
    protected final String TAG = "777";
    protected TextView emptyTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制竖屏

        registerReceiver();
    }

    protected void initTitleBackBt(String title) {
        TextView titleTv = (TextView) findViewById(R.id.center_tv);
        if (titleTv != null) {
            titleTv.setText(title);
        }
        View finishBt = findViewById(R.id.left_bt);
        if (finishBt != null) {
            finishBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    protected void initTitleBackBtSetText(String title, String backText) {
        TextView titleTv = (TextView) findViewById(R.id.center_tv);
        if (titleTv != null) {
            titleTv.setText(title);
        }
        Button finishBt = findViewById(R.id.left_bt);
        finishBt.setText(backText);
        if (finishBt != null) {
            finishBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    protected void setTv(int id, String text) {
        ((TextView) findViewById(id)).setText(text);

    }

    protected Button getTitleRightBt(String rightText) {
        Button rightBt = (Button) findViewById(R.id.title_right_bt);
        rightBt.setText(rightText);
        rightBt.setVisibility(View.VISIBLE);
        return rightBt;
    }

    /* 注册广播，监听网络异常 */
    public void registerReceiver() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        myReceiver = new ConnectionChangeReceiver();
        this.registerReceiver(myReceiver, filter);

    }

    /* 取消网络监听 */
    public void unregisterReceiver() {
        if (myReceiver != null) {
            this.unregisterReceiver(myReceiver);
        }
    }

    /**
     * 点击空白处，关闭软键盘
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver();
    }

    //透明状态栏
    protected void initStatusBar() {
        StatusBarUtil.setImmersiveStatusBar(this, true);
    }

    public class ConnectionChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo mobNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                NetworkInfo wifiNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
                    Toast.makeText(context, "当前网络不可用",Toast.LENGTH_SHORT);
                } else {
                    //    ToastShow.Toast(context, "网络可用");
                    //    Log.d("ConnectionChangeReceiver","网络可用");
                    //  JPushInterface.init(context); //重新调用jpush,得到消息
                }

            }
        }

    }
}
