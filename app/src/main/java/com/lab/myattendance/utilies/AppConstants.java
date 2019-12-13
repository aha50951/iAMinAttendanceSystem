package com.lab.myattendance.utilies;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class AppConstants {


    /**
     * Called to check the internet Connection
     * */
    public static boolean isNetworkConnected(Context ctx) {
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }


    /**
     * Hides the soft input keyboard if it is shown to the screen.
     */

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * Convert the returned Base64 Image String to Array of bytes
     * */
    public static byte[] fromStringToByte(String base64) {
        byte[] data = Base64.decode(base64, Base64.DEFAULT);
        return data;
    }


}

