package com.loftschool.ozaharenko.loftcoin19.data;

import androidx.annotation.NonNull;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

public interface WalletsRepo {

    @NonNull
    Observable<List<Wallet>> wallets(@NonNull Currency currency);

    @NonNull
    Observable<List<Transaction>> transactions(@NonNull Wallet wallet);

    @NonNull
    Single<Wallet> createWallet(@NonNull Coin coin);

}
