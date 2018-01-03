package com.techbridge.smsreader.views.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.techbridge.smsreader.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(SplashActivity.this, DashboardActivity.class));
        // close splash activity
        finish();
    }
}
