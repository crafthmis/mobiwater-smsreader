package com.techbridge.smsreader.utils;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v4.content.WakefulBroadcastReceiver;
import com.techbridge.smsreader.R;
import com.techbridge.smsreader.db.DBHelper;
import com.techbridge.smsreader.views.activities.EditReminderActivity;

import java.util.Calendar;

public class AlarmReceiver extends WakefulBroadcastReceiver {
    AlarmManager mAlarmManager;
    PendingIntent mPendingIntent;

    public void onReceive(Context context, Intent intent) {
        int mReceivedID = Integer.parseInt(intent.getStringExtra(EditReminderActivity.EXTRA_REMINDER_ID));
        String mTitle = new DBHelper(context).getReminder(mReceivedID).getTitle();
        Intent editIntent = new Intent(context, EditReminderActivity.class);
        editIntent.putExtra(EditReminderActivity.EXTRA_REMINDER_ID, Integer.toString(mReceivedID));
        Builder mBuilder = new Builder(context).setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_action_name)).setSmallIcon(R.drawable.ic_alarm_on_white_24dp).setContentTitle(context.getResources().getString(R.string.app_name)).setTicker(mTitle).setContentText(mTitle).setSound(RingtoneManager.getDefaultUri(2)).setContentIntent(PendingIntent.getActivity(context, mReceivedID, editIntent, 134217728)).setAutoCancel(true).setOnlyAlertOnce(true);
        Vibrator v = (Vibrator) context.getSystemService("vibrator");
        v.vibrate(500);
        SystemClock.sleep(500);
        v.vibrate(500);
        SystemClock.sleep(500);
        v.vibrate(500);
        SystemClock.sleep(500);
        ((NotificationManager) context.getSystemService("notification")).notify(mReceivedID, mBuilder.build());
    }

    public void setAlarm(Context context, Calendar calendar, int ID) {
        this.mAlarmManager = (AlarmManager) context.getSystemService("alarm");
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(EditReminderActivity.EXTRA_REMINDER_ID, Integer.toString(ID));
        this.mPendingIntent = PendingIntent.getBroadcast(context, ID, intent, 268435456);
        this.mAlarmManager.set(3, SystemClock.elapsedRealtime() + (calendar.getTimeInMillis() - Calendar.getInstance().getTimeInMillis()), this.mPendingIntent);
        context.getPackageManager().setComponentEnabledSetting(new ComponentName(context, BootReceiver.class), 1, 1);
    }

    public void setRepeatAlarm(Context context, Calendar calendar, int ID, long RepeatTime) {
        this.mAlarmManager = (AlarmManager) context.getSystemService("alarm");
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(EditReminderActivity.EXTRA_REMINDER_ID, Integer.toString(ID));
        this.mPendingIntent = PendingIntent.getBroadcast(context, ID, intent, 268435456);
        long j = RepeatTime;
        this.mAlarmManager.setRepeating(3, SystemClock.elapsedRealtime() + (calendar.getTimeInMillis() - Calendar.getInstance().getTimeInMillis()), j, this.mPendingIntent);
        context.getPackageManager().setComponentEnabledSetting(new ComponentName(context, BootReceiver.class), 1, 1);
    }

    public void cancelAlarm(Context context, int ID) {
        this.mAlarmManager = (AlarmManager) context.getSystemService("alarm");
        this.mPendingIntent = PendingIntent.getBroadcast(context, ID, new Intent(context, AlarmReceiver.class), 0);
        this.mAlarmManager.cancel(this.mPendingIntent);
        context.getPackageManager().setComponentEnabledSetting(new ComponentName(context, BootReceiver.class), 2, 1);
    }
}
