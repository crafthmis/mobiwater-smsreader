package com.techbridge.smsreader.utils;

import android.content.Context;
import android.widget.Toast;

public class Utils {
    public static Toast showShortToast(Context ctx, String msg) {
        return Toast.makeText(ctx, msg, Toast.LENGTH_SHORT);
    }

    public static Toast showLongToast(Context ctx, String msg) {
        return Toast.makeText(ctx, msg, Toast.LENGTH_LONG);
    }
}
