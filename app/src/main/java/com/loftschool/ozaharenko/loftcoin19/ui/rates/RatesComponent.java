package com.loftschool.ozaharenko.loftcoin19.ui.rates;

import com.loftschool.ozaharenko.loftcoin19.BaseComponent;
import com.loftschool.ozaharenko.loftcoin19.vm.VmModule;

import dagger.Component;

@Component(modules = {
    RatesModule.class,
    VmModule.class
}, dependencies = {
    BaseComponent.class
})

abstract class RatesComponent {
    abstract void inject(RatesFragment ratesFragment);

    abstract void inject(RatesCurrencyDialog ratesCurrencyDialog);

}
