package me.yintaibing.universaldrawable.util;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;

/**
 * 工具类
 * <p>
 * Created by yintaibing on 2018/2/9.
 */

public class Utils {
    public static int dp2px(Resources r, float dp) {
        return (int) (r.getDisplayMetrics().density * dp + 0.5f);
    }

    public static boolean hasPositiveValue(int... values) {
        for (int v : values) {
            if (v > 0) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasStroke(int strokeWidth, int strokeColor) {
        return strokeWidth > 0 && strokeColor != Color.TRANSPARENT;
    }

    public static void asBackground(Drawable drawable, View view) {
        if (view != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                view.setBackground(drawable);
            } else {
                view.setBackgroundDrawable(drawable);
            }
        }
    }

    public static void asForeground(Drawable drawable, View view) {
        if (view != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            view.setForeground(drawable);
        }
    }

    public static void asImageDrawable(Drawable drawable, ImageView imageView) {
        if (imageView != null) {
            imageView.setImageDrawable(drawable);
        }
    }
}
