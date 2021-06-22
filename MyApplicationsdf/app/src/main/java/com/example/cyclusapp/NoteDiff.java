package com.example.cyclusapp;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class NoteDiff extends DiffUtil.ItemCallback<Notes> {
    @Override
    public boolean areItemsTheSame(@NonNull Notes oldItem, @NonNull Notes newItem) {
        return Objects.equals(oldItem.getId(), newItem.getId());
    }

    @Override
    public boolean areContentsTheSame(@NonNull @NotNull Notes oldItem, @NonNull @NotNull Notes newItem) {
        return false;
    }
}
