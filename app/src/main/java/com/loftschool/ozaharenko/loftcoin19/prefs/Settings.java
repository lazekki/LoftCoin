package com.loftschool.ozaharenko.loftcoin19.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

public class Settings {

    private static final String SHOULD_SHOW_WELCOME_SCREEN = "should_show_welcome_screen";

    private final SharedPreferences prefs;

    Settings(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public boolean shouldShowWelcomeScreen() {
        return prefs.getBoolean(SHOULD_SHOW_WELCOME_SCREEN, true);
    }

    public void doNotShowWelcomeScreenNextTime() {
        prefs.edit().putBoolean(SHOULD_SHOW_WELCOME_SCREEN, false).apply();
    }

}