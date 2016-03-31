package org.coderswithoutborders.deglancer.bus;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Created by Renier on 2016/03/29.
 */
public class RxBus {

    private final Subject<Object, Object> mBus = new SerializedSubject<>(PublishSubject.create());

    public void post(Object o) {
        mBus.onNext(o);
    }

    public Observable<Object> toObserverable() {
        return mBus;
    }
}
