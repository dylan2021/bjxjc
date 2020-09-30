package com.haocang.bjxjc.ui.equipment.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.haocang.bjxjc.R;
import com.haocang.bjxjc.requestcenter.DataModel;
import com.haocang.bjxjc.ui.equipment.bean.EquipmentBean;
import com.haocang.bjxjc.ui.tools.activity.ImageLookActivity;
import com.haocang.bjxjc.ui.tools.activity.ImageLookWebActivity;
import com.haocang.bjxjc.ui.tools.activity.VideoShowActivity;
import com.haocang.bjxjc.ui.tools.adapter.FileListAdapter;
import com.haocang.bjxjc.utils.bean.FileBean;
import com.haocang.bjxjc.utils.tools.ApiConstant;
import com.haocang.bjxjc.utils.tools.ToastUtil;
import com.haocang.bjxjc.utils.tools.UrlConst;
import com.haocang.bjxjc.utils.tools.CacheData;
import com.haocang.bjxjc.utils.tools.MyUtils;
import com.haocang.commonlib.network.interfaces.InitDataBeanBack;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.HttpHandler;

import java.util.List;

/**
 * 设备附件详情
 */

public class EquipmentFileFragment extends Fragment {
    private View view;
    private Context context;
    private ListView enclo_list_01;
    private LinearLayout downfile_jd_lin;
    private TextView downfile_jd_num;
    private TextView downfile_jd_size;
    private ProgressBar downfile_jd;
    private FileListAdapter mImageDocAdapter;

    private String billid;
    private boolean IsDowning = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_equfile, container, false);
        context = this.getActivity();
        initView();
        initEvent();
        initData();
        return view;
    }

    private void initView() {
        EquipmentBean model = CacheData.mEquipmentBean;
        enclo_list_01 = view.findViewById(R.id.enclo_list_01);
        downfile_jd_lin = view.findViewById(R.id.downfile_jd_lin);
        downfile_jd_num = view.findViewById(R.id.downfile_jd_num);
        downfile_jd_size = view.findViewById(R.id.downfile_jd_size);
        downfile_jd = view.findViewById(R.id.downfile_jd);

        mImageDocAdapter = new FileListAdapter(context);
        billid = model.getEID();

        enclo_list_01.setAdapter(mImageDocAdapter);
        downfile_jd_lin.setVisibility(View.GONE);

    }

    private void initEvent() {
        enclo_list_01.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if (IsDowning) {
                    ToastShow("正在下载文件中");
                    return;
                }
                FileBean mImgDocBean = mImageDocAdapter.getItem(arg2);
                if (mImgDocBean.getF_FileName().indexOf(".mp4") > -1) {
                    String Sd_file = Environment.getExternalStorageDirectory().getPath() + "/" + ApiConstant.APP_Code + "/video/" + mImgDocBean.getF_FileName();
                    if (!MyUtils.FileIsExists(Sd_file)) {
                        String imgUrl = ApiConstant.host + mImgDocBean.getF_FilePath().substring(1);
                        downFile(imgUrl, Sd_file, "MP4");
                    } else {
                        Intent videointent = new Intent(context, VideoShowActivity.class);
                        videointent.putExtra("URL", Sd_file);
                        startActivity(videointent);
                    }
                } else if (mImgDocBean.getF_FileName().indexOf(".wmv") > -1) {
                    String Sd_file = Environment.getExternalStorageDirectory().getPath() + "/" + ApiConstant.APP_Code + "/video/" + mImgDocBean.getF_FileName();
                    if (!MyUtils.FileIsExists(Sd_file)) {
                        String imgUrl = ApiConstant.host + mImgDocBean.getF_FilePath().substring(1);
                        downFile(imgUrl, Sd_file, "WMV");
                    } else {
                        Intent videointent = new Intent(context, VideoShowActivity.class);
                        videointent.putExtra("URL", Sd_file);
                        startActivity(videointent);
                    }
                } else if (mImgDocBean.getF_FileName().indexOf(".png") > -1 || mImgDocBean.getF_FileName().indexOf(".jpg") > -1) {
                    String imgUrl = ApiConstant.host + mImgDocBean.getF_FilePath().substring(1);

                    String Sd_file = Environment.getExternalStorageDirectory().getPath() + "/" + ApiConstant.APP_Code + "/camera/" + mImgDocBean.getF_FileName();
                    if (!MyUtils.FileIsExists(Sd_file)) {
                        downFile(imgUrl, Sd_file, "IMG");
                    } else {
                        Intent imagelookintent = new Intent(context, ImageLookActivity.class);
                        imagelookintent.putExtra("path", Sd_file);
                        startActivity(imagelookintent);
                    }
                } else {
                    Intent imagelookintent = new Intent(context, ImageLookWebActivity.class);
                    imagelookintent.putExtra("filename", mImgDocBean.getF_FileName());
                    imagelookintent.putExtra("path", mImgDocBean.getF_FilePath().substring(1));
                    startActivity(imagelookintent);
                }


            }

        });
    }

    private void initData() {
        String ApiUrl = ApiConstant.host + UrlConst.Api_GetFileListByFolderID + billid;
        DataModel.requestGETMode("FileBean", ApiUrl, new InitDataBeanBack() {
            @Override
            public void callbak(boolean b, List list, String msg) {
                if (b) {
                    mImageDocAdapter.setList(list);
                    mImageDocAdapter.notifyDataSetChanged();
                    if (list.size() <= 0) {
                        ToastUtil.show(R.string.no_attachment);
                    }
                } else {
                    ToastShow("附件请求失败,稍后重试");
                }
            }
        });

    }

    private void downFile(final String url, final String SD_URL, final String Type) {
        downfile_jd_lin.setVisibility(View.VISIBLE);

        FinalHttp fh = new FinalHttp();
        //调用download方法开始下载
        HttpHandler handler = fh.download(url, //这里是下载的路径
                //true,//true:断点续传 false:不断点续传（全新下载）
                SD_URL, //这是保存到本地的路径
                new AjaxCallBack() {
                    public void onLoading(long count, long current) {
                        int fi = (int) count / 100;
                        int schedule = (int) (current) / fi;
                        downfile_jd.setProgress(schedule);
                        long size = (count / (long) 1024) / (long) 1024;
                        downfile_jd_num.setText("文件下载中(" + schedule + "%)");
                        downfile_jd_size.setText("文件大小约:" + size + "M");
                        IsDowning = true;
                    }

                    @Override
                    public void onSuccess(Object t) {
                        super.onSuccess(t);
                        downfile_jd_lin.setVisibility(View.GONE);
                        IsDowning = false;
                        switch (Type) {
                            case "WMV":
                                Intent videointent = new Intent(context, VideoShowActivity.class);
                                videointent.putExtra("URL", SD_URL);
                                startActivity(videointent);
                                break;
                            case "MP4":
                                Intent videointentmp4 = new Intent(context, VideoShowActivity.class);
                                videointentmp4.putExtra("URL", SD_URL);
                                startActivity(videointentmp4);
                                break;
                            case "IMG":
                                Intent imagelookintent = new Intent(context, ImageLookActivity.class);
                                imagelookintent.putExtra("path", SD_URL);
                                startActivity(imagelookintent);
                                break;

                        }
                    }

                    @Override
                    public void onFailure(Throwable t, int errorNo, String strMsg) {
                        super.onFailure(t, errorNo, strMsg);
                        IsDowning = false;
                        ToastShow("下载失败" + strMsg);
                    }

                });
    }

    protected void ToastShow(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

}
