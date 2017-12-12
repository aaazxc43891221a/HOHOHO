package com.first.myapp.com.myapplication.util;

import android.os.AsyncTask;
import android.os.Build;

/**
 * Created by allanhwmac on 15/10/22.
 */
public class AsyncTaskExecutorUtil {

    public static void executeAsyncTask(AsyncTask asyncTask) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 0);
        } else {
            asyncTask.execute();
        }
    }
}
