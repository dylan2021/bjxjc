package com.haocang.bjxjc.ui.floodpoint.activity;

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
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.core.geometry.Point;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.haocang.bjxjc.R;
import com.haocang.bjxjc.activity.comm.CommMapFinalActivity;
import com.haocang.bjxjc.requestcenter.DataModel;
import com.haocang.bjxjc.ui.tools.activity.MultiSelectDialog;
import com.haocang.bjxjc.ui.tools.enums.MultiSelectEnum;
import com.haocang.bjxjc.utils.bean.XyBean;
import com.haocang.bjxjc.utils.tools.UrlConst;
import com.haocang.commonlib.network.interfaces.InitDataCallBack;
import com.haocang.bjxjc.ui.floodpoint.bean.FloodPointBean;
import com.haocang.bjxjc.utils.bean.ProvinceBean;
import com.haocang.bjxjc.utils.tools.ApiConstant;
import com.haocang.commonlib.loadingdialog.LoadingDialog;
import com.haocang.bjxjc.utils.tools.CacheData;
import com.haocang.bjxjc.utils.tools.MyTextUtils;
import com.haocang.bjxjc.utils.tools.OptionsItemsUtils;

import net.tsz.afinal.annotation.view.ViewInject;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/*
  易淹点详情,编辑;新增,;
 */
public class FloodPointAddDeatilActivity extends CommMapFinalActivity {
    @ViewInject(id = R.id.fd_2)
    TextView fd_2;
    @ViewInject(id = R.id.fd_3)
    TextView fd_3;
    @ViewInject(id = R.id.fd_4)
    EditText fd_4;
    @ViewInject(id = R.id.fd_8)
    EditText fd_8;
    @ViewInject(id = R.id.fd_9)
    TextView fd_9;
    @ViewInject(id = R.id.fd_10)
    EditText fd_10;
    @ViewInject(id = R.id.fd_11)
    TextView fd_11;
    @ViewInject(id = R.id.fd_12)
    EditText fd_12;
    @ViewInject(id = R.id.submit_bt)
    Button submitBt;
    @ViewInject(id = R.id.char_Keyboard)
    LinearLayout char_Keyboard;
    private LoadingDialog dialog;
    private FloodPointBean model;
    protected GraphicsLayer gsLayerPos;//自定义图层
    private String isActive = "Y";//有效性
    private String disposingPersonID = "", disposingPersonName = "";
    private FloodPointAddDeatilActivity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        gsLayerPos = new GraphicsLayer();
        mMapView.addLayer(gsLayerPos);
        initEvent();
        if (CacheData.FloodPoint_UPDATE) {
            initData();
        }
    }
    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_flood_point_deatil;
    }

    //提交
    private void submitPost() {
        String Year = fd_2.getText().toString();
        String location = fd_4.getText().toString();
        String Type = MyTextUtils.GetOpitemValue(OptionsItemsUtils.GetFloodPointType(), 0, fd_3.getText().toString());
        String Road = fd_8.getText().toString();
        String DistrictPhone = fd_10.getText().toString();
        String SeeperReason = fd_11.getText().toString();
        String Memo = fd_12.getText().toString();

        if (MyTextUtils.IsNull(Year)) {
            ToastShow("年份不能为空");
            return;
        }
     /*   if (SingleTap_X == 0 || SingleTap_Y == 0) {
            ToastShow("请设置坐标");
            return;
        }*/
        if (MyTextUtils.IsNull(location)) {
            ToastShow("地点不能为空");
            return;
        }
        if (MyTextUtils.IsNull(Type)) {
            ToastShow("类型不能为空");
            return;
        }
        if (MyTextUtils.IsNull(Road)) {
            ToastShow("道路不能为空");
            return;
        }

        if (MyTextUtils.IsNull(disposingPersonID)) {
            ToastShow("值守人不能为空");
            return;
        }
        if (MyTextUtils.IsNull(DistrictPhone)) {
            ToastShow("值守人电话不能为空");
            return;
        }
        if (MyTextUtils.IsNull(SeeperReason)) {
            ToastShow("积水原因不能为空");
            return;
        }
        HashMap<String, Object> maps = new HashMap<>();
        if (CacheData.FloodPoint_UPDATE) {
            maps.put("ID", model.getID());
        }
        maps.put("Year", Year);
        maps.put("Type", Type);
        maps.put("Location", location);
        maps.put("X", SingleTap_MX);
        maps.put("Y", SingleTap_MY);
        maps.put("MX", SingleTap_X);
        maps.put("MY", SingleTap_Y);
        maps.put("Road", Road);
        maps.put("FloodUser", disposingPersonID);
        maps.put("DistrictPhone", DistrictPhone);
        maps.put("SeeperReason", SeeperReason);
        maps.put("Memo", Memo);
        maps.put("IsActive", isActive);
        maps.put("IsHandle", 0);

        dialog = new LoadingDialog(context, "保存中");
        dialog.show();
        DataModel.requestPOST(mContext, ApiConstant.host + UrlConst.Api_PostFloodPoint, maps, new InitDataCallBack() {

            @Override
            public void callbak(boolean b, String string) {
                if (b) {
                    Toast.makeText(mContext, "保存成功", Toast.LENGTH_SHORT).show();
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                } else {
                    Toast.makeText(mContext, "保存失败" + string, Toast.LENGTH_SHORT).show();
                }
                dialog.close();
            }
        });
    }

    private void initEvent() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        submitBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPost();
            }
        });
        fd_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseTime();
            }
        });
        fd_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseType(OptionsItemsUtils.GetFloodPointType(), 1);
            }
        });
        fd_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(context, MultiSelectDialog.class);
                it.putExtra("ID", "");
                it.putExtra("DECIDE", "ID");
                it.putExtra("TYPE", MultiSelectEnum.AllUser);
                it.putExtra("SELECTED", disposingPersonID);
                it.putExtra("IsAlone", true);
                startActivityForResult(it, 101);
            }
        });

        fd_11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(context, MultiSelectDialog.class);
                it.putExtra("ID", "");
                it.putExtra("DECIDE", "Name");
                it.putExtra("TYPE", MultiSelectEnum.SeeperReason);
                it.putExtra("SELECTED", fd_11.getText().toString());
                it.putExtra("IsAlone", false);
                it.putExtra("ManuaInput", true);
                startActivityForResult(it, 103);
            }
        });
        mMapView.setOnSingleTapListener(new OnSingleTapListener() {

            @Override
            public void onSingleTap(float x, float y) {
                final Point mapPoint = mMapView.toMapPoint(x, y);
                final String ApiUrl = ApiConstant.MAPCoordTransformUrl + "?inSR=%7B%22wkt%22%3A%22PROJCS%5B%5C%22BNAH%5C%22%2CGEOGCS%5B%5C%22GCS_WGS_1984%5C%22%2CDATUM%5B%5C%22D_WGS_1984%5C%22%2CSPHEROID%5B%5C%22WGS_1984%5C%22%2C6378137.0%2C298.257223563%5D%5D%2CPRIMEM%5B%5C%22Greenwich%5C%22%2C0.0%5D%2CUNIT%5B%5C%22Degree%5C%22%2C0.0174532925199433%5D%5D%2CPROJECTION%5B%5C%22Berghaus_Star%5C%22%5D%2CPARAMETER%5B%5C%22False_Easting%5C%22%2C500000.0%5D%2CPARAMETER%5B%5C%22False_Northing%5C%22%2C500000.0%5D%2CPARAMETER%5B%5C%22Central_Meridian%5C%22%2C116.385%5D%2CPARAMETER%5B%5C%22Latitude_Of_Origin%5C%22%2C39.461%5D%2CPARAMETER%5B%5C%22XY_Plane_Rotation%5C%22%2C-7.0%5D%2CUNIT%5B%5C%22Meter%5C%22%2C1.0%5D%5D%22%7D&outSR=4326&geometries=%7B%22geometryType%22%3A%22esriGeometryPoint%22%2C%22geometries%22%3A%5B%7B%22x%22%3A" + x + "%2C%22y%22%3A" + y + "%2C%22spatialReference%22%3A%7B%22wkt%22%3A%22PROJCS%5B%5C%22BNAH%5C%22%2CGEOGCS%5B%5C%22GCS_WGS_1984%5C%22%2CDATUM%5B%5C%22D_WGS_1984%5C%22%2CSPHEROID%5B%5C%22WGS_1984%5C%22%2C6378137.0%2C298.257223563%5D%5D%2CPRIMEM%5B%5C%22Greenwich%5C%22%2C0.0%5D%2CUNIT%5B%5C%22Degree%5C%22%2C0.0174532925199433%5D%5D%2CPROJECTION%5B%5C%22Berghaus_Star%5C%22%5D%2CPARAMETER%5B%5C%22False_Easting%5C%22%2C500000.0%5D%2CPARAMETER%5B%5C%22False_Northing%5C%22%2C500000.0%5D%2CPARAMETER%5B%5C%22Central_Meridian%5C%22%2C116.385%5D%2CPARAMETER%5B%5C%22Latitude_Of_Origin%5C%22%2C39.461%5D%2CPARAMETER%5B%5C%22XY_Plane_Rotation%5C%22%2C-7.0%5D%2CUNIT%5B%5C%22Meter%5C%22%2C1.0%5D%5D%22%7D%7D%5D%7D&transformation=&transformForward=true&vertical=false&f=json";
                DataModel.requestGET(context, ApiUrl, new InitDataCallBack() {
                    @Override
                    public void callbak(boolean b, String string) {
                        if (b) {
                            try {
                                Gson gson = new Gson();
                                JSONObject jsonobject = new JSONObject(string);
                                List<XyBean> datalist = gson.fromJson(jsonobject.getString("geometries"), new TypeToken<List<XyBean>>() {
                                }.getType());
                                if (datalist.size() > 0) {
                                    if (mapPoint != null) {
                                        SingleTap_X = mapPoint.getX();
                                        SingleTap_Y = mapPoint.getY();
                                    }
                                    XyBean xyBean = datalist.get(0);
                                    if (xyBean != null) {
                                        SingleTap_MX = xyBean.getX();
                                        SingleTap_MY = xyBean.getY();
                                    }
                                    addfloodpoint(mapPoint);

                                } else {
                                    ToastShow("转换为wgs84坐标失败");
                                }
                            } catch (Exception e) {
                                ToastShow("转换为wgs84坐标失败:" + e.toString());
                            }
                        } else {
                            ToastShow("转换为wgs84坐标失败:" + string);
                        }
                    }
                });
            }
        });
        setKeyboardInputLayoutUp();
    }
    private void initData() {
        model = CacheData.mFloodPointBean;
        disposingPersonID = model.getFloodUser();
        disposingPersonName = model.getFloodUserName();
        fd_2.setText(model.getYear());
        fd_3.setText(MyTextUtils.GetOpitemValue(OptionsItemsUtils.GetFloodPointType(), 1, model.getType()));
        fd_4.setText(model.getLocation());
        fd_8.setText(model.getRoad());
        fd_9.setText(model.getFloodUserName());
        fd_10.setText(model.getDistrictPhone());
        fd_11.setText(model.getSeeperReason());
        fd_12.setText(model.getMemo());
        isActive = model.getIsActive();

        SingleTap_MX = model.getX();
        SingleTap_MY = model.getY();
        SingleTap_X = model.getMX();
        SingleTap_Y = model.getMY();
        addpoint(new Point(SingleTap_X, SingleTap_Y));
        mMapView.centerAt(new Point(SingleTap_X, SingleTap_Y), true);
    }
    //选择类型
    private void chooseType(final List<ProvinceBean> items, final int pos) {
        hideKeyBoard();
        OptionsPickerView.Builder builder = new OptionsPickerView.Builder(mContext, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String itemName = items.get(options1).getPickerViewText();
                switch (pos) {
                    case 1:
                        fd_3.setText(itemName);
                        break;
                }
            }
        });
        OptionsPickerView<ProvinceBean> pickerView = new OptionsPickerView<>(builder);
        pickerView.setPicker(items);
        pickerView.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == 100) {
            disposingPersonID = data.getStringExtra("ID");
            disposingPersonName = data.getStringExtra("Name");
            fd_9.setText(disposingPersonName);
        }
        if (requestCode == 103 && resultCode == 100) {
            String w_name = data.getStringExtra("Name");
            fd_11.setText(w_name);
        }

    }

    //选择时间
    private void chooseTime() {
        hideKeyBoard();
        TimePickerView.Builder builder = new TimePickerView.Builder(
                context, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy",
                        Locale.getDefault());
                String time = MyTextUtils.date2String(date, simpleDateFormat);
                fd_2.setText(time);
            }
        });
        builder.setType(new boolean[]{true, false, false, false, false, false});
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


    @Override
    protected boolean showNavigation() {
        return true;
    }

    @Override
    protected boolean showMedia() {
        return false;
    }

    @Override
    protected int showMoreIcon() {
        return 0;
    }

    @Override
    protected String showTitleName() {
        return "易淹点";
    }

    @Override
    protected boolean showMapPipe() {
        return false;
    }

    //软键盘状态监听列表
    private ViewTreeObserver.OnGlobalLayoutListener mLayoutChangeListener;
    private boolean mIsSoftKeyboardShowing = false;
    private ArrayList<OnSoftKeyboardStateChangedListener> mKeyboardStateListeners = new ArrayList<>();

    public interface OnSoftKeyboardStateChangedListener {
        void OnSoftKeyboardStateChanged(boolean isKeyBoardShow, int keyboardHeight);
    }

    //输入框跟随软键盘弹出,上移
    private void setKeyboardInputLayoutUp() {
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
                //如果屏幕高度和Window可见区域高度差值大于整个屏幕高度的1/3，
                // 则表示软键盘显示中，否则软键盘为隐藏状态。
                int heightDifference = screenHeight - (r.bottom - r.top);
                boolean isKeyboardShowing = heightDifference > screenHeight / 3;
                //如果之前软键盘状态为显示，现在为关闭，或者之前为关闭，
                //现在为显示，则表示软键盘的状态发生了改变
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
}
