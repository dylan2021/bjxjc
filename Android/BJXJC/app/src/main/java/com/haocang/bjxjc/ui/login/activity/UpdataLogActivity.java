package com.haocang.bjxjc.ui.login.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.haocang.bjxjc.R;
import com.haocang.bjxjc.activity.comm.CommFinalActivity;
import com.haocang.bjxjc.utils.tools.ApiConstant;
import com.haocang.bjxjc.utils.tools.CustomDialog;
import com.haocang.bjxjc.utils.tools.MyUtils;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.HttpHandler;


import java.io.File;

public class UpdataLogActivity extends CommFinalActivity {

    @ViewInject(id = R.id.updatelog_msg)
    TextView updatelog_msg;
    @ViewInject(id = R.id.updatelog_down)
    TextView updatelog_down;
    @ViewInject(id = R.id.updatelog_jd)
    ProgressBar updatelog_jd;

    private String nowDate,appUrl,applog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initEvent();
        initData();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_updata_log;
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
        return "程序更新";
    }


    private void initView() {

        Intent intent = getIntent();
        appUrl = intent.getStringExtra("AppUrl");
        applog= intent.getStringExtra("AppLog");
        nowDate = MyUtils.getNowStrData();
        updatelog_jd.setVisibility(View.GONE);

    }

    private void initEvent(){

        updatelog_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                installBefore();
            }
        });

    }



    private void initData(){
        String s = applog.replace("。","\n");
        updatelog_msg.setText(s);
    }



    //安装前检测权限，安装android8.0 需要权限
    public void installBefore() {
        boolean haveInstallPermission;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //先获取是否有安装未知来源应用的权限
            haveInstallPermission = UpdataLogActivity.this.getPackageManager().canRequestPackageInstalls();
            if (!haveInstallPermission) {//没有权限


                CustomDialog.Builder builder = new CustomDialog.Builder(this);
                builder.setTitle("提示");
                builder.setMessage("安装应用需要打开未知来源权限，请去设置中开启权限");
                builder.setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //设置你的操作事项
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            Uri packageURI = Uri.parse("package:" + UpdataLogActivity.this.getPackageName());
                            //注意这个是8.0新API
                            Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI);
                            UpdataLogActivity.this.startActivityForResult(intent,8888);
                            dialog.cancel();
                        }
                    }
                });

                builder.setNegativeButton("退出",
                        new android.content.DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                System.exit(0);
                            }
                        });
                builder.create().show();

                return;
            }
        }
        //有权限，开始安装应用程序
        downFile(appUrl);


    }



    private void downFile(final String url) {
        updatelog_down.setVisibility(View.GONE);
        updatelog_jd.setVisibility(View.VISIBLE);

        String Sd_file  = Environment.getExternalStorageDirectory().getPath()+"/"+ ApiConstant.APP_Code+"/apk/"+nowDate+".apk";
        FinalHttp fh = new FinalHttp();
        //调用download方法开始下载
        HttpHandler handler = fh.download(url, //这里是下载的路径
                //true,//true:断点续传 false:不断点续传（全新下载）
                Sd_file, //这是保存到本地的路径
                new AjaxCallBack() {
                    public void onLoading(long count, long current) {
                        int fi =(int) count / 100;
                        int schedule  =  (int) (current) / fi;
                        updatelog_jd.setProgress(schedule);
                    }

                    public void onSuccess(File t) {
                    }

                    @Override
                    public void onSuccess(Object t) {
                        // TODO Auto-generated method stub
                        super.onSuccess(t);
                        install();
                        //Toast.makeText(UpdataLogActivity.this, "下载完成", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Throwable t, int errorNo,String strMsg) {
                        // TODO Auto-generated method stub
                        super.onFailure(t, errorNo, strMsg);
                        Toast.makeText(UpdataLogActivity.this, "下载失败"+strMsg, Toast.LENGTH_SHORT).show();
                    }

                });
    }

    private
    void install() {

        Intent intent = new Intent(Intent.ACTION_VIEW);

        String apk_Url =  Environment.getExternalStorageDirectory().getPath()+"/"+ ApiConstant.APP_Code+"/apk/"+nowDate+".apk";
        Uri apkUri =  MyUtils.GetFileProviderUri(UpdataLogActivity.this,apk_Url);
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+ ApiConstant.APP_Code+"/apk",nowDate+".apk");
        //判读版本是否在7.0以上
        if (Build.VERSION.SDK_INT   >= 24) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");

        }else{
            intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        if (UpdataLogActivity.this.getPackageManager().queryIntentActivities(intent, 0).size() > 0) {
            UpdataLogActivity.this.startActivity(intent);
        }
    }


}
