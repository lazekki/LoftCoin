package com.loftschool.ozaharenko.loftcoin19.ui.wallets;

import androidx.lifecycle.ViewModel;

import com.loftschool.ozaharenko.loftcoin19.ui.wallets.WalletsViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;

@Module
abstract class WalletsModule {

    @Binds
    @IntoMap
    @ClassKey(WalletsViewModel.class)
    abstract ViewModel viewModel(WalletsViewModel impl);
}
