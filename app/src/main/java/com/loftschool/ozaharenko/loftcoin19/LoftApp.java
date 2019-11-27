package com.loftschool.ozaharenko.loftcoin19;

import android.app.Application;
import android.os.StrictMode;

import com.loftschool.ozaharenko.loftcoin19.BuildConfig;
import com.loftschool.ozaharenko.loftcoin19.log.LoftTree;

import timber.log.Timber;

public class LoftApp extends Application {

    private AppComponent component;

    public AppComponent getComponent() {
        return component;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if(BuildConfig.DEBUG) {
            StrictMode.enableDefaults();
            Timber.plant(new LoftTree());
        }

        //if you use Builder:
        /*component = DaggerAppComponent.builder()
                .application(this)
                .build();
        */

        //if you use Factory:
        component = DaggerAppComponent.factory().create(this);

        //when we do:
        //component.context();
        //there is next calls stack:
        //component -> module -> factory.context(app) -> app.getApplicationContext()
                                // (factory.context() = -> some implementation -> context for this app)

        //template to use new version of LoftTree.log method:
        //enter into any place where you need to throw something into Logcat, next code:
        //Timber.d("%s", this);
    }


}
