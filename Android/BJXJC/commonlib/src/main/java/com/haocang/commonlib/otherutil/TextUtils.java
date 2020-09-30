package com.haocang.commonlib.otherutil;

import android.support.annotation.NonNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 创建时间：2019/5/24
 * 编 写 人：ShenC
 * 功能描述：
 */

public class TextUtils {

    public static String formatDateToMD(String str) {
        SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMMddHHmm");
        SimpleDateFormat sf2 = new SimpleDateFormat("MM-dd HH:mm");
        String formatStr = "";
        try {
            formatStr = sf2.format(sf1.parse(str));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formatStr;
    }

    public static String formatDateToMD(String dt,String format1,String format2) {
        SimpleDateFormat sf1 = new SimpleDateFormat(format1);
        SimpleDateFormat sf2 = new SimpleDateFormat(format2);
        String formatStr = "";
        try {
            formatStr = sf2.format(sf1.parse(dt));
        } catch (ParseException e) {
            e.printStackTrace();
            return dt;
        }
        return formatStr;
    }

    /**
     * ===============================================
     * 创建时间：2019/1/23 11:21
     * 编 写 人：ShenC
     * 方法说明：yyyy-mm-dd string时间比较，结束时间是否大于等于开始时间
     * ================================================
     * 修改内容：      修改时间：      修改人：
     * ===============================================
     */
    public static boolean CompareStrData(String str_startdt, String str_enddt) {

        try {
            String ls_year1 = str_startdt.substring(0, 4);
            String ls_month1 = str_startdt.substring(5, 7);
            String ls_day1 = str_startdt.substring(8, 10);
            String ls_hour1 = str_startdt.substring(11, 13);
            String ls_min1 = str_startdt.substring(14, 16);

            String ls_year2 = str_enddt.substring(0, 4);
            String ls_month2 = str_enddt.substring(5, 7);
            String ls_day2 = str_enddt.substring(8, 10);
            String ls_hour2 = str_enddt.substring(11, 13);
            String ls_min2 = str_enddt.substring(14, 16);

            int li_year1 = Integer.parseInt(ls_year1);
            int li_month1 = Integer.parseInt(ls_month1);
            int li_day1 = Integer.parseInt(ls_day1);
            int li_hour1 =  Integer.parseInt(ls_hour1);
            int li_min1 =  Integer.parseInt(ls_min1);

            int li_year2 = Integer.parseInt(ls_year2);
            int li_month2 = Integer.parseInt(ls_month2);
            int li_day2 = Integer.parseInt(ls_day2);
            int li_hour2 =  Integer.parseInt(ls_hour2);
            int li_min2 =  Integer.parseInt(ls_min2);

            if (li_year2 < li_year1) {  //结束年份小于开始年份 不允许
                return false;
            } else if (li_year2 == li_year1 && li_month2 < li_month1) {  //结束年份==开始年份，月份小于 不允许
                return false;
            } else if (li_year2 == li_year1 && li_month2 == li_month1 && li_day2 < li_day1) {  //结束年份==开始年份，月份相等 ，日小于 不允许
                return false;
            } else if (li_year2 == li_year1 && li_month2 == li_month1 && li_day2 == li_day1 && li_hour2 < li_hour1) {  //结束年份==开始年份，月份相等 ，日小于 不允许
                return false;
            } else if (li_year2 == li_year1 && li_month2 == li_month1 && li_day2 == li_day1 && li_hour2 == li_hour1 && li_min2 < li_min1) {  //结束年份==开始年份，月份相等 ，日小于 不允许
                return false;
            }

            return true;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static String date2String(final Date date, @NonNull final DateFormat format) {
        return format.format(date);
    }

}
