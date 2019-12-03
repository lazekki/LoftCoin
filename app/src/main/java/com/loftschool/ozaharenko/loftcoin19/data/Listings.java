package com.loftschool.ozaharenko.loftcoin19.data;

import com.google.auto.value.AutoValue;

import java.util.Collections;
import java.util.List;

@AutoValue
public abstract class Listings {

    abstract List<AutoValue_CmcCoin> data();

    public List<Coin> coins() {
        return Collections.unmodifiableList(data());
    }
}
