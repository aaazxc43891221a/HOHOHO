package com.first.myapp.com.myapplication.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by chauvard on 6/26/17.
 */

public class PictureDecodeUtils {
    public static Bitmap decodePictureForWholeScreen(Context context, int resID) {
        return decodePicture(context, resID, 1f);
    }

    public static Bitmap decodePicture(Context context, int resID, float rate) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display defaultDisplay = wm.getDefaultDisplay();
        return decodeSampledBitmapFromResource(context.getResources(), resID,
                (int) (defaultDisplay.getWidth() * rate), (int) (defaultDisplay.getHeight() * rate));
    }

    public static void decodePictureForWholeScreen(Context context, View v, int resID) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display defaultDisplay = wm.getDefaultDisplay();
        int width = defaultDisplay.getWidth();
        int height = defaultDisplay.getHeight();
        setBackGround(context, v, resID, width, height, 1);
    }

    public static void setBackGround(Context context, View v, int resID, int width, int height, float rate) {
        try {
            v.setBackground(new BitmapDrawable(decodeSampledBitmapFromResource(context.getResources(), resID, (int) (width * rate), (int) (height * rate))));
        } catch (OutOfMemoryError e) {
            setBackGround(context, v, resID, width, height, rate / 2);
        }
    }

    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {
        final int originalHeight = options.outHeight;
        final int originalWidth = options.outWidth;

        int inSampleSize = 1;
        if (originalHeight > reqHeight || originalWidth > reqWidth) {
            final int heightRatio = Math.round((float) originalHeight / (float) reqHeight);
            final int widthRatio = Math.round((float) originalWidth / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }
}
