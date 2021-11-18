package com.example.cyclusapp;

import android.provider.ContactsContract;
import android.service.autofill.UserData;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UsersDataDao {

    @Query("SELECT * FROM usersdata")
    LiveData<List<UsersData>> list();

    @Query("SELECT * FROM usersdata WHERE email like :searchMail")
    UsersData getByMail(String searchMail);

    @Query("SELECT * FROM usersdata WHERE id = :id")
    UsersData getById(long id);

    @Query("SELECT * FROM usersdata WHERE( email LIKE :searchMail AND mesiac like :searchMonth)")
    UsersData getAllWithEmailAndMonthLike(String searchMail, String searchMonth);


    @Insert
    void save(UsersData usersData);

    @Update()
    void update(UsersData usersData);
}
