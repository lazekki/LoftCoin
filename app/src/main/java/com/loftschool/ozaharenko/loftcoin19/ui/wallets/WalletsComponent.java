package com.loftschool.ozaharenko.loftcoin19.ui.wallets;

import com.loftschool.ozaharenko.loftcoin19.BaseComponent;
import com.loftschool.ozaharenko.loftcoin19.vm.VmModule;

import dagger.Component;

@Component(modules = {
    WalletsModule.class,
        VmModule.class
}, dependencies = {
        BaseComponent.class
})
abstract class WalletsComponent {

    abstract void inject(WalletsFragment fmt);


}
