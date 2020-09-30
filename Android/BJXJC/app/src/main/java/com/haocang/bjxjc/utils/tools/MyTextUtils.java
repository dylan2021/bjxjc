package com.haocang.bjxjc.utils.tools;

import android.support.annotation.NonNull;
import android.text.format.Time;
import android.util.Log;

import com.haocang.bjxjc.utils.bean.ProvinceBean;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * 创建时间：2019/3/4
 * 编 写 人：ShenC
 * 功能描述：
 */

public class MyTextUtils {

    public static String  getNowDatatoString(){
        Time t=new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料
        t.setToNow(); // 取得系统时间。
        int year = t.year;
        String month = (t.month + 1)+"";
        String date = t.monthDay+"";
        String hour = t.hour+"";
        String minute = t.minute +"";
        String second = t.second +"";
        if(month.length() == 1){  month = "0"+ month;}
        if(date.length() == 1){  date = "0"+ date;}
        if(hour.length() == 1){  hour = "0"+ hour;}
        if(minute.length() == 1){  minute = "0"+ minute;}
        if(second.length() == 1){  second = "0"+ second;}
        String system_time = year+"-"+month+"-"+date+" "+hour+":"+minute+":"+second;
        return system_time;
    }




    public static String  getNowTimetoString(){
        Time t=new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料
        t.setToNow(); // 取得系统时间。
        String hour = t.hour+"";
        String minute = t.minute +"";
        if(hour.length() == 1){  hour = "0"+ hour;}
        if(minute.length() == 1){  minute = "0"+ minute;}
        String system_time = hour+":"+minute;
        return system_time;
    }

    public static String  getNowDaytoString(){
        Time t=new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料
        t.setToNow(); // 取得系统时间。
        int year = t.year;

        String month = (t.month + 1)+"";
        String date = t.monthDay+"";
        int li_weekday =   t.weekDay;

        String str_wk = "日";
        switch (li_weekday){
            case 1: str_wk = "一"; break;
            case 2: str_wk = "二"; break;
            case 3: str_wk = "三"; break;
            case 4: str_wk = "四"; break;
            case 5: str_wk = "五"; break;
            case 6: str_wk = "六"; break;
            case 7: str_wk = "日"; break;
            case 0: str_wk = "日"; break;
        }

        if(month.length() == 1){  month = "0"+ month;}
        if(date.length() == 1){  date = "0"+ date;}
        String system_time = year+"年"+month+"月"+date+"日  星期"+  str_wk;
        return system_time;
    }



    public static String date2String(final Date date, @NonNull final DateFormat format) {
        return format.format(date);
    }

    public static String getTimeFormat(Date date, String mat) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat(mat);
        return format.format(date);
    }

    /**
     * 通过选项获取对应的值
     *
     * @param list
     * @param Type  0 获取id ，1 获取text
     * @param value
     * @return
     */
    public static String GetOpitemValue(ArrayList<ProvinceBean> list, int Type, String value) {
        if (value == null) {
            return "";
        }
        if (Type == 0) {
            for (ProvinceBean model : list) {
                if (model.getPickerViewText().equals(value)) {
                    return model.getDescription();
                }
            }
        } else {
            for (ProvinceBean model : list) {
                if (model.getDescription().equals(value)) {
                    return model.getName();
                }
            }
        }
        return "";
    }

    /**

     */
    public static Boolean IsNull(String value) {
        if (value == null || value.equals("")
                || value.equals("null") || value.equals("NULL")
                || value.equals("Null") || value.equals("undefined")) {
            return true;
        }
        return false;
    }


    public static String getString(String value) {
        if (IsNull(value)) {
            return "";
        }
        return value;
    }


    public static String GetStringByValue(String value) {

        if (value == null || value.equals("")
                || value.equals("null") || value.equals("NULL")
                || value.equals("Null") || value.equals("undefined")) {
            return "";
        }
        return value;
    }


    /**
     * ===============================================
     * 创建时间：2018/12/24 17:35
     * 编 写 人：ShenC
     * 方法说明：将时间转换为简洁的描述 例如 今天是 2018年12月24日 ，则 2018-12-24 14：22 》 下午2:22
     * ================================================
     * 修改内容：      修改时间：      修改人：
     * ===============================================
     */
    public static String GetSimpleDtName(String str) {

        String str_dt=str;
        try {

            if (IsNull(str_dt)) {
                return "";
            }
            str_dt = MyTextUtils.toYMDHM(str_dt);
            //当是今天的消息时
            if (MyUtils.getLastStrData(0).equals(str_dt.substring(0, 10))) {
                str_dt =  str_dt.substring(11);
            } else if (MyUtils.getLastStrData(-1).equals(str_dt.substring(0, 10))) {
                str_dt = "昨天 " + str_dt.substring(11);
            } else if (MyUtils.getLastStrData(-2).equals(str_dt.substring(0, 10))) {
                str_dt = "前天 " + str_dt.substring(11);
            }
            return str_dt;

        }catch (Exception e){
            return "";
        }
    }

    public static String GetTimeName(String type) {

        String str = MyUtils.getDtOfyyyyMMddHHmmss()
                + "-" + UUID.randomUUID().toString().substring(0, 8) + type;
        return str;
    }


    public static String GetNo(String type) {
        String str = type + MyUtils.getDtOfyyyyMMddHHmmss();
        return str;
    }


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
            String ls_year2 = str_enddt.substring(0, 4);
            String ls_month2 = str_enddt.substring(5, 7);
            String ls_day2 = str_enddt.substring(8, 10);

            int li_year1 = Integer.parseInt(ls_year1);
            int li_month1 = Integer.parseInt(ls_month1);
            int li_day1 = Integer.parseInt(ls_day1);
            int li_year2 = Integer.parseInt(ls_year2);
            int li_month2 = Integer.parseInt(ls_month2);
            int li_day2 = Integer.parseInt(ls_day2);

            if (li_year2 < li_year1) {  //结束年份小于开始年份 不允许
                return false;
            } else if (li_year2 == li_year1 && li_month2 < li_month1) {  //结束年份==开始年份，月份小于 不允许
                return false;
            } else if (li_year2 == li_year1 && li_month2 == li_month1 && li_day2 < li_day1) {  //结束年份==开始年份，月份相等 ，日小于 不允许
                return false;
            }

            return true;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String toYMDHM(String str) {
        try {
            SimpleDateFormat sf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat sf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String formatStr = "";
            try {
                formatStr = sf2.format(sf1.parse(str));
                return formatStr;
            } catch (ParseException e) {
                e.printStackTrace();
                return  formatStr;
            }
        }catch (Exception e){
            Log.d("数据", "解析失败"+e.toString());
            return "";
        }



    }


    public static String toYMD(String str) {

        SimpleDateFormat sf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sf2 = new SimpleDateFormat("yyyy-MM-dd");
        String formatStr = "";
        try {
            formatStr = sf2.format(sf1.parse(str));
            return formatStr;
        } catch (ParseException e) {
            e.printStackTrace();
            return  str;
        }

    }
    /**===============================================
     * 创建时间：2019/4/15 10:41
     * 编 写 人：ShenC
     * 方法说明：double 保留2为小时
     *================================================
     * 修改内容：      修改时间：      修改人：
     *===============================================*/
    public static double ToTwoplaceDouble(double dou){
       // double d  = Double.valueOf(str);
        double  d = (double) Math.round(dou * 100) / 100;
        return d;
    }


    /**===============================================
     * 创建时间：2019/4/15 10:41
     * 编 写 人：ShenC
     * 方法说明：string 转double 保留2为小时
     *================================================
     * 修改内容：      修改时间：      修改人：
     *===============================================*/
    public static double StringToTwoplaceDouble(String str){
        try {
            double dou  = Double.valueOf(str);
            double  d = (double) Math.round(dou * 100) / 100;
            return d;
        }catch (Exception e){
            return 0;
        }
    }




}
