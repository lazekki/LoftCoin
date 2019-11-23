package com.loftschool.ozaharenko.loftcoin19.ui.welcome;

import com.google.auto.value.AutoValue;

@AutoValue
abstract class WelcomePage {

    static WelcomePage create(int imageResId, int titleResId, int subtitleResId) {
        return new AutoValue_WelcomePage(imageResId, titleResId, subtitleResId);
    }

    abstract int imageResId();

    abstract int titleResId();

    abstract int subtitleResId();

}
