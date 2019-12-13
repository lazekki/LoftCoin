package com.loftschool.ozaharenko.loftcoin19.data;

import com.google.auto.value.AutoValue;

public interface Coin {

    long id();

    String name();

    String symbol();

    double price();

    double change24h();

    int rank();
}
