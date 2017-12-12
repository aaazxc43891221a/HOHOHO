package com.first.myapp.com.myapplication.util;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;

import java.util.List;

/**
 * Created by lynnliu on 11/22/15.
 */
public class ProcessUtil {

    /**
     * 判断进程是否运行
     *
     * @return
     */
    public static boolean isProcessRunning(Context context, String proessName) {

        boolean isRunning = false;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        List<RunningAppProcessInfo> lists = am.getRunningAppProcesses();
        for (RunningAppProcessInfo info : lists) {
            if (info.processName.equals(proessName)) {
                isRunning = true;
            }
        }

        return isRunning;
    }

}
