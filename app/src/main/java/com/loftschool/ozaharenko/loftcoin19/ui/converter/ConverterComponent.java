package com.loftschool.ozaharenko.loftcoin19.ui.converter;

import com.loftschool.ozaharenko.loftcoin19.BaseComponent;
import com.loftschool.ozaharenko.loftcoin19.vm.VmModule;

import dagger.Component;

@Component(modules = {
    ConverterModule.class,
    VmModule.class
}, dependencies  = {
    BaseComponent.class
})

abstract class ConverterComponent {
    abstract void inject(ConverterFragment fmt);

    abstract void inject(CoinsSheetDialog fmt);
}
