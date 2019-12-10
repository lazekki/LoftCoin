package com.loftschool.ozaharenko.loftcoin19.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

@Singleton
class FirestoreWalletsRepo implements WalletsRepo {

    private final FirebaseFirestore firestore;

    private final Executor executor;

    @Inject
    FirestoreWalletsRepo() {
        firestore = FirebaseFirestore.getInstance();
        executor = Schedulers.io()::scheduleDirect;
    }

    @NonNull
    @Override
    public Observable<List<Wallet>> wallets() {
        return Observable
            .<QuerySnapshot>create(emitter -> {
                final ListenerRegistration registration = firestore
                    .collection("wallets")
                    .addSnapshotListener(executor, (snapshots, e) -> {
                        if (e != null) {
                            emitter.tryOnError(e);
                        }
                        else if (snapshots != null) {
                            emitter.onNext(snapshots);
                        }
                    });
                emitter.setCancellable(registration::remove);
        })  //Observable<QuerySnapshot>
        .map(QuerySnapshot::getDocuments)    //Observable<List<DocumentSnapshot>>
        .switchMapSingle(documents -> Observable
                .fromIterable(documents)
                .map(document -> Wallet.create(
                    document.getId(),
                    document.getLong("coinId"),
                    document.getDouble("balance")
                ))
                .toList()
        )
        .switchMap(docs -> Observable.empty());
    }
}
