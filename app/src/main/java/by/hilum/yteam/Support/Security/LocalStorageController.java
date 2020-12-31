package by.hilum.yteam.Support.Security;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import java.io.File;

public class LocalStorageController {
    private static final String AUTH_PREFS_LABEL = "yteam_auth";
    //Shared Preferences Data Labels
    private static final String LOGIN_PREFS_NAME = "username";
    private static final String PASS_PREFS_NAME = "password";
    private static final String DEFAULT_VALUE = "";

    /**
     * Getting Shared Prefs Current State
     *
     * @param context Context
     * @return boolean
     */
    public static boolean isPrefsAlive(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(AUTH_PREFS_LABEL, Context.MODE_PRIVATE);

        return !sharedPreferences.getString(LOGIN_PREFS_NAME, DEFAULT_VALUE).equals(DEFAULT_VALUE) && !sharedPreferences.getString(PASS_PREFS_NAME, DEFAULT_VALUE).equals(DEFAULT_VALUE);
    }

    /**
     * Remove Shared Preferences Data
     *
     * @param context Context
     */
    public static void ClearStorage(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.deleteSharedPreferences(AUTH_PREFS_LABEL);
        } else {
            new File(context.getCacheDir().getParent() + "/shared_prefs/").delete();
        }
    }

    /**
     * Save Data To Shared Preferences
     *
     * @param context  Context
     * @param login    String
     * @param password String
     */
    public static void SaveData(Context context, String login, String password) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(AUTH_PREFS_LABEL, Context.MODE_PRIVATE);
        sharedPreferences.edit()
                .putString(LOGIN_PREFS_NAME, login)
                .putString(PASS_PREFS_NAME, password)
                .apply();

    }

    /**
     * Getting Data From SharedPreferences
     *
     * @param context Context
     * @return String[]
     */
    public static String[] GetData(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(AUTH_PREFS_LABEL, Context.MODE_PRIVATE);

        String login = sharedPreferences.getString(LOGIN_PREFS_NAME, DEFAULT_VALUE);
        String password = sharedPreferences.getString(PASS_PREFS_NAME, DEFAULT_VALUE);

        return new String[]{login, password};
    }
}
