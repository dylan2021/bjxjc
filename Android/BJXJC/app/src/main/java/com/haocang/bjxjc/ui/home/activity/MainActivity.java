package com.haocang.bjxjc.ui.home.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.AMapLocationQualityReport;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.haocang.bjxjc.R;
import com.haocang.bjxjc.activity.comm.CommFinalActivity;
import com.haocang.bjxjc.activity.tool.CacheActivityUtil;
import com.haocang.bjxjc.requestcenter.DataModel;
import com.haocang.commonlib.network.interfaces.InitDataCallBack;
import com.haocang.commonlib.network.interfaces.InitDataOneBeanBack;
import com.haocang.bjxjc.ui.home.bean.MsgNumberBean;
import com.haocang.bjxjc.ui.home.fragment.MainFragment;
import com.haocang.bjxjc.ui.home.interfaces.SendMsaFromFragment;
import com.haocang.bjxjc.ui.msg.fragment.MsgFragment;
import com.haocang.bjxjc.ui.msg.fragment.TaskFragment;
import com.haocang.bjxjc.ui.user.fragment.UserFragment;
import com.haocang.bjxjc.utils.bean.XyBean;
import com.haocang.bjxjc.utils.tools.ApiConstant;
import com.haocang.bjxjc.utils.tools.UrlConst;
import com.haocang.bjxjc.utils.tools.CacheData;
import com.umeng.message.PushAgent;
import com.zsoft.signala.hubs.HubConnection;
import com.zsoft.signala.hubs.HubInvokeCallback;
import com.zsoft.signala.hubs.HubOnDataCallback;
import com.zsoft.signala.hubs.IHubProxy;
import com.zsoft.signala.transport.StateBase;
import com.zsoft.signala.transport.longpolling.LongPollingTransport;

import net.tsz.afinal.annotation.view.ViewInject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends CommFinalActivity implements View.OnClickListener, SendMsaFromFragment {
    @ViewInject(id = R.id.tv_home_lin)
    LinearLayout mTvHomeLin;
    @ViewInject(id = R.id.tv_home)
    TextView mTvHome;
    @ViewInject(id = R.id.tv_home_text)
    TextView mTvhomeText;
    @ViewInject(id = R.id.tv_stats)
    TextView mTvStats;
    @ViewInject(id = R.id.tv_stats_text)
    TextView mTvStatsText;
    @ViewInject(id = R.id.tv_user_lin)
    LinearLayout mTvUserLin;
    @ViewInject(id = R.id.tv_user)
    TextView mTvUser;
    @ViewInject(id = R.id.tv_user_text)
    TextView mTvUserText;
    @ViewInject(id = R.id.tv_msg)
    TextView mTvMsg;
    @ViewInject(id = R.id.tv_msg_text)
    TextView mTvMsgText;
    @ViewInject(id = R.id.tv_msg_num)
    TextView tv_msg_num;
    @ViewInject(id = R.id.tv_task_num)
    TextView tv_task_num;
    @ViewInject(id = R.id.rl_msg)
    RelativeLayout rl_msg;
    @ViewInject(id = R.id.rl_task)
    RelativeLayout rl_task;
    @ViewInject(id = R.id.home_iv_more)
    LinearLayout home_iv_more;
    @ViewInject(id = R.id.iv_more_icon)
    ImageView iv_more_icon;

    private MainActivity context;
    private PowerManager.WakeLock wl;
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    private boolean isSartLocation = false;
    boolean isCreateChannel = false;
    private static final String NOTIFICATION_CHANNEL_NAME = "BackgroundLocation";
    private NotificationManager notificationManager = null;
    private Fragment currentFragment = new Fragment();
    private MainFragment mMainFragment;
    private TaskFragment mTaskFragment;
    private UserFragment mUserFragment;
    private MsgFragment mMsgFragment;
    private IHubProxy hub = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;

        //添加到栈
        if (!CacheActivityUtil.activityList.contains(MainActivity.this)) {
            CacheActivityUtil.addActivity(MainActivity.this);
        }

        PushAgent.getInstance(this).onAppStart();

        initView();

        initLocation();//初始化定位，开始定位

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, this.getClass().getName());
        wl.acquire();

        beginConnect();//开始推送

    }


    /**
     * 获取通知权限
     *
     * @param context
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private boolean isNotificationEnabled(Context context) {

        String CHECK_OP_NO_THROW = "checkOpNoThrow";
        String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

        AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        ApplicationInfo appInfo = context.getApplicationInfo();
        String pkg = context.getApplicationContext().getPackageName();
        int uid = appInfo.uid;

        Class appOpsClass = null;
        try {
            appOpsClass = Class.forName(AppOpsManager.class.getName());
            Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE,
                    String.class);
            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);

            int value = (Integer) opPostNotificationValue.get(Integer.class);
            return ((Integer) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        //如果已经开始定位了，显示通知栏
        if (isSartLocation && !isCreateChannel) {
            //启动后台定位，第一个参数为通知栏ID，建议整个APP使用一个
            locationClient.enableBackgroundLocation(1, buildNotification());
        }
    }

    //销毁
    @Override
    protected void onDestroy() {
        super.onDestroy();

        //关闭后台定位，参数为true时会移除通知栏，为false时不会移除通知栏，但是可以手动移除
        locationClient.disableBackgroundLocation(true);
        destroyLocation();
        wl.release();

        updateMobUserClientId("0");
        stopSignalA();
    }


    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_main;
    }

    @Override
    protected boolean showNavigation() {
        return false;
    }

    @Override
    protected int showMoreIcon() {
        return R.drawable.add_selector_01;
    }

    @Override
    protected String showTitleName() {
        return "首页";
    }

    //初始化
    private void initView() {
        mTvHomeLin.setOnClickListener(this);
        rl_task.setOnClickListener(this);
        mTvUserLin.setOnClickListener(this);
        rl_msg.setOnClickListener(this);
        changeStatus(0);
        // 创建首页的Fragment
        mMainFragment = new MainFragment();
        mTaskFragment = new TaskFragment();
        mUserFragment = new UserFragment();
        mMsgFragment = new MsgFragment();
        switchFragment(mMsgFragment).commit();
        switchFragment(mTaskFragment).commit();
        switchFragment(mUserFragment).commit();
        switchFragment(mMainFragment).commit();

        getMsgNum();
        mMainFragment.getWarmData();
    }

    private void changeStatus(int index) {
        if (index == 0) {
            mTvhomeText.setSelected(true);
            mTvHome.setBackgroundResource(R.mipmap.v_home_press1);
            mTvMsgText.setSelected(false);
            mTvMsg.setBackgroundResource(R.mipmap.v_home_msg1);
            mTvStatsText.setSelected(false);
            mTvStats.setBackgroundResource(R.mipmap.v_home_task);
            mTvUserText.setSelected(false);
            mTvUser.setBackgroundResource(R.mipmap.v_home_user1);

            mTvHome.animate().scaleX(1.1f).scaleY(1.1f);
            mTvMsg.animate().scaleX(1.0f).scaleY(1.0f);
            mTvStats.animate().scaleX(1.0f).scaleY(1.0f);
            mTvUser.animate().scaleX(1.0f).scaleY(1.0f);
        } else if (index == 1) {
            mTvhomeText.setSelected(false);
            mTvHome.setBackgroundResource(R.mipmap.v_home1);
            mTvMsgText.setSelected(true);
            mTvMsg.setBackgroundResource(R.mipmap.v_home_msg_press1);
            mTvStatsText.setSelected(false);
            mTvStats.setBackgroundResource(R.mipmap.v_home_task);
            mTvUserText.setSelected(false);
            mTvUser.setBackgroundResource(R.mipmap.v_home_user1);
            mTvHome.animate().scaleX(1.0f).scaleY(1.0f);
            mTvMsg.animate().scaleX(1.1f).scaleY(1.1f);
            mTvStats.animate().scaleX(1.0f).scaleY(1.0f);
            mTvUser.animate().scaleX(1.0f).scaleY(1.0f);
        } else if (index == 2) {
            mTvhomeText.setSelected(false);
            mTvHome.setBackgroundResource(R.mipmap.v_home1);
            mTvMsgText.setSelected(false);
            mTvMsg.setBackgroundResource(R.mipmap.v_home_msg1);
            mTvStatsText.setSelected(true);
            mTvStats.setBackgroundResource(R.mipmap.v_home_task_press);
            mTvUserText.setSelected(false);
            mTvUser.setBackgroundResource(R.mipmap.v_home_user1);
            mTvHome.animate().scaleX(1.0f).scaleY(1.0f);
            mTvMsg.animate().scaleX(1.0f).scaleY(1.0f);
            mTvStats.animate().scaleX(1.1f).scaleY(1.1f);
            mTvUser.animate().scaleX(1.0f).scaleY(1.0f);
        } else {
            mTvhomeText.setSelected(false);
            mTvHome.setBackgroundResource(R.mipmap.v_home1);
            mTvMsgText.setSelected(false);
            mTvMsg.setBackgroundResource(R.mipmap.v_home_msg1);
            mTvStatsText.setSelected(false);
            mTvStats.setBackgroundResource(R.mipmap.v_home_task);
            mTvUserText.setSelected(true);
            mTvUser.setBackgroundResource(R.mipmap.v_home_user_press1);
            mTvHome.animate().scaleX(1.0f).scaleY(1.0f);
            mTvMsg.animate().scaleX(1.0f).scaleY(1.0f);
            mTvStats.animate().scaleX(1.0f).scaleY(1.0f);
            mTvUser.animate().scaleX(1.1f).scaleY(1.1f);
        }

    }

    private FragmentTransaction switchFragment(Fragment targetFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if (!targetFragment.isAdded()) {
            //第一次使用switchFragment()时currentFragment为null，所以要判断一下      
            if (currentFragment != null) {
                transaction.hide(currentFragment);
            }
            transaction.add(R.id.rl_container, targetFragment, targetFragment.getClass().getName());
        } else {
            transaction.hide(currentFragment).show(targetFragment);
        }
        currentFragment = targetFragment;
        return transaction;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_home_lin:
                changeStatus(0);
                switchFragment(mMainFragment).commit();
                home_iv_more.setVisibility(View.INVISIBLE);
                break;
            case R.id.rl_msg:
                changeStatus(1);
                switchFragment(mMsgFragment).commit();
                home_iv_more.setVisibility(View.VISIBLE);
                iv_more_icon.setBackgroundResource(R.drawable.ic_add_user);
                break;
            case R.id.rl_task:
                changeStatus(2);
                switchFragment(mTaskFragment).commit();
                home_iv_more.setVisibility(View.VISIBLE);
                iv_more_icon.setBackgroundResource(R.drawable.ic_add_user);
                break;
            case R.id.tv_user_lin:
                changeStatus(3);
                switchFragment(mUserFragment).commit();
                home_iv_more.setVisibility(View.INVISIBLE);
                mUserFragment.RefreshPageData();
                break;
        }
    }

    //初始化定位设置
    private void initLocation() {
        //初始化client
        locationClient = new AMapLocationClient(this.getApplicationContext());
        locationOption = getDefaultOption();
        //设置定位参数
        locationClient.setLocationOption(locationOption);
        // 设置定位监听
        locationClient.setLocationListener(locationListener);

        startLocation();
    }

    /**
     * 方法说明：默认的定位参数
     */
    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(true);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(5000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(true);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        return mOption;
    }

    /**
     * 方法说明：定位监听
     */
    public AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(final AMapLocation location) {
            Log.d(TAG, "坐标转换: 获得定位" + location);
            if (null != location) {
                //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
                if (location.getErrorCode() == 0) {
                    CacheData.MyLocation = location;
                    CacheData.LocationCity = location.getCity();
                    CacheData.LocationDistrict = location.getDistrict();

                    final String ApiUrl = ApiConstant.MAPCoordTransformUrl +
                            "?inSR=4326&outSR=%7B%22wkt%22%3A%22PROJCS%5B%5C%22BNAH%5C%22%2CGEOGCS%5B%5C%22GCS_WGS_1984%5C%22%2CDATUM%5B%5C%22D_WGS_1984%5C%22%2CSPHEROID%5B%5C%22WGS_1984%5C%22%2C6378137.0%2C298.257223563%5D%5D%2CPRIMEM%5B%5C%22Greenwich%5C%22%2C0.0%5D%2CUNIT%5B%5C%22Degree%5C%22%2C0.0174532925199433%5D%5D%2CPROJECTION%5B%5C%22Berghaus_Star%5C%22%5D%2CPARAMETER%5B%5C%22False_Easting%5C%22%2C500000.0%5D%2CPARAMETER%5B%5C%22False_Northing%5C%22%2C500000.0%5D%2CPARAMETER%5B%5C%22Central_Meridian%5C%22%2C116.385%5D%2CPARAMETER%5B%5C%22Latitude_Of_Origin%5C%22%2C39.461%5D%2CPARAMETER%5B%5C%22XY_Plane_Rotation%5C%22%2C-7.0%5D%2CUNIT%5B%5C%22Meter%5C%22%2C1.0%5D%5D%22%7D&geometries=%7B%22geometryType%22%3A%22esriGeometryPoint%22%2C%22geometries%22%3A%5B%7B%22x%22%3A"
                            + location.getLongitude() + "%2C%22y%22%3A" + location.getLatitude() + "%2C%22spatialReference%22%3A%7B%22wkid%22%3A4326%7D%7D%5D%7D&transformation=&transformForward=true&f=json";
                    DataModel.requestGET(context, ApiUrl, new InitDataCallBack() {
                        @Override
                        public void callbak(boolean isSucess, String string) {
                            Log.d(TAG, isSucess + ",坐标转换,返回:" + string);
                            if (isSucess) {
                                try {
                                    Gson gson = new Gson();
                                    JSONObject jsonobject = new JSONObject(string);
                                    List<XyBean> datalist = gson.fromJson(jsonobject.getString("geometries"),
                                            new TypeToken<List<XyBean>>() {
                                            }.getType());
                                    if (datalist.size() > 0) {
                                        //todo 上传人员坐标(后面直接通过gps获取经纬度,直接上传,参数:mx和x传一样的)
                                        String ApiLocationUrl = ApiConstant.host + UrlConst.Api_UserUploadLocation
                                                + "strUserId=" + CacheData.UserID()
                                                + "&x=" + location.getLongitude() + "&y=" + location.getLatitude()
                                                + "&mx=" + datalist.get(0).getX() + "&my=" + datalist.get(0).getY();
                                        DataModel.requestGETOneMode("XyBean",
                                                ApiLocationUrl, new InitDataOneBeanBack<XyBean>() {
                                                    @Override
                                                    public void callbak(boolean b, XyBean data, String msg) {
                                                        if (b) {
                                                            CacheData.Cache_MyLocation = data;
                                                        } else {
                                                            Log.i(ApiConstant.LogFlag, "上传坐标失败" + msg);
                                                        }
                                                    }
                                                });
                                        return;
                                    }
                                } catch (Exception e) {
                                    Log.d(TAG, "坐标转换异常:" + e.toString());
                                }
                            } else {
                                //todo 上传失败
                                //ToastShow("定位坐标上传失败");
                            }
                        }
                    });


                    //获取高德经纬度坐标
                    //Location  mLocation = new Location();
                    //Gps L =  ConvertGPS.gcj_To_Gps84(location.getLatitude(), location.getLongitude());
                    //Config.myLocation1 = new Point(L.getWgLon(),L.getWgLat());
                    //Config.mySpeed = location.getSpeed();
                    //ToastShow("纬度" + location.getLatitude());
                    //  gps_status.setVisibility(View.GONE);
//                    sb.append("定位成功" + "\n");
//                    sb.append("定位类型: " + location.getLocationType() + "\n");
//                    sb.append("经    度    : " + location.getLongitude() + "\n");
//                    sb.append("纬    度    : " + location.getLatitude() + "\n");
//                    sb.append("精    度    : " + location.getAccuracy() + "米" + "\n");
//                    sb.append("提供者    : " + location.getProvider() + "\n");
//                    sb.append("速    度    : " + location.getSpeed() + "米/秒" + "\n");
//                     sb.append("角    度    : " + location.getBearing() + "\n");
//                     // 获取当前提供定位服务的卫星个数
//                     sb.append("星    数    : " + location.getSatellites() + "\n");
//                     sb.append("国    家    : " + location.getCountry() + "\n");
//                     sb.append("省            : " + location.getProvince() + "\n");
//                     sb.append("市            : " + location.getCity() + "\n");
//                     sb.append("城市编码 : " + location.getCityCode() + "\n");
//                     sb.append("区            : " + location.getDistrict() + "\n");
//                     sb.append("区域 码   : " + location.getAdCode() + "\n");
//                     sb.append("地    址    : " + location.getAddress() + "\n");
//                     sb.append("兴趣点    : " + location.getPoiName() + "\n");
                    //定位完成的时间
                    //                   sb.append("定位时间: " + formatUTC(location.getTime(), "yyyy-MM-dd HH:mm:ss") + "\n");
                } else {
                    //定位失败
//                    sb.append("定位失败" + "\n");
//                    sb.append("错误码:" + location.getErrorCode() + "\n");
//                    sb.append("错误信息:" + location.getErrorInfo() + "\n");
//                    sb.append("错误描述:" + location.getLocationDetail() + "\n");
                    //
                    // gps_status.setVisibility(View.VISIBLE);
                    //Config.myLocation1 = null;
                }
                //                sb.append("***定位质量报告***").append("\n");
                //                sb.append("* WIFI开关：").append(location.getLocationQualityReport().isWifiAble() ? "开启" : "关闭").append("\n");
                //                sb.append("* GPS状态：").append(getGPSStatusString(location.getLocationQualityReport().getGPSStatus())).append("\n");
                //                sb.append("* GPS星数：").append(location.getLocationQualityReport().getGPSSatellites()).append("\n");
                //                sb.append("****************").append("\n");
                //                //定位之后的回调时间
                //                sb.append("回调时间: " + formatUTC(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss") + "\n");
                //
                //                //解析定位结果，
                //                String result = sb.toString();
            } else {
            }
        }
    };

    //获取GPS状态的字符串
    private String getGPSStatusString(int statusCode) {
        String str = "";
        switch (statusCode) {
            case AMapLocationQualityReport.GPS_STATUS_OK:
                str = "GPS状态正常";
                break;
            case AMapLocationQualityReport.GPS_STATUS_NOGPSPROVIDER:
                str = "手机中没有GPS Provider，无法进行GPS定位";
                break;
            case AMapLocationQualityReport.GPS_STATUS_OFF:
                str = "GPS关闭，建议开启GPS，提高定位质量";
                break;
            case AMapLocationQualityReport.GPS_STATUS_MODE_SAVING:
                str = "选择的定位模式中不包含GPS定位，建议选择包含GPS定位的模式，提高定位质量";
                break;
            case AMapLocationQualityReport.GPS_STATUS_NOGPSPERMISSION:
                str = "没有GPS定位权限，建议开启gps定位权限";
                break;
        }
        return str;
    }

    //开始定位
    private void startLocation() {
        // 设置定位参数
        locationClient.setLocationOption(locationOption);
        // 启动定位
        locationClient.startLocation();
        isSartLocation = true;
    }

    //停止定位
    private void stopLocation() {
        // 停止定位
        locationClient.stopLocation();
        isSartLocation = false;
    }

    //销毁定位
    private void destroyLocation() {
        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
        isSartLocation = false;
    }

    //后台运行图标设置
    @SuppressLint("NewApi")
    private Notification buildNotification() {
        Notification.Builder builder = null;
        Notification notification = null;
        if (android.os.Build.VERSION.SDK_INT >= 26) {
            //Android O上对Notification进行了修改，如果设置的targetSDKVersion>=26建议使用此种方式创建通知栏
            if (null == notificationManager) {
                notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            }
            String channelId = getPackageName();
            if (!isCreateChannel) {
                NotificationChannel notificationChannel = new NotificationChannel(channelId,
                        NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
                notificationChannel.enableLights(true);//是否在桌面icon右上角展示小圆点
                notificationChannel.setLightColor(Color.BLUE); //小圆点颜色
                notificationChannel.setSound(null, null);
                notificationChannel.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知
                notificationManager.createNotificationChannel(notificationChannel);
                isCreateChannel = true;
            }
            builder = new Notification.Builder(getApplicationContext(), channelId);
        } else {
            builder = new Notification.Builder(getApplicationContext());
        }
        builder.setSmallIcon(R.mipmap.ic_app_logo)
                .setContentTitle("北京新机场")
                .setContentText("正在后台运行")
                .setWhen(System.currentTimeMillis());

        if (android.os.Build.VERSION.SDK_INT >= 16) {
            notification = builder.build();
        } else {
            return builder.getNotification();
        }
        return notification;
    }

    //预警和消息通知推送
    @SuppressLint("NewApi")
    private Notification buildNotification(final String title, final String context) {

        Notification.Builder builder = null;
        Notification notification = null;
        if (android.os.Build.VERSION.SDK_INT >= 26) {
            //Android O上对Notification进行了修改，如果设置的targetSDKVersion>=26建议使用此种方式创建通知栏
            if (null == notificationManager) {
                notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            }
            String channelId = getPackageName();
            if (!isCreateChannel) {
                NotificationChannel notificationChannel = new NotificationChannel(channelId,
                        NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
                notificationChannel.enableLights(true);//是否在桌面icon右上角展示小圆点
                notificationChannel.setLightColor(Color.BLUE); //小圆点颜色
                notificationChannel.setSound(null, null);
                notificationChannel.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知
                notificationManager.createNotificationChannel(notificationChannel);
                isCreateChannel = true;
            }
            builder = new Notification.Builder(getApplicationContext(), channelId);
        } else {
            builder = new Notification.Builder(getApplicationContext());
        }
        builder.setSmallIcon(R.mipmap.ic_app_logo)
                .setContentTitle(title)
                .setContentText(context)
                .setWhen(System.currentTimeMillis());

        if (android.os.Build.VERSION.SDK_INT >= 16) {
            notification = builder.build();
        } else {
            return builder.getNotification();
        }
        return notification;
    }

    //初始化推送连接
    private void beginConnect() {
        try {
            //创建连接（PDAServerHub是服务端的类名）
            hub = conn.CreateHubProxy("ChatsHub");
        } catch (Exception e) {
            e.printStackTrace();
            Log.v(ApiConstant.LogFlag, "signalr > 连接异常！");
        }

        //接收服务端返回的消息(GetMess是服务端返回消息的方法)
        hub.On("GetMess", new HubOnDataCallback() {
            @Override
            public void OnReceived(JSONArray args) {
                try {
                    Log.i(ApiConstant.LogFlag, "signalr > GetMess > message:" + args.toString());
                    getMsgNum();
                    //使消息fragment界面更新
                    mMsgFragment.RefreshPageData();
                    sendReceiver("通知消息：" + args.opt(1).toString(), args.opt(2).toString());

                } catch (Exception E) {
                    ToastShow(E.toString());
                }

            }
        });

        //预警行动 发生改变时
        hub.On("GetWaterReasonData", new HubOnDataCallback() {
            @Override
            public void OnReceived(JSONArray args) {
                try {
                    Log.i(ApiConstant.LogFlag, "signalr > GetWaterReasonData > message:" + args.toString());
                    getMsgNum();
                    mMainFragment.getWarmData();

                    sendReceiver("预警行动发生变更", args.opt(2).toString());

                } catch (Exception E) {
                    ToastShow(E.toString());
                }

            }
        });

        //积水点 发生改变时
        hub.On("GetWaterPointData", new HubOnDataCallback() {
            @Override
            public void OnReceived(JSONArray args) {
                try {
                    Log.i(ApiConstant.LogFlag, "signalr > GetWaterPointData > message:" + args.toString());
                    getMsgNum();
                    mTaskFragment.RefreshPageData();

                    sendReceiver("积水任务更新", args.opt(2).toString());

                } catch (Exception E) {
                    ToastShow(E.toString());
                }

            }
        });

        //公告消息
        hub.On("GetNoticeData", new HubOnDataCallback() {
            @Override
            public void OnReceived(JSONArray args) {
                try {

                    Log.i(ApiConstant.LogFlag, "signalr > GetNoticeData > message:" + args.toString());
                    //   getMsgNum();
                    //使消息fragment界面更新
                    //    mMsgFragment.RefreshPageData();
                    //    sendReceiver("公告消息："+args.opt(1).toString(),args.opt(2).toString());

                } catch (Exception E) {
                    ToastShow(E.toString());
                }

            }
        });

        //开启连接
        startSignalA();
    }

    public void startSignalA() {
        if (conn != null)
            conn.Start();
    }

    public void stopSignalA() {
        if (conn != null)
            conn.Stop();
    }

    /**
     * 发送广播
     */
    private void sendReceiver(String msg, String dt) {
        Log.i(ApiConstant.LogFlag, "single  > 触发广播 > 信息 >" + msg);
        Intent intent1 = new Intent("com.haocang.bjxjc.receiver");
        intent1.putExtra("msg", msg);
        intent1.putExtra("dt", dt);
        intent1.setComponent(new ComponentName("com.haocang.bjxjc", "com.haocang.bjxjc.push.receiver.MyReceiver"));
        sendBroadcast(intent1);
    }

    //hub链接
    private HubConnection conn = new HubConnection(ApiConstant.HUB_URL, context, new LongPollingTransport()) {
        @Override
        public void OnError(Exception exception) {
            Log.d(ApiConstant.LogFlag, "OnError=" + exception.getMessage());
            beginConnect();
            ToastShow(getString(R.string.no_network));
        }

        @Override
        public void OnMessage(String message) {
            if (message != null) {
                Log.d(ApiConstant.LogFlag, "message=" + message);
            }

        }

        @Override
        public void OnStateChanged(StateBase oldState, StateBase newState) {
            Log.i(ApiConstant.LogFlag, "OnStateChanged=" + oldState.getState() + " -> " + newState.getState());

            try {
                if ((newState.getState() + "").equals("Connected")) {
                    updateMobUserClientId("1");
                }
            } catch (Exception e) {
                ToastShow("推送功能失去连接，请重新登录。");
            }
        }
    };

    //更新用户组连接Id信息
    private void updateMobUserClientId(String isOnline) {
        HubInvokeCallback callback = new HubInvokeCallback() {
            @Override
            public void OnResult(boolean succeeded, String response) {
                if (succeeded) {
                    Log.d(ApiConstant.LogFlag, "signalr > 用户绑定连接成功");

                } else {
                    Log.e(ApiConstant.LogFlag, "signalr > 用户绑定连接失败");
                    ToastShow("连接错误");
                }
            }

            @Override
            public void OnError(Exception ex) {
                Log.e(ApiConstant.LogFlag, "signalr > 用户绑定连接失败" + ex.getMessage());
                ToastShow("连接错误：" + ex.getMessage());
            }
        };

        List<String> args = new ArrayList<>(2);
        args.add(isOnline);
        args.add(CacheData.UserID());
        hub.Invoke("updateMobUserClientId", args, callback);
    }

    //显示消息数量角标
    public void getMsgNum() {
        String ApiUrl = ApiConstant.host
                + UrlConst.url_msg_num + CacheData.UserID();
        DataModel.requestGETOneMode("MsgNumberBean", ApiUrl, new InitDataOneBeanBack<MsgNumberBean>() {

            @Override
            public void callbak(boolean b, MsgNumberBean data, String msg) {
                if (b) {
                    //"消息数量:" + data.getMsgNumber() ,
                    //"任务数量：" + data.getTaskNumber());
                    CacheData.IsFloodUser = data.getIsFloodUser();
                    if (data.getMsgNumber().equals("0")) {
                        tv_msg_num.setVisibility(View.GONE);
                    } else {
                        tv_msg_num.setVisibility(View.VISIBLE);
                        tv_msg_num.setText(data.getMsgNumber());
                    }

                    if (data.getTaskNumber().equals("0")) {
                        tv_task_num.setVisibility(View.GONE);
                    } else {
                        tv_task_num.setVisibility(View.VISIBLE);
                        tv_task_num.setText(data.getTaskNumber());
                    }
                    mMainFragment.getBottomData(data);
                } else {
                    ToastShow(msg);
                }
            }
        });
    }

    @Override
    public void RefreshNumber() {
        getMsgNum();
    }

    private boolean isExit = false;

    //退出程序
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!isExit) {
                isExit = true;
                ToastShow("再点一次退出");
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        isExit = false;
                    }
                }, 2000);
            } else {
                CacheActivityUtil.finishSingleActivity(MainActivity.this);
            }
        }
        return false;
    }

}
