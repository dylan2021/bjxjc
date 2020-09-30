package com.haocang.bjxjc.activity.comm;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
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
import com.haocang.bjxjc.activity.adapter.ImageFileAdapter;
import com.haocang.bjxjc.activity.bean.FileIndexBean;
import com.haocang.bjxjc.utils.tools.ApiConstant;
import com.haocang.bjxjc.utils.tools.Const;
import com.haocang.bjxjc.utils.tools.ImageUtil;
import com.haocang.bjxjc.utils.tools.ToastUtil;
import com.haocang.commonlib.loadingdialog.LoadingDialog;
import com.haocang.commonlib.recyclerView.BaseAdapter;
import com.haocang.bjxjc.utils.tools.MyUtils;
import com.haocang.bjxjc.utils.tools.MyTextUtils;
import com.qiniu.pili.droid.shortvideo.PLShortVideoTranscoder;
import com.qiniu.pili.droid.shortvideo.PLVideoSaveListener;

import net.tsz.afinal.FinalActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.qiniu.pili.droid.shortvideo.PLErrorCode.ERROR_LOW_MEMORY;
import static com.qiniu.pili.droid.shortvideo.PLErrorCode.ERROR_NO_VIDEO_TRACK;
import static com.qiniu.pili.droid.shortvideo.PLErrorCode.ERROR_SRC_DST_SAME_FILE_PATH;

//多媒体文件上传功能
public abstract class CommMediaActivity extends FinalActivity {
    protected RecyclerView picRvLayoutAdd;
    protected ImageFileAdapter picListAdapter;
    protected static CommMediaActivity mContext;
    protected LinearLayout fileLayout;
    protected LinearLayout barLayout;
    protected LinearLayout backLayout;
    protected TextView tv_name;
    protected LinearLayout attachmentLayout;
    protected ImageView moreBt, videoPlayBt;
    protected String capturePath = null;
    protected final int REQUEST_CODE_PICK_IMAGE = 1111;//相册
    protected final int REQUEST_CODE_CAPTURE_CAMEIA = 2222;//拍照
    protected final int REQUEST_CODE_CAPTURE_VIDEO = 3333;//录像
    protected ImageView videoAddBt;
    protected ImageView videoCloseBt;
    protected List<FileIndexBean> picFilelist = new ArrayList<>();
    protected List<File> vedioFilelist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext = this;
        initWindows();
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());
        initView();
        initEvent();
        picFilelist.add(new FileIndexBean(true));
        picListAdapter.setNewData(picFilelist);
    }

    private void initView() {
        fileLayout = findViewById(R.id.media);
        barLayout = findViewById(R.id.bar_lin);
        backLayout = findViewById(R.id.iv_back);
        tv_name = findViewById(R.id.tv_name);
        attachmentLayout = findViewById(R.id.iv_more);
        moreBt = findViewById(R.id.iv_more_icon);
        videoPlayBt = findViewById(R.id.iv_video_play);

        videoAddBt = findViewById(R.id.add_video1);
        videoCloseBt = findViewById(R.id.iv_video_close);
        videoCloseBt.setVisibility(View.GONE);
        picRvLayoutAdd = findViewById(R.id.pic_rv_layout_add);
        tv_name.setText(showTitleName());
        if (showMoreIcon() == 0) {
            attachmentLayout.setVisibility(View.GONE);
        } else {
            attachmentLayout.setVisibility(View.VISIBLE);
            moreBt.setBackgroundResource(showMoreIcon());
        }
        if (showNavigation()) {
            barLayout.setVisibility(View.VISIBLE);
        } else {
            barLayout.setVisibility(View.GONE);
        }

        if (showMedia()) {
            fileLayout.setVisibility(View.VISIBLE);
        } else {
            fileLayout.setVisibility(View.GONE);
        }


        picListAdapter = new ImageFileAdapter(mContext);
        picRvLayoutAdd.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        picRvLayoutAdd.setAdapter(picListAdapter);
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
     * 多媒体
     *
     * @return
     */
    protected abstract boolean showMedia();

    /**
     * 设置显示的图标,0为隐藏
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

    protected void ToastShow(int msg) {
        ToastUtil.show(msg);
    }

    protected void ToastShow(String msg) {
        ToastUtil.show(msg);
    }


    private void initEvent() {
        videoAddBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vedioFilelist.size() > 0) {
                    //视频有多个:
                } else {
                    //无视频
                    choosePicType(Const.type_video);
                }
            }
        });

        videoCloseBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vedioFilelist.clear();
                videoAddBt.setImageResource(R.mipmap.ic_attachment_add);
                videoCloseBt.setVisibility(View.GONE);
                videoPlayBt.setVisibility(View.GONE);
            }
        });

        picListAdapter.setOnItemChildClickListener(new BaseAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseAdapter adapter, View v, int position) {
                FileIndexBean fileItem = picListAdapter.getItem(position);
                //删除
                if (v.getId() == R.id.iv_close) {
                    picFilelist.remove(fileItem);
                    picListAdapter.setNewData(picFilelist);
                    return;
                }
                //添加
                if (v.getId() == R.id.iv_add) {
                    if (position == picFilelist.size() - 1) {
                        if (picFilelist.size() <= 8) {
                            choosePicType(Const.type_pic);
                        } else {
                            ToastUtil.show(R.string.pic_upload_tip_most_8);
                        }
                    } else {
                        //预览
                    }
                }
            }
        });
    }

    //选择图片,视频
    protected void choosePicType(final int type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CommMediaActivity.this, R.style
                .dialog_appcompat_theme);
        //    指定下拉列表的显示数据
        final String[] chooseFileArr = {"拍摄", "从手机相册选择"};
        //    设置一个下拉的列表选择项
        builder.setItems(chooseFileArr, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                switch (i) {
                    //相册
                    case 0:
                        if (type == Const.type_pic) {
                            getPicCamera();
                        } else {
                            getVideoCamera();
                        }
                        break;
                    //相机
                    case 1:
                        getImageFromAlbum();
                        break;
                }
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        //图片
        if (requestCode == REQUEST_CODE_PICK_IMAGE) {
            if (data != null) {
                Uri uri = data.getData();
                String url = MyUtils.getRealFilePath(mContext, uri);
                setPicVideoUrl(url);
            }
        } else if (requestCode == REQUEST_CODE_CAPTURE_CAMEIA) {//相机
            setPicVideoUrl(capturePath);
            //视频
        } else if (requestCode == REQUEST_CODE_CAPTURE_VIDEO) {
            //压缩文件
            String out_file_path = Environment.getExternalStorageDirectory().getPath() + "/" + ApiConstant.APP_Code + "/video/thumb/";
            File dir = new File(out_file_path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            //获取文件名称
            String name = capturePath;
            int index = name.indexOf("video");
            String filename = name.substring(index + 6, name.length());
            String videourl = out_file_path + filename;

            //处理视频
            postVideoResouce(capturePath, videourl);
        }

    }

    //图片--从相册
    protected void getImageFromAlbum() {
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT < 19) {
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
        } else {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
    }

    //摄像--视频
    protected void getVideoCamera() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            String out_file_path = Environment.getExternalStorageDirectory().getPath() + "/" + ApiConstant.APP_Code + "/video/";
            File dir = new File(out_file_path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            capturePath = out_file_path + MyUtils.getNowTimetoString() + ".mp4";
            intent.putExtra(MediaStore.EXTRA_OUTPUT, MyUtils.GetFileProviderUri(mContext, capturePath));
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10);//限制录制时间10秒
            startActivityForResult(intent, REQUEST_CODE_CAPTURE_VIDEO);
            //ToastShow("视频只能录制10秒！");
        } else {
            Toast.makeText(getApplicationContext(), "请确认已经插入SD卡", Toast.LENGTH_LONG).show();
        }
    }

    //相机 --图片
    protected void getPicCamera() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            String out_file_path = Environment.getExternalStorageDirectory().getPath() + "/" + ApiConstant.APP_Code + "/camera/";
            File dir = new File(out_file_path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            capturePath = out_file_path + MyUtils.getNowTimetoString() + ".jpg";
            intent.putExtra(MediaStore.EXTRA_OUTPUT, MyUtils.GetFileProviderUri(mContext, capturePath));
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            startActivityForResult(intent, REQUEST_CODE_CAPTURE_CAMEIA);
        } else {
            Toast.makeText(getApplicationContext(), "请确认已经插入SD卡", Toast.LENGTH_LONG).show();
        }
    }

    //图片  视频
    protected void setPicVideoUrl(String url) {
        if (ImageUtil.isImageSuffix(url)) {
            setPic(url);
        } else {
            setVideo(url);
        }
    }

    protected void setPic(String imgUrl) {
        for (FileIndexBean m : picFilelist) {
            if (m.isAdd()) {
                picFilelist.remove(m);
            }
        }
        picFilelist.add(new FileIndexBean(picFilelist.size() + 1, imgUrl));
        picFilelist.add(new FileIndexBean(true));
        picListAdapter.setNewData(picFilelist);
    }

    //视频
    protected void setVideo(String videoUrl) {
        File file = new File(videoUrl);
        vedioFilelist.add(file);
        videoCloseBt.setVisibility(View.VISIBLE);
        videoPlayBt.setVisibility(View.VISIBLE);
        //设置视频的缩略图
        Bitmap bp = ImageUtil.getVideoThumbnail(videoUrl, videoAddBt.getWidth(), videoAddBt.getHeight(),
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        //videoAddBt.setPadding(10,10,0,0);
        videoAddBt.setImageBitmap(bp);
    }

    //获取文件列表文件
    protected List<File> getPicFilelist() {
        List<File> files = new ArrayList<>();
        for (FileIndexBean f : picFilelist) {
            if (!f.isAdd()) {
                File imgfile = new File(f.getFileUrl());
                Bitmap mp = MyUtils.getBitmapFromFilePath(f.getFileUrl());
                File newFile = MyUtils.saveFile(mp, imgfile.getName());
                files.add(newFile);
            }
        }
        if (vedioFilelist.size() > 0) {
            files.add(vedioFilelist.get(0));
        }
        return files;
    }

    /**
     * 压缩视频
     */
    public void postVideoResouce(String filepath, final String newfilepath) {
        if (MyTextUtils.IsNull(filepath)) {
            ToastShow("请先选择转码文件！");
            return;
        }
        final LoadingDialog dialog = new LoadingDialog(mContext, "加载中");
        dialog.show();
        //PLShortVideoTranscoder初始化，三个参数，第一个context，第二个要压缩文件的路径，第三个视频压缩后输出的路径
        PLShortVideoTranscoder mShortVideoTranscoder = new PLShortVideoTranscoder(mContext, filepath, newfilepath);
        MediaMetadataRetriever retr = new MediaMetadataRetriever();
        retr.setDataSource(filepath);
        String height = retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT); // 视频高度
        String width = retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH); // 视频宽度
        String rotation = retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION); // 视频旋转方向
        int transcodingBitrateLevel = 6;//我这里选择的2500*1000压缩，这里可以自己选择合适的压缩比例
        mShortVideoTranscoder.transcode(Integer.parseInt(width), Integer.parseInt(height), getEncodingBitrateLevel(transcodingBitrateLevel), false, new PLVideoSaveListener() {
            @Override
            public void onSaveVideoSuccess(String s) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //视频压缩完成
                        dialog.close();
                        setPicVideoUrl(newfilepath);
                    }
                });
            }

            @Override
            public void onSaveVideoFailed(final int errorCode) {
                dialog.close();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        switch (errorCode) {
                            case ERROR_NO_VIDEO_TRACK:
                                ToastShow("该文件没有视频信息");
                                break;
                            case ERROR_SRC_DST_SAME_FILE_PATH:
                                ToastShow("源文件路径和目标路径不能相同");
                                break;
                            case ERROR_LOW_MEMORY:
                                ToastShow("手机内存不足，视频解析失败");
                                break;
                            default:
                                ToastShow("视频解析失败: " + errorCode);
                        }
                    }
                });
            }

            @Override
            public void onSaveVideoCanceled() {
                dialog.close();
            }

            @Override
            public void onProgressUpdate(float percentage) {
//				runOnUiThread(new Runnable() {
//					@Override
//					public void run() {
//						setVideo(newfilepath);
//						ToastShow("压缩完成！");
//					}
//				});
            }
        });
    }

    /**
     * 设置压缩质量
     *
     * @param position
     * @return
     */
    private int getEncodingBitrateLevel(int position) {
        return ENCODING_BITRATE_LEVEL_ARRAY[position];
    }

    /**
     * 选的越高文件质量越大，质量越好
     */
    public static final int[] ENCODING_BITRATE_LEVEL_ARRAY = {
            500 * 1000,
            800 * 1000,
            1000 * 1000,
            1200 * 1000,
            1600 * 1000,
            2000 * 1000,
            2500 * 1000,
            4000 * 1000,
            8000 * 1000,
    };

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

