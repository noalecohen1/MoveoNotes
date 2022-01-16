package com.noalecohen1.moveonotes.utils;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class spacingItemDecorator extends RecyclerView.ItemDecoration{

    private final int verticalSpaceHeight;

    public spacingItemDecorator(int verticalSpaceHeight){
        this.verticalSpaceHeight = verticalSpaceHeight;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.bottom = verticalSpaceHeight;
    }
}
