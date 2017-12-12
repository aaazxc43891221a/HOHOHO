package com.first.myapp.com.myapplication.configs;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by chauvard on 1/20/17.
 */

public class APPConfig {
    private static APPConfig appConfig;
    private Context mContext;
    private static final String SP_NAME = "appConfig";
    private static final String IF_NEED_REFRESH_MYCONTACT = "ifNeedRefreshMyContact";
    private static final String LAST_REFRESH_MYCONTACT_TIME = "lastRefreshMycontactTime";
    private SharedPreferences sp;

    private APPConfig(Context context) {
        mContext = context;
        sp = mContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    public APPConfig getInstance(Context context) {
        synchronized (this) {
            if (appConfig == null) {
                appConfig = new APPConfig(context);
            }
            return appConfig;
        }
    }

    public void setNeedRefreshMyContact(boolean ifNeedRefreshMyContact){
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(IF_NEED_REFRESH_MYCONTACT,ifNeedRefreshMyContact);
        editor.commit();
    }

    public boolean NeedRefreshMyContact(){
        return sp.getBoolean(IF_NEED_REFRESH_MYCONTACT,true);
    }

    public void setLastRefreshMycontactTime(long lastRefreshMycontactTime){
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(LAST_REFRESH_MYCONTACT_TIME,lastRefreshMycontactTime);
        editor.commit();
    }

    public long getLastRefreshMycontactTime(){
        return sp.getLong(LAST_REFRESH_MYCONTACT_TIME,0);
    }
}
