package com.loftschool.ozaharenko.loftcoin19.ui.wallets;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.loftschool.ozaharenko.loftcoin19.data.CoinsRepo;
import com.loftschool.ozaharenko.loftcoin19.data.CurrencyRepo;
import com.loftschool.ozaharenko.loftcoin19.data.Transaction;
import com.loftschool.ozaharenko.loftcoin19.data.Wallet;
import com.loftschool.ozaharenko.loftcoin19.data.WalletsRepo;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

class WalletsViewModel extends ViewModel {

    private final Subject<Integer> walletPosition = BehaviorSubject.createDefault(0); // adapter position, 0 by default, element number 0 from the list

    private final WalletsRepo walletsRepo;

    private final CurrencyRepo currencyRepo;

    private final CoinsRepo coinsRepo;

    private final Observable<List<Wallet>> wallets;

    private Observable<List<Transaction>> transactions;

    @Inject
    WalletsViewModel(WalletsRepo walletsRepo, CurrencyRepo currencyRepo, CoinsRepo coinsRepo) {
        this.walletsRepo = walletsRepo;
        this.currencyRepo = currencyRepo;
        this.coinsRepo = coinsRepo;

        wallets = currencyRepo.currency()
                .switchMap(walletsRepo::wallets)
                .replay(1)
                .autoConnect()
                .subscribeOn(Schedulers.io());

        transactions = wallets
                .filter(listOfWallets -> !listOfWallets.isEmpty())
                .switchMap(w -> walletPosition
                        .observeOn(Schedulers.io())
                        .distinctUntilChanged()
                        .map(w::get)
                )
                .switchMap(walletsRepo::transactions)
                .replay(1)
                .autoConnect()
                .subscribeOn(Schedulers.io());
    }

    @NonNull
    Observable<List<Wallet>> wallets() {
        return wallets
                .filter(listOfWallets -> !listOfWallets.isEmpty())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @NonNull
    Observable<List<Transaction>> transactions() {
        return transactions.observeOn(AndroidSchedulers.mainThread());
    }

    @NonNull
    Single<Wallet> addNextWallet() {
        return wallets.firstOrError()
                .flatMap(list -> Observable.fromIterable(list)
                        .map(wallet -> wallet.coin().id())
                        .toList()
                )
                .flatMap(ids -> currencyRepo.currency().firstOrError()
                        .flatMap(currency -> coinsRepo
                                .nextWithIdNotIn(currency, ids)
                        )
                )
                .flatMap(walletsRepo::createWallet)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    void selectWallet(int position) {

        walletPosition.onNext(position);
    }
}
