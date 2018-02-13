package me.yintaibing.universaldrawable;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.view.View;
import android.widget.ImageView;

import me.yintaibing.universaldrawable.util.Clipper;

/**
 * 通用Drawable接口
 * <p>
 * Created by yintaibing on 2018/2/8.
 */

public interface IUniversalDrawable {
    IUniversalDrawable shape(int shape);

    IUniversalDrawable fillMode(int fillMode);

    IUniversalDrawable scaleType(int scaleType);

    IUniversalDrawable radius(int radius);

    IUniversalDrawable radius(Context context, float radiusDp);

    IUniversalDrawable radius(int leftTop, int rightTop, int rightBottom, int leftBottom);

    IUniversalDrawable radius(Context context, float leftTopDp, float rightTopDp, float rightBottomDp,
                              float leftBottomDp);

    IUniversalDrawable strokeWidth(int strokeWidth);

    IUniversalDrawable strokeWidth(Context context, float strokeWidthDp);

    IUniversalDrawable strokeDash(int solidLength, int spaceLength);

    IUniversalDrawable strokeDash(Context context, float solidLengthDp, float spaceLengthDp);

    IUniversalDrawable colorStroke(@ColorInt int color);

    IUniversalDrawable colorStroke(Context context, @ColorRes int colorRes);

    IUniversalDrawable colorFill(@ColorInt int color);

    IUniversalDrawable colorFill(Context context, @ColorRes int colorRes);

    IUniversalDrawable colorGradient(@ColorInt int startColor, @ColorInt int endColor);

    IUniversalDrawable colorGradient(Context context, @ColorRes int startColorRes,
                                     @ColorRes int endColorRes);

    IUniversalDrawable linearGradientOrientation(int orientation);

    IUniversalDrawable bitmap(Resources res, Bitmap bitmap);

    IUniversalDrawable alphaFill(float alpha);

    IUniversalDrawable alphaStroke(float alpha);

    IUniversalDrawable clip(Clipper clipper);

    void asBackground(View view);

    void asForeground(View view);

    void asImageDrawable(ImageView imageView);
}
