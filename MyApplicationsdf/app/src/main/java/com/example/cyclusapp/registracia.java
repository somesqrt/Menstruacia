package com.example.cyclusapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class registracia extends AppCompatActivity {

    CoordinatorLayout coordinatorLayout;
    private EditText name, mail, pass, repass;
    private DatabaseReference mDataBase;
    static String USER_KEY = "user";
    private List<String> listData;
    Button btnPrihlasFromRegister;
    public static String dnesDate;

    NoteViewModel noteViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registracia);


        ViewModelProvider.AndroidViewModelFactory viewModelFactory
                = new ViewModelProvider.AndroidViewModelFactory(getApplication());
        ViewModelProvider viewModelProvider = new ViewModelProvider(this, viewModelFactory);
        noteViewModel = viewModelProvider.get(NoteViewModel.class);


        init();
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("yyyyMM", Locale.getDefault());
        String formattedDate = df.format(c);

        if(formattedDate.charAt(4)!=0){
            dnesDate = "";
            for (int i = 0; i < formattedDate.length(); i++) {
                if(i!=4){
                    dnesDate = dnesDate+formattedDate.charAt(i);
                }
            }
        }
    }

    private void init(){
         name = findViewById(R.id.meno);
         mail = findViewById(R.id.Email);
         pass = findViewById(R.id.Pass);
         repass = findViewById(R.id.Repass);
         listData = new ArrayList<>();
         mDataBase = FirebaseDatabase.getInstance().getReference(USER_KEY);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onClickLogin(View v) throws ExecutionException, InterruptedException {
            String meno = name.getText().toString();
            String email = mail.getText().toString();
            String password = pass.getText().toString();
            String repassword = repass.getText().toString();

            if (meno.equals("") || mail.equals("") || password.equals("") ) {
                Toast.makeText(registracia.this, "Zadajte udaje", Toast.LENGTH_SHORT).show();
            }else{
                if(password.equals(repassword)){
                    /*CompletableFuture.supplyAsync(() -> {
                        try {
                            return noteViewModel.checkMail(email);
                        } catch (Exception e) {
                            return -1L;
                        }
                    })*/
                    noteViewModel.checkMail(email).thenAccept(forchek -> {
                        if (forchek != -1) {
                            Toast.makeText(registracia.this, "User already exist", Toast.LENGTH_SHORT).show();
                        } else {
                            MainActivity.meno = meno;
                            Signup.idForData = email;
                            MainActivity.password = password;


                            noteViewModel.addUser(email, password, meno);


                            Signup.idForData = email;
                            Intent intent = new Intent(this, MainActivity.class);
                            String key = email + dnesDate;
                            MainActivity.password = password;
                            Signup.meno = meno;

                            startActivity(intent);
                        }
                    });
                }else{
                    Toast.makeText(registracia.this, "Wrong password", Toast.LENGTH_SHORT).show();
                }
            }

    }

    public void GoToPrihlas(View v){
        switch (v.getId()){
            case R.id.gotoprihlas:
                Intent intent = new Intent(this, Signup.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}