package com.haocang.bjxjc.utils.tools;

import android.util.Log;

import com.haocang.bjxjc.App;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 时间Utils
 */
public class TimeUtils {

    public static String getTimeYM(long time) {
        if (time == 0) {
            return "";
        }
        sdf_Ym.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        return sdf_Ym.format(new Date(time));
    }


    private static SimpleDateFormat sdf_YmdHms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat sdf_YmdHm = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private static SimpleDateFormat sdf_Ymd = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat sdf_YYYYMMDD = new SimpleDateFormat("yyyyMMdd");
    private static SimpleDateFormat sdf_Ym = new SimpleDateFormat("yyyy-MM");


    public static String getTimeYYYYMMDD(long time) {
        if (time == 0) {
            return "";
        }
        sdf_YYYYMMDD.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        return sdf_YYYYMMDD.format(new Date(time));
    }

    public static String getTimeYmdHm(long time) {
        if (time == 0) {
            return "";
        }
        sdf_YmdHm.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        return sdf_YmdHm.format(new Date(time));
    }

    public static String getTimeYmdHms(long time) {
        if (time == 0) {
            return "";
        }
        sdf_YmdHms.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        return sdf_YmdHms.format(new Date(time));
    }


    /*两个时间距离多少小时*/
    public static double getDiffHours8(long startTime, long endTime) {
        if (startTime == 0 || endTime == 0) {
            return 0;
        }
        try {
            long diff = endTime - startTime;//这样得到的差值是微秒级别
            long days = diff / (1000 * 60 * 60 * 24);
            long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
            long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
            double minHour = (minutes > 0 & minutes <= 30) ? 0.5 : minutes > 30 ? 1 : 0;
            return days * 24 + hours + minHour;
        } catch (Exception e) {
            return 0;
        }
    }

    public static long getDiffHours1(long endTime, long startTime) {
        long oneHour = 1000 * 60 * 60;
        // 获得两个时间的毫秒时间差异
        long totalMillis = endTime - startTime;
        long hour = totalMillis / oneHour + (totalMillis % oneHour > 0 ? 1 : 0);
        return hour;
    }
    public static int getYear(){
        Calendar cd = Calendar.getInstance();
        return  cd.get(Calendar.YEAR);
    }


    public static long getDiffHours(long startTime, long endTime) {
        long oneHour = 1000 * 60 * 60;
        // 获得两个时间的毫秒时间差异
        long totalMillis = endTime - startTime;
        long hour = totalMillis / oneHour;
        return hour;
    }

    private static long getAmPmTime(Date amPmDate, String amPm) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(amPmDate);
        calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(amPm.substring(0, 2)));
        calendar.set(Calendar.MINUTE, Integer.valueOf(amPm.substring(3, 5)));
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime().getTime();
    }

    public static int getDifferentDays(long startTime, long endTime) {
        int days = (int) ((endTime - startTime) / (1000 * 3600 * 24));
        return days;
    }

    public static double betweenHours(long startTime, long endTime) {
        if (startTime == 0 || endTime == 0) {
            return 0;
        }
        try {
            long diff = endTime - startTime;//这样得到的差值是微秒级别
            long days = diff / (1000 * 60 * 60 * 24);
            long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
            long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
            double minHour = (minutes > 0 & minutes <= 30) ? 0.5 : minutes > 30 ? 1 : 0;
            return days * 24 + hours + minHour;
        } catch (Exception e) {
            return 0;
        }
    }

    public static String betweenHoursAttend(long startTime, long endTime) {
        if (startTime == 0 || endTime == 0) {
            return "";
        }
        try {
            long diff = endTime - startTime;//这样得到的差值是微秒级别

            long days = diff / (1000 * 60 * 60 * 24);
            long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
            long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
            double minHour = (minutes > 0 & minutes <= 30) ? 0.5 : minutes > 30 ? 1 : 0;
            return days * 24 + hours + minHour + "";
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * local时间转换成UTC时间
     *
     * @return
     */
    public static String millonsToUTC(long millons) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        return format.format(new Date(millons));
    }

    public static SimpleDateFormat getFormatYmdHm() {
        return sdf_YmdHm;
    }

    public static SimpleDateFormat getFormatYmdHms() {
        return sdf_YmdHms;
    }

    public static SimpleDateFormat getFormatYmd() {
        return sdf_Ymd;
    }

    public static long getTodayZeroTime() {
        Date date = new Date();
        long l = 24 * 60 * 60 * 1000; //每天的毫秒数
        //date.getTime()是现在的毫秒数，它 减去 当天零点到现在的毫秒数（ 现在的毫秒数%一天总的毫秒数，取余。），理论上等于零点的毫秒数，不过这个毫秒数是UTC+0时区的。
        //减8个小时的毫秒值是为了解决时区的问题。
        return (date.getTime() - (date.getTime() % l) - 8 * 60 * 60 * 1000);
    }

    public static long getTodayZeroTime(Date curDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(curDate);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long date = calendar.getTime().getTime();
        return date;
    }

    public static String getTimeYmd(long time) {
        if (time == 0) {
            return "";
        }
        sdf_Ymd.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        return sdf_Ymd.format(new Date(time));
    }

    public static String getPreviousTime(String dt, final int type) {
        Date date1 = strToDateLong(dt);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date1);//设置起时间
        if (type == 1) {
            cal.add(Calendar.MONTH, -1);
        } else if (type == 2) {
            cal.add(Calendar.DATE, -7);
        } else if (type == 3) {
            cal.add(Calendar.DATE, -1);
        } else if (type == 4) {
            cal.add(Calendar.HOUR, -1);
        }
        Date datexx = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String system_time = df.format(datexx);

        return system_time;
    }


    public static String getNextTime(String dt, final int type) {
        Date date1 = strToDateLong(dt);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date1);//设置起时间
        if (type == 1) {
            cal.add(Calendar.MONTH, 1);
        } else if (type == 2) {
            cal.add(Calendar.DATE, 7);
        } else if (type == 3) {
            cal.add(Calendar.DATE, 1);
        } else if (type == 4) {
            cal.add(Calendar.HOUR, 1);
        }
        Date datexx = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String system_time = df.format(datexx);

        return system_time;
    }

    public static Date strToDateLong(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }


}
