package com.loftschool.ozaharenko.loftcoin19.widget;

import android.graphics.Outline;
import android.view.View;
import android.view.ViewOutlineProvider;

public class CircleOutlineProvider extends ViewOutlineProvider {

    @Override
    public void getOutline(View view, Outline outline) {

        final float maxDimension = Math.max(view.getWidth(), view.getHeight());
        outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), maxDimension / 2);
    }
}
