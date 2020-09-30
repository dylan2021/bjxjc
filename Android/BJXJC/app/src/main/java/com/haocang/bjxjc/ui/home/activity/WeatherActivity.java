package com.haocang.bjxjc.ui.home.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.haocang.bjxjc.R;
import com.haocang.bjxjc.activity.comm.CommFinalActivity;
import com.haocang.bjxjc.requestcenter.DataModel;
import com.haocang.bjxjc.ui.home.util.HomeUtils;
import com.haocang.commonlib.network.interfaces.InitDataCallBack;
import com.haocang.bjxjc.ui.home.adapter.WeatherAdapter;
import com.haocang.bjxjc.ui.home.bean.WeatherBean;
import com.haocang.bjxjc.utils.tools.ApiConstant;
import net.tsz.afinal.annotation.view.ViewInject;
import org.json.JSONObject;
import java.util.List;

public class WeatherActivity extends CommFinalActivity  {

    @ViewInject(id = R.id.iv_home_weather)
    ImageView iv_home_weather;
    @ViewInject(id = R.id.tv_home_weather_wd)
    TextView tv_home_weather_wd;
    @ViewInject(id = R.id.tv_home_weather_memo)
    TextView tv_home_weather_memo;
    @ViewInject(id = R.id.tv_home_weather_memo1)
    TextView tv_home_weather_memo1;
    @ViewInject(id = R.id.tv_home_weather_pubdate)
    TextView tv_home_weather_pubdate;
    @ViewInject(id = R.id.iv_home_weather_close)
    ImageView iv_home_weather_close;
    @ViewInject(id = R.id.list)
    ListView list;
    private WeatherAdapter mAdapter;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        initView();

        initEvent();

        initData();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_weather;
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
        return "天气预报";
    }

    private void initView(){


    }

    private void initEvent(){

        iv_home_weather_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

    }

    private void initData(){


        DataModel.requestGET(context, ApiConstant.WeatherURL, new InitDataCallBack() {
            @Override
            public void callbak(boolean b, String string) {

                if (b) {
                    try {
                        Gson gson = new Gson();
                        JSONObject jsondata = new JSONObject(string);
                        if(jsondata.getInt("Code") == 0) {

                            JSONObject jsonroot = new JSONObject(jsondata.getString("Data"));
                            List<WeatherBean> datalist = gson.fromJson(jsonroot.getString("Weather"), new TypeToken<List<WeatherBean>>() {
                            }.getType());


                            if(datalist.size() > 0){
                                WeatherBean model = datalist.get(0);
                                String str_low = model.getLow().replace("低温 ","");
                                String str_high= model.getHigh().replace("高温 ","");
                                String str_wendu = str_low+"~"+str_high;
                                String str_type =  model.getDay().getType();
                                String str_fx = model.getDay().getFengxiang();
                                String str_fl =  model.getDay().getFengli().toString().replace("{#cdata-section=","").replace("}","");
                                String Memo = str_type+","+str_fx+","+str_fl;

                                tv_home_weather_wd.setText(str_wendu);
                                tv_home_weather_memo.setText("");
                                tv_home_weather_memo1.setText(Memo);
                                tv_home_weather_pubdate.setText(model.getDate());

                                iv_home_weather.setImageResource(HomeUtils.GetWeatherImg(str_type));

                                mAdapter = new WeatherAdapter(context);
                                list.setAdapter(mAdapter);
                                mAdapter.setList(datalist);

                            }else{
                                tv_home_weather_wd.setText("暂无数据");
                                tv_home_weather_memo.setText("暂无数据");
                            }

                        }else{
                            ToastShow("天气获取失败," + jsondata.getString("info"));
                        }

                    } catch (Exception e) {
                        ToastShow("天气数据解析失败," + e.toString());
                    }
                } else {
                    ToastShow("天气获取失败," + string);
                }
            }
        });

    }




    //退出程序
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            return super.onKeyDown(keyCode, event);
        }else{
            return  false;
        }
    }

}
