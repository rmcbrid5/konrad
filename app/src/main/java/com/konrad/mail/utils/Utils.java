package com.konrad.mail.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public interface Utils {

    static boolean checkPermission(Context context, String permission) {
        int result = ContextCompat.checkSelfPermission(context, permission);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    static void printErrorMessage(String tag, String uniqueMessage, Throwable t) {
        String errStr = "__test ERR: " + uniqueMessage + "// msg: " + t.getMessage();
        Log.e(tag, errStr);
    }

    static void showToast(Context context, String message) {
        Toast t = Toast.makeText(context, message, Toast.LENGTH_LONG);
        t.getView().setBackgroundColor(Color.RED);
        TextView tv = t.getView().findViewById(android.R.id.message);
        tv.setTextColor(Color.WHITE);
        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        t.show();
    }

    static void showToast(Context context, @StringRes int message) {
        showToast(context, context.getString(message));
    }

}
