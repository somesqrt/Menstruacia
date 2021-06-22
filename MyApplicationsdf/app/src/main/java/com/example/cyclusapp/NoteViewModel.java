package com.example.cyclusapp;

import android.app.Application;
import android.content.Intent;
import android.os.Build;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.*;

public class NoteViewModel extends AndroidViewModel {
    private UserDao userDao;
    private UsersDataDao usersDataDao;
    private AppDatabase db;
    private User user;
    private UsersData usersData;
    private NotesDao notesDao;
    private NotInfoDao notInfoDao;

    private LiveData<List<Notes>> notes;
    private CompletableFuture CompletableFuture;


    @RequiresApi(api = Build.VERSION_CODES.O)
    public NoteViewModel(@NonNull Application application) {
        super(application);
        db = AppDatabase.getDb(application);
        userDao = db.userDao();
        usersDataDao = db.usersDataDao();
        notesDao = db.notesDao();
        notInfoDao = db.notInfoDao();

    }

    public void addNotification(String mesiac, String den) {
        NotInfo notInfo = new NotInfo();

        notInfo.mesiac = mesiac;
        notInfo.notification = den;

        db.getTransactionExecutor().execute(() -> notInfoDao.save(notInfo));
    }

    public NotInfo getSimilarDayForNotification(String mesiac, String day) throws ExecutionException, InterruptedException {

        ExecutorService service = Executors.newSingleThreadExecutor();
/*
        CompletableFuture<NotInfo> completablefuture = (CompletableFuture<NotInfo>) service.submit(() -> notInfoDao.getAllWithMonthAndDaysToNotifikation(mesiac, day));
*/
        CompletableFuture<NotInfo> completablefuture = CompletableFuture.supplyAsync(() -> notInfoDao.getAllWithMonthAndDaysToNotifikation(mesiac, day));

        return completablefuture.get();

    }

    public LiveData<List<Notes>> getNotes(String user, String month) {

        return notes = notesDao.list(user, month);

    }

    /*+*/public CompletableFuture<UsersData> firstGetSimilar(String email, String month) throws ExecutionException, InterruptedException {
        return CompletableFuture.supplyAsync(() -> {
              return usersDataDao.getAllWithEmailAndMonthLike(email, month);
        });
    }

    /*+*/public CompletableFuture<UsersData> getSimilar(String email, String month) throws ExecutionException, InterruptedException {
        return CompletableFuture.supplyAsync(() -> {
        return usersDataDao.getAllWithEmailAndMonthLike(email, month); //**********************************************
        });
    }


    public void addNoteToUser(String email, String mesiac, String note) {

        Notes notes = new Notes();

        notes.email = email;
        notes.mesiac = mesiac;
        notes.setDescription(note);

        db.getTransactionExecutor().execute(() -> notesDao.save(notes));

    }

    public void addDni(String email, String mesiac, String dni) {

        UsersData usersData = new UsersData();
        /*boolean random = false;
        long cislo = 0;
        while (random == false){
            cislo = 1 + ((int)Math.random() * Integer.MAX_VALUE);
            random = getUsersDataById(cislo);
        }

        usersData.id = cislo;*/
        usersData.email = email;
        usersData.mesiac = mesiac;
        usersData.dni = dni;

        db.getTransactionExecutor().execute(() -> usersDataDao.save(usersData));

    }

    /*+*/public CompletableFuture<String> getUsersDataByMail(String search) throws ExecutionException, InterruptedException {
        return CompletableFuture.supplyAsync(() -> {
        String email;
        usersData = usersDataDao.getByMail(search);
        if (usersData != null) {
            email = usersData.email;
            return email;
        }
        return null;
        });
    }

    /*+*/public CompletableFuture<String> getMenoByID(long id) throws ExecutionException, InterruptedException {
        return CompletableFuture.supplyAsync(() -> {
        String meno;
        user = userDao.getById(id);
        if (user != null) {
            meno = user.name;
            return meno;
        }
        return null;
        });
    }

    /*+*/public CompletableFuture<Integer> checkLogIn(String email, String password) {
        return CompletableFuture.supplyAsync(() -> {
            Integer id = -1;
            user = userDao.getAllWithNameLike(email);
            if (user.mail.equals(email) && user.password.equals(password))
                id = (int) user.id;
            return id;
        });
    }

    /*+*/public CompletableFuture<Integer> checkMail(String email){
        return CompletableFuture.supplyAsync(() -> {
            Integer id = -1;
        user = userDao.getAllWithNameLike(email);
        if (user.mail.equals(email))
            id = (int) user.id;
        return id;
        });
    }

    public void addUser(String email, String password, String meno) {
        User user1 = new User();
        user1.mail = email;
        user1.password = password;
        user1.name = meno;

        db.getTransactionExecutor().execute(() -> userDao.save(user1));

    }

    /*+*/public CompletableFuture<User> getUserByMail(String searchMail) throws ExecutionException, InterruptedException {
        return CompletableFuture.supplyAsync(() -> {
        user = userDao.getByMail(searchMail);
        if (user != null) {
            return user;
        }
        return null;
        });
    }

    public void updateDni(UsersData usersData) {
        db.getTransactionExecutor().execute(() -> usersDataDao.update(usersData));
    }

    public void updateHeslo(User user) {
        db.getTransactionExecutor().execute(() -> userDao.update(user));
    }
}