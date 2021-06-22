package com.example.cyclusapp;

import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CalendarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public static ArrayList <String> alldays = new ArrayList <String>();
    public final TextView dayOfMonth;
    private final CalendarAdapter.OnItemListener onItemListener;




    @RequiresApi(api = Build.VERSION_CODES.O)
    public CalendarViewHolder(@NonNull View itemView, CalendarAdapter.OnItemListener onItemListener)
    {
        super(itemView);
        dayOfMonth = itemView.findViewById(R.id.cellDayText);
        this.onItemListener = onItemListener;
        itemView.setOnClickListener(this);

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View view)
    {
        if(MainActivity.dniToView != null) {
            MainActivity.dniToViewToArrayList(MainActivity.dniToView);
            if (MainActivity.dniToTwolick.contains(dayOfMonth.getText())) {
                MainActivity.DayForDeleteMayBe = "";
                MainActivity.DayForDeleteMayBe = String.valueOf(dayOfMonth.getText());
                alldays.add(String.valueOf(dayOfMonth.getText()));
                onItemListener.onItemClick(getAdapterPosition(), (String) dayOfMonth.getText());


                dayOfMonth.setTextColor(Color.parseColor("#F200F6"));
                dayOfMonth.setBackgroundColor(Color.parseColor("#FAFAFA"));

            } else {
                MainActivity.DayForDeleteMayBe = "";
                MainActivity.DayForDeleteMayBe = String.valueOf(dayOfMonth.getText());
                alldays.add(String.valueOf(dayOfMonth.getText()));
                onItemListener.onItemClick(getAdapterPosition(), (String) dayOfMonth.getText());

                dayOfMonth.setTextColor(Color.parseColor("#FFFFFF"));
                dayOfMonth.setBackgroundColor(Color.parseColor("#FF0000"));

            }
        }else{
            MainActivity.DayForDeleteMayBe = "";
            MainActivity.DayForDeleteMayBe = String.valueOf(dayOfMonth.getText());
            alldays.add(String.valueOf(dayOfMonth.getText()));
            onItemListener.onItemClick(getAdapterPosition(), (String) dayOfMonth.getText());

            dayOfMonth.setTextColor(Color.parseColor("#FFFFFF"));
            dayOfMonth.setBackgroundColor(Color.parseColor("#FF0000"));
        }
    }



}
