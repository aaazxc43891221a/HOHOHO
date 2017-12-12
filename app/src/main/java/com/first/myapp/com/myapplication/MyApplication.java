package com.first.myapp.com.myapplication;

import android.app.Application;

import com.first.myapp.com.myapplication.database.MyContactDBService;
import com.first.myapp.com.myapplication.mytest.CrashHandler;

/**
 * Created by chauvard on 10/13/16.
 */

public class MyApplication extends Application {


    protected static MyApplication MyApplication = null;

    public static synchronized MyApplication getInstance() {
        if (MyApplication == null) {
            throw new RuntimeException("Application hasn't been initialed!");
        } else {
            return MyApplication;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MyApplication = this;
        initInstance();
        if (BuildConfig.DEBUG) {
            // save crash info to sd card
            CrashHandler crashHandler = CrashHandler.getInstance();
            crashHandler.init(getApplicationContext());
        }
    }

    private void initInstance() {

    }


}
