package org.coderswithoutborders.deglancer.pretest;

import org.coderswithoutborders.deglancer.bus.RxBus;
import org.coderswithoutborders.deglancer.interactor.IPreTestInteractor;

import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;



/**
 * Created by Lauripal on 29.6.2016.
 */
public class PreTestPresenter implements IPreTestPresenter {

    private CompositeSubscription mSubscriptions;

    private IPreTestView mView;
    private IPreTestInteractor mPreTestInteractor;
    private RxBus mBus;

    private static final String TAG = "PreTestPresenter";

    public PreTestPresenter(IPreTestInteractor preTestInteractor, RxBus bus) {
        this.mPreTestInteractor = preTestInteractor;
        this.mBus = bus;
    }

    @Override
    public void onAttached() {
        if (mSubscriptions == null || mSubscriptions.isUnsubscribed()) {
            mSubscriptions = new CompositeSubscription();
        } else if (!mSubscriptions.isUnsubscribed()) {
            mSubscriptions.unsubscribe();
            mSubscriptions = new CompositeSubscription();
        };    }

    @Override
    public void onDetached() {
        if (!mSubscriptions.isUnsubscribed())
            mSubscriptions.unsubscribe();
    }

    // @Override
    public void submitPreTestResults(int answerone, int answertwo, int answerthree, int answerfour, int answerfive, int answersix, int answerseven, int answereight, int answernine, int answerten) {
        // Timber.d( "Uploading results now - before uploadPreTestResults");
        mPreTestInteractor.uploadPreTestResults(answerone, answertwo, answerthree, answerfour, answerfive, answersix, answerseven, answereight, answernine, answerten);

        //        .subscribeOn(AndroidSchedulers.mainThread())
        //        .observeOn(AndroidSchedulers.mainThread())
        //        .subscribe();

        if (mView != null) {
            mView.moveToMainView();
            mView.finishActivity();
        }
    }


}
