package com.loftschool.ozaharenko.loftcoin19.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
abstract class CoinsDao {

    @Query("SELECT * FROM RoomCoin")
    abstract LiveData<List<RoomCoin>> fetchAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void insertAll(List<RoomCoin> coins);

}
