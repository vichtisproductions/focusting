package org.coderswithoutborders.deglancer.interactor;

import android.content.Context;
import android.util.Log;

import com.firebase.client.Firebase;

import org.coderswithoutborders.deglancer.bus.RxBus;
import org.coderswithoutborders.deglancer.model.Results;

import java.util.UUID;

import rx.Observable;

/**
 * Created by Lapa on 2016/06/29.
 */
public class PreTestInteractor implements IPreTestInteractor {
    private static final String TAG = "PreTestInteractor";
    private Context mContext;
    private RxBus mBus;
    private IDatabaseInteractor mDatabaseInteractor;
    private Firebase mFirebaseClient;
    private IUserInteractor mUserInteractor;

    public PreTestInteractor(
            Context context,
            IDatabaseInteractor mDatabaseInteractor,
            RxBus mBus,
            Firebase firebaseClient,
            IUserInteractor userInteractor) {
        this.mDatabaseInteractor = mDatabaseInteractor;
        this.mBus = mBus;
        this.mFirebaseClient = firebaseClient;
        this.mUserInteractor = userInteractor;
    }

    @Override
    public void uploadPreTestResults(
            String preTestQ1,
            String preTestQ2,
            String preTestQ3,
            String preTestQ4,
            String preTestQ5,
            String preTestQ6,
            String preTestQ7,
            String preTestQ8,
            String preTestQ9,
            String preTestQ10) {

        String uploadUUID = UUID.randomUUID().toString();

        Results r = new Results(
                uploadUUID,
                preTestQ1,
                preTestQ2,
                preTestQ3,
                preTestQ4,
                preTestQ5,
                preTestQ6,
                preTestQ7,
                preTestQ8,
                preTestQ9,
                preTestQ10);

        mDatabaseInteractor.commitPreTestResults(r);
        Log.d(TAG, "Here is the data to be uploaded.");
        Log.d(TAG, uploadUUID + " " + preTestQ1 + " " + preTestQ2 + " " + preTestQ3 + " " + preTestQ4 + " " + preTestQ5 + " " + preTestQ6 + " " + preTestQ7 + " " + preTestQ8 + " " + preTestQ9 + " " + preTestQ10);
        Firebase ref = mFirebaseClient.child(mUserInteractor.getInstanceIdSynchronous()).child("Results");
        ref.push().setValue(r);
    }
}
