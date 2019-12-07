package com.loftschool.ozaharenko.loftcoin19.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(version = 1, entities = {
    RoomCoin.class
}, exportSchema = false)
//https://developer.android.com/training/data-storage/room/defining-data#java
abstract class CoinsDb extends RoomDatabase {
    abstract CoinsDao coins();
}
