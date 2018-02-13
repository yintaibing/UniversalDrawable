package me.yintaibing.universaldrawable.view;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.AttributeSet;

import me.yintaibing.universaldrawable.UniversalDrawableFactory;

/**
 * 万能Drawable View
 * <p>
 * Created by yintaibing on 2018/2/9.
 */

public class UniversalDrawableCheckBox extends AppCompatCheckBox {

    public UniversalDrawableCheckBox(Context context) {
        this(context, null, 0);
    }

    public UniversalDrawableCheckBox(Context context, AttributeSet attrSet) {
        this(context, attrSet, 0);
    }

    public UniversalDrawableCheckBox(Context context, AttributeSet attrSet, int style) {
        super(context, attrSet, style);
        UniversalDrawableFactory.fromXml(this, context, attrSet);
    }
}
