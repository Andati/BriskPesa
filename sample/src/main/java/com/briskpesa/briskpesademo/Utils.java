package com.briskpesa.briskpesademo;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by rodgers on 05/05/16.
 */
public class Utils {
    private static final String PREFERENCES_FILE = "briskpesademo_settings";

    public static void saveSharedSetting(Context ctx, String settingName, String settingValue) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(settingName, settingValue);
        editor.commit();
        //editor.apply();
    }

    public static String readSharedSetting(Context ctx, String settingName, String defaultValue) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        return sharedPref.getString(settingName, defaultValue);
    }

    public static Boolean isValidPhoneNumber(String phoneNumber){
        return phoneNumber.matches("^(?:\\+?254)?(0|7|07)\\d{8}$");
    }

    public static String sanitizePhoneNumber(String phoneNumber){
        if (phoneNumber.charAt(0) == '0') {
            return phoneNumber;
        }
        else if (phoneNumber.charAt(0) == '+') {
            return "0" + phoneNumber.substring(4);
        }
        else if (phoneNumber.length() == 9) {
            return "0" + phoneNumber;
        }
        else if (phoneNumber.startsWith("254")) {
            return "0" + phoneNumber.substring(3);
        }
        return phoneNumber;
    }
}
