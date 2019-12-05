package com.loftschool.ozaharenko.loftcoin19.util;

import android.widget.ImageView;

import androidx.annotation.NonNull;

public interface ImageLoader {

    @NonNull
    ImageRequest load(@NonNull String uri);

    interface ImageRequest {
        void into(@NonNull ImageView view);
    }

}
