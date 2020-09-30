package com.haocang.commonlib.gismap;

/**
 * 创建时间：2019/5/20
 * 编 写 人：ShenC
 * 功能描述：
 */

public class GisMapConfig {


    public static int MAP_DEFAULT_RES = 10;
    public static int MAP_MIN_RES = 0;
    public static int MAP_MAX_RES = 11;
    public static int MAP_DEFAULT_SCALE = 10;
    public static int MAP_MIN_SCALE  = 0;
    public static int MAP_MAX_SCALE = 11;

    public static double[] resolution = new double[]{
            132.2919312505292,
            79.37515875031751,
            66.1459656252646,
            31.750063500127002,
            15.875031750063501,
            7.9375158750317505,
            3.9687579375158752,
            2.116670900008467,
            1.0583354500042335,
            0.5291677250021167,
            0.26458386250105836,
            0.13229193125052918
    };
    public static double[] scale = new double[]{
            500000,
            300000,
            250000,
            120000,
            60000,
            30000,
            15000,
            8000,
            4000,
            2000,
            1000,
            500
    };

}
