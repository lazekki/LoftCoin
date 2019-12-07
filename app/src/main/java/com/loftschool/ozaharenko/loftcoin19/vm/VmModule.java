package com.loftschool.ozaharenko.loftcoin19.vm;

import androidx.lifecycle.ViewModelProvider;

import javax.inject.Named;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class VmModule {

    //@Vm
    @Binds
    abstract ViewModelProvider.Factory vmFactory(VmFactory impl);

}


