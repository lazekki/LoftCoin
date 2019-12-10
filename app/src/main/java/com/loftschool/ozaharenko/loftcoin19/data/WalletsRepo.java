package com.loftschool.ozaharenko.loftcoin19.data;

import androidx.annotation.NonNull;

import java.util.List;

import io.reactivex.Observable;

public interface WalletsRepo {

    @NonNull
    Observable<List<Wallet>> wallets();

}
