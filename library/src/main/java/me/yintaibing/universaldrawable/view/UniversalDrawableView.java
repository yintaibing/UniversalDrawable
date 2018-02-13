package me.yintaibing.universaldrawable.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import me.yintaibing.universaldrawable.UniversalDrawableFactory;

/**
 * 通用Drawable View
 * <p>
 * Created by yintaibing on 2018/2/9.
 */

public class UniversalDrawableView extends View {

    public UniversalDrawableView(Context context) {
        this(context, null, 0);
    }

    public UniversalDrawableView(Context context, AttributeSet attrSet) {
        this(context, attrSet, 0);
    }

    public UniversalDrawableView(Context context, AttributeSet attrSet, int style) {
        super(context, attrSet, style);
        UniversalDrawableFactory.fromXml(this, context, attrSet);
    }
}
