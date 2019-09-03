package fishrungames.tes.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefUtil {

    private final static String PREFS_NAME = "tes_prefs";
    private final static String KEY_TOKEN = "user_token";


    private static void setInt(Context context, String key, int value) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME,0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    private static int getInt(Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getInt(key, 0);
    }

    private static void setStr(Context context, String key, String value) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME,0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    private static String getStr(Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getString(key,"");
    }

    private static void setBool(Context context, String key, boolean value) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME,0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    private static boolean getBool(Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getBoolean(key,false);
    }


    public static void setToken(Context context, String token) {
        PrefUtil.setStr(context, KEY_TOKEN, token);
    }

    public static String getToken(Context context) {
        return PrefUtil.getStr(context, KEY_TOKEN);
    }

}