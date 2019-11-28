package com.loftschool.ozaharenko.loftcoin19.prefs;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public abstract class SettingsModule {

    @Provides
    @Singleton
    static Settings settings(Context context) {
        return new Settings(context);
    }

}
