package com.example.cyclusapp;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.cyclusapp.User;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * FROM user WHERE id = :id")
    User getById(long id);

    @Query("SELECT * FROM user WHERE mail like :searchMail")
    User getByMail(String searchMail);

    @Query("SELECT * FROM user WHERE mail LIKE :search")
    User getAllWithNameLike(String search);

    @Query("SELECT * FROM user")
    LiveData<List<User>> list();

    @Insert
    void save(User user);

    @Update()
    void update(User user);

}
