package com.mobilesolutionworks.works.sample.rx;


import com.mobilesolutionworks.works.core.SimpleWorksController;

import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * Created by lucas34990 on 17/5/16.
 * Synchronise RX result with the UI flow by using simple controller
 * This will make sure the success callback will be executed when the UI is displayed
 */
public class WorksCompleteObserver<D extends SimpleWorksController<?>, T> extends DisposableObserver<T> {

    private final D host;
    private final Consumer<Exception> fail;
    private final Runnable runnableSuccess;

    public WorksCompleteObserver(D host, Runnable success, Consumer<Exception> fail) {
        this.host = host;
        this.fail = fail;
        this.runnableSuccess = host.wrap(success);
    }

    @Override
    public void onComplete() {
        runnableSuccess.run();
    }

    @Override
    public void onError(Throwable throwable) {
        if (throwable instanceof Exception) {
            host.runOnUIWhenIsReady(() -> {
                try {
                    fail.accept((Exception) throwable);
                } catch (Exception e) {
                    RxJavaPlugins.onError(e);
                }
            });
        } else {
            throw Exceptions.propagate(throwable);
        }
    }

    @Override
    public void onNext(T t) {
        // Nothing to do
    }

}