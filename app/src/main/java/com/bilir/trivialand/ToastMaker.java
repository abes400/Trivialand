package com.bilir.trivialand;

import android.content.Context;
import android.widget.Toast;

public class ToastMaker {
    public static void makeToastShort(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void makeToastLong(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
