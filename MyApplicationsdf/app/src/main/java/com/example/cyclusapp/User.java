package com.example.cyclusapp;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public String mail;

    public String name;

    public String password;
}