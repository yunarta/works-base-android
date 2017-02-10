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
public class WorksTakeFirstObserver<D extends SimpleWorksController<?>, T> extends DisposableObserver<T> {

    private boolean isCompleted;
    private final D host;
    private final Consumer<Exception> fail;
    private final Runnable runnableSucess;

    private T result;

    public WorksTakeFirstObserver(D host, Consumer<T> success, Consumer<Exception> fail) {
        this.isCompleted = false;
        this.host = host;
        this.fail = fail;
        this.result = null;
        runnableSucess = host.addTask(() -> {
            try {
                success.accept(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onComplete() {
        if (!isCompleted) {
            isCompleted = true;
            runnableSucess.run();
        }
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
        if (!isCompleted) {
            isCompleted = true;
            dispose();
            result = t;
            runnableSucess.run();
        }
    }

}