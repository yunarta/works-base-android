package com.mobilesolutionworks.works.sample.rx;


import com.mobilesolutionworks.works.core.Controller;
import com.mobilesolutionworks.works.core.Host;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;

/**
 * Synchronise RX result with the UI flow by using simple controller
 * This will make sure the success callback will be executed when the UI is displayed
 */
public class WorksCompleteObserver<H extends Host, D extends Controller<H>, T> extends DisposableObserver<T> {

    private final D host;
    private final Consumer<Throwable> fail;
    private final Runnable runnableSuccess;

    public WorksCompleteObserver(D host, Action success, Consumer<Throwable> fail) {
        this.host = host;
        this.fail = fail;
        this.runnableSuccess = RxWorksControllerUtil.wrap(host, success);
    }

    @Override
    public void onComplete() {
        runnableSuccess.run();
    }

    @Override
    public void onError(Throwable throwable) {
        RxWorksControllerUtil.safeConsume(host, fail, throwable);
    }

    @Override
    public void onNext(T t) {
        // Nothing to do
    }

}