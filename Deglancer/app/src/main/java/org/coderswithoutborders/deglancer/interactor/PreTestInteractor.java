package org.coderswithoutborders.deglancer.interactor;

import com.firebase.client.Firebase;

import org.coderswithoutborders.deglancer.bus.RxBus;
import org.coderswithoutborders.deglancer.model.Results;

import java.util.UUID;

import rx.Observable;

/**
 * Created by Lapa on 2016/06/29.
 */
public class PreTestInteractor implements IPreTestInteractor {

    private IDatabaseInteractor mDatabaseInteractor;
    private RxBus mBus;
    private Firebase mFirebaseClient;
    private IUserInteractor mUserInteractor;

    public PreTestInteractor(IDatabaseInteractor mDatabaseInteractor, RxBus mBus, Firebase firebaseClient, IUserInteractor userInteractor) {
        this.mDatabaseInteractor = mDatabaseInteractor;
        this.mBus = mBus;
        this.mFirebaseClient = firebaseClient;
        this.mUserInteractor = userInteractor;
    }

    @Override
    public void uploadPreTestResults(String preTestQ1, String preTestQ2, String preTestQ3, String preTestQ4, String preTestQ5, String preTestQ6, String preTestQ7, String preTestQ8, String preTestQ9, String preTestQ10) {
        Results r = new Results(UUID.randomUUID().toString(), preTestQ1, preTestQ2, preTestQ3, preTestQ4, preTestQ5, preTestQ6, preTestQ7, preTestQ8, preTestQ9, preTestQ10);

        mDatabaseInteractor.commitPreTestResults(r);

        Firebase ref = mFirebaseClient.child(mUserInteractor.getInstanceIdSynchronous()).child("Results");
        ref.push().setValue(r);
    }
}
