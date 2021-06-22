package com.example.cyclusapp;

import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder> {
    public static ArrayList<String> daysForView = new ArrayList<String>();
    private final ArrayList<String> daysOfMonth;
    private final OnItemListener onItemListener;


    public CalendarAdapter(ArrayList<String> daysOfMonth, OnItemListener onItemListener) {
        this.daysOfMonth = daysOfMonth;
        this.onItemListener = onItemListener;

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) (parent.getHeight() * 0.166666666);
        return new CalendarViewHolder(view, onItemListener);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {

        if (daysForView.contains(daysOfMonth.get(position)) && daysOfMonth.get(position) != "") {
                holder.dayOfMonth.setText(daysOfMonth.get(position));

                holder.dayOfMonth.setTextColor(Color.parseColor("#FFFFFF"));

                holder.dayOfMonth.setBackgroundColor(Color.parseColor("#FF0000"));
        } else if(MainActivity.ShadowDays.contains(daysOfMonth.get(position))) {

            holder.dayOfMonth.setText(daysOfMonth.get(position));

            holder.dayOfMonth.setBackgroundColor(Color.parseColor("#F7F30A"));

            holder.dayOfMonth.setTextColor(Color.parseColor("#FFFFFF"));

        }else{
                holder.dayOfMonth.setText(daysOfMonth.get(position));
            }

    }

    @Override
    public int getItemCount() {
        return daysOfMonth.size();
    }

    public interface OnItemListener {
        void onItemClick(int position, String dayText);
    }


}
