package me.yintaibing.universaldrawable.util;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;

/**
 * 返回万能Drawable的剪切路径
 * <p>
 * Created by yintaibing on 2018/2/9.
 */

public abstract class Clipper {
    private boolean mDirty = true;

    private boolean mStrokeFollowClip = false;
    private Path mClipPath;

    private int mCanvasSaveCount;
    private Paint mMutiplyPaint;

    public Clipper() {
        mClipPath = new Path();
        mMutiplyPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMutiplyPaint.setColor(Color.WHITE);
        mMutiplyPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY));
    }

    public final void updateClipPath(RectF bounds, Attributes attrs) {
        mStrokeFollowClip = buildClipPath(mClipPath, bounds, attrs);
        mDirty = false;
    }

    public final Path getClipPath() {
        return mClipPath;
    }

    public final boolean isStrokeFollowClip() {
        return mStrokeFollowClip;
    }

    public final void saveCanvasLayer(Canvas canvas) {
        if (!mClipPath.isEmpty()) {
            mCanvasSaveCount = canvas.saveLayer(0, 0, canvas.getWidth(), canvas.getHeight(),
                    null, Canvas.ALL_SAVE_FLAG);
        }
    }

    public final void clipAndRestoreCanvas(Canvas canvas) {
        if (!mClipPath.isEmpty()) {
            canvas.drawPath(mClipPath, mMutiplyPaint);
            canvas.restoreToCount(mCanvasSaveCount);
        }
    }

    /**
     * 剪切
     *
     * @param path   剪切路径
     * @param bounds 剪切区域
     * @param attrs  属性
     * @return 是否跟随剪切路径进行描边
     */
    public abstract boolean buildClipPath(Path path, RectF bounds, Attributes attrs);

    public boolean hasClip() {
        return mClipPath != null && !mClipPath.isEmpty();
    }

    public boolean isDirty() {
        return mDirty;
    }

    public void invalidate() {
        mDirty = true;
    }
}
