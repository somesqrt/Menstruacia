package com.example.cyclusapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AccountInformation extends AppCompatActivity {

    NoteViewModel noteViewModel;
    private EditText oldpass, newpass, newrepass;
    private TextView name, posta;
    //private DatabaseReference mDataBase;
    //private List<String> listPassData;
    //private List<String> DaysData;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_information);


        init();

        ViewModelProvider.AndroidViewModelFactory viewModelFactory
                = new ViewModelProvider.AndroidViewModelFactory(getApplication());
        ViewModelProvider viewModelProvider = new ViewModelProvider(this, viewModelFactory);
        noteViewModel = viewModelProvider.get(NoteViewModel.class);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void init() {
        oldpass = findViewById(R.id.OldPassword);
        newpass = findViewById(R.id.newPass);
        newrepass = findViewById(R.id.NewRepass);
        name = findViewById(R.id.meno);
        posta = findViewById(R.id.emailForVypis);
        listPassData = new ArrayList<>();
        DaysData = new ArrayList<>();

        name.setText(Signup.meno);
        name.setVisibility(View.VISIBLE);
        posta.setText(Signup.idForData);
        posta.setVisibility(View.VISIBLE);


    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void OnKlickChangePass(View v) throws ExecutionException, InterruptedException {
        switch (v.getId()) {
            case R.id.ChangePass:
                String oldpassword = oldpass.getText().toString();
                String newpassword = newpass.getText().toString();
                String newrepassword = newrepass.getText().toString();
                if (oldpassword.equals("") || newpassword.equals("") || newrepassword.equals("")) {
                    Toast.makeText(AccountInformation.this, "Zadajte udaje", Toast.LENGTH_SHORT).show();
                } else {
                    if (newrepassword.equals(newpassword)) {
                        if (newpassword.equals(oldpassword)) {
                            Toast.makeText(AccountInformation.this, "Staré a nové heslo sú identické.", Toast.LENGTH_SHORT).show();
                        } else {
                            /*final User[] user = {new User()};
                            CompletableFuture.runAsync(() ->{
                                try {
                                    user[0] = noteViewModel.getUserByMail((String) posta.getText());
                                } catch (Exception e) {

                                }
                            });*/
                            noteViewModel.getUserByMail((String) posta.getText()).thenAccept(user1 -> {
                                if (user1 != null) {
                                    user1.password = newrepassword;
                                    noteViewModel.updateHeslo(user1);
                                } else {
                                    Toast.makeText(AccountInformation.this, "Error.", Toast.LENGTH_SHORT).show();
                                }
                            });

                            Intent intent = new Intent(this, AccountInformation.class);
                            startActivity(intent);


                        }
                    } else {
                        Toast.makeText(AccountInformation.this, "Nové heslo nebolo potvrdené.", Toast.LENGTH_SHORT).show();
                    }
                }
            default:
                break;
        }
    }

    public void Back(View v) {
        switch (v.getId()) {
            case R.id.behind:
                MainActivity.dniToView = "";
                MainActivity.dniToTwolick.clear();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

}