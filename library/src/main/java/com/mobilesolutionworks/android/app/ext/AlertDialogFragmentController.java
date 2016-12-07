package com.mobilesolutionworks.android.app.ext;

import com.mobilesolutionworks.android.app.WorksDialogFragment;
import com.mobilesolutionworks.android.bolts.BoltsWorksController3;
import com.pacoworks.rxtuples.RxTuples;

import org.javatuples.Pair;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by yunarta on 7/12/16.
 */

public class AlertDialogFragmentController<Data> extends BoltsWorksController3<WorksDialogFragment> {

    private Data mData;

    private final PublishSubject<Integer> mObservable;

    public AlertDialogFragmentController() {
        mObservable = PublishSubject.create();
    }

    public Observable<Pair<Integer, Data>> setData(Data data) {
        mData = data;
        return Observable.zip(mObservable, Observable.just(mData), RxTuples.toPair());
    }

    public void postResult(int choiceRes) {
        mObservable.onNext(choiceRes);
        mObservable.onCompleted();
    }
}
