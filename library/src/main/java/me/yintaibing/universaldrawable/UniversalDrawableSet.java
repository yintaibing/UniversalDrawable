package me.yintaibing.universaldrawable;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.StateListDrawable;
import android.view.View;
import android.widget.ImageView;

import me.yintaibing.universaldrawable.util.Clipper;
import me.yintaibing.universaldrawable.util.Utils;

/**
 * 多状态的万能Drawable
 * <p>
 * Created by yintaibing on 2018/2/9.
 */

public class UniversalDrawableSet extends StateListDrawable implements IUniversalDrawable {
    public static final int STATE_MODE_STATELESS = 0;       // 无状态
    public static final int STATE_MODE_CLICKABLE = 1;       // 点击模式（类似Button）
    public static final int STATE_MODE_CHECKABLE = 2;       // 选择模式（类似CheckBox）

    private static final int STATE_COUNT_CLICK = 3;
    private static final int CLICK_STATE_DISABLED = 0;
    private static final int CLICK_STATE_NORMAL = 1;
    private static final int CLICK_STATE_PRESSED = 2;

    private static final int STATE_COUNT_CHECK = 3;
    private static final int CHECK_STATE_DISABLED = 0;
    private static final int CHECK_STATE_UNCHECKED = 1;
    private static final int CHECK_STATE_CHECKED = 2;

    private int mStateMode;
    private UniversalDrawable[] mUniversalDrawables;
    private boolean mPacked;

    UniversalDrawableSet() {
        this(STATE_MODE_STATELESS);
    }

    UniversalDrawableSet(int stateMode) {
        mStateMode = stateMode;
        int size = mStateMode == STATE_MODE_CLICKABLE ? STATE_COUNT_CLICK :
                mStateMode == STATE_MODE_CHECKABLE ? STATE_COUNT_CHECK : 1;
        mUniversalDrawables = new UniversalDrawable[size];
        if (size > 1) {
            for (int i = 0; i < size; i++) {
                mUniversalDrawables[i] = new UniversalDrawable();
            }
        } else {
            mUniversalDrawables[0] = new UniversalDrawable();
        }
    }

    public UniversalDrawable theDisabled() {
        if (mStateMode == STATE_MODE_CLICKABLE) {
            return mUniversalDrawables[CLICK_STATE_DISABLED];
        }
        if (mStateMode == STATE_MODE_CHECKABLE) {
            return mUniversalDrawables[CHECK_STATE_DISABLED];
        }
        return null;
    }

    public UniversalDrawable theNormal() {
        if (mStateMode == STATE_MODE_CLICKABLE) {
            return mUniversalDrawables[CLICK_STATE_NORMAL];
        }
        return null;
    }

    public UniversalDrawable thePressed() {
        if (mStateMode == STATE_MODE_CLICKABLE) {
            return mUniversalDrawables[CLICK_STATE_PRESSED];
        }
        return null;
    }

    public UniversalDrawable theUnchecked() {
        if (mStateMode == STATE_MODE_CHECKABLE) {
            return mUniversalDrawables[CHECK_STATE_UNCHECKED];
        }
        return null;
    }

    public UniversalDrawable theChecked() {
        if (mStateMode == STATE_MODE_CHECKABLE) {
            return mUniversalDrawables[CHECK_STATE_CHECKED];
        }
        return null;
    }

    public UniversalDrawableSet pack() {
        if (!mPacked) {
            if (mStateMode == STATE_MODE_CLICKABLE) {
                // 以下顺序不可更改
                // when disabled
                addState(new int[]{-android.R.attr.state_enabled},
                        mUniversalDrawables[CLICK_STATE_DISABLED]);
                // View.PRESSED_ENABLED_STATE_SET
                addState(new int[]{android.R.attr.state_pressed,
                                android.R.attr.state_enabled},
                        mUniversalDrawables[CLICK_STATE_PRESSED]);
                // View.ENABLED_FOCUSED_STATE_SET
                addState(new int[]{android.R.attr.state_enabled,
                                android.R.attr.state_focused},
                        mUniversalDrawables[CLICK_STATE_NORMAL]);
                // View.ENABLED_STATE_SET
                addState(new int[]{android.R.attr.state_enabled},
                        mUniversalDrawables[CLICK_STATE_NORMAL]);
                // View.FOCUSED_STATE_SET
                addState(new int[]{android.R.attr.state_focused},
                        mUniversalDrawables[CLICK_STATE_NORMAL]);
                // View.EMPTY_STATE_SET
                addState(new int[]{}, mUniversalDrawables[CLICK_STATE_NORMAL]);
                // View.WINDOW_FOCUSED_STATE_SET
                addState(new int[]{android.R.attr.state_window_focused},
                        mUniversalDrawables[CLICK_STATE_DISABLED]);
            } else if (mStateMode == STATE_MODE_CHECKABLE) {
                // 以下顺序不可更改
                // when disabled
                addState(new int[]{-android.R.attr.state_enabled},
                        mUniversalDrawables[CHECK_STATE_DISABLED]);
                addState(new int[]{android.R.attr.state_checked,
                                android.R.attr.state_enabled},
                        mUniversalDrawables[CHECK_STATE_CHECKED]);
                addState(new int[]{-android.R.attr.state_checked,
                                android.R.attr.state_enabled},
                        mUniversalDrawables[CHECK_STATE_UNCHECKED]);
                addState(new int[]{}, mUniversalDrawables[CHECK_STATE_UNCHECKED]);
                addState(new int[]{android.R.attr.state_window_focused},
                        mUniversalDrawables[CLICK_STATE_DISABLED]);
            } else {
                addState(new int[]{}, mUniversalDrawables[0]);
            }
            mPacked = true;
        }
        return this;
    }

    @Override
    public IUniversalDrawable shape(int shape) {
        for (UniversalDrawable d : mUniversalDrawables) {
            d.shape(shape);
        }
        return this;
    }

    @Override
    public IUniversalDrawable fillMode(int fillMode) {
        for (UniversalDrawable d : mUniversalDrawables) {
            d.fillMode(fillMode);
        }
        return this;
    }

    @Override
    public IUniversalDrawable scaleType(int scaleType) {
        for (UniversalDrawable d : mUniversalDrawables) {
            d.scaleType(scaleType);
        }
        return this;
    }

    @Override
    public IUniversalDrawable radius(int radius) {
        for (UniversalDrawable d : mUniversalDrawables) {
            d.radius(radius);
        }
        return this;
    }

    @Override
    public IUniversalDrawable radius(Context context, float radiusDp) {
        for (UniversalDrawable d : mUniversalDrawables) {
            d.radius(context, radiusDp);
        }
        return this;
    }

    @Override
    public IUniversalDrawable radius(int leftTop, int rightTop, int rightBottom, int leftBottom) {
        for (UniversalDrawable d : mUniversalDrawables) {
            d.radius(leftTop, rightTop, rightBottom, leftBottom);
        }
        return this;
    }

    @Override
    public IUniversalDrawable radius(Context context, float leftTopDp, float rightTopDp,
                                     float rightBottomDp, float leftBottomDp) {
        for (UniversalDrawable d : mUniversalDrawables) {
            d.radius(context, leftTopDp, rightTopDp, rightBottomDp, leftBottomDp);
        }
        return this;
    }

    @Override
    public IUniversalDrawable strokeWidth(int strokeWidth) {
        for (UniversalDrawable d : mUniversalDrawables) {
            d.strokeWidth(strokeWidth);
        }
        return this;
    }

    @Override
    public IUniversalDrawable strokeWidth(Context context, float strokeWidthDp) {
        for (UniversalDrawable d : mUniversalDrawables) {
            d.strokeWidth(context, strokeWidthDp);
        }
        return this;
    }

    @Override
    public IUniversalDrawable strokeDash(int solidLength, int spaceLength) {
        for (UniversalDrawable d : mUniversalDrawables) {
            d.strokeDash(solidLength, spaceLength);
        }
        return this;
    }

    @Override
    public IUniversalDrawable strokeDash(Context context, float solidLengthDp, float spaceLengthDp) {
        for (UniversalDrawable d : mUniversalDrawables) {
            d.strokeDash(context, solidLengthDp, spaceLengthDp);
        }
        return this;
    }

    @Override
    public IUniversalDrawable colorStroke(int color) {
        for (UniversalDrawable d : mUniversalDrawables) {
            d.colorStroke(color);
        }
        return this;
    }

    @Override
    public IUniversalDrawable colorStroke(Context context, int colorRes) {
        for (UniversalDrawable d : mUniversalDrawables) {
            d.colorStroke(context, colorRes);
        }
        return this;
    }

    @Override
    public IUniversalDrawable colorFill(int color) {
        for (UniversalDrawable d : mUniversalDrawables) {
            d.colorFill(color);
        }
        return this;
    }

    @Override
    public IUniversalDrawable colorFill(Context context, int colorRes) {
        for (UniversalDrawable d : mUniversalDrawables) {
            d.colorFill(context, colorRes);
        }
        return this;
    }

    @Override
    public IUniversalDrawable colorGradient(int startColor, int endColor) {
        for (UniversalDrawable d : mUniversalDrawables) {
            d.colorGradient(startColor, endColor);
        }
        return this;
    }

    @Override
    public IUniversalDrawable colorGradient(Context context, int startColorRes, int endColorRes) {
        for (UniversalDrawable d : mUniversalDrawables) {
            d.colorGradient(context, startColorRes, endColorRes);
        }
        return this;
    }

    @Override
    public IUniversalDrawable linearGradientOrientation(int orientation) {
        for (UniversalDrawable d : mUniversalDrawables) {
            d.linearGradientOrientation(orientation);
        }
        return this;
    }

    @Override
    public IUniversalDrawable bitmap(Resources res, Bitmap bitmap) {
        for (UniversalDrawable d : mUniversalDrawables) {
            d.bitmap(res, bitmap);
        }
        return this;
    }

    @Override
    public IUniversalDrawable alphaFill(float alpha) {
        for (UniversalDrawable d : mUniversalDrawables) {
            d.alphaFill(alpha);
        }
        return this;
    }

    @Override
    public IUniversalDrawable alphaStroke(float alpha) {
        for (UniversalDrawable d : mUniversalDrawables) {
            d.alphaStroke(alpha);
        }
        return this;
    }

    @Override
    public IUniversalDrawable clip(Clipper clipper) {
        for (UniversalDrawable d : mUniversalDrawables) {
            d.clip(clipper);
        }
        return this;
    }

    @Override
    public void asBackground(View view) {
        Utils.asBackground(pack(), view);
    }

    @Override
    public void asForeground(View view) {
        Utils.asForeground(pack(), view);
    }

    @Override
    public void asImageDrawable(ImageView imageView) {
        Utils.asImageDrawable(pack(), imageView);
    }
}
