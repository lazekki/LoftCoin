package com.loftschool.ozaharenko.loftcoin19.widget;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import io.reactivex.Observable;
import io.reactivex.android.MainThreadDisposable;

public class RxRecyclerView {

    @NonNull
    public static Observable<View> onSnap(@NonNull RecyclerView rv, @NonNull SnapHelper snapHelper) {
        return Observable.create(emitter -> {
            MainThreadDisposable.verifyMainThread();
            final RecyclerView.OnScrollListener listener = new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    if (RecyclerView.SCROLL_STATE_IDLE == newState) {
                        final View snapView = snapHelper.findSnapView(recyclerView.getLayoutManager());
                        if (snapView != null) {
                            emitter.onNext(snapView);
                        }
                    }
                }
            };
            emitter.setCancellable(() -> rv.removeOnScrollListener(listener));
            rv.addOnScrollListener(listener);
        });
    }

}
