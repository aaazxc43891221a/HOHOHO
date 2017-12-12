package com.first.myapp.com.myapplication.util;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;

import java.io.File;

/**
 * Created by lynnliu on 11/16/15.
 */
public class SystemUtil {

    private static final String SONIM_PHONE = "sonim";

    private Context mContext;
    private String mPackageName;
    private String mVersionName;
    private int mVersionCode = -1;
    private String mIMEI = "";
    private TelephonyManager mTelephonyManager;
    private boolean mIsSonimPhone = false;
    private boolean mIsSonimPhoneChecked = false;

    private static SystemUtil systemUtil;
    private String mPhoneNumber;

    //root
    private final static int kSystemRootStateUnknow = -1;
    private final static int kSystemRootStateDisable = 0;
    private final static int kSystemRootStateEnable = 1;
    private static int systemRootState = kSystemRootStateUnknow;

    public static SystemUtil shareInstance(Context context) {
        if (systemUtil == null) {
            systemUtil = new SystemUtil(context);
        }
        return systemUtil;
    }

    private SystemUtil(Context context) {
        mContext = context;
    }


    public String getPackageName() {
        try {
            if (StringUtil.isEmpty(mPackageName)) {
                mPackageName = mContext.getPackageName();
            }
            return mPackageName;
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return null;
    }

    public String getVersionName() {
        try {
            if (StringUtil.isEmpty(mVersionName)) {
                mVersionName = mContext.getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            }
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return mVersionName;
    }

    public int getVersionCode() {
        try {
            if (mVersionCode == -1) {
                mVersionCode = mContext.getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
            }
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return mVersionCode;
    }

    public String getIMEI() {
        if (StringUtil.isEmpty(mIMEI)) {
            if (mTelephonyManager == null) {
                mTelephonyManager = (TelephonyManager) mContext.getSystemService(Context
                        .TELEPHONY_SERVICE);
            }
            mIMEI = mTelephonyManager.getDeviceId();
        }
        if (StringUtil.isEmpty(mIMEI) || mIMEI.equals("0")) {
            mIMEI = mTelephonyManager.getSubscriberId();
        }
        if (StringUtil.isEmpty(mIMEI) || mIMEI.equals("0")) {
            mIMEI = getLocalMacAddress();
        }
        return mIMEI;
    }

    public String getLocalMacAddress() {
        WifiManager wifi = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }

    public String getPhoneNumber() {
        if (StringUtil.isEmpty(mPhoneNumber)) {
            if (mTelephonyManager == null) {
                mTelephonyManager = (TelephonyManager) mContext.getSystemService(Context
                        .TELEPHONY_SERVICE);
            }
            mPhoneNumber = mTelephonyManager.getLine1Number();
        }
        return mPhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        mPhoneNumber = phoneNumber;
    }

    public String getDeviceManufacturer() {
        return Build.MANUFACTURER;
    }

    public boolean isSonimPhone() {
        if (!mIsSonimPhoneChecked) {
            String manufacturer = getDeviceManufacturer();
            mIsSonimPhone = !(StringUtil.isEmpty(manufacturer) || !manufacturer.toLowerCase().contains(SONIM_PHONE));
            mIsSonimPhoneChecked = true;
        }
        return mIsSonimPhone;
    }

    public static boolean isRootSystem() {
        if (systemRootState == kSystemRootStateEnable) {
            return true;
        } else if (systemRootState == kSystemRootStateDisable) {
            return false;
        }
        File f = null;
        final String kSuSearchPaths[] = {"/system/bin/", "/system/xbin/",
                "/system/sbin/", "/sbin/", "/vendor/bin/"};
        try {
            for (int i = 0; i < kSuSearchPaths.length; i++) {
                f = new File(kSuSearchPaths[i] + "su");
                if (f != null && f.exists()) {
                    systemRootState = kSystemRootStateEnable;
                    return true;
                }
            }
        } catch (Exception e) {
        }
        systemRootState = kSystemRootStateDisable;
        return false;
    }
}
