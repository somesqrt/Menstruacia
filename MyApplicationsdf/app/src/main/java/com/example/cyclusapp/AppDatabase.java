package com.example.cyclusapp;

import android.content.Context;

import androidx.room.*;

@Database(
        entities = {User.class, UsersData.class, Notes.class, NotInfo.class}, version = 1)

public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDao userDao();
    public abstract UsersDataDao usersDataDao();
    public abstract NotesDao notesDao();
    public abstract NotInfoDao notInfoDao();

    private static volatile AppDatabase db;

    public static AppDatabase getDb(Context context) {
        AppDatabase result = db;
        if (result == null) {
            synchronized (AppDatabase.class) {
                if (db == null) {
                    db = result =
                            Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "users").build();
                }
            }
        }
        return result;
    }
}