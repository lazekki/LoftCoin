package com.loftschool.ozaharenko.loftcoin19.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

@Dao
abstract class CoinsDao {

    @Query("SELECT * FROM RoomCoin")
    abstract Observable<List<RoomCoin>> fetchAll();

    @Query("SELECT * FROM RoomCoin WHERE id=:id")
    abstract Single<RoomCoin> fetchOne(long id);

    @Query("SELECT * FROM RoomCoin ORDER BY rank ASC LIMIT :limit")
    abstract Observable<List<RoomCoin>> fetchAllSortedByRank(int limit);

    @Query("SELECT * FROM RoomCoin ORDER BY price ASC LIMIT :limit")
    abstract Observable<List<RoomCoin>> fetchAllSortedByPriceAsc(int limit);

    @Query("SELECT * FROM RoomCoin ORDER BY price DESC LIMIT :limit")
    abstract Observable<List<RoomCoin>> fetchAllSortedByPriceDesc(int limit);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void insertAll(List<RoomCoin> coins);
}
