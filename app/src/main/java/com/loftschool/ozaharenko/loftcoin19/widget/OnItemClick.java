package com.loftschool.ozaharenko.loftcoin19.widget;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.GestureDetectorCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.loftschool.ozaharenko.loftcoin19.util.Consumer;

import java.util.Objects;

public class OnItemClick implements RecyclerView.OnItemTouchListener {

    private final GestureDetectorCompat gestureDetector;

    private final Consumer<View> onClick;

    public OnItemClick(@NonNull Context context, @NonNull Consumer<View> onClick) {
        gestureDetector = new GestureDetectorCompat(Objects.requireNonNull(context),
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {
                        return true;
                    }
                });
        this.onClick = Objects.requireNonNull(onClick);
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
        final View child = rv.findChildViewUnder(e.getX(), e.getY());
        if (child != null && gestureDetector.onTouchEvent(e)) {
            onClick.apply(child);
        }
        return false;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

}