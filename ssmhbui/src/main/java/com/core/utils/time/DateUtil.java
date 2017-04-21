package com.core.utils.time;

/**
 * <p>
 * Title: 时间和日期的工具类
 * </p>
 * <p>
 * Description: DateUtil类包含了标准的时间和日期格式，以及这些格式在字符串及日期之间转换的方法
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007 advance,Inc. All Rights Reserved
 * </p>
 * <p>
 * Company: advance,Inc.
 * </p>
 *
 * @version 1.0
 */
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {
    // ~ Static fields/initializers
    // =============================================

    private static String datePattern = "MM/dd/yyyy";
    private static String datePattern1 = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_YMD = "yyyy-MM-dd";
    private static String timePattern = datePattern + " HH:MM a";

    /**
     * 获取当前毫秒数
     *
     * @return
     */
    public static String getNowMill() {
        return System.currentTimeMillis()+"";
    }

    // ~ Methods
    // ================================================================
    /**
     * Return default datePattern (MM/dd/yyyy)
     *
     * @return a string representing the date pattern on the UI
     */
    public static String getDatePattern() {
        return datePattern;
    }

    /**
     * 将日期转成格式为‘yyyy-MM-dd’类型的字符串
     *
     * @param date
     * @return
     */
    public static String dateToStrYMD(java.util.Date date) {
        return dateToStr(date, DATE_FORMAT_YMD);
    }

    /**
     * 将日期转化成指定格式的字符串
     *
     * @param date
     * @param format
     * @return
     */
    public static String dateToStr(Date date, String format) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat df = new SimpleDateFormat(format);
        try {
            return df.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * This method attempts to convert an Oracle-formatted date in the form
     * dd-MMM-yyyy to mm/dd/yyyy.
     *
     * @param aDate date from database as a string
     * @return formatted string for the ui
     */
    public static final String getDate(Date aDate) {
        SimpleDateFormat df = null;
        String returnValue = "";

        if (aDate != null) {
            df = new SimpleDateFormat(datePattern);
            returnValue = df.format(aDate);
        }

        return (returnValue);
    }

    /**
     *
     * @author xiaosun
     *
     * @createtime 2016-7-11-上午11:51:07
     *
     * @fileName DateUtil.java
     *
     * @effect 作用(用于实现什么)
     *
     * @remarks 日期专字符串
     *
     * @param aDate
     * @return
     */
    public static final String date2Str(Date aDate) {
        SimpleDateFormat df = null;
        String returnValue = "";

        if (aDate != null) {
            df = new SimpleDateFormat(datePattern);
            returnValue = df.format(aDate);
        }

        return (returnValue);
    }

    public static final String date2StrNew(Date aDate) {
        SimpleDateFormat df = null;
        String returnValue = "";

        if (aDate != null) {
            df = new SimpleDateFormat(datePattern1);
            returnValue = df.format(aDate);
        }

        return (returnValue);
    }

    public static final String date2Str(String pattern, Date aDate) {
        SimpleDateFormat df = null;
        String returnValue = "";

        if (aDate != null) {
            df = new SimpleDateFormat(pattern);
            returnValue = df.format(aDate);
        }
        return (returnValue);
    }

    /**
     * This method generates a string representation of a date/time in the
     * format you specify on input
     *
     * @param aMask the date pattern the string is in
     * @param strDate a string representation of a date
     * @return a converted Date object
     * @see java.text.SimpleDateFormat
     * @throws ParseException
     */
    public static final Date convertStringToDate(String aMask, String strDate) throws ParseException {
        SimpleDateFormat df = null;
        Date date = null;
        df = new SimpleDateFormat(aMask);

        try {
            date = df.parse(strDate);
        } catch (ParseException pe) {
            return null;
        }

        return (date);
    }

    public static final Date str2Date(String aMask, String strDate) throws ParseException {
        SimpleDateFormat df = null;
        Date date = null;
        df = new SimpleDateFormat(aMask);

        try {
            date = df.parse(strDate);
        } catch (ParseException pe) {
            return null;
        }

        return (date);
    }

    /**
     * This method returns the current date time in the format: MM/dd/yyyy HH:MM
     * a
     *
     * @param theTime the current time
     * @return the current date/time
     */
    public static String getTimeNow(Date theTime) {
        return getDateTime(timePattern, theTime);
    }

    /**
     * This method returns the current date in the format: MM/dd/yyyy
     *
     * @return the current date
     * @throws ParseException
     */
    public static Calendar getToday() throws ParseException {
        Date today = new Date();
        SimpleDateFormat df = new SimpleDateFormat(datePattern);

        // This seems like quite a hack (date -> string -> date),
        // but it works ;-)
        String todayAsString = df.format(today);
        Calendar cal = new GregorianCalendar();
        cal.setTime(convertStringToDate(todayAsString));

        return cal;
    }

    /**
     * data-string
     *
     * @param d
     * @param format
     * @return
     */
    public String dateinto(Date d, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat();
        if (format == null || format.equals("")) {
            formatter = new SimpleDateFormat("yyyy-MM-dd");
        } else {
            formatter = new SimpleDateFormat(format);
        }
        String data = formatter.format(d);
        return data;
    }

    /**
     * 获取现在时间
     *
     * @return 返回时间类型
     */
    public static Date getNowDate() {
        Date currentTime = new Date();
        return currentTime;
    }

    /**
     * 获取现在时间
     *
     * @return 返回时间类型 yyyy-MM-dd
     */
    public static Date getNowDate2(String format) {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        String dateString = formatter.format(currentTime);
        ParsePosition pos = new ParsePosition(8);
        Date currentTime_2 = formatter.parse(dateString, pos);
        return currentTime_2;
    }

    /**
     * 获取当前时间
     *
     * @param format
     * @return
     */
    public static Date getNowDateFormat(String format) {
        Date currentTime = new Date();
        if (format == null || format.equals("'")) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        String dateString = formatter.format(currentTime);
        Date d1 = new Date();
        try {
            d1 = formatter.parse(dateString);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return d1;
    }

    ;

  /**
   * 获取现在时间
   * 
     * @return 
   * @return返回短时间格式 yyyy-MM-dd
   */
  public static Date getNowDateShort() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(currentTime);
        ParsePosition pos = new ParsePosition(8);
        Date currentTime_2 = formatter.parse(dateString, pos);
        return currentTime_2;
    }

    /**
     * 当前时间转换成时间返回
     *
     * @param format yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static Date dateformat(String format) {
        Date currentTime = new Date();
        if (format == null || format.equals("'")) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        String dateString = formatter.format(currentTime);
        Date date = null;
        try {
            date = formatter.parse(dateString);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date;
    }

    /**
     * @author xiaosun
     * @text输入格式:yyyy/MM/dd--hh:mm;ss--E返回当前时间
     * @param format 转换格式
     * @return
     */
    public static String date(String format) {
        Date date = new Date();
        if (format == null || format.equals("")) {
            format = "yyyy-MM-dd";
        }
        SimpleDateFormat smp = new SimpleDateFormat(format);
        String data = smp.format(date);
        return data;
    }

    /**
     *
     * @param datetime
     * @param format
     * @return
     */
    public static String stringfromatdate(String datetime, String format) {
        String now = "";
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(datetime);
            now = new SimpleDateFormat(format).format(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return now;
    }

    /**
     * 字符串时间转时间
     *
     * @param datetime 需要转换的时间
     * @param format 转换的格式 --yyyy-MM-dd默认
     * @return
     */
    public static String stringformatstrdate(String datetime, String format) {
        if (datetime == null || datetime.equals("")) {
            return null;
        }
        SimpleDateFormat formatter = new SimpleDateFormat();
        if (format == null || format.equals("")) {
            formatter = new SimpleDateFormat("yyyy-MM-dd");
        } else {
            formatter = new SimpleDateFormat(format);
        }
        Date date = null;
        try {
            date = formatter.parse(datetime);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        DateFormat df = new SimpleDateFormat(format);
        return df.format(date);
    }

    /**
     * 字符串时间转时间
     *
     * @param datetime 需要转换的时间
     * @param format 转换的格式 --yyyy-MM-dd默认
     * @return
     */
    public static Date stringformatdate(String datetime, String format) {
        if (datetime == null || datetime.equals("")) {
            return null;
        }
        SimpleDateFormat formatter = new SimpleDateFormat();
        if (format == null || format.equals("")) {
            formatter = new SimpleDateFormat("yyyy-MM-dd");
        } else {
            formatter = new SimpleDateFormat(format);
        }
        Date date = null;
        try {
            date = formatter.parse(datetime);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date;
    }

    /**
     * This method generates a string representation of a date's date/time in
     * the format you specify on input
     *
     * @param aMask the date pattern the string is in
     * @param aDate a date object
     * @return a formatted string representation of the date
     *
     * @see java.text.SimpleDateFormat
     */
    public static final String getDateTime(String aMask, Date aDate) {
        SimpleDateFormat df = null;
        String returnValue = "";

        if (aDate == null) {
            System.out.print("aDate is null!");
        } else {
            df = new SimpleDateFormat(aMask);
            returnValue = df.format(aDate);
        }

        return (returnValue);
    }

    /**
     * This method generates a string representation of a date based on the
     * System Property 'dateFormat' in the format you specify on input
     *
     * @param aDate A date to convert
     * @return a string representation of the date
     */
    public static final String convertDateToString(Date aDate) {
        return getDateTime(datePattern, aDate);
    }

    /**
     * This method converts a String to a date using the datePattern
     *
     * @param strDate the date to convert (in format MM/dd/yyyy)
     * @return a date object
     *
     * @throws ParseException
     */
    public static Date convertStringToDate(String strDate) throws ParseException {
        Date aDate = null;

        try {

            aDate = convertStringToDate(datePattern, strDate);
        } catch (ParseException pe) {
            // log.error("Could not convert '" + strDate
            // + "' to a date, throwing exception");
            pe.printStackTrace();
            return null;

        }
        return aDate;
    }

    // 日期格式转换成时间戳
    public static long getTimeStamp(String pattern, String strDate) {
        long returnTimeStamp = 0;
        Date aDate = null;
        try {
            aDate = convertStringToDate(pattern, strDate);
        } catch (ParseException pe) {
            aDate = null;
        }
        if (aDate == null) {
            returnTimeStamp = 0;
        } else {
            returnTimeStamp = aDate.getTime();
        }
        return returnTimeStamp;
    }

    // 获取当前日期的邮戳
    public static long getNowTimeStamp() {
        long returnTimeStamp = 0;
        Date aDate = null;
        try {
            aDate = convertStringToDate("yyyy-MM-dd HH:mm:ss", getNowDateTime());
        } catch (ParseException pe) {
            aDate = null;
        }
        if (aDate == null) {
            returnTimeStamp = 0;
        } else {
            returnTimeStamp = aDate.getTime();
        }
        return returnTimeStamp;
    }

    /**
     * 得到格式化后的系统当前日期
     *
     * @param strScheme 格式模式字符串
     * @return 格式化后的系统当前时间，如果有异常产生，返回空串""
     * @see java.util.SimpleDateFormat
     */
    public static final String getNowDateTime(String strScheme) {
        String strReturn = null;
        Date now = new Date();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(strScheme);
            strReturn = sdf.format(now);
        } catch (Exception e) {
            strReturn = "";
        }
        return strReturn;
    }

    public static final String getNowDateTime() {
        String strReturn = null;
        Date now = new Date();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            strReturn = sdf.format(now);
        } catch (Exception e) {
            strReturn = "";
        }
        return strReturn;
    }

    /**
     * 转化日期格式"MM/dd/YY、MM.dd.YY、MM-dd-YY、MM/dd/YY"，并输出为正常的格式yyyy-MM-dd
     *
     * @param strDate 待转换的日期格式
     * @return 格式化后的日期，如果有异常产生，返回空串""
     * @see java.util.SimpleDateFormat
     */
    public static final String convertNormalDate(String strDate) {
        String strReturn = null;
        // 先把传入参数分格符转换成java认识的分格符
        String[] date_arr = strDate.split("\\.|\\/|\\-");
        try {
            if (date_arr.length == 3) {
                if (date_arr[2].length() != 4) {
                    String nowYear = getNowDateTime("yyyy");
                    date_arr[2] = nowYear.substring(0, 2) + date_arr[2];
                }
                strReturn
                        = DateUtil.getDateTime("yyyy-MM-dd",
                                convertStringToDate(combineStringArray(date_arr, "/")));
            }

        } catch (Exception e) {
            return strReturn;
        }
        return strReturn;
    }

    /**
     * 将字符串数组使用指定的分隔符合并成一个字符串。
     *
     * @param array 字符串数组
     * @param delim 分隔符，为null的时候使用""作为分隔符（即没有分隔符）
     * @return 合并后的字符串
     * @since 0.4
     */
    public static final String combineStringArray(String[] array, String delim) {
        int length = array.length - 1;
        if (delim == null) {
            delim = "";
        }
        StringBuffer result = new StringBuffer(length * 8);
        for (int i = 0; i < length; i++) {
            result.append(array[i]);
            result.append(delim);
        }
        result.append(array[length]);
        return result.toString();
    }

    public static final int getWeekNum(String strWeek) {
        int returnValue = 0;
        if (strWeek.equals("Mon")) {
            returnValue = 1;
        } else if (strWeek.equals("Tue")) {
            returnValue = 2;
        } else if (strWeek.equals("Wed")) {
            returnValue = 3;
        } else if (strWeek.equals("Thu")) {
            returnValue = 4;
        } else if (strWeek.equals("Fri")) {
            returnValue = 5;
        } else if (strWeek.equals("Sat")) {
            returnValue = 6;
        } else if (strWeek.equals("Sun")) {
            returnValue = 0;
        } else if (strWeek == null) {
            returnValue = 0;
        }

        return returnValue;
    }

    /**
     * 得到格式化后的指定日期
     *
     * @param strScheme 格式模式字符串
     * @param date 指定的日期值
     * @return 格式化后的指定日期，如果有异常产生，返回空串""
     * @see java.util.SimpleDateFormat
     */
    public static final String getDateTime(Date date, String strScheme) {
        String strReturn = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(strScheme);
            strReturn = sdf.format(date);
        } catch (Exception e) {
            strReturn = "";
        }

        return strReturn;
    }

    /**
     * 获取日期
     *
     * @param timeType 时间类型，譬如：Calendar.DAY_OF_YEAR
     * @param timenum 时间数字，譬如：-1 昨天，0 今天，1 明天
     * @return 日期
     */
    public static final Date getDateFromNow(int timeType, int timenum) {
        Calendar cld = Calendar.getInstance();
        cld.set(timeType, cld.get(timeType) + timenum);
        return cld.getTime();
    }

    /**
     * 获取日期
     *
     * @param timeType 时间类型，譬如：Calendar.DAY_OF_YEAR
     * @param timenum 时间数字，譬如：-1 昨天，0 今天，1 明天
     * @param format_string 时间格式，譬如："yyyy-MM-dd HH:mm:ss"
     * @return 字符串
     */
    public static final String getDateFromNow(int timeType, int timenum, String format_string) {
        if ((format_string == null) || (format_string.equals(""))) {
            format_string = "yyyy-MM-dd HH:mm:ss";
        }
        Calendar cld = Calendar.getInstance();
        Date date = null;
        DateFormat df = new SimpleDateFormat(format_string);
        cld.set(timeType, cld.get(timeType) + timenum);
        date = cld.getTime();
        return df.format(date);
    }

    /**
     * 获取当前日期的字符串
     *
     * @param format_string 时间格式，譬如："yyyy-MM-dd HH:mm:ss"
     * @return 字符串
     */
    public static final String getDateNow(String format_string) {
        if ((format_string == null) || (format_string.equals(""))) {
            format_string = "yyyy-MM-dd HH:mm:ss";
        }
        Calendar cld = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat(format_string);
        return df.format(cld.getTime());
    }

    /**
     * 获取某个日期的星期几
     *
     * @param date
     * @return
     */
    public static final Integer getDateWeek(String date) {
        int dayForWeek = 0;;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();
            c.setTime(format.parse(date));
            if (c.get(Calendar.DAY_OF_WEEK) == 1) {
                dayForWeek = 7;
            } else {
                dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return dayForWeek;
    }

    ;

  /**
   * 格式化时间
   * 
   * @param dates
   * @param format
   */
  public static String getformatTime(Date dates, String format) {
        if (dates == null) {
            dates = new Date();
        }
        if (format == null || format.equals("") || format == "") {
            format = "yyyy-MM-dd";
        }
        DateFormat df = new SimpleDateFormat(format);
        return df.format(dates);
    }

    /**
     *
     * @author xiaosun
     *
     * @createtime 2016-7-11-下午02:34:55
     *
     * @fileName DateUtil.java
     *
     * @effect 作用(用于实现什么)
     *
     * @remarks 获取当前时间
     *
     * @param format
     * @return
     */
    public static Date nowDateFormat(String format) {
        Date datetime = null;
        try {
            String gdates = DateUtil.date(format);
            System.out.println("01" + gdates);
            datetime = DateUtil.stringformatdate(gdates, format);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return datetime;
    }

    /**
     * 计算连个日期之间的周数差
     *
     * @param starttime
     * @param endtime
     * @return
     */
    public static int weekByStarttimeAndEndtime(Date starttime, Date endtime) {
        int allWeeks = 0;
        long CONST_WEEK = 3600 * 1000 * 24 * 7;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar before = Calendar.getInstance();
        Calendar after = Calendar.getInstance();
        try {
            before.setTime(sdf.parse(starttime.toString()));
            after.setTime(sdf.parse(endtime.toString()));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("时间计算周期出错了");
        }

        int week = before.get(Calendar.DAY_OF_WEEK);
        before.add(Calendar.DATE, -week);
        week = after.get(Calendar.DAY_OF_WEEK);
        after.add(Calendar.DATE, 7 - week);
        int interval = (int) ((after.getTimeInMillis() - before.getTimeInMillis()) / CONST_WEEK);
        interval = interval - 1;
        if (interval == 0) {
            interval = 1;
        }
        allWeeks = interval;
        return allWeeks + 1;
    }

    /**
     * 两个时间段之间第几周星期几的日期
     *
     * @param year
     * @param month
     * @param weekOfMonth
     * @param dayOfWeek
     * @return
     */
    public static String weekdatetodata(int year, int month, int weekOfMonth, int dayOfWeek) {
        Calendar c = Calendar.getInstance();
        // 计算出 x年 y月 1号 是星期几
        c.set(year, month - 1, 1);
        // 如果i_week_day =1 的话 实际上是周日
        int i_week_day = c.get(Calendar.DAY_OF_WEEK);
        int sumDay = 0;
        // dayOfWeek+1 就是星期几（星期日 为 1）
        if (i_week_day == 1) {
            sumDay = (weekOfMonth - 1) * 7 + dayOfWeek + 1;
        } else {
            sumDay = 7 - i_week_day + 1 + (weekOfMonth - 1) * 7 + dayOfWeek + 1;
        }
        // 在1号的基础上加上相应的天数
        c.set(Calendar.DATE, sumDay);
        SimpleDateFormat sf2 = new SimpleDateFormat("yyyy-MM-dd");
        return sf2.format(c.getTime());
    }

    /**
     * 距离1970年1月1日0点0分0秒的毫秒数
     *
     * @return
     */
    public static Long getDateCurrentTimeMillis() {
        Date dt = new Date();
        Long time = dt.getTime();//这就是距离1970年1月1日0点0分0秒的毫秒数
        return time;
    }

    /**
     * 距离1970年1月1日0点0分0秒的毫秒数
     *
     * @return
     */
    public static Long getStrCurrentTimeMllis() {
        Long currenttime = System.currentTimeMillis();
        return currenttime;
    }

    public static void main(String[] args) {
        // String gdates = DateUtil.date("yyyy-MM-dd");
        // // System.out.println(DateUtil.getDateWeek(gdates));
        // System.err.println(DateUtil.getformatTime(null, "yyyy"));
        // Date nowdate = DateUtil.getNowDate2("yyyy-MM-dd");
        // System.out.println(DateUtil.nowDateFormat("yyyy-MM-dd"));
        // System.out.println(gdates + "?" + nowdate + "?" + DateUtil.getNowDateFormat("yyyy-MM-dd"));
        // 2009年1月 第二周星期三 得到2009-01-14
//        System.out.println(weekdatetodata(2009, 1, 2, 3));
//        // 2009年2月 第二周星期三 得到2009-02-11
//        System.out.println(weekdatetodata(2009, 2, 2, 3));
//        // 2009年4月 第二周星期三 得到2009-04-15
//        System.out.println(weekdatetodata(2009, 4, 2, 3));
//        // 2009年2月 第三周星期四 得到2009-02-19
//        System.out.println(weekdatetodata(2009, 2, 3, 4));
//        System.out.println(getNowDate());
        System.out.println(DateUtil.getDateCurrentTimeMillis());
    }
}
