package me.yintaibing.universaldrawable.util;

import android.graphics.Bitmap;
import android.graphics.Color;

import me.yintaibing.universaldrawable.UniversalDrawable;

/**
 * 通用背景的属性集
 * <p>
 * Created by yintaibing on 2018/2/8.
 */

public class Attributes {
    public int shape = UniversalDrawable.SHAPE_RECT;
    public int fillMode = UniversalDrawable.FILL_MODE_COLOR;
    public int scaleType = UniversalDrawable.SCALE_TYPE_CENTER;

    public int radius;
    public int[] radiusArray;

    public int strokeWidth;
    public int[] strokeDash;
    public int colorStroke = Color.TRANSPARENT;

    public int colorFill = Color.TRANSPARENT;

    public int colorGradientStart = Color.TRANSPARENT;
    public int colorGradientEnd = Color.TRANSPARENT;
    public int linearGradientOrientation = UniversalDrawable.LINEAR_GRADIENT_LR;

    public Bitmap bitmap;
    public int targetDensity;

    public float alphaFill = 1f;
    public float alphaStroke = 1f;

    public boolean hasDifferentRadius() {
        if (radiusArray != null && radiusArray.length >= 4) {
            return Utils.hasPositiveValue(radiusArray);
        }
        return false;
    }

    public boolean hasStroke() {
        return Utils.hasStroke(strokeWidth, colorStroke, alphaStroke);
    }

    public boolean hasDashStroke() {
        return strokeDash != null && strokeDash[0] > 0f && strokeDash[1] > 0f;
    }

    public boolean hasBitmap() {
        return (fillMode & UniversalDrawable.FILL_MODE_BITMAP) != 0 && bitmap != null;
    }

    public void destroy() {
        bitmap = null;
    }

    private boolean isTransparent(int color) {
        return color == Color.TRANSPARENT;
    }
}
