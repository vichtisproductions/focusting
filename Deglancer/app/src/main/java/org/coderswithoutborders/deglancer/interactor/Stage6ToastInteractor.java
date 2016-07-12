package org.coderswithoutborders.deglancer.interactor;

import com.google.firebase.database.DatabaseReference;

import org.coderswithoutborders.deglancer.bus.RxBus;
import org.coderswithoutborders.deglancer.model.Stage6Toast;
import org.coderswithoutborders.deglancer.model.Target;

import java.util.UUID;

import rx.Observable;
import timber.log.Timber;

/**
 * Created by Renier on 2016/05/14.
 */
public class Stage6ToastInteractor implements IStage6ToastInteractor {
    private static final int DEFAULT_TOAST = 1;

    private IDatabaseInteractor mDatabaseInteractor;
    private RxBus mBus;
    private DatabaseReference mFirebaseClient;
    private IUserInteractor mUserInteractor;

    public Stage6ToastInteractor(IDatabaseInteractor mDatabaseInteractor, RxBus mBus, DatabaseReference firebaseClient, IUserInteractor userInteractor) {
        this.mDatabaseInteractor = mDatabaseInteractor;
        this.mBus = mBus;
        this.mFirebaseClient = firebaseClient;
        this.mUserInteractor = userInteractor;
    }


    @Override
    public Observable<Integer> getStage6Toast(int stage) {
        return Observable.create(subscriber -> {

            subscriber.onNext(getStage6ToastSynchronous(stage));

            subscriber.onCompleted();
        });

    }

    @Override
    public int getStage6ToastSynchronous(int stage) {
        Stage6Toast toReturn = mDatabaseInteractor.getToastForStage(stage);

        if (toReturn != null) {
            return toReturn.getTarget();
        } else {
            return DEFAULT_TOAST;
        }
    }


    @Override
    public void setStage6Toast(int stage, int toast) {
        Stage6Toast t = new Stage6Toast(UUID.randomUUID().toString(), stage, toast);
        int oldToast = getStage6ToastSynchronous(6);

        if (t.getTarget() != oldToast) {
            Timber.d("Toast type has changed, let's update.");
            mDatabaseInteractor.commitToast(t);

            DatabaseReference ref = mFirebaseClient.child(mUserInteractor.getInstanceIdSynchronous()).child("Stage6Toast");
            ref.push().setValue(t);
        } else {
            Timber.d("Toast type hasn't changed, maybe it's just an app invoking...");
        }
    }

}