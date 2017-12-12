package com.first.myapp.com.myapplication.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * The util for date/time.
 * Created by liunan on 1/14/15.
 */
public class DateTimeUtil {
    private static final String TAG = "HPlusDateTimeUtil";

    public static final String DISPLAY_TIME_FORMAT = "MMM dd, yyyy HH:mm";

    public static final String DISPLAY_TIME_WITH_SECOND_FORMAT = "MMM dd, yyyy HH:mm:ss";

    public static final String LOG_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String PRG_TIME_FORMAT = "MM-dd-yyyy HH:mm:ss";

    public static final String HEAD_DETAIL_FORMAT = "MMM-dd-yyyy HH:mm:ss";

    public static final String EVENT_SUMMARY_FORMAT = "MMM/dd/yyyy HH:mm:ss";

    public static final String EVENT_DATA_DETAIL_FORMAT = "MMM dd HH:mm:ss";

    public static final String EVENT_DETAIL_FORMAT = "MMM-dd HH:mm:ss";

    public static final String EVENT_LIST_FORMAT = "MMM-dd-yyyy HH:mm";

    public static final String DATA_LOG_FILE_NAME_FORMAT = "MMM-dd-yyyy HH:mm:ss";

    public static final String FILE_NAME_FORMAT = "MMM-dd-yyyy HH:mm";

    public static final String HOUR_FORMAT = "HH:mm:ss";

    public static final String DAY_FORMAT = "MMM dd";

    public static final String YEAR_MONTH_DAY_FORMAT = "yyyy/MM/dd";

    public static final String MONTH_DAY_YEAR_FORMAT = "MM/dd/yyyy";

    public static final String MONTH_DAY_YEAR_DISPLAY_FORMAT = "MMM dd, yyyy";

    public static final String REPORT_TIME_FORMAT = "MM/dd/yyyy HH:mm:ss";

    public static final String REPORT_DETAIL_TIME_FORMAT = "MMM dd, yyyy HH:mm:ss";


    private static String[] monthes = new String[]{
            "January", "February", "March", "April", "May", "June", "July",
            "August", "September", "October", "November", "December"};

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
     * return calendar_active_unselected instance with specified format and specified time string
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
//            e.printStackTrace();
            LogUtil.log(LogUtil.LogLevel.ERROR, TAG, "date time transfer error");
        }
        return date;
    }

    /**
     * return calendar_active_unselected instance with specified format and specified time string
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


    public static String getDateTimeFromDate(String format, Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.US);
        return dateFormat.format(date);
    }

    public static int getMonthDays(int year, int month) {
        int realYear = year + 1900;
        month++;
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            case 2:
                if (((realYear % 4 == 0) && (realYear % 100 != 0)) || (realYear % 400 == 0)) {
                    return 29;
                } else {
                    return 28;
                }
            default:
                return -1;
        }
    }

    public static int getFirstDayWeek(int year, int month) {
        int realYear = year + 1900;
        Calendar calendar = Calendar.getInstance();
        calendar.set(realYear, month, 1);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    public static String getMonthString(int month) {
        return monthes[month];
    }


    public static Date getDateFromByte(byte[] time) {
        int year = time[0];
        int month = time[1];
        int dayOfMonth = time[2];
        int hours = time[3];
        int minutes = time[4];
        int seconds = time[5];

        return new Date(year + 100, month - 1, dayOfMonth, hours, minutes, seconds);
    }

    public static Date parseLong2Date(long longTime) {
        if (longTime == 0) {
            return null;
        }
        int seconds = (int) ((longTime & 0x000000000000003F));
        int minutes = (int) ((longTime & 0x0000000000000FC0) >>> 6);
        int hours = (int) ((longTime & 0x000000000001F000) >>> 12);
        int dayOfMonth = (int) ((longTime & 0x00000000003E0000) >>> 17);
        int month = (int) ((longTime & 0x0000000003C00000) >>> 22);
        int year = (int) ((longTime & 0x00000000FC000000) >>> 26);
        Date date = new Date(year + 100, month - 1, dayOfMonth, hours, minutes, seconds);
        return date;
    }
}
