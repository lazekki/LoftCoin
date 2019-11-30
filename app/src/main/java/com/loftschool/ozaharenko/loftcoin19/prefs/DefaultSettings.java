package com.loftschool.ozaharenko.loftcoin19.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

class DefaultSettings implements Settings {

    private static final String SHOULD_SHOW_WELCOME_SCREEN = "should_show_welcome_screen";
    private static final String CURRENCY_CODE = "currency_code";

    private final SharedPreferences prefs;

    DefaultSettings(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public boolean shouldShowWelcomeScreen() {
        return prefs.getBoolean(SHOULD_SHOW_WELCOME_SCREEN, true);
    }

    @Override
    public void doNotShowWelcomeScreenNextTime() {
        prefs.edit().putBoolean(SHOULD_SHOW_WELCOME_SCREEN, false).apply();
    }

    @Override
    public String getDefaultCurrencyCode() {
        return prefs.getString(CURRENCY_CODE, "USD");
    }

    @Override
    public void setDefaultCurrencyCode(String code) {
        prefs.edit().putString(CURRENCY_CODE, code).apply();
    }

}
