package com.mobilesolutionworks.works.sample.rx;

import com.mobilesolutionworks.works.core.SimpleWorksController;

import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * Created by lucas34990 on 17/5/16.
 * Synchronise RX result with the UI flow by using simple controller
 * This will make sure the success callback will be executed when the UI is displayed
 */
public class WorksTakeFirstObserver<D extends SimpleWorksController<?>, T> extends DisposableObserver<T> {

    private boolean isCompleted;
    private final D host;
    private final Consumer<Throwable> fail;
    private final Runnable runnableSuccess;

    private T result;

    public WorksTakeFirstObserver(D host, Consumer<T> success, Consumer<Throwable> fail) {
        this.isCompleted = false;
        this.host = host;
        this.fail = fail;
        this.result = null;
        runnableSuccess = host.wrap(() -> {
            try {
                success.accept(result);
            } catch (Exception e) {
                RxJavaPlugins.onError(e);
            }
        });
    }

    @Override
    public void onComplete() {
        if (!isCompleted) {
            isCompleted = true;
            runnableSuccess.run();
        }
    }

    @Override
    public void onError(Throwable throwable) {
        host.runOnUIWhenIsReady(() -> {
            try {
                fail.accept(throwable);
            } catch (Exception e) {
                RxJavaPlugins.onError(e);
            }
        });
    }

    @Override
    public void onNext(T t) {
        if (!isCompleted) {
            isCompleted = true;
            dispose();
            result = t;
            runnableSuccess.run();
        }
    }

}