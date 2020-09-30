package com.haocang.bjxjc.ui.faultrepair.activity;

import android.content.Context;
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

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.haocang.bjxjc.R;
import com.haocang.bjxjc.activity.comm.CommFinalActivity;
import com.haocang.bjxjc.requestcenter.DataModel;
import com.haocang.bjxjc.ui.faultrepair.bean.EquRepairBean;
import com.haocang.bjxjc.utils.bean.ProvinceBean;
import com.haocang.bjxjc.utils.tools.ApiConstant;
import com.haocang.bjxjc.utils.tools.UrlConst;
import com.haocang.bjxjc.utils.tools.CacheData;
import com.haocang.bjxjc.utils.tools.MyTextUtils;
import com.haocang.bjxjc.utils.tools.OptionsItemsUtils;
import com.haocang.commonlib.loadingdialog.LoadingDialog;
import com.haocang.commonlib.network.interfaces.InitDataCallBack;
import net.tsz.afinal.annotation.view.ViewInject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class RepairOrderActivity extends CommFinalActivity {
    @ViewInject(id = R.id.tv_CompleteUser)
    EditText tv_CompleteUser;
    @ViewInject(id = R.id.tv_BeginDate)
    TextView tv_BeginDate;
    @ViewInject(id = R.id.tv_CompleteDate)
    TextView tv_CompleteDate;
    @ViewInject(id = R.id.tv_Level)
    TextView tv_Level;
    @ViewInject(id = R.id.tv_FaultCategory)
    TextView tv_FaultCategory;
    @ViewInject(id = R.id.tv_Reason)
    EditText tv_Reason;

    @ViewInject(id = R.id.no)
    TextView no;
    @ViewInject(id = R.id.yes)
    TextView yes;

    @ViewInject(id = R.id.char_Keyboard)
    LinearLayout char_Keyboard;
    private ArrayList<OnSoftKeyboardStateChangedListener> mKeyboardStateListeners = new ArrayList<>();      //软键盘状态监听列表
    private ViewTreeObserver.OnGlobalLayoutListener mLayoutChangeListener;
    private boolean mIsSoftKeyboardShowing = false;
    public interface OnSoftKeyboardStateChangedListener {
        public void OnSoftKeyboardStateChanged(boolean isKeyBoardShow, int keyboardHeight);
    }
    private LoadingDialog dialog;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        initEvent();

    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_repair_order;
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
        return "结束信息";
    }


    private void initEvent() {


        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubmitBill();
            }
        });


        tv_BeginDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseTime(1);
            }
        });

        tv_CompleteDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseTime(2);
            }
        });

        tv_Level.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseDeviceCode(OptionsItemsUtils.GetDataItemList("ErrorLevel"), 1);
            }
        });

        tv_FaultCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseDeviceCode(OptionsItemsUtils.GetDataItemList("ErrorType"), 2);
            }
        });


        mLayoutChangeListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                WindowManager manager = RepairOrderActivity.this.getWindowManager();
                DisplayMetrics outMetrics = new DisplayMetrics();
                manager.getDefaultDisplay().getMetrics(outMetrics);
                //int screenWidth = outMetrics.widthPixels;
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
    private void SubmitBill() {
        EquRepairBean model = CacheData.mEquRepairBean;
        String ls_BeginDate = tv_BeginDate.getText().toString();
        String ls_CompleteDate = tv_CompleteDate.getText().toString();
        String ls_CompleteUser = tv_CompleteUser.getText().toString();
        String ls_Reason =   tv_Reason.getText().toString();
        if(MyTextUtils.IsNull(ls_BeginDate) || MyTextUtils.IsNull(ls_CompleteDate)){
            ToastShow("开始时间或完成时间不能为空！");
            return;
        }

        if(MyTextUtils.IsNull(ls_CompleteUser)){
            ToastShow("完成人员不能为空！");
            return;
        }
        if(MyTextUtils.IsNull(ls_Reason)){
            ToastShow("维修情况不能为空！");
            return;
        }

        dialog = new LoadingDialog(context, "保存中");
        dialog.show();
        HashMap<String, Object> maps = new HashMap<>();

        maps.put("F_CreateUserId", CacheData.UserID());
        maps.put("F_CreateUserName", CacheData.UserName());
        maps.put("F_Title", model.getF_Title());
        maps.put("RepairID", model.getRepairID());
        maps.put("EquID", model.getEquID());
        maps.put("EquName", model.getEquName());
        maps.put("Status", 2);
        maps.put("Description", model.getDescription());
        maps.put("RepairUser", model.getRepairUser());
        maps.put("ReceiveUserID", model.getDisposeUserID());
        maps.put("ReceiveDate", model.getReceiveDate());
        maps.put("RepairDate", model.getRepairDate());

        maps.put("BeginDate", ls_BeginDate);
        maps.put("CompleteDate", ls_CompleteDate);
        maps.put("CompleteUser", ls_CompleteUser);
        maps.put("Level",MyTextUtils.GetOpitemValue(OptionsItemsUtils.GetDataItemList("ErrorLevel"),0,tv_Level.getText().toString()));
        maps.put("FaultCategory", MyTextUtils.GetOpitemValue(OptionsItemsUtils.GetDataItemList("ErrorType"),0,tv_FaultCategory.getText().toString()));
        maps.put("Reason", ls_Reason);//维修情况

        DataModel.requestPOST(mContext, ApiConstant.host + UrlConst.Api_PostSaveFaultRepairOrder, maps, new InitDataCallBack() {

            @Override
            public void callbak(boolean b, String string) {
                if (b) {
                    Intent intent = new Intent();
                    setResult(100, intent);
                    dialog.close();
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                } else {
                    ToastShow("保存失败," + string);
                }
                dialog.close();
            }
        });

    }



    private void chooseDeviceCode(final List<ProvinceBean> items, final int pos) {
        hideKeyBoard();
        OptionsPickerView.Builder builder = new OptionsPickerView.Builder(context, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String itemName = items.get(options1).getPickerViewText();
                switch (pos) {
                    case 1:
                        tv_Level.setText(itemName);
                        break;
                    case 2:
                        tv_FaultCategory.setText(itemName);
                        break;
                }
            }
        });
        OptionsPickerView<ProvinceBean> pickerView = new OptionsPickerView<>(builder);
        pickerView.setPicker(items);
        pickerView.show();
    }


    private void chooseTime(final int pos) {
        hideKeyBoard();
        TimePickerView.Builder builder = new TimePickerView.Builder(context, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                String time = MyTextUtils.date2String(date, simpleDateFormat);
                switch (pos) {
                    case 1:
                        tv_BeginDate.setText(time);
                        break;
                    case 2:
                        tv_CompleteDate.setText(time);
                        break;
                }
            }
        });
        builder.setType(new boolean[]{true, true, true, true, true, false});
        TimePickerView pvTime = builder.build();
        pvTime.setDate(Calendar.getInstance());
        pvTime.show();
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
