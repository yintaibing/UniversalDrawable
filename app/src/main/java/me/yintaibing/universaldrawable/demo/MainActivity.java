package me.yintaibing.universaldrawable.demo;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

/**
 * Created by yintaibing on 2018/2/8.
 */

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
