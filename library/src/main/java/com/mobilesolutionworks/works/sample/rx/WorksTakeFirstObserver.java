package com.mobilesolutionworks.works.sample.rx;

import com.mobilesolutionworks.works.core.SimpleWorksController;

import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;

/**
 * Synchronise RX result with the UI flow by using simple controller
 * This will make sure the success callback will be executed when the UI is displayed
 */
public class WorksTakeFirstObserver<D extends SimpleWorksController<?>, T> extends DisposableObserver<T> {

    private final Consumer<Throwable> fail;
    private final D host;
    private final Consumer<T> success;

    private boolean isDone;

    public WorksTakeFirstObserver(D host, Consumer<T> success, Consumer<Throwable> fail) {
        this.isDone = false;
        this.host = host;
        this.fail = fail;
        this.success = success;
    }

    @Override
    public void onComplete() {
        if (!isDone) {
            isDone = true;
            RxWorksControllerUtil.safeConsume(host, success, null);
        }
    }

    @Override
    public void onError(Throwable throwable) {
        RxWorksControllerUtil.safeConsume(host, fail, throwable);
    }

    @Override
    public void onNext(T t) {
        if (!isDone) {
            isDone = true;
            RxWorksControllerUtil.safeConsume(host, success, t);
        }
    }

}