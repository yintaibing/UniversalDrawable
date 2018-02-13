package me.yintaibing.universaldrawable.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import me.yintaibing.universaldrawable.UniversalDrawableFactory;

/**
 * 万能Drawable View
 * <p>
 * Created by yintaibing on 2018/2/9.
 */

public class UniversalDrawableRelativeLayout extends RelativeLayout {

    public UniversalDrawableRelativeLayout(Context context) {
        this(context, null, 0);
    }

    public UniversalDrawableRelativeLayout(Context context, AttributeSet attrSet) {
        this(context, attrSet, 0);
    }

    public UniversalDrawableRelativeLayout(Context context, AttributeSet attrSet, int style) {
        super(context, attrSet, style);
        UniversalDrawableFactory.fromXml(this, context, attrSet);
    }
}
