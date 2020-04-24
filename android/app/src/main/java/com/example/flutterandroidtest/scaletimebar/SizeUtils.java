package com.example.flutterandroidtest.scaletimebar;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by ly on 2020/4/20.
 * Describe:
 */
public class SizeUtils {

    public static int dip2px(float dipValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int[] getScreenResolution(Context context) {
        int[] screenResolution = new int[2];
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        android.view.Display display = wm.getDefaultDisplay();
        display.getMetrics(dm);
        screenResolution[0] = dm.widthPixels;
        screenResolution[1] = dm.heightPixels;
        return screenResolution;
    }
}
