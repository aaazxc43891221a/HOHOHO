package com.first.myapp.com.myapplication.util;

import android.os.Environment;
import android.support.v7.appcompat.BuildConfig;
import android.text.TextUtils;
import android.util.Log;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * The util for log. Set whether print log, save log to file and the file log level.
 * Created by liunan on 1/14/15.
 */
public class LogUtil {
    private static final String SD_PATH = Environment.getExternalStorageDirectory().getPath();

    public enum LogLevel {

        VERBOSE,
        DEBUG,
        INFO,
        WARN,
        ERROR

    }

    private static boolean isLogEnabled = true;
    private static boolean isLogSaved = true;
    private static String mLogFileName = "";
    private static LogLevel mLogFileLevel = LogLevel.ERROR;

    public static boolean isLogEnabled() {
        return isLogEnabled;
    }

    public static void setIsLogEnabled(boolean isLogEnabled) {
        LogUtil.isLogEnabled = isLogEnabled;
    }

    public static boolean isLogSaved() {
        return isLogSaved;
    }

    public static void setIsLogSaved(boolean isLogSaved) {
        LogUtil.isLogSaved = isLogSaved;
    }

    public static String getLogFileName() {
        return mLogFileName;
    }

    public static void setLogFileName(String logFileName) {
        LogUtil.mLogFileName = logFileName;
    }

    public static LogLevel getLogFileLevel() {
        return mLogFileLevel;
    }

    public static void setLogFileLevel(LogLevel logFileLevel) {
        LogUtil.mLogFileLevel = logFileLevel;
    }

    public static void log(LogLevel level, String tag, String message) {
        if (isLogEnabled) {
            switch (level) {
                case VERBOSE:
                    Log.v(tag, message);
                    break;
                case DEBUG:
                    Log.d(tag, message);
                    break;
                case INFO:
                    Log.i(tag, message);
                    break;
                case WARN:
                    Log.w(tag, message);
                    break;
                case ERROR:
                    Log.e(tag, message);
                    break;
                default:
                    Log.d(tag, message);
                    break;
            }
        }
        if (isLogSaved && BuildConfig.DEBUG) {
            saveLogToFile(level, tag, message);
        }
    }

    private static void saveLogToFile(LogLevel level, String tag, String message) {
        if (TextUtils.isEmpty(mLogFileName)) {
            mLogFileName = DateTimeUtil.getNowDateTimeString("yyyy-MM-dd") + ".log";
        }
        try {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                String path = SD_PATH + "/DeviceConfigurator/LogUtil/";
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(new File(path + mLogFileName), true);
                fos.write((DateTimeUtil.getNowDateTimeString(DateTimeUtil.LOG_TIME_FORMAT) + "_" +
                        level.name() + "_" + tag + "_" + message + "\r\n").getBytes());
                fos.close();
            }
        } catch (IOException e) {
//            e.printStackTrace();
        }
    }
}
