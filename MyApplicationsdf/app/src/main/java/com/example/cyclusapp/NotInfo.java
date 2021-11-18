package com.example.cyclusapp;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class NotInfo {
    @PrimaryKey(autoGenerate = true)
    public long id;

    String mesiac;

    String notification;
}
