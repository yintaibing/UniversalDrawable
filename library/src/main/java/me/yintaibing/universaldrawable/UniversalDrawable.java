package me.yintaibing.universaldrawable;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;

import me.yintaibing.universaldrawable.util.Attributes;
import me.yintaibing.universaldrawable.util.Clipper;
import me.yintaibing.universaldrawable.util.Utils;

/**
 * 万能Drawable
 * <p>
 * Created by yintaibing on 2018/2/8.
 */

public class UniversalDrawable extends Drawable implements IUniversalDrawable {
    public static final int SHAPE_RECT = 0;
    public static final int SHAPE_ROUND = 1;
    public static final int SHAPE_SEMICIRCLE = 2;
    public static final int SHAPE_CIRCLE = 3;

    public static final int FILL_MODE_COLOR = 1;
    public static final int FILL_MODE_LINEAR_GRADIENT = 2;
    public static final int FILL_MODE_BITMAP = 4;

    public static final int SCALE_TYPE_CENTER = 0;
    public static final int SCALE_TYPE_CENTER_CROP = 1;
    public static final int SCALE_TYPE_FIT_CENTER = 2;
    public static final int SCALE_TYPE_FIT_XY = 3;

    public static final int LINEAR_GRADIENT_LR = 0;
    public static final int LINEAR_GRADIENT_TB = 1;
    public static final int LINEAR_GRADIENT_RL = 2;
    public static final int LINEAR_GRADIENT_BT = 3;

    private Attributes mAttrs;
    private RectF mBoundsF;
//    private RectF mStrokeBoundsF;
    private Path mDrawPath;
//    private Path mStrokePath;
    private Paint mFillPaint;
    private Paint mStrokePaint;
    private Clipper mClipper;

    private boolean mIsBoundsDirty = true;
    private boolean mIsPathDirty = true;
    private boolean mIsFillPaintDirty = true;
    private boolean mIsStrokePaintDirty = true;

    /*----------Initialize Code-----------*/

    UniversalDrawable() {
        mAttrs = new Attributes();
        mBoundsF = new RectF();
        mDrawPath = new Path();
        mFillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mFillPaint.setStyle(Paint.Style.FILL);
    }

    /*----------Draw Code-----------*/

    @Override
    public void draw(@NonNull Canvas canvas) {
        // 检查绘制区域
        boolean hasStroke = mAttrs.hasStroke();
        checkBoundsAndPath(hasStroke ? mAttrs.strokeWidth * 0.5f : 0f);

        // 获取剪切信息
        Clipper clipper = mClipper;
        boolean hasClip = false;
        boolean strokeFollowClip = false;
        Path clipPath = null;
        if (clipper != null) {
            if (clipper.isDirty()) {
                clipper.updateClipPath(mBoundsF, mAttrs);
            }
            hasClip = clipper.hasClip();
            strokeFollowClip = clipper.isStrokeFollowClip();
            clipPath = clipper.getClipPath();
        }
        if (hasClip) {
            clipper.saveCanvasLayer(canvas);
        }

        // 绘制填充
        drawFill(canvas);

        if (hasStroke) {
            // 绘制描边。如果描边跟随剪切路径，则按剪切路径描边；否则按实际边界描边
            drawStroke(canvas, strokeFollowClip ? clipPath : mDrawPath);
        }

        if (hasClip) {
            clipper.clipAndRestoreCanvas(canvas);
        }
    }

    private void checkBoundsAndPath(float strokeInset) {
        if (mIsBoundsDirty) {
            mBoundsF.set(getBounds());
            if (strokeInset > 0f) {
                mBoundsF.inset(strokeInset, strokeInset);
            }
            mIsBoundsDirty = false;
        }

        if (mIsPathDirty) {
            mDrawPath.reset();
            makeDrawPath(mDrawPath, mBoundsF, mAttrs);
            mIsPathDirty = false;
        }
    }

    public static void makeDrawPath(Path path, RectF bounds, Attributes attrs) {
        float radius;
        switch (attrs.shape) {
            case SHAPE_RECT:
            default:
                path.addRect(bounds, Path.Direction.CW);
                break;
            case SHAPE_ROUND:
                if (attrs.hasDifferentRadius()) {
                    int[] r = attrs.radiusArray;
                    float[] radii = new float[]{r[0], r[0], r[1], r[1], r[2], r[2], r[3], r[3]};
                    path.addRoundRect(bounds, radii, Path.Direction.CW);
                } else {
                    radius = attrs.radius;
                    path.addRoundRect(bounds, radius, radius, Path.Direction.CW);
                }
                break;
            case SHAPE_SEMICIRCLE:
                radius = bounds.height() * 0.5f;
                path.addRoundRect(bounds, radius, radius, Path.Direction.CW);
                break;
            case SHAPE_CIRCLE:
                float width = bounds.width();
                float height = bounds.height();
                radius = Math.min(width, height) * 0.5f;
                path.addCircle(width * 0.5f, height * 0.5f, radius, Path.Direction.CW);
                break;
        }
    }

    private void drawFill(Canvas canvas) {
        if (mIsFillPaintDirty) {
            Shader shader = createShader();
            mFillPaint.setShader(shader);
            if (shader != null) {
                // colorFilter
                if ((mAttrs.fillMode & FILL_MODE_COLOR) != 0) {
                    mFillPaint.setColorFilter(createColorFilter(mAttrs.colorFill));
                }
            } else {
                mFillPaint.setColor(mAttrs.colorFill);
            }
            mFillPaint.setAlpha((int) (mAttrs.alphaFill * 255f));
            mIsFillPaintDirty = false;
        }

        canvas.drawPath(mDrawPath, mFillPaint);
    }

    private void drawStroke(Canvas canvas, Path strokePath) {
        if (mIsStrokePaintDirty) {
            if (mStrokePaint == null) {
                mStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                mStrokePaint.setStyle(Paint.Style.STROKE);
            }
            mStrokePaint.setColor(mAttrs.colorStroke);
            mStrokePaint.setStrokeWidth(mAttrs.strokeWidth);
            if (mAttrs.hasDashStroke()) {
                int[] dash = mAttrs.strokeDash;
                mStrokePaint.setPathEffect(new DashPathEffect(new float[]{dash[0], dash[1]}, 0f));
            } else {
                mStrokePaint.setPathEffect(null);
            }
            mStrokePaint.setAlpha((int) (mAttrs.alphaStroke * 255f));
            mIsStrokePaintDirty = false;
        }

        canvas.drawPath(strokePath, mStrokePaint);
    }

    private Shader createShader() {
        // 不允许FILL_MODE_BITMAP和FILL_MODE_LINEAR_GRADIENT同时存在
        int fillMode = mAttrs.fillMode;
        Shader shader;

        // bitmap
        if (mAttrs.hasBitmap()) {
            shader = new BitmapShader(mAttrs.bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

            // scaleType
            Bitmap b = mAttrs.bitmap;
            Matrix matrix = new Matrix();
            float drawableW = mBoundsF.width(), drawableH = mBoundsF.height();
            float bitmapW = b.getScaledWidth(mAttrs.targetDensity),
                    bitmapH = b.getScaledHeight(mAttrs.targetDensity);
            float widthRatio = drawableW / bitmapW, heightRatio = drawableW / bitmapH;
            float scaleX = 1f, scaleY = 1f;
            switch (mAttrs.scaleType) {
                case SCALE_TYPE_CENTER:
                default:
                    break;
                case SCALE_TYPE_CENTER_CROP:
                    if ((bitmapW < drawableW && bitmapH < drawableH) ||
                            (bitmapW > drawableW && bitmapH > drawableH)) {
                        scaleX = scaleY = Math.max(widthRatio, heightRatio);
                    } else if (bitmapW < drawableW) {
                        scaleY = scaleX  = widthRatio;
                    } else if (bitmapH < drawableH) {
                        scaleX = scaleY = heightRatio;
                    }
                    break;
                case SCALE_TYPE_FIT_CENTER:
                    if ((bitmapW < drawableW && bitmapH < drawableH) ||
                            (bitmapW > drawableW && bitmapH > drawableH)) {
                        scaleX = scaleY = Math.min(widthRatio, heightRatio);
                    } else if (bitmapW < drawableW) {
                        scaleX = scaleY = heightRatio;
                    } else if (bitmapH < drawableH) {
                        scaleY = scaleX = widthRatio;
                    }
                    break;
                case SCALE_TYPE_FIT_XY:
                    scaleX = widthRatio;
                    scaleY = heightRatio;
                    break;
            }
            matrix.postScale(scaleX, scaleY);
            float translateX = (drawableW - bitmapW * scaleX) * 0.5f;
            float translateY = (drawableH - bitmapH * scaleY) * 0.5f;
            matrix.postTranslate(translateX, translateY);
            shader.setLocalMatrix(matrix);

            return shader;
        }

        // linearGradient
        if ((fillMode & FILL_MODE_LINEAR_GRADIENT) != 0) {
            float x0, y0, x1, y1;
            RectF bounds = mBoundsF;
            switch (mAttrs.linearGradientOrientation) {
                case LINEAR_GRADIENT_LR:
                default:
                    x0 = bounds.left;
                    x1 = bounds.right;
                    y0 = y1 = bounds.top;
                    break;
                case LINEAR_GRADIENT_TB:
                    y0 = bounds.top;
                    y1 = bounds.bottom;
                    x0 = x1 = bounds.left;
                    break;
                case LINEAR_GRADIENT_RL:
                    x0 = bounds.right;
                    x1 = bounds.left;
                    y0 = y1 = bounds.top;
                    break;
                case LINEAR_GRADIENT_BT:
                    y0 = bounds.bottom;
                    y1 = bounds.top;
                    x0 = x1 = bounds.left;
            }
            shader = new LinearGradient(x0, y0, x1, y1,
                    mAttrs.colorGradientStart, mAttrs.colorGradientEnd, Shader.TileMode.CLAMP);
            return shader;
        }
        return null;
    }

    private ColorMatrixColorFilter createColorFilter(int color) {
        float r = Color.red(color) / 255f;
        float g = Color.green(color) / 255f;
        float b = Color.blue(color) / 255f;
        float a = Color.alpha(color) / 255f;
        ColorMatrix cm = new ColorMatrix(new float[]{
                r, 0, 0, 0, 0,
                0, g, 0, 0, 0,
                0, 0, b, 0, 0,
                0, 0, 0, a, 0
        });
        return new ColorMatrixColorFilter(cm);
    }

    /*----------Super Method Code-----------*/

    @Override
    public void setAlpha(int alpha) {
        mFillPaint.setAlpha(alpha);
        if (mStrokePaint != null) {
            mStrokePaint.setAlpha(alpha);
        }
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        if (colorFilter != null) {
            mFillPaint.setColorFilter(colorFilter);
            if (mStrokePaint != null) {
                mStrokePaint.setColorFilter(colorFilter);
            }
        }
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, bottom);
        markBoundsDirty();
        markPathDirty();
    }

    @Override
    public void setBounds(@NonNull Rect bounds) {
        super.setBounds(bounds);
        markBoundsDirty();
        markPathDirty();
    }

    @Override
    public int getIntrinsicWidth() {
        if (mAttrs.hasBitmap()) {
            return mAttrs.bitmap.getScaledWidth(mAttrs.targetDensity);
        }
        return super.getIntrinsicWidth();
    }

    @Override
    public int getIntrinsicHeight() {
        if (mAttrs.hasBitmap()) {
            return mAttrs.bitmap.getScaledHeight(mAttrs.targetDensity);
        }
        return super.getIntrinsicHeight();
    }

    @Override
    public int getOpacity() {
        if (mAttrs.hasBitmap()) {
            return getBitmapDrawableOpacity();
        } else {
            return getColorDrawableOpacity();
        }
    }

    /**
     * {@link android.graphics.drawable.BitmapDrawable#getOpacity()}
     */
    private int getBitmapDrawableOpacity() {
        if (mAttrs.scaleType != SCALE_TYPE_CENTER_CROP &&
                mAttrs.scaleType != SCALE_TYPE_FIT_XY) {
            return PixelFormat.TRANSLUCENT;
        }
        return (!mAttrs.hasBitmap() || mAttrs.bitmap.hasAlpha() ||
                mFillPaint.getAlpha() < 255) ? PixelFormat.TRANSLUCENT : PixelFormat.OPAQUE;
    }

    /**
     * {@link android.graphics.drawable.ColorDrawable#getOpacity()}
     */
    private int getColorDrawableOpacity() {
        if (mFillPaint.getColorFilter() != null) {
            return PixelFormat.TRANSLUCENT;
        }
        switch (mAttrs.colorFill >>> 24) {
            case 255:
                return PixelFormat.OPAQUE;
            case 0:
                return PixelFormat.TRANSPARENT;
            default:
                return PixelFormat.TRANSLUCENT;
        }
    }

    @Override
    protected boolean onLevelChange(int level) {
        markBoundsDirty();
        markPathDirty();
        return super.onLevelChange(level);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        this.setBounds(bounds);
    }

    /*----------Implementation Code-----------*/

    @Override
    public UniversalDrawable shape(int shape) {
        if (mAttrs.shape != shape) {
            mAttrs.shape = shape;
            markPathDirty();
        }
        return this;
    }

    @Override
    public UniversalDrawable fillMode(int fillMode) {
        if (mAttrs.fillMode != fillMode) {
            mAttrs.fillMode = fillMode;
            markFillPaintDirty();
        }
        return this;
    }

    @Override
    public UniversalDrawable scaleType(int scaleType) {
        if (mAttrs.scaleType != scaleType) {
            mAttrs.scaleType = scaleType;
            markFillPaintDirty();
        }
        return this;
    }

    @Override
    public UniversalDrawable radius(int radius) {
        if (mAttrs.radius != radius) {
            mAttrs.radius = radius;
            markPathDirty();
        }
        return this;
    }

    @Override
    public UniversalDrawable radius(Context context, float radiusDp) {
        return radius(Utils.dp2px(context.getResources(), radiusDp));
    }

    @Override
    public UniversalDrawable radius(int leftTop, int rightTop, int rightBottom, int leftBottom) {
        boolean dirty = false;
        if (mAttrs.radiusArray == null) {
            mAttrs.radiusArray = new int[]{
                    leftTop, rightTop, rightBottom, leftBottom
            };
            dirty = true;
        } else {
            int[] r = mAttrs.radiusArray;
            if (r[0] != leftTop) {
                r[0] = leftTop;
                dirty = true;
            }
            if (r[1] != rightTop) {
                r[1] = rightTop;
                dirty = true;
            }
            if (r[2] != rightTop) {
                r[2] = rightTop;
                dirty = true;
            }
            if (r[3] != rightTop) {
                r[3] = rightTop;
                dirty = true;
            }
        }
        if (dirty) {
            markPathDirty();
        }
        return this;
    }

    @Override
    public UniversalDrawable radius(Context context, float leftTopDp, float rightTopDp,
                                    float rightBottomDp, float leftBottomDp) {
        Resources res = context.getResources();
        return radius(Utils.dp2px(res, leftTopDp), Utils.dp2px(res, rightTopDp),
                Utils.dp2px(res, rightBottomDp), Utils.dp2px(res, leftBottomDp));
    }

    @Override
    public UniversalDrawable strokeWidth(int strokeWidth) {
        if (mAttrs.strokeWidth != strokeWidth) {
            mAttrs.strokeWidth = strokeWidth;
            markStrokePaintDirty();
        }
        return this;
    }

    @Override
    public UniversalDrawable strokeWidth(Context context, float strokeWidthDp) {
        return strokeWidth(Utils.dp2px(context.getResources(), strokeWidthDp));
    }

    @Override
    public UniversalDrawable strokeDash(int solidLength, int spaceLength) {
        boolean dirty = false;
        if (mAttrs.strokeDash == null) {
            mAttrs.strokeDash = new int[]{solidLength, spaceLength};
            dirty = true;
        } else {
            int[] dash = mAttrs.strokeDash;
            if (dash[0] != solidLength) {
                dash[0] = solidLength;
                dirty = true;
            }
            if (dash[1] != spaceLength) {
                dash[1] = spaceLength;
                dirty = true;
            }
        }
        if (dirty) {
            markStrokePaintDirty();
        }
        return this;
    }

    @Override
    public UniversalDrawable strokeDash(Context context, float solidLengthDp, float spaceLengthDp) {
        Resources res = context.getResources();
        return strokeDash(Utils.dp2px(res, solidLengthDp), Utils.dp2px(res, spaceLengthDp));
    }

    @Override
    public UniversalDrawable colorStroke(int color) {
        if (mAttrs.colorStroke != color) {
            mAttrs.colorStroke = color;
            markStrokePaintDirty();
        }
        return this;
    }

    @Override
    public UniversalDrawable colorStroke(Context context, int colorRes) {
        return colorStroke(ContextCompat.getColor(context, colorRes));
    }

    @Override
    public UniversalDrawable colorFill(int color) {
        if (mAttrs.colorFill != color) {
            mAttrs.colorFill = color;
            markFillPaintDirty();
        }
        return this;
    }

    @Override
    public UniversalDrawable colorFill(Context context, int colorRes) {
        return colorFill(ContextCompat.getColor(context, colorRes));
    }

    @Override
    public UniversalDrawable colorGradient(int startColor, int endColor) {
        boolean dirty = false;
        if (mAttrs.colorGradientStart != startColor) {
            mAttrs.colorGradientStart = startColor;
            dirty = true;
        }
        if (mAttrs.colorGradientEnd != endColor) {
            mAttrs.colorGradientEnd = endColor;
            dirty = true;
        }
        if (dirty) {
            markFillPaintDirty();
        }
        return this;
    }

    @Override
    public UniversalDrawable colorGradient(Context context, int startColorRes, int endColorRes) {
        return colorGradient(ContextCompat.getColor(context, startColorRes),
                ContextCompat.getColor(context, endColorRes));
    }

    @Override
    public UniversalDrawable linearGradientOrientation(int orientation) {
        if (mAttrs.linearGradientOrientation != orientation) {
            mAttrs.linearGradientOrientation = orientation;
            markFillPaintDirty();
        }
        return this;
    }

    @Override
    public UniversalDrawable bitmap(Resources res, Bitmap bitmap) {
        boolean dirty = false;
        if (mAttrs.bitmap != bitmap) {
            mAttrs.bitmap = bitmap;
            dirty = true;
        }
        int density;
        if (mAttrs.targetDensity != (density = res.getDisplayMetrics().densityDpi)) {
            mAttrs.targetDensity = density;
            dirty = true;
        }
        if (dirty) {
            markFillPaintDirty();
        }
        return this;
    }

    @Override
    public IUniversalDrawable alphaFill(float alpha) {
        if (mAttrs.alphaFill != alpha) {
            mAttrs.alphaFill = alpha;
            markFillPaintDirty();
        }
        return this;
    }

    @Override
    public IUniversalDrawable alphaStroke(float alpha) {
        if (mAttrs.alphaStroke != alpha) {
            mAttrs.alphaStroke = alpha;
            markStrokePaintDirty();
        }
        return this;
    }

    @Override
    public IUniversalDrawable clip(Clipper clipper) {
        mClipper = clipper;
        return this;
    }

    @Override
    public void asBackground(View view) {
        Utils.asBackground(this, view);
    }

    @Override
    public void asForeground(View view) {
        Utils.asForeground(this, view);
    }

    @Override
    public void asImageDrawable(ImageView imageView) {
        Utils.asImageDrawable(this, imageView);
    }

    /*----------Tool Code-----------*/

    private void markBoundsDirty() {
        mIsBoundsDirty = true;
    }

    private void markPathDirty() {
        mIsPathDirty = true;
    }

    private void markFillPaintDirty() {
        mIsFillPaintDirty = true;
    }

    private void markStrokePaintDirty() {
        mIsStrokePaintDirty = true;
    }
}
