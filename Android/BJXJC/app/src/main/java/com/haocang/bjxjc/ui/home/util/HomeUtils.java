package com.haocang.bjxjc.ui.home.util;

import android.support.annotation.DrawableRes;

import com.haocang.bjxjc.R;

/**
 * 创建时间：2019/7/15
 * 编 写 人：ShenC
 * 功能描述：
 */

public class HomeUtils {


    /**
     * ===============================================
     * 创建时间：2019/7/15 15:22
     * 编 写 人：ShenC
     * 方法说明：根据天气类型获取图片
     * ================================================
     * 修改内容：      修改时间：      修改人：
     * ===============================================
     */
    public static int GetWeatherImg(String type) {

        switch (type) {
            case "暴雪":
                return R.mipmap.img_w_01;
            case "暴雨":
                return R.mipmap.img_w_02;
            case "暴雨到大暴雨":
                return R.mipmap.img_w_03;
            case "大暴雨":
                return R.mipmap.img_w_04;
            case "大暴雨到特大暴雨":
                return R.mipmap.img_w_05;
            case "大到暴雪":
                return R.mipmap.img_w_06;
            case "大到暴雨":
                return R.mipmap.img_w_07;
            case "大雾":
                return R.mipmap.img_w_08;
            case "大雪":
                return R.mipmap.img_w_09;
            case "大雨":
                return R.mipmap.img_w_10;
            case "冻雨":
                return R.mipmap.img_w_11;
            case "多云":
                return R.mipmap.img_w_12;
            case "浮尘":
                return R.mipmap.img_w_13;
            case "雷阵雨":
                return R.mipmap.img_w_14;
            case "雷阵雨伴有冰雹":
                return R.mipmap.img_w_15;
            case "霾":
                return R.mipmap.img_w_16;
            case "浓雾":
                return R.mipmap.img_w_17;
            case "强浓雾":
                return R.mipmap.img_w_18;
            case "强沙尘暴":
                return R.mipmap.img_w_19;
            case "晴":
                return R.mipmap.img_w_20;
            case "沙尘暴":
                return R.mipmap.img_w_21;
            case "特大暴雨":
                return R.mipmap.img_w_22;
            case "特强浓雾":
                return R.mipmap.img_w_23;
            case "无":
                return R.mipmap.img_w_24;
            case "雾":
                return R.mipmap.img_w_25;
            case "小到中雪":
                return R.mipmap.img_w_26;
            case "小到中雨":
                return R.mipmap.img_w_27;
            case "小雪":
                return R.mipmap.img_w_28;
            case "小雨":
                return R.mipmap.img_w_29;
            case "雪":
                return R.mipmap.img_w_30;
            case "严重霾":
                return R.mipmap.img_w_31;
            case "扬沙":
                return R.mipmap.img_w_32;
            case "阴":
                return R.mipmap.img_w_33;
            case "雨":
                return R.mipmap.img_w_34;
            case "雨夹雪":
                return R.mipmap.img_w_35;
            case "阵雪（夜间）":
                return R.mipmap.img_w_36;
            case "阵雪":
                return R.mipmap.img_w_37;
            case "阵雨":
                return R.mipmap.img_w_38;
            case "中到大雪":
                return R.mipmap.img_w_39;
            case "中到大雨":
                return R.mipmap.img_w_40;
            case "中度霾":
                return R.mipmap.img_w_41;
            case "中雪":
                return R.mipmap.img_w_42;
            case "中雨":
                return R.mipmap.img_w_43;
            case "重度霾":
                return R.mipmap.img_w_44;
            default:
                return R.mipmap.img_w_24;
        }
    }

}
