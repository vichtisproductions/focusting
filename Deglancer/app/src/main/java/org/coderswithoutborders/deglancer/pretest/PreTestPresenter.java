package org.coderswithoutborders.deglancer.pretest;

import org.coderswithoutborders.deglancer.bus.RxBus;
import org.coderswithoutborders.deglancer.interactor.IPreTestInteractor;
import org.coderswithoutborders.deglancer.interactor.IStageInteractor;
import org.coderswithoutborders.deglancer.model.Results;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import android.util.Log;

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
    public void submitPreTestResults(String answer1, String answer2, String answer3, String answer4, String answer5, String answer6, String answer7, String answer8, String answer9, String answer10) {
        Log.d(TAG, "Uploading results now - before uploadPreTestResults");
        mPreTestInteractor.uploadPreTestResults(answer1, answer2, answer3, answer4, answer5, answer6, answer7, answer8, answer9, answer10);

        //        .subscribeOn(AndroidSchedulers.mainThread())
        //        .observeOn(AndroidSchedulers.mainThread())
        //        .subscribe();
        // mPreTestInteractor.uploadPreTestResults(answer1, answer2, answer3, answer4, answer5, answer6, answer7, answer8, answer9, answer10);

        if (mView != null) {
            mView.moveToMainView();
            mView.finishActivity();
        }
    }
}
