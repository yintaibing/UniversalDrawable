package me.yintaibing.universaldrawable.demo;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import me.yintaibing.universaldrawable.UniversalDrawable;
import me.yintaibing.universaldrawable.UniversalDrawableFactory;
import me.yintaibing.universaldrawable.UniversalDrawableSet;

/**
 * Created by yintaibing on 2018/2/8.
 */

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listenClick(findViewById(R.id.universal_view));
        listenClick(findViewById(R.id.native_view));

        Drawable d = getResources().getDrawable(R.drawable.native_drawable);
        print(d.getClass().getName());

        UniversalDrawableSet set = UniversalDrawableFactory.createClickable();
        set.shape(UniversalDrawable.SHAPE_CIRCLE)
                .strokeWidth(4);// common attrs
        set.theNormal().colorFill(Color.BLACK);// different attrs
        set.thePressed().colorFill(Color.GRAY);
//        set.theChecked()...
//        set.theUnchecked()...
    }

    private void listenClick(View v) {
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable d = v.getBackground();
                print(d.getClass().getName());
            }
        });
    }

    private void print(String s) {
        Log.e("###", s);
    }
}
