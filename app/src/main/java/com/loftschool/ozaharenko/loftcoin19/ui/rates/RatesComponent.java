package com.loftschool.ozaharenko.loftcoin19.ui.rates;

import androidx.fragment.app.Fragment;

import com.loftschool.ozaharenko.loftcoin19.BaseComponent;

import dagger.Component;

@Component(modules = {
    RatesModule.class
}, dependencies = {
    BaseComponent.class,
    Fragment.class
})
abstract class RatesComponent {

    //Dagger will generate special injector for such method' signature:
    abstract void inject(RatesFragment ratesFragment);

}
