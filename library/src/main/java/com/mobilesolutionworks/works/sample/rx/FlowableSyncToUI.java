package com.mobilesolutionworks.works.sample.rx;

import com.mobilesolutionworks.works.core.SimpleWorksController;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.Flowable;
import io.reactivex.FlowableOperator;
import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.Experimental;

/**
 * Created by lucas34990 on 1/3/17.
 */
@Experimental
public class FlowableSyncToUI<T> implements FlowableTransformer<T, T> {

    private final SimpleWorksController<?> host;

    public FlowableSyncToUI(SimpleWorksController<?> host) {
        this.host = host;
    }

    @Override
    public Publisher<T> apply(Flowable<T> upstream) {
        return upstream.observeOn(AndroidSchedulers.mainThread()).onBackpressureBuffer().lift(new SyncToUiOperator<T>());
    }

    private final class SyncToUiOperator<T> implements FlowableOperator<T, T> {

        private Subscriber<? super T> observer;
        private Subscription subscription;

        @Override
        public Subscriber<? super T> apply(Subscriber<? super T> observer) throws Exception {
            this.observer = observer;

            return new Subscriber<T>() {

                @Override
                public void onSubscribe(Subscription s) {
                    subscription = s;
                    checkAndRun(() -> {
                        observer.onSubscribe(s);
                        s.request(1);
                    });
                }

                @Override
                public void onNext(T t) {
                    checkAndRun(() -> {
                        observer.onNext(t);
                        subscription.request(1);
                    });
                }

                @Override
                public void onError(Throwable e) {
                    checkAndRun(() -> observer.onError(e));
                }

                @Override
                public void onComplete() {
                    checkAndRun(() -> observer.onComplete());
                }
            };
        }

        public void checkAndRun(final Runnable runnable) {
            if(host.isResumed()) {
                runnable.run();
            } else {
                host.runOnUIWhenIsReady(new Runnable() {
                    @Override
                    public void run() {
                        runnable.run();
                    }
                });
            }
        }

    }

}
