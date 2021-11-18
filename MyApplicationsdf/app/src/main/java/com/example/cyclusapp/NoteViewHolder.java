package com.example.cyclusapp;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.*;

public class NoteViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.text)
    TextView text;

    public NoteViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setNote(Notes notes) {
        this.text.setText(notes.getDescription());
    }
}