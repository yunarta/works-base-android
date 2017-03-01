package com.mobilesolutionworks.works.sample.rx;

import com.mobilesolutionworks.works.core.SimpleWorksController;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableOperator;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.Experimental;
import io.reactivex.disposables.Disposable;

/**
 * Created by lucas34990 on 28/2/17.
 */
@Experimental
public class ObservableSyncToUI<T> implements ObservableTransformer<T, T> {

    private final SimpleWorksController<?> host;

    public ObservableSyncToUI(SimpleWorksController<?> host) {
        this.host = host;
    }

    @Override
    public ObservableSource<T> apply(Observable<T> upstream) {
        return upstream.observeOn(AndroidSchedulers.mainThread()).lift(new SyncToUiOperator<T>());
    }

    /**
     * If the controller is resumed it will run immediately
     * The observeOn MainThread will make sure I'm already in UI thread
     *
     * If the controller is paused, the item will be cached
     *
     */
    private final class SyncToUiOperator<T> implements ObservableOperator<T, T> {

        private final List<Runnable> buffer = new CopyOnWriteArrayList<>();
        private final Runnable flush = new Runnable() {
            @Override
            public void run() {
                // UI Thread
                for (Iterator<Runnable> iterator = buffer.iterator(); iterator.hasNext(); ) {
                    Runnable runnable = iterator.next();
                    runnable.run();
                    iterator.remove();
                }
            }
        };

        @Override
        public Observer<? super T> apply(Observer<? super T> observer) throws Exception {
            return new Observer<T>() {
                @Override
                public void onSubscribe(Disposable d) {
                    // UI Thread
                    checkAndRun(() -> observer.onSubscribe(d));
                }

                @Override
                public void onNext(T t) {
                    // UI Thread
                    checkAndRun(() -> observer.onNext(t));
                }

                @Override
                public void onError(Throwable e) {
                    // UI Thread
                    checkAndRun(() -> observer.onError(e));
                }

                @Override
                public void onComplete() {
                    // UI Thread
                    checkAndRun(() -> observer.onComplete());
                }
            };
        }

        private void checkAndRun(final Runnable runnable) {
            if(host.isResumed()) {
                if(buffer.isEmpty()) {
                    runnable.run();
                } else {
                    subscribe();
                }
            } else {
                buffer.add(runnable);
                subscribe();
            }
        }

        private void subscribe() {
            host.runOnUIWhenIsReady(flush); // Same instance so it will replace
        }
    }

}
