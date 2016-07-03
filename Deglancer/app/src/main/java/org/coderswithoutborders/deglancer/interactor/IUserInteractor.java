package org.coderswithoutborders.deglancer.interactor;

import rx.Observable;

/**
 * Created by Renier on 2016/04/12.
 */
public interface IUserInteractor {
    Observable<String> getInstanceId();
    String getInstanceIdSynchronous();
    void logLastUserInteraction();
    // FirebaseAuth.AuthStateListener authFirebase();
    String getFirebaseUid();
}
