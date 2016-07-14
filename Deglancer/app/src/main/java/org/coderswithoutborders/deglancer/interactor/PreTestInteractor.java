package org.coderswithoutborders.deglancer.interactor;

import android.content.Context;


import com.google.firebase.database.DatabaseReference;

import org.coderswithoutborders.deglancer.bus.RxBus;
import org.coderswithoutborders.deglancer.model.PreTestResults;

import java.util.UUID;

import timber.log.Timber;

/**
 * Created by Lapa on 2016/06/29.
 */
public class PreTestInteractor implements IPreTestInteractor {
    private static final String TAG = "PreTestInteractor";
    private Context mContext;
    private RxBus mBus;
    private IDatabaseInteractor mDatabaseInteractor;
    private DatabaseReference mFirebaseClient;
    private IUserInteractor mUserInteractor;

    public PreTestInteractor(
            Context context,
            IDatabaseInteractor mDatabaseInteractor,
            RxBus mBus,
            DatabaseReference firebaseClient,
            IUserInteractor userInteractor) {
        this.mDatabaseInteractor = mDatabaseInteractor;
        this.mBus = mBus;
        this.mFirebaseClient = firebaseClient;
        this.mUserInteractor = userInteractor;
    }

    @Override
    public void uploadPreTestResults(int answerone, int answertwo, int answerthree, int answerfour, int answerfive, int answersix, int answerseven, int answereight, int answernine, int answerten) {

        PreTestResults r = new PreTestResults(UUID.randomUUID().toString(), answerone, answertwo, answerthree, answerfour, answerfive, answersix, answerseven, answereight, answernine, answerten);
        int FirstAns = r.getAns1();
        // Timber.d( "First answer:" + Integer.toString(FirstAns));
        mDatabaseInteractor.commitPreTestResults(r);
        DatabaseReference ref = mFirebaseClient.child(mUserInteractor.getInstanceIdSynchronous()).child("PreTestResults");
        ref.push().setValue(r);
    }
}
