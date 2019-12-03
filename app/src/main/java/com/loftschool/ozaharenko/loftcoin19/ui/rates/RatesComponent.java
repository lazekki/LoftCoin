package com.loftschool.ozaharenko.loftcoin19.ui.rates;

import androidx.fragment.app.Fragment;

import com.loftschool.ozaharenko.loftcoin19.BaseComponent;
import com.loftschool.ozaharenko.loftcoin19.vm.VmModule;

import dagger.BindsInstance;
import dagger.Component;

@Component(modules = {
    RatesModule.class,
    VmModule.class
}, dependencies = {
    BaseComponent.class,
    //Fragment.class
})
abstract class RatesComponent {

    //Dagger will generate special injector for such method' signature:
    abstract void inject(RatesFragment ratesFragment);

    abstract void inject(RatesCurrencyDialog ratesCurrencyDialog);

    @Component.Factory
    interface Factory {
        RatesComponent create(
                BaseComponent baseComponent,
                @BindsInstance Fragment fragment
        );
    }

}
