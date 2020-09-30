package com.haocang.bjxjc.ui.tools.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haocang.bjxjc.R;
import com.haocang.bjxjc.activity.comm.CommFinalActivity;
import com.haocang.bjxjc.utils.tools.MyTextUtils;

import net.tsz.afinal.annotation.view.ViewInject;

import java.util.ArrayList;

public class EditMemoActivity extends CommFinalActivity {


    @ViewInject(id = R.id.no)
    TextView no;
    @ViewInject(id = R.id.yes)
    TextView yes;
    @ViewInject(id = R.id.et_editmemo)
    EditText et_editmemo;
    @ViewInject(id = R.id.char_Keyboard)
    LinearLayout char_Keyboard;


    private ArrayList<OnSoftKeyboardStateChangedListener> mKeyboardStateListeners = new ArrayList<>();      //软键盘状态监听列表
    private ViewTreeObserver.OnGlobalLayoutListener mLayoutChangeListener;
    private boolean mIsSoftKeyboardShowing = false;

    public interface OnSoftKeyboardStateChangedListener {
        public void OnSoftKeyboardStateChanged(boolean isKeyBoardShow, int keyboardHeight);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();

        initEvent();

    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_edit_memo;
    }

    @Override
    protected boolean showNavigation() {
        return false;
    }

    @Override
    protected int showMoreIcon() {
        return 0;
    }

    @Override
    protected String showTitleName() {
        return "备注";
    }

    private void initView() {

    }


    private void initEvent(){


        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //发送消息给控制室的人员
                String ls_Memo = et_editmemo.getText().toString().trim();


                if(MyTextUtils.IsNull(ls_Memo)){
                    ToastShow("请输入原因！");
                    return;
                }

                Intent intent = new Intent();
                intent.putExtra("Name", ls_Memo);
                setResult(112, intent);
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });


        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });


        mLayoutChangeListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                WindowManager manager = EditMemoActivity.this.getWindowManager();
                DisplayMetrics outMetrics = new DisplayMetrics();
                manager.getDefaultDisplay().getMetrics(outMetrics);
                int screenWidth = outMetrics.widthPixels;
                int screenHeight = outMetrics.heightPixels;

                //判断窗口可见区域大小
                Rect r = new Rect();
                getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
                //如果屏幕高度和Window可见区域高度差值大于整个屏幕高度的1/3，则表示软键盘显示中，否则软键盘为隐藏状态。
                int heightDifference = screenHeight - (r.bottom - r.top);
                boolean isKeyboardShowing = heightDifference > screenHeight / 3;

                //如果之前软键盘状态为显示，现在为关闭，或者之前为关闭，现在为显示，则表示软键盘的状态发生了改变
                if ((mIsSoftKeyboardShowing && !isKeyboardShowing) || (!mIsSoftKeyboardShowing && isKeyboardShowing)) {
                    mIsSoftKeyboardShowing = isKeyboardShowing;
                    LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) char_Keyboard.getLayoutParams();

                    if (mIsSoftKeyboardShowing) {
                        linearParams.height = heightDifference - 60;
                    } else {
                        linearParams.height = 0;
                    }
                    char_Keyboard.setLayoutParams(linearParams);

                    for (int i = 0; i < mKeyboardStateListeners.size(); i++) {
                        OnSoftKeyboardStateChangedListener listener = mKeyboardStateListeners.get(i);
                        listener.OnSoftKeyboardStateChanged(mIsSoftKeyboardShowing, heightDifference);
                    }
                }
            }
        };
        //注册布局变化监听
        getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(mLayoutChangeListener);

    }


    //退出程序
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            return super.onKeyDown(keyCode, event);
        } else {
            return false;
        }
    }

}
