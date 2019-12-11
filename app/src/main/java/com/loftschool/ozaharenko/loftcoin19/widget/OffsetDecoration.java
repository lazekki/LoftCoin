package com.loftschool.ozaharenko.loftcoin19.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OffsetDecoration extends RecyclerView.ItemDecoration {

    private final int offsetPx;

    public OffsetDecoration(@NonNull Context context, int offsetDp) {
        final DisplayMetrics dm = context.getResources().getDisplayMetrics();
        offsetPx = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, offsetDp, dm));
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                               @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        final RecyclerView.ViewHolder holder = parent.findContainingViewHolder(view);
        if (holder != null) {
            if (holder.getAdapterPosition() > 0) {
                outRect.set(offsetPx, 0, 0, 0);
            }
        } else {
            super.getItemOffsets(outRect, view, parent, state);
        }
    }

}
