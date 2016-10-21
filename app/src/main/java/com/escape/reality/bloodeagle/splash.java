package com.escape.reality.bloodeagle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

/**
 * Created by JR HARI NANDHA on 05-09-2016.
 */
public class splash extends Activity {
    private static int SPLASH_TIME_OUT = 1200;
    private ImageView imageView;
    public void onCreate(Bundle s)
    {
        super.onCreate(s);
        setContentView(R.layout.splash);
        imageView = (ImageView)findViewById(R.id.imageView2);
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(splash.this, LoginActivity.class);
                startActivity(i);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
       // imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        //imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        //sleep(2000);
       // startActivity(new Intent(this,LoginActivity.class));
    }

