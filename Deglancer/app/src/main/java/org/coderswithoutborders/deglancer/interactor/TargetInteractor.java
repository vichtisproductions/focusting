package org.coderswithoutborders.deglancer.interactor;

import com.google.firebase.database.DatabaseReference;

import org.coderswithoutborders.deglancer.bus.RxBus;
import org.coderswithoutborders.deglancer.model.Target;

import java.util.UUID;

import rx.Observable;

/**
 * Created by Renier on 2016/05/14.
 */
public class TargetInteractor implements ITargetInteractor {
    private static final int DEFAULT_TARGET = 5;

    private IDatabaseInteractor mDatabaseInteractor;
    private RxBus mBus;
    private DatabaseReference mFirebaseClient;
    private IUserInteractor mUserInteractor;

    public TargetInteractor(IDatabaseInteractor mDatabaseInteractor, RxBus mBus, DatabaseReference firebaseClient, IUserInteractor userInteractor) {
        this.mDatabaseInteractor = mDatabaseInteractor;
        this.mBus = mBus;
        this.mFirebaseClient = firebaseClient;
        this.mUserInteractor = userInteractor;
    }


    @Override
    public Observable<Integer> getTargetForStage(int stage) {
        return Observable.create(subscriber -> {

            subscriber.onNext(getTargetForStageSynchronous(stage));

            subscriber.onCompleted();
        });

    }

    @Override
    public int getTargetForStageSynchronous(int stage) {
        Target toReturn = mDatabaseInteractor.getTargetForStage(stage);

        if (toReturn != null) {
            return toReturn.getTarget();
        } else {
            return DEFAULT_TARGET;
        }
    }

    @Override
    public void setTargetForStage(int stage, int target) {
        Target t = new Target(UUID.randomUUID().toString(), stage, target);

        mDatabaseInteractor.commitTarget(t);

        DatabaseReference ref = mFirebaseClient.child(mUserInteractor.getInstanceIdSynchronous()).child("Targets");
        ref.push().setValue(t);
    }
}
