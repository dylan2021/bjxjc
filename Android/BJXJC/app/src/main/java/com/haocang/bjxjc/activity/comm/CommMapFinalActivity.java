package com.haocang.bjxjc.activity.comm;

/**
 * 创建时间：2019/3/4
 * 编 写 人：ShenC
 * 功能描述：公用的activity
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.runtime.ArcGISRuntime;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.haocang.bjxjc.utils.tools.Const;
import com.haocang.bjxjc.utils.tools.ImageUtil;
import com.haocang.bjxjc.utils.tools.ToastUtil;
import com.haocang.commonlib.gismap.CacheTiledServiceLayer;
import com.haocang.bjxjc.R;
import com.haocang.bjxjc.activity.adapter.ImageFileAdapter;
import com.haocang.bjxjc.activity.bean.FileIndexBean;
import com.haocang.bjxjc.utils.tools.ApiConstant;
import com.haocang.commonlib.recyclerView.BaseAdapter;
import com.haocang.bjxjc.utils.tools.GisMapConfig;
import com.haocang.bjxjc.utils.tools.CacheData;
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

public abstract class CommMapFinalActivity extends FinalActivity {
    protected static String TAG = CommMapFinalActivity.class.getSimpleName();
    protected static CommMapFinalActivity mContext;
    protected MapView mMapView;//地图控件
    protected TextView tv_map_in;//图层缩放按钮
    protected TextView tv_map_out;
    protected CacheTiledServiceLayer tileLayer;//缓存地图
    protected GraphicsLayer mapGraphicsLayer;//自定义图层
    protected GraphicsLayer locationGraphicsLayer;//自定义图层
    protected CacheTiledServiceLayer pipe_tileLayer;
    protected LinearLayout tv_map_mylocation;//定位到当前位置
    protected double SingleTap_X = 0, SingleTap_Y = 0;//长按获取坐标GIS
    protected double SingleTap_MX = 0, SingleTap_MY = 0;//长按获取坐标WGS84
    protected LinearLayout bar_lin;
    protected LinearLayout iv_back;
    protected TextView tv_name;
    protected LinearLayout iv_more;
    protected ImageView iv_more_icon;
    protected boolean isShowLable = true;//是否显示文字标签背景
    protected RecyclerView mRecyclerView;
    protected ImageFileAdapter picFileAdapter;
    protected LinearLayout media;
    protected String capturePath = null;
    protected final int REQUEST_CODE_PICK_IMAGE = 1111;//相册
    protected final int REQUEST_CODE_CAPTURE_CAMEIA = 2222;//拍照
    protected final int REQUEST_CODE_CAPTURE_VIDEO = 3333;//录像
    protected ImageView videoAddBt;
    protected ImageView videoCloseBt, videoPlayBt;
    protected List<FileIndexBean> picFilelist = new ArrayList<>();
    protected List<File> vedioFilelist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ArcGISRuntime.setClientId(ApiConstant.Map_ClientId);
        mContext = this;
        initWindows();
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());
        initView();
        initMap();
        initEvent();
    }

    private void initMap() {
        mapGraphicsLayer = new GraphicsLayer();
        locationGraphicsLayer = new GraphicsLayer();
        mMapView.setMaxScale(1000);
        mMapView.setMinScale(50000);
        mMapView.setMapBackground(Color.rgb(250, 251, 246), Color.WHITE, 0, 0);//设置背景颜色
        tileLayer = new CacheTiledServiceLayer(ApiConstant.MAPBASEURL, "map");
        mMapView.addLayer(tileLayer);

        if (showMapPipe()) {
            pipe_tileLayer = new CacheTiledServiceLayer(ApiConstant.MAPPIPEURL, "pipe");
            mMapView.addLayer(pipe_tileLayer);
        }

        mMapView.addLayer(mapGraphicsLayer);
        mMapView.addLayer(locationGraphicsLayer);

        mMapView.setResolution(GisMapConfig.resolution[0]);// 设置当前显示的分辨率
    }

    private void initView() {
        bar_lin = findViewById(R.id.bar_lin);
        iv_back = findViewById(R.id.iv_back);
        tv_name = findViewById(R.id.tv_name);
        iv_more = findViewById(R.id.iv_more);
        iv_more_icon = findViewById(R.id.iv_more_icon);
        tv_map_mylocation = findViewById(R.id.tv_map_location);
        mMapView = findViewById(R.id.map);
        tv_map_in = findViewById(R.id.tv_map_in);
        tv_map_out = findViewById(R.id.tv_map_out);
        videoPlayBt = findViewById(R.id.iv_video_play);

        tv_name.setText(showTitleName());

        if (showMoreIcon() == 0) {
            iv_more.setVisibility(View.GONE);
        } else {
            iv_more.setVisibility(View.VISIBLE);
            iv_more_icon.setBackgroundResource(showMoreIcon());
        }

        if (showNavigation()) {
            bar_lin.setVisibility(View.VISIBLE);
        } else {
            bar_lin.setVisibility(View.GONE);
        }
        if (showMedia()) {
            try {
                media = findViewById(R.id.media);
                videoAddBt = findViewById(R.id.add_video1);
                videoCloseBt = findViewById(R.id.iv_video_close);
                videoCloseBt.setVisibility(View.GONE);
                mRecyclerView = findViewById(R.id.pic_rv_layout_add);
                picFileAdapter = new ImageFileAdapter(mContext);
                mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL) {
                    @Override
                    public boolean canScrollVertically() {
                        return false;
                    }
                });
                mRecyclerView.setAdapter(picFileAdapter);

                picFilelist.add(new FileIndexBean(true));
                picFileAdapter.setNewData(picFilelist);

            } catch (Exception e) {
                ToastShow("缺少布局文件！Code:layout_file_upload");
            }
        }

    }

    private void initEvent() {
        tv_map_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMapView.zoomin();
            }
        });
        tv_map_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMapView.zoomout();
            }
        });
        tv_map_mylocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CacheData.Cache_MyLocation != null) {
                    mMapView.zoomToResolution(new Point(CacheData.Cache_MyLocation.getX(), CacheData.Cache_MyLocation.getY()), 11);
                    //   mMapView.centerAt(new Point(CacheData.Cache_MyLocation.getX(),CacheData.Cache_MyLocation.getY()),true);
                } else {
                    ToastShow("当前GPS未获取到位置，请稍后重试");
                }
            }
        });

        if (showMedia()) {
            videoAddBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (vedioFilelist.size() > 0) {
                        //视频有多个
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
                }
            });

            picFileAdapter.setOnItemChildClickListener(new BaseAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseAdapter adapter, View v, int position) {
                    FileIndexBean fileItem = picFileAdapter.getItem(position);
                    if (v.getId() == R.id.iv_close) {
                        //删除fileList
                        picFilelist.remove(fileItem);
                        picFileAdapter.setNewData(picFilelist);
                        return;
                    }
                    if (v.getId() == R.id.iv_add) {

                        if (position == picFilelist.size() - 1) {
                            if (picFilelist.size() <= 8) {
                                choosePicType(Const.type_pic);
                            } else {
                                ToastUtil.show(R.string.pic_upload_tip_most_8);
                            }
                        } else {

                        }
                    }
                }
            });
        }
    }

    //添加点
    protected void addpoint(Point mapPoint) {
        mapGraphicsLayer.removeAll();
        PictureMarkerSymbol locationSymbol = new PictureMarkerSymbol(mContext.getResources().getDrawable(R.mipmap.icon_geo_blue));
        Graphic graphic = new Graphic(mapPoint, locationSymbol);
        mapGraphicsLayer.addGraphic(graphic);
    }

    //添加点
    protected void addfloodpoint(Point mapPoint) {
        mapGraphicsLayer.removeAll();
        PictureMarkerSymbol locationSymbol = new PictureMarkerSymbol(mContext.getResources().getDrawable(R.mipmap.jsd_gray));
        Graphic graphic = new Graphic(mapPoint, locationSymbol);
        mapGraphicsLayer.addGraphic(graphic);
    }

    //添加点
    protected void addpoint(Point mapPoint, int icon) {
        mapGraphicsLayer.removeAll();
        PictureMarkerSymbol locationSymbol = new PictureMarkerSymbol(mContext.getResources().getDrawable(icon));
        Graphic graphic = new Graphic(mapPoint, locationSymbol);
        mapGraphicsLayer.addGraphic(graphic);
    }


    protected void initMyLocation() {
        locationGraphicsLayer.removeAll();
        if (CacheData.Cache_MyLocation != null) {
            Point mapPoint = new Point(CacheData.Cache_MyLocation.getMx(), CacheData.Cache_MyLocation.getMy());
            PictureMarkerSymbol locationSymbol = new PictureMarkerSymbol(mContext.getResources().getDrawable(R.mipmap.icon_geo_blue));
            Graphic graphic = new Graphic(mapPoint, locationSymbol);
            locationGraphicsLayer.addGraphic(graphic);
            mMapView.centerAt(mapPoint, true);
        }
    }

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

    public static int getNavigationBarHeight() {
        int resourceId = mContext.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        return mContext.getResources().getDimensionPixelSize(resourceId);
    }

    private static int getStatusBarHeight() {
        // 获得状态栏高度
        int resourceId = mContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return mContext.getResources().getDimensionPixelSize(resourceId);
    }

    //选择图片,视频
    protected void choosePicType(final int type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style
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

    protected void getImageFromAlbum() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
        } else {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);

    }

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

    //相机获取
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
        picFileAdapter.setNewData(picFilelist);
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
        videoAddBt.setImageBitmap(bp);
    }

    //子类从这里获取数据,去上传
    protected List<File> getPicVideoFileData() {
        List<File> files = new ArrayList<>();
        for (FileIndexBean f : picFilelist) {
            if (!f.isAdd()) {
                File imgfile = new File(f.getFileUrl());
                //ToastShow("SIZE:"+imgfile.length());
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

            //相机
        } else if (requestCode == REQUEST_CODE_CAPTURE_CAMEIA) {
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

    /**
     * 压缩视频
     */
    public void postVideoResouce(String filepath, final String newfilepath) {
        if (MyTextUtils.IsNull(filepath)) {
            ToastShow("请先选择转码文件！");
            return;
        }
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
                        setPicVideoUrl(newfilepath);
                        ToastShow("压缩完成！");
                    }
                });
            }

            @Override
            public void onSaveVideoFailed(final int errorCode) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        switch (errorCode) {
                            case ERROR_NO_VIDEO_TRACK:
                                ToastShow("该文件没有视频信息！");
                                break;
                            case ERROR_SRC_DST_SAME_FILE_PATH:
                                ToastShow("源文件路径和目标路径不能相同！");
                                break;
                            case ERROR_LOW_MEMORY:
                                ToastShow("手机内存不足，无法对该视频进行时光倒流！");
                                break;
                            default:
                                ToastShow("transcode failed: " + errorCode);
                        }
                    }
                });
            }

            @Override
            public void onSaveVideoCanceled() {
//                LogUtil.e("onSaveVideoCanceled");
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
                Log.d("info", "onProgressUpdate==========" + percentage);
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


    protected void ToastShow(String msg) {
        ToastUtil.show(msg);
    }

    protected void ToastShow(int msgId) {
        ToastUtil.show(msgId);
    }

    protected abstract int getLayoutResourceId();

    /**
     * 是否显示bar
     */
    protected abstract boolean showNavigation();

    /**
     * 是否显示管线
     */
    protected abstract boolean showMapPipe();

    protected abstract int showMoreIcon();

    protected abstract String showTitleName();

    protected abstract boolean showMedia();

    protected void hideKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
    }

}
