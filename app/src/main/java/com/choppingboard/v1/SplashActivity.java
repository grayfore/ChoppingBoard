package com.choppingboard.v1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by Gray on 6/19/2015.
 */
public class SplashActivity extends Activity {

    // Duration of time that the splash page is displayed
    private final int SPLASH_DISPLAY_LENGTH = 5000;

    /**
     * Setup process for this class when it is created as an activity externally
     *
     * @param savedInstanceState Passed in data by an external class
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashActivity.this, RegisterScreen.class);
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
