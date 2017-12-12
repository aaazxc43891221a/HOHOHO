package com.first.myapp.com.myapplication.util;



import java.util.HashMap;
import java.util.Map;

/**
 * Created by chauvard on 10/17/16.
 */

public class ButtonUtils {
    private static long lastClickTime = 0;
    private static long DIFF = 400;
    private static int lastButtonId = -1;

    public static boolean isFastDoubleClick(String name) {
        return isFastDoubleClick(-1, DIFF, name);
    }

    public static boolean isFastDoubleClick(int buttonId, String name) {

        return isFastDoubleClick(buttonId, DIFF, name);
    }

    public static boolean isFastDoubleClick(int buttonId, long diff, String name) {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (lastButtonId == buttonId && lastClickTime > 0 && timeD < diff) {
            return true;
        }

        Map<String, String> ubsinfo = new HashMap<>();
        String ubsKey = "Click";
        String ubsVal = name;
        ubsinfo.put(ubsKey, ubsVal);
//        UBSManager.tagEventWithAttributes("Button", ubsinfo);

        lastClickTime = time;
        lastButtonId = buttonId;
        return false;
    }

}
