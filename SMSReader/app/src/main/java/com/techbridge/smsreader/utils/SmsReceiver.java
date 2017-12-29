package com.techbridge.smsreader.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v7.app.NotificationCompat;
import android.telephony.SmsMessage;
import android.util.Log;
import com.techbridge.smsreader.R;
import com.techbridge.smsreader.db.DBHelper;
import com.techbridge.smsreader.views.activities.DashboardActivity;
import org.apache.commons.lang3.StringUtils;

public class SmsReceiver extends BroadcastReceiver {
    private int currentNotificationID = 0;
    private DBHelper dbhelper;
    private Builder notificationBuilder;
    private NotificationManager notificationManager;

    public void onReceive(Context context, Intent intent) {
        try {
            notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            dbhelper = new DBHelper(context);
            if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    for (Object pdu : (Object[]) bundle.get("pdus")) {
                        SmsMessage tmp;
                        if (VERSION.SDK_INT >= 23) {
                            tmp = SmsMessage.createFromPdu((byte[]) pdu, bundle.getString("format"));
                        } else {
                            tmp = SmsMessage.createFromPdu((byte[]) pdu);
                        }
                        String senderRawMobileNo = tmp.getOriginatingAddress();
                        final String senderMsg = tmp.getMessageBody();
                        final String senderMobileNo = "0" + StringUtils.substring(senderRawMobileNo, senderRawMobileNo.length() - 9, senderRawMobileNo.length());
                        String sms = tmp.getMessageBody();
                        if (!(!dbhelper.getAllTankMsisdns().contains(senderMobileNo) || senderMsg.toLowerCase().contains("ac power") || senderMsg.toLowerCase().contains("rtu power") || senderMsg.toLowerCase().contains("host arm") || senderMsg.toLowerCase().contains("daily sms report") || senderMsg.toLowerCase().contains("(y)"))) {
                            final Context context2 = context;
                            new Handler().post(new Runnable() {
                                public void run() {
                                    Vibrator v = (Vibrator)context2.getSystemService(Context.VIBRATOR_SERVICE);;
                                    if (!(senderMsg.toLowerCase().contains("alert") || senderMsg.toLowerCase().contains("alarm"))) {
                                        v.vibrate(1500);
                                    }
                                    SystemClock.sleep(2000);
                                    dbhelper.updateTankReadings();
                                    setDataForSimpleNotification(context2, dbhelper.getLastTextTanklevel(dbhelper.getTankUidByMsisdn(senderMobileNo))[2]);
                                }
                            });
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e("sms exception", e.getMessage());
        }
    }

    private void sendNotification(Context context) {
        notificationBuilder.setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context, DashboardActivity.class), PendingIntent.FLAG_UPDATE_CURRENT));
        Notification notification = notificationBuilder.build();
        notification.flags |= 16;
        notification.defaults |= 1;
        currentNotificationID++;
        int notificationId = currentNotificationID;
        if (notificationId == 2147483646) {
            notificationId = 0;
        }
        notificationManager.notify(notificationId, notification);
    }

    private void setDataForSimpleNotification(Context context, String text) {
        notificationBuilder = new NotificationCompat.Builder(context).setSmallIcon(R.drawable.ic_action_name).setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_action_name)).setContentTitle("Mobi Water").setContentText(text);
        sendNotification(context);
    }
}
