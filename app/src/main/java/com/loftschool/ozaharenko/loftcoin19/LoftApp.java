package com.loftschool.ozaharenko.loftcoin19;

import android.app.Application;
import android.os.StrictMode;

import com.loftschool.ozaharenko.loftcoin19.log.LoftTree;

import timber.log.Timber;

public class LoftApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if(BuildConfig.DEBUG) {
            StrictMode.enableDefaults();
            Timber.plant(new LoftTree());
        }
    }


}
