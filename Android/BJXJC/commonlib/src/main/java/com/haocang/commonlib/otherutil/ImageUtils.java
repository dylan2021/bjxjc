package com.haocang.commonlib.otherutil;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

/**
 * 创建时间：2019/5/27
 * 编 写 人：ShenC
 * 功能描述：
 */

public class ImageUtils {


    /**===============================================
     * 创建时间：2019/5/27 10:38
     * 编 写 人：ShenC
     * 方法说明：本地图片压缩，并显示为bitmap
     *================================================
     * 修改内容：      修改时间：      修改人：
     *===============================================*/
    public static Bitmap getBitmapFilePath(String ImgUrl) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 3;//图片宽高都为原来的二分之一，即图片为原来的四分之一
        Bitmap bm = BitmapFactory.decodeFile(ImgUrl, options);
        int src_w,src_h;
        try {
             src_w = bm.getWidth();
             src_h = bm.getHeight();
        }catch (Exception e){
             src_w = 500;
             src_h = 800;
        }

        float scale_w = ((float) 100) / src_w;
        float scale_h = ((float) 100) / src_h;
        Matrix matrix = new Matrix();
        matrix.postScale(scale_w, scale_h);
        Bitmap dstbmp = Bitmap.createBitmap(bm, 0, 0, src_w, src_h, matrix, true);
        return dstbmp;
    }
}
