package com.example.cyclusapp;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class UsersData {

    @PrimaryKey(autoGenerate = true)
    public long id;

    String email;

    String mesiac;

    String dni;


}
