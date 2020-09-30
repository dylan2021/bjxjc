package com.haocang.bjxjc.utils.tools;

import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.format.Time;

import com.haocang.bjxjc.App;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * 数据Util
 */
public class MyUtils {
    public static String getNowDatatoString() {
        Time t = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料
        t.setToNow(); // 取得系统时间。
        int year = t.year;
        String month = (t.month + 1) + "";
        String date = t.monthDay + "";
        String hour = t.hour + "";
        String minute = t.minute + "";
        String second = t.second + "";
        if (month.length() == 1) {
            month = "0" + month;
        }
        if (date.length() == 1) {
            date = "0" + date;
        }
        if (hour.length() == 1) {
            hour = "0" + hour;
        }
        if (minute.length() == 1) {
            minute = "0" + minute;
        }
        if (second.length() == 1) {
            second = "0" + second;
        }
        String system_time = year + "-" + month + "-" + date + " " + hour + ":" + minute + ":" + second;
        return system_time;
    }

    public static String getNowDatatoStringyyyyMMddhhmm() {

        Time t = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料
        t.setToNow(); // 取得系统时间。
        int year = t.year;
        String month = (t.month + 1) + "";
        String date = t.monthDay + "";
        String hour = t.hour + "";
        String minute = t.minute + "";
        String second = t.second + "";
        if (month.length() == 1) {
            month = "0" + month;
        }
        if (date.length() == 1) {
            date = "0" + date;
        }
        if (hour.length() == 1) {
            hour = "0" + hour;
        }
        if (minute.length() == 1) {
            minute = "0" + minute;
        }
        if (second.length() == 1) {
            second = "0" + second;
        }
        String system_time = year + "-" + month + "-" + date + " " + hour + ":" + minute;
        return system_time;
    }


    public static String getDtOfyyyyMMddHHmmss() {

        Time t = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料
        t.setToNow(); // 取得系统时间。
        int year = t.year;
        String month = (t.month + 1) + "";
        String date = t.monthDay + "";
        String hour = t.hour + "";
        String minute = t.minute + "";
        String second = t.second + "";
        if (month.length() == 1) {
            month = "0" + month;
        }
        if (date.length() == 1) {
            date = "0" + date;
        }
        if (hour.length() == 1) {
            hour = "0" + hour;
        }
        if (minute.length() == 1) {
            minute = "0" + minute;
        }
        if (second.length() == 1) {
            second = "0" + second;
        }
        String system_time = year + "" + month + "" + date + "-" + hour + "" + minute + "" + second;
        return system_time;
    }

    /**
     * 获取当前日期 格式 yyyy-mm-dd
     *
     * @return
     */
    public static String getNowStrData() {

        Time t = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料
        t.setToNow(); // 取得系统时间。
        int year = t.year;
        String month = (t.month + 1) + "";
        String date = t.monthDay + "";

        if (month.length() == 1) {
            month = "0" + month;
        }
        if (date.length() == 1) {
            date = "0" + date;
        }

        String system_time = year + "-" + month + "-" + date;
        return system_time;
    }

    /**
     * 获取当前日期 格式 yyyy-mm-dd
     * @return
     */
    public static String getLastStrData(int day) {
        Date date1 = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date1);//设置起时间
        cal.add(Calendar.DATE, day);//增加一天
        //cd.add(Calendar.DATE, -10);//减10天
        //cd.add(Calendar.MONTH, n);//增加一个月
        Date datexx = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String system_time = df.format(datexx);

        return system_time;
    }


    public static String getLastStrDataBYHour(int hour) {
        Date date1 = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date1);//设置起时间
        cal.add(Calendar.HOUR, hour);//增加一天
        //cd.add(Calendar.DATE, -10);//减10天
        //cd.add(Calendar.MONTH, n);//增加一个月
        Date datexx = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String system_time = df.format(datexx);

        return system_time;
    }

    /**
     * 获取当前的下一天 日期 格式 yyyy-MM-dd hh:mm:ss
     *
     * @return
     */
    public static String getLastDatatoString() {
        Date date1 = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date1);//设置起时间
        cal.add(Calendar.DATE, 1);//增加一天
        //cd.add(Calendar.DATE, -10);//减10天
        //cd.add(Calendar.MONTH, n);//增加一个月


        Date datexx = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String system_time = df.format(datexx);

        return system_time;
    }


    /**
     * 获取年份和月份   yyyymm
     *
     * @return
     */
    public String getMonthDt() {

        String system_time = getNowDatatoString();

        String year = system_time.substring(0, 3);
        String month = system_time.substring(5, 6);


        return year + month;
    }

    /**
     * <p>Description:get System time（获取系统时间,hh:mm:ss） </p>
     * <p>return: time</p>
     *
     * @author shencong
     * @date 2016-7-21 下午3:18:46
     */
    public Time getNowData() {

        Time t = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料
        t.setToNow(); // 取得系统时间。

        return t;
    }


    public static String getNowTimetoString() {

        Time t = new Time();
        t.setToNow();
        int year = t.year;
        String month = (t.month + 1) + "";
        String date = t.monthDay + "";
        String hour = t.hour + "";
        String minute = t.minute + "";
        String second = t.second + "";
        if (month.length() == 1) {
            month = "0" + month;
        }
        if (date.length() == 1) {
            date = "0" + date;
        }
        if (hour.length() == 1) {
            hour = "0" + hour;
        }
        if (minute.length() == 1) {
            minute = "0" + minute;
        }
        if (second.length() == 1) {
            second = "0" + second;
        }
        String system_time = year + "" + month + "" + date + "" + hour + "" + minute + "" + second;
        return system_time;
    }

    /**
     * <p>Description: get uuid（获取UUID）</p>
     * <p>return: String(uuids)</p>
     *
     * @author shencong
     * @date 2016-7-21 下午3:21:49
     */
    public String getUUID() {
        UUID uuid = UUID.randomUUID();
        String uuids = uuid.toString();
        return uuids;

    }

    public static int computeSampleSize(BitmapFactory.Options options,
                                        int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength,
                maxNumOfPixels);

        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options,
                                                int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
                .sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math
                .floor(w / minSideLength), Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    public static Bitmap getBitmapFromFile(File dst, int width, int height) {
        if (null != dst && dst.exists()) {
            BitmapFactory.Options opts = null;
            if (width > 0 && height > 0) {
                opts = new BitmapFactory.Options();

                //设置inJustDecodeBounds为true后，decodeFile并不分配空间，此时计算原始图片的长度和宽度
                opts.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(dst.getPath(), opts);
                // 计算图片缩放比例
                final int minSideLength = Math.min(width, height);
                opts.inSampleSize = computeSampleSize(opts, minSideLength,
                        width * height);

                //这里一定要将其设置回false，因为之前我们将其设置成了true
                opts.inJustDecodeBounds = false;
                opts.inInputShareable = true;
                opts.inPurgeable = true;
            }
            try {
                return BitmapFactory.decodeFile(dst.getPath(), opts);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 判断 GPS是否开启
     */
    public static final boolean isOPenGPS(final Context context) {
        LocationManager locationManager
                = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps) {
            return true;
        }

        return false;
    }

    public static void gotoLocServiceSettings(Context context) {
        final Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 强制帮用户打开GPS
     */
    public static final void openGPS(Context context) {
        Intent GPSIntent = new Intent();
        GPSIntent.setClassName("com.android.settings",
                "com.android.settings.widget.SettingsAppWidgetProvider");
        GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
        GPSIntent.setData(Uri.parse("custom:3"));
        try {
            PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
        } catch (CanceledException e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载本地图片 http://bbs.3gstdy.com
     */
    public static Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static boolean FileIsExists(String url) {
        try {
            File f = new File(url);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    public static Bitmap getBitmapFromFilePath(String ImgUrl) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 3;//图片宽高都为原来的二分之一，即图片为原来的四分之一
        Bitmap bm = BitmapFactory.decodeFile(ImgUrl, options);

        int src_w = bm.getWidth();
        int src_h = bm.getHeight();

        float scale_w = ((float) 100) / src_w;
        float scale_h = ((float) 100) / src_h;
        Matrix matrix = new Matrix();
        //  matrix.postScale(scale_w, scale_h);
        Bitmap dstbmp = Bitmap.createBitmap(bm, 0, 0, src_w, src_h, matrix,
                true);

        return dstbmp;
    }

    /**
     * 保存文件
     *
     * @param bm
     * @param fileName
     */
    public static File saveFile(Bitmap bm, String fileName) {
        try {
            String out_file_path = Environment.getExternalStorageDirectory().getPath() + "/" + ApiConstant.APP_Code + "/camera/";
            File dir = new File(out_file_path);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File myCaptureFile = new File(out_file_path + "img_" + fileName);
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
            bm.compress(Bitmap.CompressFormat.JPEG, 95, bos);
            bos.flush();
            bos.close();

            return myCaptureFile;
        } catch (Exception e) {
            return null;
        }
    }

    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) {
            return null;
        }
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }


    public static Uri GetFileProviderUri(Context context, String path) {
        Uri URI = FileProvider.getUriForFile(context, App.getInstance().getPackageName() + ".provider", new File(path));
        return URI;
    }


    /**
     * 文字转换BitMap
     *
     * @param text
     * @return
     */
    public static Drawable createMapBitMap(Context context, String text, int color) {

        int text_size = 12;

        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(text_size);
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);
        float textLength = paint.measureText(text);
        int width = (int) textLength + 2;
        int height = text_size + 5;

        Bitmap newb = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas cv = new Canvas(newb);
        cv.drawColor(color);
        cv.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        cv.drawText(text, width / 2, text_size, paint);
        cv.save(Canvas.ALL_SAVE_FLAG);// 保存
        cv.restore();// 存储

        return new BitmapDrawable(context.getResources(), newb);

    }


    /**
     * ===============================================
     * 创建时间：2019/4/18 12:18
     * 编 写 人：ShenC
     * 方法说明：当前时间变化N天
     * ================================================
     * 修改内容：      修改时间：      修改人：
     * ===============================================
     */
    public static String ConverToDateToString(String strDate, int day) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date dt = df.parse(strDate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);//设置起时间
        cal.add(Calendar.DATE, day);//增加一天
        Date datexx = cal.getTime();
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
        String system_time = df1.format(datexx);

        return system_time;

    }

}
