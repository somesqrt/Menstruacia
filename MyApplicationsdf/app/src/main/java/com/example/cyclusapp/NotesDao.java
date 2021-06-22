package com.example.cyclusapp;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NotesDao {

    @Query("SELECT * FROM notes WHERE( email LIKE :searchMail AND mesiac like :searchMonth)")
    LiveData<List<Notes>> list(String searchMail, String searchMonth);

    @Insert
    void save(Notes notes);

}
