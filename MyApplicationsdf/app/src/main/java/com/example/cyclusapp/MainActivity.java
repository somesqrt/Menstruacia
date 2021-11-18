package com.example.cyclusapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;
import com.onesignal.OneSignal;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import butterknife.*;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MainActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener {


    public static int datastart;
    public static ArrayList<String> ShadowDays = new ArrayList<String>();
    public static String id, meno, password;
    public static int OneCycle = 35;
    public static String DayForDeleteMayBe = "";
    public static ArrayList<String> allID = new ArrayList<String>();
    public static ArrayList<String> dniToTwolick = new ArrayList<String>();
    public static String dniToView = "";
    public static LocalDate selectedDate;
    public static String MonthId;
    static int count = 0;
    private static List<String> listID;
    private final int NOTIFICATOIN_ID = 127;
    NoteListAdapter noteListAdapter;
    UsersData usersData = new UsersData();
    NoteViewModel noteViewModel;
    @BindView(R.id.noteRecyclerView1)
    RecyclerView noteRecyclerView;
    private NotificationManager nm;
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;

    public static void dniToViewToArrayList(String dniToView) {
        System.out.println("*//*//******************* я запустился " + dniToView);
        int i = 0;
        String time = "";
        while (i != dniToView.length()) {

            if (dniToView.charAt(i) != ' ') {
                time = time + dniToView.charAt(i);
                i++;
            } else if (dniToView.charAt(i) == ' ') {
                dniToTwolick.add(time);
                time = "";
                i++;
            }
        }
    }

    public static void vypocet_Montch() {
        MonthId = String.valueOf(selectedDate.getYear());
        MonthId = MonthId + String.valueOf(selectedDate.getMonthValue());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ButterKnife.bind(this);


        noteRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));

        noteListAdapter = new NoteListAdapter();

        noteRecyclerView.setAdapter(noteListAdapter);

        ViewModelProvider.AndroidViewModelFactory viewModelFactory
                = new ViewModelProvider.AndroidViewModelFactory(getApplication());
        ViewModelProvider viewModelProvider = new ViewModelProvider(this, viewModelFactory);
        noteViewModel = viewModelProvider.get(NoteViewModel.class);


        count = allID.size();

        listID = new ArrayList<>();

        initWidgets();
        selectedDate = LocalDate.now();
        setMonthView();
        noteViewModel.getNotes(Signup.idForData, MonthId).observe(this, notes -> noteListAdapter.submitList(notes));
        try {
            firstInitWithData(MonthId);
            Thread.sleep(3000);

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd", Locale.getDefault());
        String formattedDate = df.format(c);
        if (formattedDate.charAt(0) == '0') {
            String two = String.valueOf(formattedDate.charAt(1));
            formattedDate = two;
        }
        formattedDate = "4";
        NotInfo notInfo = null;
        try {
            notInfo = noteViewModel.getSimilarDayForNotification("20217", formattedDate);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (notInfo != null) {
            showNotification();
        }
    }

    public void showNotification() {
        nm = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(getApplicationContext());

        Intent intent = new Intent(getApplicationContext(), Signup.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_baseline_circle_notifications_24)
                .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.ic_baseline_circle_notifications_24))
                .setTicker("Сегодня месячные")
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentTitle("Сегодня месячные")
                .setContentText("Не забудьте отметить");

        Notification notification = builder.build();

        nm.notify(NOTIFICATOIN_ID, notification);
    }

    @OnClick(R.id.fab1)
    public void onAddNote(View view) {
        EditText descriptionEditText = new EditText(this);
        new AlertDialog.Builder(this)
                .setView(descriptionEditText)
                .setPositiveButton("Save", (dialog, which) -> onDialogOkClick(descriptionEditText))
                .show();
        noteViewModel.getNotes(Signup.idForData, MonthId).observe(this, notes -> noteListAdapter.submitList(notes));
    }

    public void onDialogOkClick(EditText descriptionEditText) {
        noteViewModel.addNoteToUser(Signup.idForData, MonthId, descriptionEditText.getText().toString());
    }

    public void firstInitWithData(String key) throws ExecutionException, InterruptedException {
        /*CompletableFuture.supplyAsync(() -> {
            try {
                return noteViewModel.getSimilar(Signup.idForData, key);
            } catch (Exception e) {
                Log.i("Error", "Govno kod");
            }

            return null;
        })*/
        noteViewModel.getSimilar(Signup.idForData, key).thenAccept(usersData -> {
            Log.i(Thread.currentThread().getName(), "VOT TUT ETO ****************");
            if (usersData != null) {
                dniToView = usersData.dni;
                dniToViewToArrayList(dniToView);
            }
        });


    }

    public void initWithData(String key) throws ExecutionException, InterruptedException {
        /*CompletableFuture.supplyAsync(() -> {
            try {
                return noteViewModel.getSimilar(Signup.idForData, key);
            } catch (Exception e) {
                Log.i("Error", "Govno kod");
            }

            return null;
        })*/
        noteViewModel.getSimilar(Signup.idForData, key).thenAccept(usersData -> {
            if (usersData != null) {
                dniToView = usersData.dni;
                dniToViewToArrayList(dniToView);
            }
        });

        //*************************************************************
        //
        // *********************
        if (dniToView != null) {
            if (dniToView.equals("")) {
                /*CompletableFuture.supplyAsync(() -> {
                    try {
                        return noteViewModel.getSimilar(Signup.idForData, (String.valueOf(Integer.parseInt(key) - 1)));
                    } catch (Exception e) {
                    }

                    return null;
                })*/
                noteViewModel.getSimilar(Signup.idForData, (String.valueOf(Integer.parseInt(key) - 1))).thenAccept(usersData1 -> {
                    try {
                        podInit(usersData1);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });


            }
        }
    }

    private void podInit(UsersData usersData1) throws ExecutionException, InterruptedException {
        if (usersData1 != null) {
            int vypocet = Integer.parseInt(MonthId) - 1;
            String shadoKey = String.valueOf(vypocet);
            dniToViewToArrayList(usersData1.dni);

            /*CompletableFuture.supplyAsync(() -> {
                try {
                    return noteViewModel.getSimilar(Signup.idForData, shadoKey);
                } catch (Exception e) {
                    Log.i("Error", "Govno kod");
                }

                return null;
            })*/
            noteViewModel.getSimilar(Signup.idForData, shadoKey).thenAccept(usersData -> {
                if (usersData != null) {
                    dniToView = usersData.dni;
                    int pocet = 0;
                    dniToTwolick.clear();
                    dniToViewToArrayList(dniToView);

                    while (dniToTwolick.contains("")) {
                        dniToTwolick.remove("");
                    }

                    for (int i = 0; i < dniToTwolick.size(); i++) {
                        pocet++;
                    }


                    int prvyden = Integer.valueOf(dniToTwolick.get(0)) - 1;

                    YearMonth yearMonth = YearMonth.from(selectedDate);
                    int daysToEndMonth = yearMonth.lengthOfMonth();
                    daysToEndMonth = daysToEndMonth - prvyden;
                    int firstFreeDaysInNextStartMonth = OneCycle - daysToEndMonth;
                    noteViewModel.addNotification(MonthId, String.valueOf(firstFreeDaysInNextStartMonth));
                    for (int i = 0; i < pocet; i++) {
                        ShadowDays.add(String.valueOf(firstFreeDaysInNextStartMonth));
                        firstFreeDaysInNextStartMonth++;
                    }
                }
            });
        }
    }

    public String clear(String toClear) {
        String result = "";
        for (int i = 0; i < toClear.length(); i++) {
            if (toClear.charAt(i) == ' ' || Character.isDigit(toClear.charAt(i))) {
                result = result + toClear.charAt(i);
            }
        }
        return result;
    }

    public void ClickToDen() throws ExecutionException, InterruptedException {
        dniToView = null;

        count = allID.size();

        if (!CalendarAdapter.daysForView.contains(DayForDeleteMayBe)) {

            String key = MonthId;

            String[] times = new String[CalendarViewHolder.alldays.size()]; // нажатые дни за сесию

            for (int i = 0; i < CalendarViewHolder.alldays.size(); i++) {
                times[i] = (String) CalendarViewHolder.alldays.get(i);
            }

            String str = String.join(" ", times);

            initWithData(key);
            if (dniToView != null) {
                str = str + " " + dniToView;
            }
            String[] delete = str.split("[ ]");
            HashSet<String> deleteSymbols = new HashSet<>(Arrays.asList(delete));
            ArrayList<String> fordeleteSymbols = new ArrayList<String>();
            fordeleteSymbols.addAll(deleteSymbols);
            str = "";
            for (int i = 0; i < fordeleteSymbols.size(); i++) {
                str = str + fordeleteSymbols.get(i) + " ";
            }

            String id = Signup.idForData;
            String dniToDatabase = str;
            String mesiac = MonthId;

            /*CompletableFuture.supplyAsync(() -> {
                try {
                    return noteViewModel.getUsersDataByMail(Signup.idForData);
                } catch (Exception e) {
                    return -1L;
                }
            })*/
            noteViewModel.getUsersDataByMail(Signup.idForData).thenAccept(emailForCheckEmpty -> {


                if (emailForCheckEmpty == null) {
                    noteViewModel.addDni(id, mesiac, dniToDatabase);
                } else {

                /*CompletableFuture.supplyAsync(() -> {
                    try {
                        return noteViewModel.getSimilar(id, mesiac);
                    } catch (Exception e) {
                        Log.i("Error", "Govno kod");
                    }

                    return null;
                })*/
                    try {
                        noteViewModel.getSimilar(id, mesiac).thenAccept(usersData -> {

                            if (usersData != null) {
                                usersData.dni = dniToDatabase;
                                noteViewModel.updateDni(usersData);
                            } else {
                                noteViewModel.addDni(id, mesiac, dniToDatabase);
                            }
                        });
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


            });
            CalendarAdapter.daysForView.add(DayForDeleteMayBe);
            dniToTwolick.add(DayForDeleteMayBe);
        } else { //********************************************************************************************************************************************************************
            while (CalendarViewHolder.alldays.contains(DayForDeleteMayBe)) {
                CalendarViewHolder.alldays.remove(DayForDeleteMayBe);
            }

            String key = MonthId;

            String[] times = new String[CalendarViewHolder.alldays.size()]; // нажатые дни за сесию

            for (int i = 0; i < CalendarViewHolder.alldays.size(); i++) {
                times[i] = (String) CalendarViewHolder.alldays.get(i);
            }

            String str = String.join(" ", times);

            initWithData(key);
            while (dniToTwolick.contains(DayForDeleteMayBe)) {
                dniToTwolick.remove(DayForDeleteMayBe);
            }
            dniToView = "";
            for (int i = 0; i < dniToTwolick.size(); i++) {
                dniToView = dniToView + dniToTwolick.get(i) + ' ';
            }

            str = str + " " + dniToView;

            String[] delete = str.split("[ ]");
            HashSet<String> deleteSymbols = new HashSet<>(Arrays.asList(delete));
            ArrayList<String> fordeleteSymbols = new ArrayList<String>();
            fordeleteSymbols.addAll(deleteSymbols);
            str = "";
            for (int i = 0; i < fordeleteSymbols.size(); i++) {
                str = str + fordeleteSymbols.get(i) + " ";
            }

            String id = Signup.idForData;
            String dniToDatabase = str;
            String mesiac = MonthId;

            /*CompletableFuture.supplyAsync(() -> {
                try {
                    return noteViewModel.getSimilar(id, mesiac);
                } catch (Exception e) {
                    Log.i("Error", "Govno kod");
                }

                return null;
            })*/
            noteViewModel.getSimilar(id, mesiac).thenAccept(usersData -> {

                if (usersData != null) {
                    usersData.dni = dniToDatabase;
                    noteViewModel.updateDni(usersData);
                }

            });
            while (CalendarAdapter.daysForView.contains(DayForDeleteMayBe)) {
                CalendarAdapter.daysForView.remove(DayForDeleteMayBe);
            }
        }
    }

    private void initWidgets() {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setMonthView() {
        monthYearText.setText(monthYearFromDate(selectedDate));

        ArrayList<String> daysInMonth = daysInMonthArray(selectedDate);

        vypocet_Montch();

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private ArrayList<String> daysInMonthArray(LocalDate date) {
        ArrayList<String> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);

        int daysInMonth = yearMonth.lengthOfMonth();

        LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        for (int i = 1; i <= 42; i++) {
            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek) {
                daysInMonthArray.add("");
            } else {
                daysInMonthArray.add(String.valueOf(i - dayOfWeek));
            }
        }
        return daysInMonthArray;
    }

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

    }

    public void previusOrNextMonth(String key) throws ExecutionException, InterruptedException {

        /*CompletableFuture.supplyAsync(() -> {
            try {
                return noteViewModel.getSimilar(Signup.idForData, key);
            } catch (Exception e) {
                Log.i("Error", "Govno kod");
            }

            return null;
        })*/
        noteViewModel.getSimilar(Signup.idForData, key).thenAccept(usersData -> {

            if (usersData != null)
                decoder(usersData.dni);

        });
        noteViewModel.getNotes(Signup.idForData, MonthId).observe(this, notes -> noteListAdapter.submitList(notes));
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private String monthYearFromDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void previousMonthAction(View view) throws ExecutionException, InterruptedException {
        CalendarViewHolder.alldays.clear();
        dniToView = "";
        selectedDate = selectedDate.minusMonths(1);
        CalendarAdapter.daysForView.clear();
        vypocet_Montch();
        previusOrNextMonth(MonthId);
        dniToTwolick.clear();
        ShadowDays.clear();

        previusOrNextMonth(MonthId);
        initWithData(MonthId);
        setMonthView();


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void nextMonthAction(View view) throws ExecutionException, InterruptedException {
        CalendarViewHolder.alldays.clear();                  //дни используются для отображения и записи в ДБ
        dniToView = "";                                      //дни на изначальное отображение
        selectedDate = selectedDate.plusMonths(1);
//текущая дата
        CalendarAdapter.daysForView.clear();
        vypocet_Montch();                                    //дата год-месяц YYYYMM
        previusOrNextMonth(MonthId);   //д
        dniToTwolick.clear();                                           //лист дней дл проверки на случай удаления
        ShadowDays.clear();
        initWithData(MonthId);       //доставание данных с ДБ
        setMonthView();                                     //отображение всего

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onItemClick(int position, String dayText) {
        try {
            ClickToDen();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //init();
        //save();
        if (!dayText.equals("") && !dayText.equals(null)) {
            String message = "Selected Date " + dayText + " " + monthYearFromDate(selectedDate);
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }

    }

    public void AccountInfo(View v) {
        switch (v.getId()) {
            case R.id.acount:
                Intent intent = new Intent(this, AccountInformation.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit").setMessage("Naozaj sa chcete odhlásiť??").setPositiveButton("Ano", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        }).setNegativeButton("Nie", null).show();
    }

    public void contact(View v) {
        switch (v.getId()) {
            case R.id.img_b:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"CyclusContact@gmail.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "");
                intent.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(Intent.createChooser(intent, ""));
                break;
            default:
                break;
        }
    }
}