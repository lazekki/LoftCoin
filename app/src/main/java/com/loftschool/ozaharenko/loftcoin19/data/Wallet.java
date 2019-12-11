package com.loftschool.ozaharenko.loftcoin19.data;

import androidx.annotation.NonNull;

import com.google.auto.value.AutoValue;

@AutoValue
abstract public class Wallet {

    @NonNull
    static Wallet create(String id, Coin coin, Double balance) {
        return new AutoValue_Wallet(id, coin, balance);
    }

    public abstract String id();

    public abstract Coin coin();

    public abstract double balance();

}

