package com.haocang.bjxjc.ui.event;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.haocang.bjxjc.App;
import com.haocang.bjxjc.R;
import com.haocang.bjxjc.activity.comm.CommMediaActivity;
import com.haocang.bjxjc.activity.tool.CacheActivityUtil;
import com.haocang.bjxjc.utils.tools.Const;
import com.haocang.commonlib.network.request.UploadUtil;
import com.haocang.bjxjc.ui.tools.activity.AttachmentListActivity;
import com.haocang.bjxjc.utils.bean.ProvinceBean;
import com.haocang.bjxjc.utils.tools.ApiConstant;
import com.haocang.bjxjc.utils.tools.UrlConst;
import com.haocang.commonlib.loadingdialog.LoadingDialog;
import com.haocang.bjxjc.utils.tools.CacheData;
import com.haocang.bjxjc.utils.tools.MyTextUtils;
import com.haocang.bjxjc.utils.tools.OptionsItemsUtils;

import net.tsz.afinal.annotation.view.ViewInject;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

//事件详情
public class EventDetailActivity extends CommMediaActivity {
    @ViewInject(id = R.id.tv_01)
    TextView tv_01;
    @ViewInject(id = R.id.tv_02)
    TextView tv_02;
    @ViewInject(id = R.id.tv_03)
    TextView tv_03;
    @ViewInject(id = R.id.tv_04)
    TextView tv_04;
    @ViewInject(id = R.id.tv_05)
    TextView tv_05;
    @ViewInject(id = R.id.tv_06)
    TextView tv_06;
    @ViewInject(id = R.id.tv_07)
    EditText tv_07;
    @ViewInject(id = R.id.tv_08)
    TextView tv_08;
    @ViewInject(id = R.id.tv_09)
    EditText tv_09;
    @ViewInject(id = R.id.tv_10)
    TextView tv_10;
    @ViewInject(id = R.id.tv_11)
    TextView tv_11;
    @ViewInject(id = R.id.tv_12)
    TextView tv_12;
    @ViewInject(id = R.id.tv_13)
    EditText tv_13;
    @ViewInject(id = R.id.tv_14)
    EditText tv_14;
    @ViewInject(id = R.id.submit_bt)
    Button submitBt;
    private ArrayList<OnSoftKeyboardStateChangedListener> mKeyboardStateListeners = new ArrayList<>();      //软键盘状态监听列表
    private ViewTreeObserver.OnGlobalLayoutListener mLayoutChangeListener;
    private boolean mIsSoftKeyboardShowing = false;
    private EventDetailActivity context;

    public interface OnSoftKeyboardStateChangedListener {
        void OnSoftKeyboardStateChanged(boolean isKeyBoardShow, int keyboardHeight);
    }

    @ViewInject(id = R.id.char_Keyboard)
    LinearLayout char_Keyboard;
    private EventBean model;
    private LoadingDialog dialog;
    private double SingleTap_X = 0, SingleTap_Y = 0, SingleTap_MX = 0, SingleTap_MY = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        CacheActivityUtil.addActivity(context);
        initView();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CacheActivityUtil.finishSingleActivity(context);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_event_detail;
    }

    @Override
    protected boolean showNavigation() {
        return true;
    }

    @Override
    protected boolean showMedia() {
        return true;
    }

    @Override
    protected int showMoreIcon() {
        return R.drawable.icon_fujian_01;
    }

    @Override
    protected String showTitleName() {
        return "事件明细";
    }

    private ArrayList<TextView> tvList;

    private void initView() {
        backLayout.setOnClickListener(onListener);
        attachmentLayout.setOnClickListener(onListener);
        submitBt.setOnClickListener(onListener);

        tvList = new ArrayList<>(Arrays.asList(tv_01, tv_02, tv_03, tv_06,
                tv_08, tv_10, tv_11, tv_12));
        for (TextView tv : tvList) {
            tv.setOnClickListener(onListener);
        }
        mLayoutChangeListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                WindowManager manager = context.getWindowManager();
                DisplayMetrics outMetrics = new DisplayMetrics();
                manager.getDefaultDisplay().getMetrics(outMetrics);
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

    private void initData() {
        if (!CacheData.Event_UPDATE) {
            Intent i = getIntent();
            SingleTap_X = i.getDoubleExtra("X", 0);
            SingleTap_Y = i.getDoubleExtra("Y", 0);
            SingleTap_MX = i.getDoubleExtra("MX", 0);
            SingleTap_MY = i.getDoubleExtra("MY", 0);
            attachmentLayout.setVisibility(View.GONE);

            tv_04.setText(MyTextUtils.getNowDatatoString());
            tv_05.setText(CacheData.UserName());
        } else {
            model = CacheData.mEventBean;
            attachmentLayout.setVisibility(View.VISIBLE);

            //预警行动"已结束"的事件不能再操作
            if (model.getStatus() == 0) {
                submitBt.setVisibility(View.VISIBLE);
                fileLayout.setVisibility(View.VISIBLE);
            } else {
                submitBt.setVisibility(View.GONE);
                fileLayout.setVisibility(View.GONE);
                setViewUnable();//设置界面控件不可以再操作
            }

            SingleTap_X = model.getXLng();
            SingleTap_Y = model.getYLat();
            SingleTap_MX = model.getMX();
            SingleTap_MY = model.getMY();

            tv_01.setText(MyTextUtils.GetOpitemValue(OptionsItemsUtils.GetDeptList(), 1, model.getDeptID()));
            tv_02.setText(MyTextUtils.GetOpitemValue(OptionsItemsUtils.GetDataItemList("EventType"), 1, model.getTypeID()));
            tv_03.setText(model.getWarningName());
            tv_04.setText(model.getCreateDT());
            tv_05.setText(model.getCreatorName());
            tv_06.setText(MyTextUtils.GetOpitemValue(OptionsItemsUtils.GetDataItemList("EventLevel"), 1, model.getLevel() + ""));
            tv_07.setText(model.getMemo());
            tv_08.setText(MyTextUtils.GetOpitemValue(OptionsItemsUtils.GetEventStatus(), 1, model.getStatus() + ""));
            tv_09.setText(model.getAlarmTime() + "");
            tv_10.setText(model.getProcessortorName());
            tv_11.setText(model.getNotifytorName());//知会人
            tv_12.setText(model.getResolveDT());
            tv_13.setText(model.getResolvetor());
            tv_14.setText(model.getResolveMemo());
        }
    }

    //让界面不能操作
    private void setViewUnable() {
        for (TextView tv : tvList) {
            tv.setOnClickListener(null);
            tv.setBackground(null);
        }
        tv_07.setEnabled(false);
        tv_09.setEnabled(false);
        tv_13.setEnabled(false);
        tv_14.setEnabled(false);
    }

    //提交数据
    private void post() {
        if (SingleTap_X == 0 || SingleTap_Y == 0) {
            ToastShow("请设置坐标");
            return;
        }
        dialog = new LoadingDialog(context, getString(R.string.loading));
        dialog.show();

        String DeptID = MyTextUtils.GetOpitemValue(OptionsItemsUtils.GetDeptList(), 0, tv_01.getText().toString());
        String TypeID = MyTextUtils.GetOpitemValue(OptionsItemsUtils.GetDataItemList("EventType"), 0, tv_02.getText().toString());
        String Status = MyTextUtils.GetOpitemValue(OptionsItemsUtils.GetEventStatus(), 0, tv_08.getText().toString());
        String Level = MyTextUtils.GetOpitemValue(OptionsItemsUtils.GetDataItemList("EventLevel"), 0, tv_06.getText().toString());
        String Processortor = MyTextUtils.GetOpitemValue(OptionsItemsUtils.GetUserList(), 0, tv_10.getText().toString());
        String Notifytor = MyTextUtils.GetOpitemValue(OptionsItemsUtils.GetUserList(), 0, tv_11.getText().toString());
        String Memo = tv_07.getText().toString();
        String ResolveDT = tv_12.getText().toString();
        String Resolvetor = tv_13.getText().toString();
        String ResolveMemo = tv_14.getText().toString();
        String WarningID = MyTextUtils.GetOpitemValue(OptionsItemsUtils.GetAllReasonList(), 0, tv_03.getText().toString());
        String AlarmTime = tv_09.getText().toString();
        if (MyTextUtils.IsNull(AlarmTime)) {
            AlarmTime = "0";
        }
        if (MyTextUtils.IsNull(TypeID)) {
            ToastShow("事件类型不能为空");
            return;
        }
        if (MyTextUtils.IsNull(Level)) {
            ToastShow("等级不能为空");
            return;
        }
        if (MyTextUtils.IsNull(Memo)) {
            ToastShow("事件描述不能为空");
            return;
        }
        if (MyTextUtils.IsNull(Status)) {
            ToastShow("状态不能为空");
            return;
        }
        if (MyTextUtils.IsNull(Processortor)) {
            ToastShow("处理人不能为空");
            return;
        }

        try {
            JSONObject maps = new JSONObject();
            if (CacheData.Event_UPDATE) {//修改
                maps.put("ID", model.getID());
            } else { //新增
                maps.put("CreateDT", MyTextUtils.getNowDatatoString());
                maps.put("Creator", CacheData.UserID());//上报人编号
            }

            maps.put("DeptID", DeptID);
            maps.put("TypeID", TypeID);
            maps.put("Status", Status);
            maps.put("Level", Level);
            maps.put("Processortor", Processortor);
            maps.put("Notifytor", Notifytor);
            maps.put("Memo", Memo);
            maps.put("ResolveDT", MyTextUtils.IsNull(ResolveDT) ? null : ResolveDT);
            maps.put("Resolvetor", Resolvetor);
            maps.put("ResolveMemo", ResolveMemo);
            maps.put("WarningID", WarningID);
            maps.put("AlarmTime", AlarmTime);
            maps.put("XLng", SingleTap_X);
            maps.put("YLat", SingleTap_Y);
            maps.put("MX", SingleTap_MX);
            maps.put("MY", SingleTap_MY);
            JSONObject param = new JSONObject();
            param.put("info", maps);
            param.put("filename", "Event");
            param.put("CreateUserId", CacheData.UserID());
            param.put("CreateUserName", CacheData.UserName());

            UploadUtil request = new UploadUtil(ApiConstant.host + UrlConst.fileUpload,
                    Const.file_p,  getPicFilelist(), param, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject josnobject = new JSONObject(response);
                        if (josnobject.getBoolean("Result")) {
                            ToastShow("提交成功");
                            finish();
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        } else {
                            ToastShow("提交失败！" + josnobject.getString("Msg"));
                        }
                    } catch (JSONException e) {
                        ToastShow("提交失败！" + e.toString());
                        e.printStackTrace();
                    }
                    dialog.close();
                }

            },  new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dialog.close();
                    ToastShow(R.string.request_failure);
                }
            });
            App.requestQueue.add(request);

        } catch (JSONException e1) {
            ToastShow(e1.toString());
            e1.printStackTrace();
        }
    }

    private void chooseType(final List<ProvinceBean> items, final int pos) {
        hideKeyBoard();
        OptionsPickerView.Builder builder = new OptionsPickerView.Builder(mContext,
                new OptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        String itemName = items.get(options1).getPickerViewText();
                        switch (pos) {
                            case 1:
                                tv_01.setText(itemName);
                                break;
                            case 2:
                                tv_02.setText(itemName);
                                break;
                            case 3:
                                tv_03.setText(itemName);
                                break;
                            case 4:
                                tv_06.setText(itemName);
                                break;
                            case 5:
                                tv_08.setText(itemName);
                                break;
                            case 6:
                                tv_10.setText(itemName);
                                break;
                            case 7:
                                tv_11.setText(itemName);
                                break;

                        }
                    }
                });
        OptionsPickerView<ProvinceBean> pickerView = new OptionsPickerView<>(builder);
        pickerView.setPicker(items);
        pickerView.show();
    }

    private void chooseTime(final int position) {
        hideKeyBoard();
        TimePickerView.Builder builder = new TimePickerView.Builder(mContext, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                String time = MyTextUtils.date2String(date, simpleDateFormat);
                switch (position) {
                    case 1:
                        tv_12.setText(time);
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

    private View.OnClickListener onListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_back:
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    break;
                case R.id.iv_more:
                    Intent intent = new Intent(mContext, AttachmentListActivity.class);
                    intent.putExtra("BILLID", model.getID());
                    startActivity(intent);
                    break;
                case R.id.submit_bt:
                    post();
                    break;
                case R.id.tv_01:
                    chooseType(OptionsItemsUtils.GetDeptList(), 1);
                    break;
                case R.id.tv_02:
                    chooseType(OptionsItemsUtils.GetDataItemList("EventType"), 2);
                    break;
                case R.id.tv_03:
                    chooseType(OptionsItemsUtils.getWarmList(), 3);
                    break;
                case R.id.tv_06:
                    chooseType(OptionsItemsUtils.GetDataItemList("EventLevel"), 4);
                    break;
                case R.id.tv_08:
                    chooseType(OptionsItemsUtils.GetEventOnlyStatus(), 5);
                    break;
                case R.id.tv_10:
                    chooseType(OptionsItemsUtils.GetUserList(), 6);
                    break;
                case R.id.tv_11:
                    chooseType(OptionsItemsUtils.GetUserList(), 7);
                    break;
                case R.id.tv_12:
                    chooseTime(1);
                    break;
            }
        }
    };

}
