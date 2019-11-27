package com.loftschool.ozaharenko.loftcoin19;

//Dagger.module is responsible to provide dependency
import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
abstract class AppModule {

    @Provides   //last keyword from Dagger (@Module, @Component, @Provides)
    @Singleton  //this keyword allows to manage singleton behavior, and means that method context(app) will be called only once, then be cached, and all next attempts
                //to get context will be addressed to cached context. Might be useful if you don't want to create more than one instance of a class.
                //To get this Singleton workable, the component, what provides this Singleton, must be saved also as Singleton.
    static Context context(Application app) {
        return app.getApplicationContext();
    }

}
