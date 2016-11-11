package com.imaginet.ventures.step.utility.constant;

import android.content.Context;
import android.content.res.Resources;
import android.widget.Toast;

/**
 * Created by IM028 on 9/6/16.
 */
public class ConstantFunction {
    public static int getBottomHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    public static void toast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}
