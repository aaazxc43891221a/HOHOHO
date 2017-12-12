package com.first.myapp.com.myapplication.mytest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by chauvard on 12/11/17.
 */

public class DateTimeUtil {
    private static final String TAG = "HPlusDateTimeUtil";

    public static final String LOG_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String PRG_TIME_FORMAT = "MM-dd-yyyy HH:mm:ss";

    /**
     * return current time string use the specified format
     *
     * @param format date/time format
     * @return date/time string
     */
    public static String getNowDateTimeString(String format) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.US);
        return dateFormat.format(calendar.getTime());
    }

    /**
     * return calendar instance with specified format and specified time string
     *
     * @param format     date/time format
     * @param timeString data/time string
     * @return date
     */
    public static Date getDateTimeFromString(String format, String timeString) {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.US);
        try {
            date = dateFormat.parse(timeString);
        } catch (ParseException e) {
            e.printStackTrace();
            LogUtil.log(LogUtil.LogLevel.ERROR, TAG, "date time transfer error");
        }
        return date;
    }

    /**
     * return calendar instance with specified format and specified time string
     *
     * @param format       date/time format
     * @param timeInMillis data/time string
     * @return date string
     */
    public static String getDateTimeFromLong(String format, long timeInMillis) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.US);
        return dateFormat.format(new Date(timeInMillis));
    }

    /**
     * return time string with the specified format
     *
     * @param date   date need to be formatted
     * @param format date/time format
     * @return date/time string
     */
    public static String getDateTimeString(Date date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.US);
        return dateFormat.format(date);
    }
}
