package com.mobilesolutionworks.works.sample.rx;


import com.mobilesolutionworks.works.core.SimpleWorksController;

import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by lucas34990 on 17/5/16.
 * Syncronise RX result with the UI flow by using simple controller
 * This will make sure the sucess callback will be executed when the UI is displayed
 */
public class WorksCompleteObserver<D extends SimpleWorksController<?>, T> extends DisposableObserver<T> {

    private final D host;
    private final Consumer<Exception> fail;
    private final Runnable runnableSucess;

    public WorksCompleteObserver(D host, Runnable success, Consumer<Exception> fail) {
        this.host = host;
        this.fail = fail;
        this.runnableSucess = host.addTask(success::run);
    }

    @Override
    public void onComplete() {
        runnableSucess.run();
    }

    @Override
    public void onError(Throwable throwable) {
        if (throwable instanceof Exception) {
            host.runOnUIWhenIsReady(() -> {
                try {
                    fail.accept((Exception) throwable);
                } catch (Exception e) {
                    e.printStackTrace();
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