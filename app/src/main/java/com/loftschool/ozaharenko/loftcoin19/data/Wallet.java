package com.loftschool.ozaharenko.loftcoin19.data;

import androidx.annotation.NonNull;

import com.google.auto.value.AutoValue;

@AutoValue
abstract public class Wallet {

    @NonNull
    static Wallet create(String id, Long coinId, Double balance) {
        return new AutoValue_Wallet(id, coinId, balance);
    }

    public abstract String id();

    public abstract long coinId();

    public abstract double balance();

}

