package com.loftschool.ozaharenko.loftcoin19;

import android.app.Application;

import com.loftschool.ozaharenko.loftcoin19.data.DataModule;
import com.loftschool.ozaharenko.loftcoin19.prefs.SettingsModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

//we can say that our component will be a singleton using next keyword:
@Singleton
//but to provide the "singleton functionality" we must on our own (it was done when we save the component in LoftApp.java as Application
//- it is singleton in our app context.

//next construction lets to Component to know about Dagger.modules.
//Component knows everything what Module knows.
//Module can be considered as abstract of memory bus from real world.
@Component(modules = {
        AppModule.class,
        SettingsModule.class,
        DataModule.class
})
//https://dagger.dev/users-guide
abstract class AppComponent implements BaseComponent {

    /*@Component.Builder
    abstract static class Builder {

        @BindsInstance  //link instance of the application app with the component AppComponent
        abstract Builder application(Application app);  //we can sent also context to the method there, but app itself provides way to get a context, so we sent an application

        abstract AppComponent build();

    }*/

    //Factory component considers as more safe then Builder. You may forget to send app to Builder and application will crashed in runtime.
    //But, if your component depends on huge amount of fields, use Builder.
    @Component.Factory
    abstract static class Factory {
        abstract AppComponent create(@BindsInstance Application app);
    }
}
