package com.techbridge.smsreader.views.activities;

import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build.VERSION;
import android.provider.Telephony.Sms.Sent;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class BaseActivity extends AppCompatActivity {
    public void sendSMS(String telNumber, String messageBody) {
        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent("SMS_SENT"), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent("SMS_DELIVERED"), 0);
        ContentValues sentSms = new ContentValues();
        sentSms.put("address", telNumber);
        sentSms.put("body", messageBody);
        ContentResolver contentResolver = getContentResolver();
        if (VERSION.SDK_INT >= 19) {
            contentResolver.insert(Sent.CONTENT_URI, sentSms);
        } else {
            contentResolver.insert(Uri.parse("content://sms/sent"), sentSms);
        }
        SmsManager.getDefault().sendTextMessage(telNumber, null, messageBody, sentPI, deliveredPI);
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
