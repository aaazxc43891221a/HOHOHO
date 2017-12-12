package com.first.myapp.com.myapplication.mytest;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by chauvard on 12/11/17.
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
//        if (isLogSaved && (BuildConfig.DEBUG || isDebugMode())) {
        saveLogToFile(level, tag, message);
//        }
    }

    private static void saveLogToFile(LogLevel level, String tag, String message) {
//        if (TextUtils.isEmpty(mLogFileName)) {
        mLogFileName = DateTimeUtil.getNowDateTimeString("yyyy-MM-dd-HH") + ".log";
//        }
        try {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                String path = SD_PATH + "/mp_log/LogUtil/";
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
            e.printStackTrace();
        }
    }

    private static boolean isDebugMode() {
        String fileName = SD_PATH + "/sc_log/LogUtil/sc_debug";
        File file = new File(fileName);
        return file.exists();
    }
}

