package com.alihashem.sokna.models;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {
    private final int verticalSpaceHeight;

    public VerticalSpaceItemDecoration(int verticalSpaceHeight) {
        this.verticalSpaceHeight = verticalSpaceHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1) {
            outRect.bottom = verticalSpaceHeight;
        } else
            outRect.bottom = verticalSpaceHeight / 2;
        if (parent.getChildAdapterPosition(view) == 1) {
            outRect.top = verticalSpaceHeight;
        } else
            outRect.top = verticalSpaceHeight / 2;


        outRect.right = verticalSpaceHeight / 2;
        outRect.left = verticalSpaceHeight / 2;

    }
}
