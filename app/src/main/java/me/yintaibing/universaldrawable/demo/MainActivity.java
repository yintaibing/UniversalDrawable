package me.yintaibing.universaldrawable.demo;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.View;

import me.yintaibing.universaldrawable.UniversalDrawable;
import me.yintaibing.universaldrawable.UniversalDrawableFactory;
import me.yintaibing.universaldrawable.util.Attributes;
import me.yintaibing.universaldrawable.util.Clipper;
import me.yintaibing.universaldrawable.util.Utils;

/**
 * Created by yintaibing on 2018/2/8.
 */

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        setContentView(R.layout.activity_main_2);

        View clippedViewNotFollow = findViewById(R.id.clipped_view_not_follow);
        UniversalDrawableFactory.createStateless()
                .shape(UniversalDrawable.SHAPE_ROUND)
                .radius(0, 20, 20, 0)
                .colorFill(Color.LTGRAY)
                .strokeWidth(Utils.dp2px(getResources(), 2f))
                .colorStroke(Color.RED)
                .clip(new Clipper() {
                    @Override
                    public boolean buildClipPath(Path clipPath, RectF bounds, Attributes attrs) {
                        UniversalDrawable.makeDrawPath(clipPath, bounds, attrs);
                        float radius = bounds.height() * 0.5f;
                        clipPath.addCircle(bounds.left, bounds.top, radius, Path.Direction.CW);
                        clipPath.setFillType(Path.FillType.EVEN_ODD);
                        return false;
                    }
                })
                .asBackground(clippedViewNotFollow);

        View clippedViewFollow = findViewById(R.id.clipped_view_follow);
        UniversalDrawableFactory.createStateless()
                .shape(UniversalDrawable.SHAPE_ROUND)
                .radius(0, 20, 20, 0)
                .colorFill(Color.LTGRAY)
                .strokeWidth(Utils.dp2px(getResources(), 2f))
                .colorStroke(Color.RED)
                .clip(new Clipper() {
                    @Override
                    public boolean buildClipPath(Path clipPath, RectF bounds, Attributes attrs) {
                        UniversalDrawable.makeDrawPath(clipPath, bounds, attrs);
                        float radius = bounds.height() * 0.5f;
                        clipPath.addCircle(bounds.left, bounds.top, radius, Path.Direction.CW);
                        clipPath.setFillType(Path.FillType.EVEN_ODD);
                        return true;
                    }
                })
                .asBackground(clippedViewFollow);
    }
}
