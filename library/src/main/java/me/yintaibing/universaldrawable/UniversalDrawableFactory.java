package me.yintaibing.universaldrawable;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import me.yintaibing.universaldrawable.util.Utils;

/**
 * 万能Drawable工厂
 * <p>
 * Created by yintaibing on 2018/2/9.
 */

public class UniversalDrawableFactory {
    public static void fromXml(View view, Context context, AttributeSet attrSet) {
        if (attrSet == null) {
            return;
        }

        TypedArray ta = context.obtainStyledAttributes(attrSet, R.styleable.UniversalDrawable);
        int stateMode = ta.getInt(R.styleable.UniversalDrawable_bg_stateMode,
                UniversalDrawableSet.STATE_MODE_STATELESS);
        IUniversalDrawable drawable;
        switch (stateMode) {
            case UniversalDrawableSet.STATE_MODE_STATELESS:
            default:
                drawable = createStateless();
                break;
            case UniversalDrawableSet.STATE_MODE_CLICKABLE:
                drawable = createClickable();
                break;
            case UniversalDrawableSet.STATE_MODE_CHECKABLE:
                drawable = createCheckable();
                break;
        }

        // public
        int shape = ta.getInt(R.styleable.UniversalDrawable_bg_shape,
                UniversalDrawable.SHAPE_RECT);
        int fillMode = ta.getInt(R.styleable.UniversalDrawable_bg_fillMode,
                UniversalDrawable.FILL_MODE_COLOR);
        int scaleType = ta.getInt(R.styleable.UniversalDrawable_bg_scaleType,
                UniversalDrawable.SCALE_TYPE_CENTER);
        float alphaFill = ta.getFloat(R.styleable.UniversalDrawable_bg_alphaFill, 1f);
        drawable.shape(shape)
                .fillMode(fillMode)
                .scaleType(scaleType)
                .alphaFill(alphaFill);

        // radius
        int r0 = ta.getDimensionPixelSize(R.styleable.UniversalDrawable_bg_radiusLeftTop, 0);
        int r1 = ta.getDimensionPixelSize(R.styleable.UniversalDrawable_bg_radiusRightTop, 0);
        int r2 = ta.getDimensionPixelSize(R.styleable.UniversalDrawable_bg_radiusRightBottom, 0);
        int r3 = ta.getDimensionPixelSize(R.styleable.UniversalDrawable_bg_radiusLeftBottom, 0);
        if (Utils.hasPositiveValue(r0, r1, r2, r3)) {
            drawable.radius(r0, r1, r2, r3);
        } else {
            int radius = ta.getDimensionPixelSize(R.styleable.UniversalDrawable_bg_radius, 0);
            drawable.radius(radius);
        }

        // stroke
        int strokeWidth = ta.getDimensionPixelSize(R.styleable.UniversalDrawable_bg_strokeWidth, 0);
        int colorStroke = ta.getColor(R.styleable.UniversalDrawable_bg_colorStroke,
                Color.TRANSPARENT);
        float alphaStroke = ta.getFloat(R.styleable.UniversalDrawable_bg_alphaStroke, 1f);
        if (Utils.hasStroke(strokeWidth, colorStroke, alphaStroke)) {
            drawable.strokeWidth(strokeWidth)
                    .colorStroke(colorStroke)
                    .alphaStroke(alphaStroke);
            int dashSolid = ta.getDimensionPixelSize(R.styleable.UniversalDrawable_bg_strokeDashSolid, 0);
            int dashSpace = ta.getDimensionPixelSize(R.styleable.UniversalDrawable_bg_strokeDashSpace, 0);
            if (dashSolid > 0 && dashSpace > 0) {
                drawable.strokeDash(dashSolid, dashSpace);
            }
        }

        // fill color
        if (stateMode == UniversalDrawableSet.STATE_MODE_CLICKABLE) {
            int colorDisabled = ta.getColor(R.styleable.UniversalDrawable_bg_colorDisabled,
                    Color.TRANSPARENT);
            int colorNormal = ta.getColor(R.styleable.UniversalDrawable_bg_colorNormal,
                    Color.TRANSPARENT);
            int colorPressed = ta.getColor(R.styleable.UniversalDrawable_bg_colorPressed,
                    Color.TRANSPARENT);
            UniversalDrawableSet set = (UniversalDrawableSet) drawable;
            set.theDisabled().colorFill(colorDisabled);
            set.theNormal().colorFill(colorNormal);
            set.thePressed().colorFill(colorPressed);
        } else if (stateMode == UniversalDrawableSet.STATE_MODE_CHECKABLE) {
            int colorDisabled = ta.getColor(R.styleable.UniversalDrawable_bg_colorDisabled,
                    Color.TRANSPARENT);
            int colorUnchecked = ta.getColor(R.styleable.UniversalDrawable_bg_colorUnchecked,
                    Color.TRANSPARENT);
            int colorChecked = ta.getColor(R.styleable.UniversalDrawable_bg_colorChecked,
                    Color.TRANSPARENT);
            UniversalDrawableSet set = (UniversalDrawableSet) drawable;
            set.theDisabled().colorFill(colorDisabled);
            set.theUnchecked().colorFill(colorUnchecked);
            set.theChecked().colorFill(colorChecked);
        } else {
            int colorNormal = ta.getColor(R.styleable.UniversalDrawable_bg_colorNormal,
                    Color.TRANSPARENT);
            drawable.colorFill(colorNormal);
        }

        // gradient
        if ((fillMode & UniversalDrawable.FILL_MODE_LINEAR_GRADIENT) != 0) {
            int colorGradientStart = ta.getColor(R.styleable.UniversalDrawable_bg_colorGradientStart,
                    Color.TRANSPARENT);
            int colorGradientEnd = ta.getColor(R.styleable.UniversalDrawable_bg_colorGradientEnd,
                    Color.TRANSPARENT);
            int linearGradientOrientation = ta.getInt(
                    R.styleable.UniversalDrawable_bg_linearGradientOrientation,
                    UniversalDrawable.LINEAR_GRADIENT_LR);
            drawable.colorGradient(colorGradientStart, colorGradientEnd)
                    .linearGradientOrientation(linearGradientOrientation);
        }

        // bitmap and density
        Resources res = context.getResources();
        Bitmap bitmap = BitmapFactory.decodeResource(res,
                ta.getResourceId(R.styleable.UniversalDrawable_bg_bitmap, 0));
        drawable.bitmap(res, bitmap);

        boolean asImageDrawable = ta.getBoolean(R.styleable.UniversalDrawable_bg_asImageDrawable,
                false);
        boolean asForeground = ta.getBoolean(R.styleable.UniversalDrawable_bg_asForeground,
                false);
        ta.recycle();

        if (asImageDrawable && view instanceof ImageView) {
            drawable.asImageDrawable((ImageView) view);
        } else if (asForeground) {
            drawable.asForeground(view);
        } else {
            drawable.asBackground(view);
        }
    }

    public static UniversalDrawable createStateless() {
        return new UniversalDrawable();
    }

    public static UniversalDrawableSet createClickable() {
        return new UniversalDrawableSet(UniversalDrawableSet.STATE_MODE_CLICKABLE);
    }

    public static UniversalDrawableSet createCheckable() {
        return new UniversalDrawableSet(UniversalDrawableSet.STATE_MODE_CHECKABLE);
    }
}
