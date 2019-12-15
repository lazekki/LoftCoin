package com.loftschool.ozaharenko.loftcoin19.data;

public class CoinStub {

    public static Coin create(long id, String symbol, double price) {
        return new AutoValue_RoomCoin(symbol, symbol, price, 0d, 1, id);
    }
}
