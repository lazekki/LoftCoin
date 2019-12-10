package com.loftschool.ozaharenko.loftcoin19.ui.wallets;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.loftschool.ozaharenko.loftcoin19.data.Wallet;
import com.loftschool.ozaharenko.loftcoin19.data.WalletsRepo;

import java.util.List;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import javax.inject.Inject;

class WalletsViewModel extends ViewModel {

    private final WalletsRepo walletsRepo;

    private final Observable<List<Wallet>> wallets;

    @Inject
    WalletsViewModel(WalletsRepo walletsRepo) {
        this.walletsRepo = walletsRepo;
        wallets = walletsRepo.wallets()
        .replay(1)
        .autoConnect()
        .subscribeOn(Schedulers.io());
    }

    @NonNull
    public Observable<List<Wallet>> wallets() {
        return wallets.observeOn(AndroidSchedulers.mainThread());
    }
}
