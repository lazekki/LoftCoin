package com.loftschool.ozaharenko.loftcoin19.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Observable;

@Dao
abstract class CoinsDao {

    @Query("SELECT * FROM RoomCoin")
    abstract Observable<List<RoomCoin>> fetchAll();

    @Query("SELECT * FROM RoomCoin ORDER BY price ASC")
    abstract Observable<List<RoomCoin>> fetchAllSortedByPriceAsc();

    @Query("SELECT * FROM RoomCoin ORDER BY price DESC")
    abstract Observable<List<RoomCoin>> fetchAllSortedByPriceDesc();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void insertAll(List<RoomCoin> coins);
}
