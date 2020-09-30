package com.haocang.bjxjc.ui.tools.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.haocang.bjxjc.R;
import com.haocang.bjxjc.activity.comm.CommFinalActivity;
import com.haocang.bjxjc.activity.tool.CacheActivityUtil;
import com.haocang.bjxjc.utils.tools.ApiConstant;
import com.haocang.bjxjc.utils.tools.CustomDialog;
import com.haocang.bjxjc.utils.tools.FileUtils;
import com.haocang.bjxjc.utils.tools.SharedPreferencesUtil;

import net.tsz.afinal.annotation.view.ViewInject;

import java.io.File;

public class FileInfoActivity extends CommFinalActivity {
    @ViewInject(id = R.id.bt_clear)
    Button bt_clear;
    @ViewInject(id = R.id.ck_img)
    CheckBox ck_img;
    @ViewInject(id = R.id.tv_img)
    TextView tv_img;
    @ViewInject(id = R.id.ck_video)
    CheckBox ck_video;
    @ViewInject(id = R.id.tv_video)
    TextView tv_video;
    @ViewInject(id = R.id.ck_audio)
    CheckBox ck_audio;
    @ViewInject(id = R.id.tv_audio)
    TextView tv_audio;
    @ViewInject(id = R.id.ck_map)
    CheckBox ck_map;
    @ViewInject(id = R.id.tv_map)
    TextView tv_map;

    @ViewInject(id = R.id.ck_log)
    CheckBox ck_log;
    @ViewInject(id = R.id.tv_log)
    TextView tv_log;

    @ViewInject(id = R.id.ck_userinfo)
    CheckBox ck_userinfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CacheActivityUtil.addActivity(FileInfoActivity.this);
        initView();
        initEvent();
        initData();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_file_info;
    }

    @Override
    protected boolean showNavigation() {
        return true;
    }

    @Override
    protected int showMoreIcon() {
        return 0;
    }

    @Override
    protected String showTitleName() {
        return "缓存数据信息";
    }


    private void initView() {
    }

    private void initEvent() {

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        bt_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });


        bt_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialog.Builder builder = new CustomDialog.Builder(FileInfoActivity.this);
                builder.setTitle("提示");
                builder.setMessage("是否清除选择的数据？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //删除缓存文件
                        File sd = Environment.getExternalStorageDirectory();
                        String paths = sd.getPath() + "/" + ApiConstant.APP_Code;

                        if(ck_img.isChecked()){
                            FileUtils.deleteDirectory(paths+"/camera");
                        }
                        if(ck_video.isChecked()){
                            FileUtils.deleteDirectory(paths+"/video");
                        }
                        if(ck_audio.isChecked()){
                            FileUtils.deleteDirectory(paths+"/audio");
                        }
                        if(ck_map.isChecked()){
                            FileUtils.deleteDirectory(paths+"/map");
                        }
                        if(ck_log.isChecked()){
                            FileUtils.deleteDirectory(paths+"/log");
                            FileUtils.deleteDirectory(paths+"/cache");
                        }
                        if(ck_userinfo.isChecked()){
                            SharedPreferencesUtil.SaveUserInfo(FileInfoActivity.this, "", "", "", "", "", "", "", "N");
                        }
                        initData();

                        ToastShow("缓存信息清除完成!");

                    }
                });

                builder.setNegativeButton("取消",
                        new android.content.DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                            }
                        });
                builder.create().show();
            }
        });

    }

    private void initData() {

        //计算文件夹的大小
        File sd = Environment.getExternalStorageDirectory();
        String paths = sd.getPath() + "/" + ApiConstant.APP_Code;
        String img_filesize = FileUtils.getFolderSize_Str(new File(paths + "/camera"));
        tv_img.setText(img_filesize );

        String video_filesize = FileUtils.getFolderSize_Str(new File(paths + "/video"));
        tv_video.setText(video_filesize );

        String audio_filesize = FileUtils.getFolderSize_Str(new File(paths + "/audio"));
        tv_audio.setText(audio_filesize );

        String map_filesize = FileUtils.getFolderSize_Str(new File(paths + "/map"));
        tv_map.setText(map_filesize );

        int log_filesize = FileUtils.getFolderSize(new File(paths + "/log"));
        log_filesize += FileUtils.getFolderSize(new File(paths + "/cache"));
        tv_log.setText(log_filesize+"Kb");


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        CacheActivityUtil.finishSingleActivity(FileInfoActivity.this);
    }

    //退出程序
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        return super.onKeyDown(keyCode, event);
    }
}
