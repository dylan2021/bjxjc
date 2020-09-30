package com.haocang.commonlib.gismap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.haocang.commonlib.otherutil.TextUtils;

/**
 * 创建时间：2019/6/13
 * 编 写 人：ShenC
 * 功能描述：
 */

public class MapUtils {


    /**
     * 文字转换BitMap
     *
     * @param text
     * @return
     */
    public static Drawable createMapBitMap(Context context, String text) {


        if(text == null)text ="";

        //对text进行长度处理
        if(text.length() > 7){
            text = text.substring(0,7)+"...";
        }


        int text_size = 14;
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(text_size);
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);

        float textLength = paint.measureText(text);
        int width = (int) textLength + 6;
        int height = text_size + 5;
        Bitmap newb = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas cv = new Canvas(newb);
        // cv.drawColor(Color.argb(150,0,0,0));
        cv.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        Paint paint1 = new Paint();
        paint1.setColor(Color.argb(150, 0, 0, 0));
        paint1.setStyle(Paint.Style.FILL);
        RectF r1 = new RectF(0, 0, width, height);
        cv.drawRoundRect(r1, 3, 3, paint1);
        cv.drawText(text, width / 2, text_size, paint);
        cv.save(Canvas.ALL_SAVE_FLAG);// 保存
        cv.restore();// 释放画布


        return new BitmapDrawable(context.getResources(), newb);

    }

}
