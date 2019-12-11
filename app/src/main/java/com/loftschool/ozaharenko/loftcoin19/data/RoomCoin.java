package com.loftschool.ozaharenko.loftcoin19.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.auto.value.AutoValue;

@Entity
@AutoValue
abstract class RoomCoin implements Coin {

    @NonNull
    static RoomCoin create(
            long id,
            String name,
            String symbol,
            double price,
            double change24h,
            int rank) {
        return new AutoValue_RoomCoin(name, symbol, price, change24h, rank, id);
    }

    @Override
    @PrimaryKey
    @AutoValue.CopyAnnotations
    public abstract long id();

}
