package com.loftschool.ozaharenko.loftcoin19.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executor;
import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

@Singleton
class FirestoreWalletsRepo implements WalletsRepo {

    private static final Random PRNG = new SecureRandom();

    private final FirebaseFirestore firestore;

    private final Executor executor;

    private final CoinsRepo coinsRepo;

    @Inject
    FirestoreWalletsRepo(CoinsRepo coinsRepo) {
        this.coinsRepo = coinsRepo;
        firestore = FirebaseFirestore.getInstance();
        executor = Schedulers.io()::scheduleDirect;
    }

    @NonNull
    @Override
    public Observable<List<Wallet>> wallets(@NonNull Currency currency) {
        return query(firestore
                .collection("wallets")
                .orderBy("created", Query.Direction.ASCENDING))
                .map(QuerySnapshot::getDocuments)    //Observable<List<DocumentSnapshot>>
                .switchMap(documents -> Observable
                        .fromIterable(documents)
                        //difference between concatMapSingle, switchMap and flatMap:
                        //switchMap takes last result only, flatMap takes all results, concatMapSingle takes all results
                        // keeping order.
                        .concatMapSingle(document -> coinsRepo
                                .coin(currency, document.getLong("coinId"))
                                .map(coin -> Wallet.create(
                                        document.getId(),
                                        coin,
                                        document.getDouble("balance")
                                ))
                        )
                        .toList().toObservable()
                );
    }


    @NonNull
    @Override
    public Observable<List<Transaction>> transactions(@NonNull Wallet wallet) {
        return query(firestore
            .collection("wallets")
            .document(wallet.id())
            .collection("transactions")
            .orderBy("timestamp", Query.Direction.DESCENDING))
            .map(QuerySnapshot::getDocuments)
            .switchMapSingle(documents -> Observable
                .fromIterable(documents)
                    .map(document -> Transaction.create(
                        document.getId(),
                        wallet.coin(),
                        document.getDouble("amount"),
                        document.getDate("timestamp")
                ))
                .toList()
            );
    }

    @NonNull
    @Override
    public Single<Wallet> createWallet(@NonNull Coin coin) {
        return Single
            .<DocumentReference>create(emitter -> {
                final Map<String, Object> data = new HashMap<>();
                data.put("coinId", coin.id());
                data.put("balance", PRNG.nextDouble() * (1 + PRNG.nextInt(25)));
                data.put("created", FieldValue.serverTimestamp());
                firestore.collection("wallets").add(data)
                    .addOnSuccessListener(executor, emitter::onSuccess)
                    .addOnFailureListener(executor, emitter::tryOnError);
                })
                .flatMap(reference -> Single.<DocumentSnapshot>create(emitter -> {
                    final ListenerRegistration registration = reference
                        .addSnapshotListener(executor, (snapshot, e) -> {
                            if (e != null) {
                                emitter.tryOnError(e);
                            } else if (snapshot != null) {
                                emitter.onSuccess(snapshot);
                            }
                        });
                    emitter.setCancellable(registration::remove);
                }))
                .map(document -> Wallet.create(
                        document.getId(),
                        coin,
                        document.getDouble("balance")
                ));
    }

    @NonNull
    private Observable<QuerySnapshot> query(@NonNull Query reference) {
        return Observable.create(emitter -> {
            final ListenerRegistration registration = reference
                    .addSnapshotListener(executor, (snapshots, e) -> {
                        if (e != null) {
                            emitter.tryOnError(e);
                        } else if (snapshots != null) {
                            emitter.onNext(snapshots);
                        }
                    });
            emitter.setCancellable(registration::remove);
        });
    }

}
