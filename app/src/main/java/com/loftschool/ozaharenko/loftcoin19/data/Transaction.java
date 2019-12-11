package com.loftschool.ozaharenko.loftcoin19.data;

import androidx.annotation.NonNull;

import com.google.auto.value.AutoValue;

import java.util.Date;

@AutoValue
public abstract class Transaction {

    @NonNull
    static Transaction create(String id, Coin coin, Double amount, Date timestamp) {
        return new AutoValue_Transaction(id, coin, amount, timestamp);
    }

    public abstract String id();

    public abstract Coin coin();

    public abstract Double amount();

    public abstract Date timestamp();

}
