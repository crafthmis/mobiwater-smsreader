package com.techbridge.smsreader.app;

import android.app.Application;
import android.content.Intent;

import com.techbridge.smsreader.R;
import com.techbridge.smsreader.utils.BackgroundService;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Mwaura on 11/19/2017.
 */

public class MobiApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        startService(new Intent(this, BackgroundService.class));
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto-Medium.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
}
