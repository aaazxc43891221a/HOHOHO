package com.first.myapp.com.myapplication;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import com.first.myapp.com.myapplication.activity.MainActivity;


/**
 * Created by chauvard on 10/10/16.
 */
public class SmsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        switch (intent.getAction()) {
            case "android.intent.action.NEW_OUTGOING_CALL":
                String phone = getResultData();//得到外拔电话
                if (phone.equals("991722")) {
                    setResultData(null);
//                    setResultData(null);
//                    abortBroadcast();
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
//                                Thread.sleep(3000);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                            AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
//                            mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
//                            setResultData(null);
//                            mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
//                        }
//                    }).start();

//                    TelephonyManager tManager = (TelephonyManager)
//                            context.getSystemService(Context.TELEPHONY_SERVICE);
////初始化iTelephony
//                    Class<TelephonyManager> c = TelephonyManager.class;
//                    Method getITelephonyMethod = null;
//                    try {
//                        getITelephonyMethod = c.getDeclaredMethod("getITelephony", (Class[]) null);
//                        getITelephonyMethod.setAccessible(true);
//                        ITelephony iTelephony = (ITelephony) getITelephonyMethod.invoke(tManager, (Object[]) null);
//                        iTelephony.endCall();//挂断电话
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }


                    ComponentName componentName = new ComponentName(context, MainActivity.class);
                    PackageManager manager = context.getPackageManager();
                    manager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
                    Intent intentToMainAcivity = new Intent();
//                    intentToMainAcivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intentToMainAcivity.setClass(context, MainActivity.class);
                    intentToMainAcivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intentToMainAcivity);
                }
                break;
            default:
                break;
        }
    }
}
