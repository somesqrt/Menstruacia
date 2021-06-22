package com.example.cyclusapp;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface NotInfoDao {
    @Query("SELECT * FROM NotInfo WHERE( mesiac like :searchMonth AND notification like :serchDay)  ")
    NotInfo getAllWithMonthAndDaysToNotifikation(String searchMonth, String serchDay);

    @Insert
    void save(NotInfo notInfo);
}
