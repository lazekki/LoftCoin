package com.loftschool.ozaharenko.loftcoin19.util;

import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.squareup.picasso.Picasso;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PicassoImageLoader implements ImageLoader {

    @Inject
    PicassoImageLoader() {
    }

    @NonNull
    @Override
    public ImageRequest load(@NonNull String uri) {
        return new PicassoImageRequest(uri);
    }

    public static class PicassoImageRequest implements ImageRequest {
        private final String uri;

        PicassoImageRequest(String uri) {
            this.uri = uri;
        }

        @Override
        public void into(@NonNull ImageView view) {
            Picasso.get().load(uri).into(view);
        }
    }
}
