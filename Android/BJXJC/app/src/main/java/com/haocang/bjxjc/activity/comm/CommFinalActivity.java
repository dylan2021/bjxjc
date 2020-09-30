package com.haocang.bjxjc.activity.comm;

/**
 * 创建时间：2019/3/4
 * 编 写 人：ShenC
 * 功能描述：公用的activity
 */

import net.tsz.afinal.FinalActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.haocang.bjxjc.R;

public abstract class CommFinalActivity extends FinalActivity {
    protected static String TAG = CommFinalActivity.class.getSimpleName();
    protected static Context mContext;
    protected LinearLayout bar_lin;
    protected LinearLayout iv_back;
    protected TextView tv_name;
    protected LinearLayout iv_more;
    protected ImageView iv_more_icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext = this;
        initWindows();
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());

        bar_lin = findViewById(R.id.bar_lin);
        iv_back = findViewById(R.id.iv_back);
        tv_name = findViewById(R.id.tv_name);
        iv_more = findViewById(R.id.iv_more);
        iv_more_icon = findViewById(R.id.iv_more_icon);


        tv_name.setText(showTitleName());

        if (showMoreIcon() == 0) {
            iv_more.setVisibility(View.GONE);
        } else {
            iv_more.setVisibility(View.VISIBLE);
            iv_more_icon.setBackgroundResource(showMoreIcon());
        }

        //tv_name.setText("温州智慧排水");

        if (showNavigation()) {
            bar_lin.setVisibility(View.VISIBLE);
        } else {
            bar_lin.setVisibility(View.GONE);
        }

    }

    /**
     * 获取布局文件
     *
     * @return
     */
    protected abstract int getLayoutResourceId();

    /**
     * 是否显示bar
     *
     * @return
     */
    protected abstract boolean showNavigation();

    /**
     * 设置右上角 "更多" ,0为隐藏
     *
     * @return
     */
    protected abstract int showMoreIcon();

    /**
     * 设置标题
     *
     * @return
     */
    protected abstract String showTitleName();

    @SuppressLint("NewApi")
    private void initWindows() {
        Window window = getWindow();
        int color = getResources().getColor(android.R.color.transparent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //| WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    // | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏颜色
            window.setStatusBarColor(color);
            //设置导航栏颜色
            // window.setNavigationBarColor(color);
            ViewGroup contentView = ((ViewGroup) findViewById(android.R.id.content));
            View childAt = contentView.getChildAt(0);
            if (childAt != null) {
                childAt.setFitsSystemWindows(true);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            // window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            //设置contentview为fitsSystemWindows
            ViewGroup contentView = (ViewGroup) findViewById(android.R.id.content);
            View childAt = contentView.getChildAt(0);
            if (childAt != null) {
                childAt.setFitsSystemWindows(true);
            }
            //给statusbar着色
            View view = new View(mContext);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight()));
            view.setBackgroundColor(color);
            contentView.addView(view);
        }
    }

    /**
     * 获取导航栏高度
     *
     * @return 导航栏高度
     */
    public static int getNavigationBarHeight() {
        int resourceId = mContext.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        return mContext.getResources().getDimensionPixelSize(resourceId);
    }

    /**
     * 获取状态栏高度
     *
     * @return 状态栏高度
     */
    private static int getStatusBarHeight() {
        // 获得状态栏高度
        int resourceId = mContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return mContext.getResources().getDimensionPixelSize(resourceId);
    }


    protected void ToastShow(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }
    protected void ToastShow(int id) {
        ToastShow(mContext.getString(id));
    }

    /**
     * ===============================================
     * 创建时间：2019/7/15 16:46
     * 编 写 人：ShenC
     * 方法说明：设置隐藏软键盘
     * ================================================
     * 修改内容：      修改时间：      修改人：
     * ===============================================
     */
    protected void hideKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
    }

}
