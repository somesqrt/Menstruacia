package com.example.cyclusapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.iid.FirebaseInstanceId;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Signup extends AppCompatActivity {

    private static final String TAG = "RegIntentService";
    public static ArrayList<String> allID = new ArrayList<String>();
    public static String idForData;
    public static ArrayList<String> dniForDatabase = new ArrayList<String>();
    public static String idteraz;
    public static String dnesDate;
    public static String meno;
    public static long id_for_check_LogIn;
    static UsersData usersData;
    public User IDmailfromDB;
    NoteViewModel noteViewModel;
    Button login;
    private EditText mail, pass;
    private List<String> listData;
    private List<String> listPassData;
    private List<String> listID;
    private List<String> listNamesUsers;

    public void decoder(String dni) {
        String time = "";
        int i = 0;
        while (i != dni.length()) {

            if (dni.charAt(i) != ' ') {
                while (dni.charAt(i) != ' ') {
                    time = time + dni.charAt(i);
                    i++;
                }
            } else {
                CalendarAdapter.daysForView.add(time);
                time = "";
                i++;
            }
        }
        Log.i(TAG, "FCM Registration Token: " + MyFirebaseMessagingService.getToken(this));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.i(TAG, "FCM Registration Token: " + token);


        ViewModelProvider.AndroidViewModelFactory viewModelFactory
                = new ViewModelProvider.AndroidViewModelFactory(getApplication());
        ViewModelProvider viewModelProvider = new ViewModelProvider(this, viewModelFactory);
        noteViewModel = viewModelProvider.get(NoteViewModel.class);


        init();
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("yyyyMM", Locale.getDefault());
        String formattedDate = df.format(c);

        if (formattedDate.charAt(4) != 0) {
            dnesDate = "";
            for (int i = 0; i < formattedDate.length(); i++) {
                if (i != 4) {
                    dnesDate = dnesDate + formattedDate.charAt(i);
                }
            }
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void init() {
        mail = findViewById(R.id.editEmail);
        pass = findViewById(R.id.editPass);
        listData = new ArrayList<>();
        listPassData = new ArrayList<>();
        listID = new ArrayList<>();
        listNamesUsers = new ArrayList<>();


    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onClickLogin(View v) throws ExecutionException, InterruptedException {
        String email = mail.getText().toString();
        idteraz = email;
        String password = pass.getText().toString();

        if (email.equals("") || password.equals("")) {
            Toast.makeText(Signup.this, "Zadajte udaje", Toast.LENGTH_SHORT).show();
        } else {
            /*CompletableFuture.supplyAsync(() -> {
                try {
                    return noteViewModel.checkLogIn(email, password);
                } catch (Exception e) {
                    return -1L;
                }
            }).thenAccept(id -> {*/
            noteViewModel.checkLogIn(email, password).thenAccept(id -> {
                if (id == -1) {
                    Toast.makeText(Signup.this, "Wrong password or email", Toast.LENGTH_SHORT).show();
                    return;
                }
                idForData = email;
                Intent intent = new Intent(this, MainActivity.class);

                MainActivity.password = password;

                /*CompletableFuture.runAsync(() -> {
                    try {
                        usersData = noteViewModel.firstGetSimilar(email, dnesDate);
                    } catch (Exception e) {

                    }
                });*/
                try {
                    noteViewModel.firstGetSimilar(email, dnesDate).thenAccept(usersData -> {
                        Signup.usersData = usersData;
                        CalendarAdapter.daysForView.clear();
                        if (usersData != null)
                            decoder(usersData.dni);
                    });
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                /*CompletableFuture.runAsync(() -> {
                    try {
                        meno = noteViewModel.getMenoByID(id);
                    } catch (Exception e) {

                    }
                });*/
                try {
                    noteViewModel.getMenoByID(id).thenAccept(meno -> {
                        Signup.meno = meno;
                    });
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                startActivity(intent);
            });

        }
    }

    public void onClickRegestracia(View v) {
        switch (v.getId()) {
            case R.id.regestracia:
                Intent intent = new Intent(this, registracia.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}