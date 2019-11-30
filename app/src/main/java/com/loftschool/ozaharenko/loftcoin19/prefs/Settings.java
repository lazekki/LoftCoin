package com.loftschool.ozaharenko.loftcoin19.prefs;

public interface Settings {

    boolean shouldShowWelcomeScreen();

    void doNotShowWelcomeScreenNextTime();

    String getDefaultCurrencyCode();

    void setDefaultCurrencyCode(String code);

}

